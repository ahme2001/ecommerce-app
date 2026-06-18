package com.ecommerce.sb_ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private Long userId;

    private String jwtToken;

    private String usernaem;

    private List<String> roles;
}
