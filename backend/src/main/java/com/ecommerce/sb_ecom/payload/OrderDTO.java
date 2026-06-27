package com.ecommerce.sb_ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private List<OrderItemDTO> items;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Double totalPrice;
    private PaymentDTO paymentDTO;
    private Long addressId;
}
