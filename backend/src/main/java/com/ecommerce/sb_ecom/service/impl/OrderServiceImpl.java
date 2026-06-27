package com.ecommerce.sb_ecom.service.impl;


import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.*;
import com.ecommerce.sb_ecom.payload.*;
import com.ecommerce.sb_ecom.repository.*;
import com.ecommerce.sb_ecom.service.OrderService;
import com.ecommerce.sb_ecom.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final AuthUtils  authUtils;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public OrderServiceImpl(AuthUtils authUtils, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, PaymentRepository paymentRepository, CartRepository cartRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.authUtils = authUtils;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(String paymentMethod, OrderRequestDTO orderRequestDTO) {
        User user = authUtils.loggedInUser();
        Cart cart = user.getCart();
        if(cart == null){
            throw new ResourceNotFoundException("User doesn't have cart till now.");
        }
        Address address = user.getAddresses().stream().filter( add ->
                add.getAddressId().equals(orderRequestDTO.getAddressId())
        ).findFirst().orElse(null);
        if (address == null) {
            throw new ResourceNotFoundException("Address doesn't exist.");
        }

        Order order = createOrder(user,cart,address,orderRequestDTO,paymentMethod);

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
        order = orderRepository.save(order);

        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() - orderItem.getQuantity());
            productRepository.save(product);
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
            orderItemDTO.setProductDTO(productDTO);
            orderItemDTOS.add(orderItemDTO);
        }

        user.setCart(null);
        userRepository.save(user);
        cartRepository.deleteById(cart.getCartId());

        PaymentDTO paymentDTO = modelMapper.map(order.getPayment(), PaymentDTO.class);
        return new OrderDTO(order.getOrderId(),
                user.getEmail(), orderItemDTOS,order.getOrderDate(),
                order.getOrderStatus(), order.getTotalPrice(),
                paymentDTO, address.getAddressId());
    }

    private @NonNull OrderItem getOrderItem(CartItems cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        Product product = cartItem.getProduct();
        orderItem.setProduct(product);
        if (product.getQuantity() >=  cartItem.getQuantity())
            orderItem.setQuantity(cartItem.getQuantity());
        else
            throw new ResourceNotFoundException("Product doesn't have enough quantity.");
        orderItem.setDiscount(cartItem.getDiscount());
        orderItem.setOrderProductPrice(cartItem.getProductPrice());
        return orderItem;
    }

    private Order createOrder(User user,Cart cart, Address address, OrderRequestDTO orderRequestDTO, String paymentMethod) {
        Order order = new Order();
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("Processing");
        order.setEmail(user.getEmail());
        order.setTotalPrice(cart.getTotalPrice());

        Payment payment = new Payment(paymentMethod,
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage(),
                orderRequestDTO.getPgName(),
                order);

        payment = paymentRepository.save(payment);
        order.setPayment(payment);
        return orderRepository.save(order);
    }
}
