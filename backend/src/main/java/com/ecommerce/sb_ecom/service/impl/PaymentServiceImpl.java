package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.APIException;
import com.ecommerce.sb_ecom.payload.ClientKeys;
import com.ecommerce.sb_ecom.service.PaymentService;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret.key}")
    public String stripe_SecretKey;


    public ClientKeys getCLientSecretKey() {
        StripeClient client = new StripeClient(stripe_SecretKey);

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(2500L)
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
}
