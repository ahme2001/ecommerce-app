package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.payload.CheckoutDTO;
import com.ecommerce.sb_ecom.payload.ClientKeys;

public interface PaymentService {

    ClientKeys getCLientSecretKey(CheckoutDTO checkoutDTO);

    void listenToStripeWebhook(String signature, String payload);
}
