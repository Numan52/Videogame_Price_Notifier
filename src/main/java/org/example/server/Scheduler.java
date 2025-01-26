package org.example.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Daos.UserDao;
import org.example.Entities.ScrapedVideogame;
import org.example.Entities.UserVideogame;
import org.example.Scrapers.InstantGamingScraper;
import org.example.Scrapers.Scraper;
import org.example.Scrapers.SteamScraper;
import org.example.Utils.EmailSender;
import org.example.Utils.VideogamesUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Scheduler {
    private UserDao userDao;
    private Scraper[] scrapers;
    private ScheduledExecutorService executorService;
    private static Dotenv dotenv = Dotenv.load();
    private static Logger logger = LogManager.getLogger(Server.class);

    public Scheduler(UserDao userDao, Scraper[] scrapers, ScheduledExecutorService executorService) {
        this.userDao = userDao;
        this.scrapers = scrapers;
        this.executorService = executorService;
    }

    // TODO: ADD PARALLELISM
    public void scheduleTask() {
        Runnable task = () -> executeTask();

        try {
            executorService.scheduleAtFixedRate(task, 0, 24, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void executeTask() {
        Map<String, List<UserVideogame>> userVideogamesMap = new HashMap<>(); // map users to their registered games

        List<UserVideogame> userVideogames = userDao.findUserVideogames();
        if (userVideogames.isEmpty()) {
            System.out.println("user videogames is empty in executeTask()");
            return;
        }

        for (UserVideogame userVideogame : userVideogames) {
            userVideogamesMap.merge(
                    userVideogame.getUser().getEmail(),
                    new ArrayList<>(List.of(userVideogame)),
                    (oldList, newList) ->  {
                        oldList.addAll(newList);
                        return oldList;
                    }
            );
        }

        Map<String, List<ScrapedVideogame>> mappedVideogame = new HashMap<>(); // Map user to the scraped videogame with the lowest price

        for (Map.Entry<String, List<UserVideogame>> userVideogameEntry : userVideogamesMap.entrySet()) {
            // MAP<"numan@gmail.com", {(god of war, 30, 1), (fifa 18, 15, 1)}>

            Map<String, List<ScrapedVideogame>> scrapedGames = new HashMap<>(); // key = searchstring, value = list of scrapedVideogames (1 game per scraped website)

            for (UserVideogame userVideogame : userVideogameEntry.getValue()) {
                // (god of war, 30, 1)
                try {
                    scrapeVideogames(userVideogame, scrapedGames);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!scrapedGames.isEmpty()) {
                sendMail(scrapedGames, userVideogameEntry.getKey());
            }

        }
    }



    public void sendMail(Map<String, List<ScrapedVideogame>> games, String to) {
        StringBuilder htmlSb = new StringBuilder("<h2>Following Games are on sale:<h2>");
        StringBuilder textSb = new StringBuilder("Following Games are on sale:");

        for (Map.Entry<String, List<ScrapedVideogame>> entry : games.entrySet()) {
            textSb.append(entry.getKey()).append(": \n");
            htmlSb.append("<h3>").append(entry.getKey()).append(": </h3>");

            for (ScrapedVideogame game : entry.getValue()) {
                textSb.append(game.getWebsite()).append(": ")
                        .append(game.getPrice()).append("€ \n");

                htmlSb.append("<p>").append(game.getWebsite()).append(": ")
                        .append(game.getPrice()).append("€ \n").append("</p>");
            }

            textSb.append("\n");
            htmlSb.append("\n");
        }


        EmailSender emailSender = new EmailSender(
                "smtp.gmail.com",
                587,
                dotenv.get("EMAIL_ADDRESS"),
                dotenv.get("EMAIL_PASSWORD"),
                to,
                "Your bookmarked games are on sale!",
                textSb.toString(),
                htmlSb.toString()
        );
        emailSender.send();

    }


    public  void scrapeVideogames(UserVideogame userVideogame, Map<String, List<ScrapedVideogame>> allScrapedGames) throws IOException {

        for (Scraper scraper : scrapers) {
            List<ScrapedVideogame> scrapedGames = scraper.scrapeSite(userVideogame.getVideogame());
            List<ScrapedVideogame> filteredGames = VideogamesUtil.filterGames(scrapedGames);

            Optional<ScrapedVideogame> scrapedGame = filteredGames.stream()
                    .min((game1, game2) -> Float.compare(game1.getPrice(), game2.getPrice()));



            scrapedGame.ifPresent((_game) -> {
                if (_game.getPrice() <= userVideogame.getPriceThreshold()) {
                    allScrapedGames.merge(
                            _game.getSearchString(),
                            new ArrayList<>(List.of(_game)),
                            (oldList, newList) ->  {
                                oldList.addAll(newList);
                                return oldList;
                            }
                    );
                }

            });
        }
    }
}
