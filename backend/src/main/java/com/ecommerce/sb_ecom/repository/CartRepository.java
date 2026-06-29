package com.ecommerce.sb_ecom.repository;

import com.ecommerce.sb_ecom.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByPaymentIntentId(String paymentIntentId);
}
