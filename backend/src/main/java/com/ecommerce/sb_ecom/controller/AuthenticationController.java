package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.payload.LoginRequest;
import com.ecommerce.sb_ecom.payload.RegisterDTO;
import com.ecommerce.sb_ecom.service.AuthenticationService;
import com.ecommerce.sb_ecom.utils.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterDTO loginRequest) {
        return authenticationService.registerUser(loginRequest);
    }

    @GetMapping("/username")
    public String getUsername(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        }
        return "";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        UserDetailsImpl  userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return authenticationService.getUserDetails(userDetails);
    }


    @PostMapping("/signout")
    public ResponseEntity<?> logout() {
        return authenticationService.logout();
    }
}
