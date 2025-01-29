package org.example.server;

import com.sun.net.httpserver.HttpServer;
import org.example.Daos.UserDao;
import org.example.Daos.VideogameDao;
import org.example.Scrapers.InstantGamingScraper;
import org.example.Scrapers.Scraper;
import org.example.Scrapers.SteamScraper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;


public class Server {


    public static void main(String[] args) {
        VideogameDao videogameDao = new VideogameDao();
        UserDao userDao = new UserDao();

        Scheduler scheduler = new Scheduler(
                new UserDao(),
                new Scraper[]{new InstantGamingScraper(), new SteamScraper()},
                Executors.newScheduledThreadPool(10)
        );

        scheduler.scheduleTask();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/games", new GamesHandler(videogameDao));
            server.createContext("/api/subscription", new SubscriptionHandler(userDao));
            server.setExecutor(Executors.newFixedThreadPool(10));
            server.start();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("server listening on port 8080");




    }



}
