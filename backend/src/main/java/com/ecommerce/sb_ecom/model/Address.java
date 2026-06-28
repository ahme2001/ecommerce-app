package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "address")
public class Address extends  BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    private String street;
    private String buildingName;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
