package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItems {
    /*
     * This table represent relationship between cart and product
     * in which cart can contain many products
     * and product also can put in many cards
     * so relation is many to many
     */


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    private Double discount;

    private Double productPrice;

    private Integer quantity;

    @ManyToOne()
    @JoinColumn(name = "cart_id")
    private Cart cart;


    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    public CartItems(Cart cart, Product product, Integer quantity,  Double discount, Double productPrice) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
        this.productPrice = productPrice;
    }
}
