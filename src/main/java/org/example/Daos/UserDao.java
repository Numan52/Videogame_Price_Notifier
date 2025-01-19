package org.example.Daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Entities.User;
import org.example.HibernateUtil;

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
}
