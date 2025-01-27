package org.example.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.Daos.UserDao;
import org.example.Entities.User;
import org.example.Entities.UserVideoGameDto;
import org.example.Entities.UserVideogame;
import org.example.Utils.HibernateUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SubscriptionHandler implements HttpHandler {
    private UserDao userDao;

    public SubscriptionHandler(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:5173"); // Allow the specific origin

        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        System.out.println("before parsebody");
        UserVideoGameDto userVideoGameDto = parseBody(exchange);
        if (userVideoGameDto == null) {
            exchange.close();
            return;
        }

        System.out.println("after parsebody");

        EntityManagerFactory emFactory = HibernateUtil.getEmFactory();
        EntityManager em = emFactory.createEntityManager();

        boolean isNewUser = false;

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            User user = userDao.findUser(userVideoGameDto.getEmail());
            if (user == null) {
                isNewUser = true;
                user = new User(userVideoGameDto.getEmail(), new ArrayList<>());
            }

            for (UserVideogame subscribedGame : user.getSubbedGames()) {
                if (subscribedGame.getVideogame().equals(userVideoGameDto.getGameTitle())) {
                    ApiUtility.sendErrorResponse(exchange, 400, "You are already subscribed to " + "\"" + userVideoGameDto.getGameTitle() + "\"");
                    return;
                }
            }

            user.getSubbedGames().add(new UserVideogame(user, userVideoGameDto.getGameTitle(), userVideoGameDto.getPriceThreshold()));

            if (isNewUser) {
                em.persist(user);
            } else {
                em.merge(user);
            }

            transaction.commit();


            exchange.sendResponseHeaders(200, -1);

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            em.close();
            exchange.close();
        }

    }


    public UserVideoGameDto parseBody(HttpExchange exchange) {
        Gson gson = new Gson();
        try {
            InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (!jsonElement.isJsonObject()) {
                ApiUtility.sendErrorResponse(exchange, 400, "Incorrect json format");
                return null;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : null;
            String gameTitle = jsonObject.has("gameTitle") ? jsonObject.get("gameTitle").getAsString() : null;
            String priceThreshold = jsonObject.has("priceThreshold") ? jsonObject.get("priceThreshold").getAsString() : null;

            if (email == null || gameTitle == null || priceThreshold == null) {
                ApiUtility.sendErrorResponse(exchange, 400, "email, gameTitle and priceThreshold fields required.");
                return null;
            }

            return new UserVideoGameDto(email, gameTitle, Float.parseFloat(priceThreshold));
        } catch (Exception e) {
            e.printStackTrace();
            ApiUtility.sendErrorResponse(exchange, 400, "Incorrect json format");
            return null;
        }

    }
}
