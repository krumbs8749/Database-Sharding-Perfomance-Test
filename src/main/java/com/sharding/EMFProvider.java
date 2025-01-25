package com.sharding;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EMFProvider {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("ShardingPU");

    public static EntityManagerFactory getEMF() {
        return EMF;
    }
}
