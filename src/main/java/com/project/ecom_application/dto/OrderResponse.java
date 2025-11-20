package com.project.ecom_application.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private List<OrderItemDto> items;

    private LocalDateTime createdAt;
}
