package org.example.Daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Entities.User;
import org.example.Entities.UserVideogame;
import org.example.Utils.HibernateUtil;

import java.util.List;

public class UserDao {
    private EntityManager entityManager;

    public UserDao() {
        entityManager = HibernateUtil.getEmFactory().createEntityManager();
    }

    public User findUser(String emailAddress) {
        try {
            return entityManager.createQuery("select u from users u where u.email = :email", User.class)
                    .setParameter("email", emailAddress)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.err.println("No user found with this email address");
            return null;
        }
    }

    public List<UserVideogame> findUserVideogames() {
        try {
            return entityManager.createQuery("select uv from UserVideogame uv", UserVideogame.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
