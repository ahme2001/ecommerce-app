package com.ecommerce.sb_ecom.service.impl;


import com.ecommerce.sb_ecom.exception.APIException;
import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.*;
import com.ecommerce.sb_ecom.repository.*;
import com.ecommerce.sb_ecom.service.OrderService;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;


    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void placeOrder(String paymentIntentId, Long paidAmount) {
        Cart cart = cartRepository.findByPaymentIntentId(paymentIntentId).orElseThrow(
                () -> new APIException("Payment Intent not found.")
        );
        User user = cart.getUser();

        Address address = cart.getAddress();
        if (address == null) {
            throw new ResourceNotFoundException("Address doesn't exist.");
        }

        Order order = createOrder(user,cart,address);

        List<CartItems> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty.");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;
        for (CartItems cartItem : cartItems) {
            OrderItem orderItem = getOrderItem(cartItem, order);
            orderItems.add(orderItem);
            totalPrice = totalPrice + ((cartItem.getProductPrice() - (cartItem.getProductPrice() * (cartItem.getDiscount() * 0.01))) * cartItem.getQuantity());
        }
        orderItems = orderItemRepository.saveAll(orderItems);

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setPaidAmount(paidAmount);
        orderRepository.save(order);

        user.setCart(null);
        userRepository.save(user);
        cartRepository.deleteById(cart.getCartId());
    }

    private @NonNull OrderItem getOrderItem(CartItems cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        Product product = cartItem.getProduct();
        orderItem.setProduct(product);
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setDiscount(cartItem.getDiscount());
        orderItem.setOrderProductPrice(cartItem.getProductPrice());

        int quantity = (cartItem.getQuantity() >= product.getQuantity()) ? 0 :  (product.getQuantity() - cartItem.getQuantity());
        product.setQuantity(quantity);
        productRepository.save(product);
        return orderItem;
    }

    private Order createOrder(User user,Cart cart, Address address) {
        Order order = new Order();
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("Processing");
        order.setEmail(user.getEmail());
        order.setTotalPrice(cart.getTotalPrice());

        return orderRepository.save(order);
    }
}
