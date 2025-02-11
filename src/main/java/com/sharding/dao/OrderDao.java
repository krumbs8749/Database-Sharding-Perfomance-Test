package com.sharding.dao;

import com.sharding.entities.OrderEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderDao {

    private final EntityManager em;

    public OrderDao(EntityManager em) {
        this.em = em;
    }

    public OrderEntity findById(Long orderId) {
        return em.find(OrderEntity.class, orderId);
    }

    public List<OrderEntity> findAll() {
        return em.createQuery("SELECT o FROM OrderEntity o", OrderEntity.class).getResultList();
    }
    // Returns the first N orders ordered by orderId (or another relevant column)
    public List<OrderEntity> findFirstN(int n) {
        TypedQuery<OrderEntity> query = em.createQuery("SELECT o FROM OrderEntity o ORDER BY o.orderId ASC", OrderEntity.class);
        query.setMaxResults(n);
        return query.getResultList();
    }

    // Returns orders for a specific userId
    public List<OrderEntity> findByUserId(Long userId) {
        TypedQuery<OrderEntity> query = em.createQuery("SELECT o FROM OrderEntity o WHERE o.userId = :userId", OrderEntity.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public void save(OrderEntity entity) {
        em.persist(entity);
    }

    public void update(OrderEntity entity) {
        em.merge(entity);
    }

    public void delete(OrderEntity entity) {
        em.remove(entity);
    }

    public boolean existsById(Long orderId) {
        return (em.find(OrderEntity.class, orderId) != null);
    }
}
