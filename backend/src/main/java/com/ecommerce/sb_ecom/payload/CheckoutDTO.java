package com.ecommerce.sb_ecom.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckoutDTO {

    @NotNull
    private long cartId;

    @NotNull(message = "You must choose or create you address")
    private long addressId;
}
