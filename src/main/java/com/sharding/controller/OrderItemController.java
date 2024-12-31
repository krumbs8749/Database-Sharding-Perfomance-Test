package com.sharding.controller;

import com.sharding.entities.OrderItemEntity;
import com.sharding.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;

    @GetMapping
    public List<OrderItemEntity> getAllOrderItems() {
        // Returns all order items
        return orderItemRepository.findAll();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItemEntity> getOrderItemById(@PathVariable Long itemId) {
        // Returns a single order item by ID
        return orderItemRepository.findById(itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderItemEntity> createOrderItem(@RequestBody OrderItemEntity item) {
        // Creates a new order item
        OrderItemEntity saved = orderItemRepository.save(item);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<OrderItemEntity> updateOrderItem(
            @PathVariable Long itemId,
            @RequestBody OrderItemEntity updatedItem
    ) {
        // Updates an existing order item
        if (!orderItemRepository.existsById(itemId)) {
            return ResponseEntity.notFound().build();
        }
        updatedItem.setItemId(itemId);
        OrderItemEntity saved = orderItemRepository.save(updatedItem);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long itemId) {
        // Deletes order item by ID
        if (!orderItemRepository.existsById(itemId)) {
            return ResponseEntity.notFound().build();
        }
        orderItemRepository.deleteById(itemId);
        return ResponseEntity.noContent().build();
    }
}
