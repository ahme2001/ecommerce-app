package com.ecommerce.sb_ecom.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckoutDTO {

    @NotBlank
    private long cartId;

    @NotBlank
    private long addressId;
}
