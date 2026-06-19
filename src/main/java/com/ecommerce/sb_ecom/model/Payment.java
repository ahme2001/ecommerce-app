package com.ecommerce.sb_ecom.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String paymentMethod;

    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    @OneToOne(mappedBy = "payment",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Order order;


    public Payment(String paymentMethod, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName, Order order) {
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
        this.order = order;
    }

}
