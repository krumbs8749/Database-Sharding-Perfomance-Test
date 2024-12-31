package com.sharding.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    private Long itemId;

    private Long orderId;       // Sharding column, references orders.order_id
    private String productName;
    private Double price;       // Or BigDecimal for production
}
