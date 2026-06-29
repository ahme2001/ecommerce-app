package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.APIException;
import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.payload.CheckoutDTO;
import com.ecommerce.sb_ecom.payload.ClientKeys;
import com.ecommerce.sb_ecom.repository.CartRepository;
import com.ecommerce.sb_ecom.service.PaymentService;
import com.ecommerce.sb_ecom.utils.AuthUtils;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret.key}")
    private String stripe_SecretKey;

    private AuthUtils authUtils;
    private CartRepository  cartRepository;


    public PaymentServiceImpl(AuthUtils authUtils,  CartRepository cartRepository) {
        this.authUtils = authUtils;
        this.cartRepository = cartRepository;
    }

    public ClientKeys getCLientSecretKey(CheckoutDTO checkoutDTO) {
        Cart cart = checkAuthForCart(checkoutDTO);
        long totalPrice = Math.round(cart.getTotalPrice()) * 100;

        if(totalPrice < 3000)
            totalPrice = 3000;

        StripeClient client = new StripeClient(stripe_SecretKey);

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
        String idempotencyKey = UUID.randomUUID().toString();

        RequestOptions options =
                RequestOptions.builder()
                        .setIdempotencyKey(idempotencyKey)
                        .build();

        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = client.v1().paymentIntents().create(params,options);
        }
        catch (StripeException e) {
            log.error(e.getMessage());
            throw new APIException(e.getMessage());
        }
        assert paymentIntent != null;
        String clientSecretKey = paymentIntent.getClientSecret();
        return new ClientKeys(idempotencyKey, clientSecretKey);
    }

    private Cart checkAuthForCart(CheckoutDTO checkoutDTO) {
        long userId = authUtils.loggedInUserId();
        System.out.println("userId = " + userId);
        System.out.println("checkoutDTO = " + checkoutDTO.getCartId());
        Cart cart = cartRepository.findById(checkoutDTO.getCartId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );

        if(!(cart.getUser().getUserId() == userId)) {
            throw new APIException("User doesn't belong to this cart");
        }
        return cart;
    }
}
