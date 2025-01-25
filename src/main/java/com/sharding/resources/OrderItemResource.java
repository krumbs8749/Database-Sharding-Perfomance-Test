package com.sharding.resources;

import com.sharding.EMFProvider;
import com.sharding.dao.OrderItemDao;
import com.sharding.entities.OrderItemEntity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/order-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderItemResource {
    
    @GET
    public List<OrderItemEntity> getAllOrderItems() {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        try {
            OrderItemDao dao = new OrderItemDao(em);
            return dao.findAll();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/{itemId}")
    public Response getOrderItemById(@PathParam("itemId") Long itemId) {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        try {
            OrderItemDao dao = new OrderItemDao(em);
            OrderItemEntity entity = dao.findById(itemId);
            if (entity == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(entity).build();
        } finally {
            em.close();
        }
    }

    @POST
    public Response createOrderItem(OrderItemEntity item) {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            OrderItemDao dao = new OrderItemDao(em);
            tx.begin();
            dao.save(item);
            tx.commit();
            return Response.status(Response.Status.CREATED).entity(item).build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.serverError().entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("/{itemId}")
    public Response updateOrderItem(@PathParam("itemId") Long itemId, OrderItemEntity updated) {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            OrderItemDao dao = new OrderItemDao(em);
            OrderItemEntity existing = dao.findById(itemId);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            tx.begin();
            updated.setItemId(itemId);
            dao.update(updated);
            tx.commit();
            return Response.ok(updated).build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.serverError().entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("/{itemId}")
    public Response deleteOrderItem(@PathParam("itemId") Long itemId) {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            OrderItemDao dao = new OrderItemDao(em);
            OrderItemEntity existing = dao.findById(itemId);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            tx.begin();
            dao.delete(existing);
            tx.commit();
            return Response.noContent().build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.serverError().entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }
}
