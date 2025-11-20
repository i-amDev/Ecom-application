package com.project.ecom_application.service;

import com.project.ecom_application.dto.OrderItemDto;
import com.project.ecom_application.dto.OrderResponse;
import com.project.ecom_application.dto.OrderStatus;
import com.project.ecom_application.entity.CartItem;
import com.project.ecom_application.entity.Order;
import com.project.ecom_application.entity.OrderItem;
import com.project.ecom_application.entity.User;
import com.project.ecom_application.repository.OrderRepository;
import com.project.ecom_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartService cartService;

    private final UserRepository userRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        // Validate for cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        // Validate for user
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        // Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();
        order.setItems(orderItems);
        Order savedEntity = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(order));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setItems(order.getItems()
                .stream()
                .map(orderItem -> new OrderItemDto(
                        orderItem.getId(),
                        orderItem.getProduct().getId(),
                        orderItem.getQuantity(),
                        orderItem.getPrice(),
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                )).toList());

        return orderResponse;
    }
}
