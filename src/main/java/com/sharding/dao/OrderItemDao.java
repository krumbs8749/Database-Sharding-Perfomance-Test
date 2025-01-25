package com.sharding.dao;

import com.sharding.entities.OrderItemEntity;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderItemDao {

    private final EntityManager em;

    public OrderItemDao(EntityManager em) {
        this.em = em;
    }

    public OrderItemEntity findById(Long orderId) {
        return em.find(OrderItemEntity.class, orderId);
    }

    public List<OrderItemEntity> findAll() {
        return em.createQuery("SELECT o FROM OrderItemEntity o", OrderItemEntity.class).getResultList();
    }

    public void save(OrderItemEntity entity) {
        em.persist(entity);
    }

    public void update(OrderItemEntity entity) {
        em.merge(entity);
    }

    public void delete(OrderItemEntity entity) {
        em.remove(entity);
    }

    public boolean existsById(Long orderId) {
        return (em.find(OrderItemEntity.class, orderId) != null);
    }
}
