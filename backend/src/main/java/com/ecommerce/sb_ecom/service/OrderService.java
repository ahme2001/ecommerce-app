package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.payload.OrderDTO;
import com.ecommerce.sb_ecom.payload.OrderRequestDTO;

public interface OrderService {

    OrderDTO placeOrder(String paymentMethod,OrderRequestDTO orderRequestDTO);
}
