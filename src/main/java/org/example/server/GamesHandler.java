package org.example.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Daos.VideogameDao;
import org.example.Entities.Videogame;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// search for games based on game title
public class GamesHandler implements HttpHandler {

    private VideogameDao videogameDao;

    public GamesHandler(VideogameDao videogameDao) {
        this.videogameDao = videogameDao;
    }

    @Override
    public void handle(HttpExchange exchange)  {
        try {
            if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String gameQuery = exchange.getRequestURI().getQuery();
            System.out.println("query: " + gameQuery);

            Map<String, String> queryParams = parseQueryParams(gameQuery);


            if (queryParams.size() != 1 || queryParams.get("search") == null) {
                String response = "This endpoint only accepts one query parameter with the \"search\" key (eg. <url>?search=<game_title>)";
                ApiUtility.sendErrorResponse(exchange, 400, response);
                return;
            }

            List<Videogame> foundGames = videogameDao.findVideogame(queryParams.get("search"));

            Gson gson = new Gson();
            String gamesJson = gson.toJson(foundGames);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:5173"); // Allow the specific origin
            exchange.sendResponseHeaders(200, gamesJson.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(gamesJson.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            ApiUtility.sendErrorResponse(exchange, 400, "error processing json data");
        } finally {
            exchange.close();
        }

    }


    public Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();

        if (query == null || query.isEmpty()) {
            return queryParams;
        }

        try {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                System.out.println("pair: " + pair);
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
            return queryParams;
        } catch (Exception e) {
            e.printStackTrace();
            return queryParams;
        }

    }
}
