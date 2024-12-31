package com.sharding.controller;

import com.sharding.entities.OrderEntity;
import com.sharding.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping
    public List<OrderEntity> getAllOrders() {
        // Returns all orders (ShardingSphere will handle distribution if sharded)
        return orderRepository.findAll();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable Long orderId) {
        // Returns a single order by ID
        return orderRepository.findById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity order) {
        // Creates a new order; ShardingSphere routes insert based on orderId
        OrderEntity saved = orderRepository.save(order);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderEntity> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderEntity updatedOrder
    ) {
        // Updates an existing order
        if (!orderRepository.existsById(orderId)) {
            return ResponseEntity.notFound().build();
        }
        updatedOrder.setOrderId(orderId);  // Ensure ID matches path variable
        OrderEntity saved = orderRepository.save(updatedOrder);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        // Deletes order by ID
        if (!orderRepository.existsById(orderId)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }
}
