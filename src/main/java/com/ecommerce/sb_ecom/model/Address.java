package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5 , message = "Street name must be at least 5")
    private String street;

    @NotBlank
    @Size(min = 5 , message = "buildingName name must be at least 5")
    private String buildingName;

    @NotBlank
    @Size(min = 5 , message = "city name must be at least 5")
    private String city;

    @NotBlank
    @Size(min = 5 , message = "state name must be at least 5")
    private String state;

    @NotBlank
    @Size(min = 5 , message = "country name must be at least 5")
    private String country;

    @NotBlank
    @Size(min = 6 , message = "postalCode name must be at least 6")
    private String postalCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();
}
