package com.project.ecom_application.repository;

import com.project.ecom_application.entity.CartItem;
import com.project.ecom_application.entity.Product;
import com.project.ecom_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByUserAndProduct(User user, Product product);
}
