package com.ecommerce.sb_ecom.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long addressId = 0L;

    @NotBlank
    @Size(min = 5 , message = "Street name must be at least 5")
    private String street;

    @NotBlank
    @Size(min = 3 , message = "buildingName name must be at least 5")
    private String buildingName;

    @NotBlank
    @Size(min = 3 , message = "city name must be at least 5")
    private String city;

    @NotBlank
    @Size(min = 3 , message = "state name must be at least 5")
    private String state;

    @NotBlank
    @Size(min = 3 , message = "country name must be at least 5")
    private String country;

    @NotBlank
    @Size(min = 6 , message = "postalCode name must be at least 6")
    private String postalCode;
}
