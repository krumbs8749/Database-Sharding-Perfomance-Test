package com.sharding.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;       // Sharding column, references orders.order_id

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "price", nullable = false)
    private BigDecimal price;       // Or BigDecimal for production
}
