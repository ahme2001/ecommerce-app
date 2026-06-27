package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.payload.ClientKeys;
import com.ecommerce.sb_ecom.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/client-key")
    public ResponseEntity<ClientKeys> getCLientSecretKey() {
        return new ResponseEntity<>(paymentService.getCLientSecretKey(), HttpStatus.CREATED);
    }

}
