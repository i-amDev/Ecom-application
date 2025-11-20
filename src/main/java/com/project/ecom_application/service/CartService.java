package com.project.ecom_application.service;

import com.project.ecom_application.dto.CartItemRequest;
import com.project.ecom_application.entity.CartItem;
import com.project.ecom_application.entity.Product;
import com.project.ecom_application.entity.User;
import com.project.ecom_application.repository.CartItemRepository;
import com.project.ecom_application.repository.ProductRepository;
import com.project.ecom_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
        Optional<Product> productOptional = productRepository.findById(cartItemRequest.getProductId());

        if (productOptional.isEmpty()) return false;

        Product product = productOptional.get();

        if (product.getStockQuantity() < cartItemRequest.getQuantity()) return false;

        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        if (userOptional.isEmpty()) return false;

        User user = userOptional.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            // Update the quantity.
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }
        else {
            // Create a new cart item.
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        Optional<Product> productOptional = productRepository.findById(productId);

        if (userOptional.isPresent() && productOptional.isPresent()) {
            cartItemRepository.deleteByUserAndProduct(userOptional.get(), productOptional.get());
            return true;
        }

        return false;
    }

    public List<CartItem> getCart(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }

    @Transactional
    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId))
                .ifPresent(user -> cartItemRepository.deleteByUser(user));
    }

}
