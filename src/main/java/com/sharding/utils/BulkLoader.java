package com.sharding.utils;

import com.opencsv.CSVReader;
import com.sharding.entities.OrderEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BulkLoader {

    public static void main(String[] args) {
        String csvFileName = "SeedData.csv";
        bulkInsertOrders(csvFileName);
    }

    public static void bulkInsertOrders(String csvFileName) {
        // Create EMF from our persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ShardingPU");
        EntityManager em = emf.createEntityManager();

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(
                        BulkLoader.class.getClassLoader().getResourceAsStream(csvFileName)
                )
        )) {

            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            String[] line;
            List<OrderEntity> batch = new ArrayList<>();
            int batchSize = 50;

            reader.readNext(); // Skip header row
            while ((line = reader.readNext()) != null) {
                OrderEntity order = new OrderEntity();
                order.setOrderId(Long.parseLong(line[0]));
                order.setUserId(Long.parseLong(line[1]));
                order.setTotalAmount(BigDecimal.valueOf(Double.parseDouble(line[2])));


                batch.add(order);

                if (batch.size() == batchSize) {
                    for (OrderEntity o : batch) {
                        em.persist(o);
                    }
                    batch.clear();
                    em.flush();
                    em.clear();
                }


            }

            // Insert remaining
            for (OrderEntity o : batch) {
                em.persist(o);
            }

            transaction.commit();
            System.out.println("Bulk insert completed!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
