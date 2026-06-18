package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.payload.CartDTO;

import java.util.List;

public interface CartService {

    CartDTO addProductToCart(String username, Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart();

    CartDTO updateQuantityForItemInCart(Long productId, String operation);

    String deleteProductFromCart(Long cartId, Long productId);
}
