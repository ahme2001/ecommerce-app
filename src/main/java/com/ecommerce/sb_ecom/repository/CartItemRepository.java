package com.ecommerce.sb_ecom.repository;

import com.ecommerce.sb_ecom.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems,Long> {

    @Query("SELECT ci FROM CartItems ci WHERE ci.product.productId = ?1 AND ci.cart.cartId = ?2")
    CartItems findByProductIdAndCartId(Long productId, Long cartId);
}
