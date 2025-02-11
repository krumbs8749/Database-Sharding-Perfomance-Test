package com.sharding.resources;

import com.sharding.EMFProvider;
import com.sharding.dao.OrderDao;
import com.sharding.entities.OrderEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @GET
    public List<OrderEntity> getAllOrders() {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        try {
            OrderDao dao = new OrderDao(em);
            return dao.findAll();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("orderId") Long orderId) {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        try {
            OrderDao dao = new OrderDao(em);
            OrderEntity entity = dao.findById(orderId);
            if (entity == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(entity).build();
        } finally {
            em.close();
        }
    }

    // New endpoint: Retrieve only the first 100 orders (useful as a multi-shard query)
    @GET
    @Path("/firstN/{n}")
    public List<OrderEntity> getFirst100Orders(@PathParam("n") int  n) {
        var em = EMFProvider.getEMF().createEntityManager();
        try {
            OrderDao dao = new OrderDao(em);
            // You should implement the findFirstN(int n) method in your OrderDao.
            return dao.findFirstN(n);
        } finally {
            em.close();
        }
    }

    // New endpoint: Retrieve orders by a given user id (multi-shard query since the shard key is not used)
    @GET
    @Path("/by-user/{userId}")
    public List<OrderEntity> getOrdersByUserId(@PathParam("userId") Long userId) {
        var em = EMFProvider.getEMF().createEntityManager();
        try {
            OrderDao dao = new OrderDao(em);
            // You should implement the findByUserId(Long userId) method in your OrderDao.
            return dao.findByUserId(userId);
        } finally {
            em.close();
        }
    }

    @POST
    public Response createOrder(OrderEntity order) {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            OrderDao dao = new OrderDao(em);
            tx.begin();
            dao.save(order);
            tx.commit();
            return Response.status(Response.Status.CREATED).entity(order).build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.serverError().entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("/{orderId}")
    public Response updateOrder(@PathParam("orderId") Long orderId, OrderEntity updatedOrder) {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            OrderDao dao = new OrderDao(em);
            OrderEntity existing = dao.findById(orderId);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            tx.begin();
            updatedOrder.setOrderId(orderId);
            dao.update(updatedOrder);
            tx.commit();
            return Response.ok(updatedOrder).build();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return Response.serverError().entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("/{orderId}")
    public Response deleteOrder(@PathParam("orderId") Long orderId) {
        EntityManager em = EMFProvider.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            OrderDao dao = new OrderDao(em);
            OrderEntity existing = dao.findById(orderId);
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
