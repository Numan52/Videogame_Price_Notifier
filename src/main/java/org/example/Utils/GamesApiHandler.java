package org.example.Utils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import okhttp3.*;
import org.example.Entities.Videogame;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;

public class GamesApiHandler {
    private static Dotenv DOTENV = Dotenv.load();
    private static final String API_URL = "https://api.igdb.com/v4/games";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static String CLIENT_ID = DOTENV.get("IGDB_CLIENT_ID");
    private static String ACCESS_TOKEN = DOTENV.get("IGDB_ACCESS_TOKEN");
    private static final EntityManagerFactory emFactory = HibernateUtil.getEmFactory();

    private static void fillVideogamesTable() throws IOException {
        MediaType mediaType = MediaType.get("text/plain; charset=utf-8");
        int i = 0;

        try (EntityManager entityManager = emFactory.createEntityManager()) {
            while (true) {
                String requestBody = String.format(
                        "fields name;" +
                                "offset %d;" +
                                "limit 500;",
                        i
                );
                Request request = new Request.Builder()
                        .url(API_URL)
                        .addHeader("Client-ID", CLIENT_ID)
                        .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                        .method("POST", RequestBody.create(requestBody, mediaType))
                        .build();

                String body;
                try (Response response = CLIENT.newCall(request).execute()) {
                    body = response.body().string();

                }

                JSONArray games = new JSONArray(body);
                if (games.isEmpty()) {
                    break;
                }

                System.out.println(games);
                addGamesToDb(games, entityManager);

                i += 500; // next page
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static void addGamesToDb(JSONArray games, EntityManager entityManager) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            entityManager.getTransaction().begin();

            for (int i = 0; i < games.length(); i++) {
                JSONObject game = games.getJSONObject(i);
                Videogame videogame = new Videogame(game.getString("name"));

                entityManager.persist(videogame);
            }
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error saving games to db: " + e.getMessage());
        }

    }



    public static void main(String[] args) throws IOException {
//        GamesApiHandler.fillVideogamesTable();
    }

}
