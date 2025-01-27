package org.example.Daos;

import jakarta.persistence.EntityManager;
import org.example.Entities.Videogame;
import org.example.Utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class VideogameDao {


    public List<Videogame> findVideogame(String game) {
        EntityManager entityManager = HibernateUtil.getEmFactory().createEntityManager();

        try {
            return entityManager.createQuery("select distinct v.name from Videogame v where lower(v.name) like lower(:game) ", Videogame.class)
                    .setParameter("game", game + "%")
                    .setMaxResults(5)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error finding videogame");
        }

        return new ArrayList<>();
    }
}
