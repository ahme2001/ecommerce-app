package com.ecommerce.sb_ecom.service;

public interface OrderService {

    void placeOrder(String paymentIntentId, Long paidAmount);
}
