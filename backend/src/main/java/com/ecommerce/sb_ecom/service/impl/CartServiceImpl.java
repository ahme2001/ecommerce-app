package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.APIException;
import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.model.CartItems;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.model.User;
import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.repository.CartItemRepository;
import com.ecommerce.sb_ecom.repository.CartRepository;
import com.ecommerce.sb_ecom.repository.ProductRepository;
import com.ecommerce.sb_ecom.repository.UserRepository;
import com.ecommerce.sb_ecom.service.CartService;
import com.ecommerce.sb_ecom.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final AuthUtils authUtils;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(AuthUtils authUtils, UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository,
                           CartItemRepository cartItemRepository, ModelMapper modelMapper) {
        this.authUtils = authUtils;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDTO addProductToCart(String username, Long productId, Integer quantity) {
        // apply checks on username , productId and quantity
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Username " + username + " not found")
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product " + productId + " not found")
        );
        if (quantity < 0)
            throw new APIException("Quantity cannot be less than 0");
        if (product.getQuantity() < quantity)
            throw new ResourceNotFoundException("This product has only " + product.getQuantity() + " items");

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        CartItems cartItem = cartItemRepository.findByProductIdAndCartId(productId,cart.getCartId());

        // create cart item
        if (cartItem == null) {
            cartItem = new CartItems(cart, product, quantity, product.getDiscount(), product.getPrice());
        } else {
            throw new APIException("This product is already in cart");
        }
        cartItem = cartItemRepository.save(cartItem);

        // update cart with new item
        cart.setTotalPrice(cart.getTotalPrice() + product.getSpecialPrice() * quantity);
        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);

        return mappingCartToCartDTO(cart);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(this::mappingCartToCartDTO).toList();
    }

    @Override
    public CartDTO getCart() {
        User user = authUtils.loggedInUser();
        Cart cart = user.getCart();
        if (cart == null) {
            throw new APIException("Cart is empty");
        }
        return mappingCartToCartDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO updateQuantityForItemInCart(Long productId, String operation) {
        User user = authUtils.loggedInUser();
        Cart cart = user.getCart();
        if (cart == null) {
            throw new APIException("User doesn't have a cart now");
        }

        Double newCartPrice = cart.getTotalPrice();
        CartItems item = cartItemRepository.findByProductIdAndCartId(productId,cart.getCartId());
        if (item == null) {
            throw new ResourceNotFoundException("Product " + productId + " not found in this cart");
        }

        Product product = item.getProduct();
        Integer availableQuantity = product.getQuantity();
        if (productId.equals(product.getProductId())) {
            if (operation.equalsIgnoreCase("add")) {
                if (availableQuantity >= (item.getQuantity() + 1))
                    item.setQuantity(item.getQuantity() + 1);
                else
                    throw new APIException("There no available quantity for this product");
                newCartPrice = cart.getTotalPrice() + product.getSpecialPrice();
            }else if (operation.equalsIgnoreCase("subtract")) {
                if (item.getQuantity() > 1)
                    item.setQuantity(item.getQuantity() - 1);
                else {
                    cart.getCartItems().remove(item);
                }
                newCartPrice = cart.getTotalPrice() - product.getSpecialPrice();
            } else {
                throw new APIException("Invalid operation");
            }
            cartItemRepository.save(item);
        }

        cart.setTotalPrice(newCartPrice);
        cartRepository.save(cart);
        return mappingCartToCartDTO(cart);
    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        User user = authUtils.loggedInUser();
        Cart cart = user.getCart();
        if (cart == null || !cart.getCartId().equals(cartId)) {
            throw new APIException("User doesn't have a cart now");
        }

        CartItems item = cartItemRepository.findByProductIdAndCartId(productId,cart.getCartId());
        if (item == null) {
            throw new ResourceNotFoundException("Product " + productId + " not found in this cart");
        }
        System.out.println("price " + (cart.getTotalPrice() - ((item.getProductPrice() - (item.getProductPrice()* (item.getDiscount()*0.01))) * item.getQuantity())));
        cart.setTotalPrice(cart.getTotalPrice() - ((item.getProductPrice() - item.getProductPrice()* item.getDiscount()) * item.getQuantity()));
        cartItemRepository.delete(item);
        cartRepository.save(cart);
        return "Item deleted successfully";
    }

    private CartDTO mappingCartToCartDTO(Cart cart) {
        CartDTO c = new CartDTO();
        c.setCartId(cart.getCartId());
        c.setTotalPrice(cart.getTotalPrice());
        List<ProductDTO> p = cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            ProductDTO tmp =  modelMapper.map(product, ProductDTO.class);
            Integer quantity = (product.getQuantity() < cartItem.getQuantity()) ? product.getQuantity() : cartItem.getQuantity();
            tmp.setQuantity(quantity);
            return tmp;
        }).toList();
        c.setProducts(p);
        return c;
    }
}
