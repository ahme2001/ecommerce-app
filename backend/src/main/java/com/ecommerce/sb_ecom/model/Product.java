package com.ecommerce.sb_ecom.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@ToString
public class Product extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Product must have name")
    private String productName;

    @NotBlank(message = "Product must have description")
    private String description;

    private Double price;

    private Double discount;

    private String image;

    private Integer quantity;

    private Double specialPrice;

    @ManyToOne()
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "seller_id")
    private User user;


    @ToString.Exclude
    @OneToMany(mappedBy = "product",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private List<CartItems> cartItems = new ArrayList<>();
}
