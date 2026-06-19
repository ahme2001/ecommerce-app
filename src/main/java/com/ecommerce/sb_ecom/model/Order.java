package com.ecommerce.sb_ecom.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String email;

    @OneToMany(mappedBy = "order",
                cascade = {CascadeType.PERSIST, CascadeType.MERGE},
                orphanRemoval = true)
    private List<OrderItem> orderItems;

    private LocalDateTime orderDate;

    private Double totalPrice;

    private String orderStatus;

    @ManyToOne()
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne()
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
