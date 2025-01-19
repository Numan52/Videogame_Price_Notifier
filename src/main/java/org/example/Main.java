package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.Daos.UserDao;
import org.example.Daos.VideogameDao;
import org.example.Entities.User;
import org.example.Entities.UserVideogame;
import org.example.Entities.Videogame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final UserDao userDao = new UserDao();
    private static final VideogameDao videogameDao = new VideogameDao();
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Expected input format: <game1> <priceThreshold> <emailAddress>");
            return;
        }

        String game = null;
        Float priceThreshold = null;
        String emailAddress = null;
        boolean isNewUser = false;

        try {
            game = args[0];
            priceThreshold = Float.parseFloat(args[1]); // notify only if price is same or lower
            emailAddress = args[2];
        } catch (NumberFormatException e) {
            System.err.println("Invalid price threshold. Must be a number");
        }


        EntityManagerFactory emFactory = HibernateUtil.getEmFactory();
        EntityManager em = emFactory.createEntityManager();

        try {
            User user = userDao.findUser(emailAddress);
            if (user == null) {
                isNewUser = true;
                user = new User(emailAddress, new ArrayList<>());
            }

            List<Videogame> videogames = videogameDao.findVideogame(game);
            if (videogames.isEmpty()) {
                System.out.println(("The videogame was not found. Please enter the full title of the game"));
                return;
            } else {
                System.out.println(videogames.toString());
            }

            EntityTransaction transaction = em.getTransaction();
            try {

                transaction.begin();

                user.getSubbedGames().add(new UserVideogame(user, game, priceThreshold));

                if (isNewUser) {
                    em.persist(user);
                } else {
                    em.merge(user);
                }

                transaction.commit();

            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}