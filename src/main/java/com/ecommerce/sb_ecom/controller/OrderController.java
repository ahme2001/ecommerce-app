package com.ecommerce.sb_ecom.controller;


import com.ecommerce.sb_ecom.payload.OrderDTO;
import com.ecommerce.sb_ecom.payload.OrderRequestDTO;
import com.ecommerce.sb_ecom.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
            OrderDTO orderDTO = orderService.placeOrder(paymentMethod,orderRequestDTO);
            return new  ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
