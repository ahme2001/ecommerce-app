package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.payload.CheckoutDTO;
import com.ecommerce.sb_ecom.payload.ClientKeys;
import com.ecommerce.sb_ecom.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/client-key")
    public ResponseEntity<ClientKeys> getCLientSecretKey(@Valid @RequestBody CheckoutDTO  checkoutDTO) {
        return new ResponseEntity<>(paymentService.getCLientSecretKey(checkoutDTO), HttpStatus.CREATED);
    }


    @PostMapping("/webhook")
    public ResponseEntity<Void> receivePaymentIntent(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload) {
        paymentService.listenToStripeWebhook(signature,payload);
        return ResponseEntity.ok().build();
    }

}
