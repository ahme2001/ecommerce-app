package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.APIException;
import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.*;
import com.ecommerce.sb_ecom.payload.CheckoutDTO;
import com.ecommerce.sb_ecom.payload.ClientKeys;
import com.ecommerce.sb_ecom.repository.CartRepository;
import com.ecommerce.sb_ecom.service.OrderService;
import com.ecommerce.sb_ecom.service.PaymentService;
import com.ecommerce.sb_ecom.utils.AuthUtils;
import com.google.gson.JsonSyntaxException;
import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret.key}")
    private String stripe_SecretKey;

    // If we test by stripe cli locally , value of secret is changing
    // when write stripe listen --forward-to localhost:8080/api/webhook
    @Value("${stripe.webhook.secret}")
    private String stripe_Webhook;


    private final AuthUtils authUtils;
    private final CartRepository  cartRepository;
    private final OrderService orderService;

    public PaymentServiceImpl(AuthUtils authUtils,
                              CartRepository cartRepository,
                              OrderService orderService) {
        this.authUtils = authUtils;
        this.cartRepository = cartRepository;
        this.orderService = orderService;
    }

    public ClientKeys getCLientSecretKey(CheckoutDTO checkoutDTO) {
        Cart cart = checkAuthForCart(checkoutDTO);

        long finalTotalPrice = getFinalTotalPrice(cart);

        StripeClient client = new StripeClient(stripe_SecretKey);

        String idempotencyKey = UUID.randomUUID().toString();
        RequestOptions options =
                RequestOptions.builder()
                        .setIdempotencyKey(idempotencyKey)
                        .build();
        
        PaymentIntent paymentIntent;
        if (cart.getPaymentIntentId() == null) { // create new paymentIntent
            paymentIntent = createNewPaymentIntent(finalTotalPrice, cart, client, options);
        } else {
            // Retrieve old paymentIntent
            try {
                paymentIntent = client.v1().paymentIntents().retrieve(cart.getPaymentIntentId(),options);
                paymentIntent.setAmount(finalTotalPrice);
            } catch (StripeException e) {
                log.error(e.getMessage());
                throw new APIException(e.getMessage());
            }           
        }

        String clientSecretKey = paymentIntent.getClientSecret();
        return new ClientKeys(idempotencyKey, clientSecretKey);
    }

    @Override
    public void listenToStripeWebhook(String signature, String payload) {
        StripeClient client = new StripeClient(stripe_SecretKey);

        Event event = null;
        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch  (JsonSyntaxException e) {
            log.error("⚠  Webhook error while parsing basic request.");
            throw new APIException(e.getMessage());
        }

        if(stripe_Webhook != null && signature != null) {
            try {
                event = client.constructEvent(
                        payload, signature, stripe_Webhook
                );
            } catch (SignatureVerificationException e) {
                // Invalid signature
                log.error("⚠  Webhook error while validating signature.");
                throw new APIException(e.getMessage());
            }
        }

        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            log.error("⚠ Deserialization failed, probably due to an API version mismatch.");
            throw new APIException("Deserialization failed, probably due to an API version mismatch.");
        }

        // Handle the event
        if (event.getType().equals("payment_intent.succeeded")) {
            PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
            String paymentIntentId = paymentIntent.getId();
            orderService.placeOrder(paymentIntentId, paymentIntent.getAmount());
        } else {
            log.info("Unhandled event type: " + event.getType());
        }
    }

    private long getFinalTotalPrice(Cart cart) {
        List<CartItems> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty.");
        }
        double totalPrice = 0.0;
        for (CartItems cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getQuantity() >=  cartItem.getQuantity())
                totalPrice += (cartItem.getQuantity() * product.getSpecialPrice());
            else
                throw new ResourceNotFoundException("Product doesn't have enough quantity.");
        }

        long finalTotalPrice = Math.round(totalPrice * 100) ;

        if(finalTotalPrice < 3000)
            finalTotalPrice = 3000;
        return finalTotalPrice;
    }

    private Cart checkAuthForCart(CheckoutDTO checkoutDTO) {
        User user = authUtils.loggedInUser();
        Cart cart = cartRepository.findById(checkoutDTO.getCartId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );

        if(!(Objects.equals(cart.getUser().getUserId(), user.getUserId()))) {
            throw new APIException("User doesn't belong to this cart");
        }

        Address address = user.getAddresses().stream().filter(add ->
                add.getAddressId().equals(checkoutDTO.getAddressId())
        ).findFirst().orElse(null);
        if (address == null) {
            throw new ResourceNotFoundException("Address doesn't exist.");
        }

        if (cart.getAddress() == null ||
                cart.getAddress().getAddressId() != checkoutDTO.getAddressId()) {
            cart.setAddress(address);
        }
        return cart;
    }

    private PaymentIntent createNewPaymentIntent(long totalPrice, Cart cart, StripeClient client, RequestOptions options) {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(totalPrice)
                        .setCurrency("EGP")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
        PaymentIntent paymentIntent;
        try {
            paymentIntent = client.v1().paymentIntents().create(params, options);
            cart.setPaymentIntentId(paymentIntent.getId());
            cartRepository.save(cart);
        } catch (StripeException e) {
            log.error(e.getMessage());
            throw new APIException(e.getMessage());
        }
        return paymentIntent;
    }
}
