package com.ecommerce.sb_ecom.repository;

import com.ecommerce.sb_ecom.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems,Long> {
}
