package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static EntityManagerFactory EM_FACTORY = null;

    public static synchronized EntityManagerFactory getEmFactory() {
        if (EM_FACTORY == null) {
            EM_FACTORY = Persistence.createEntityManagerFactory("videogame_scraper");
        }
        return EM_FACTORY;
    }

    public static void closeEmFactory() {
        if (EM_FACTORY.isOpen()) {
            EM_FACTORY.close();
        }
    }

}
