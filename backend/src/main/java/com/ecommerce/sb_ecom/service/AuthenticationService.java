package com.ecommerce.sb_ecom.service;


import com.ecommerce.sb_ecom.payload.LoginRequest;
import com.ecommerce.sb_ecom.payload.RegisterDTO;
import com.ecommerce.sb_ecom.utils.UserDetailsImpl;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> registerUser(RegisterDTO registerDTO);

    ResponseEntity<?> getUserDetails(UserDetailsImpl userDetails);

    ResponseEntity<?> logout();
}
