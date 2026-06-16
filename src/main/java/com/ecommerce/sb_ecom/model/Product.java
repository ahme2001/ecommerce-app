package com.ecommerce.sb_ecom.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

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

}
