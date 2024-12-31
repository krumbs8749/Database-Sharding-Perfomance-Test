package com.sharding.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private Long orderId;

    private Long userId;
    private Double totalAmount;  // Use BigDecimal in production
}
