package org.example.Utils;

import org.example.Entities.ScrapedVideogame;

import java.util.List;
import java.util.stream.Collectors;

public class VideogamesUtil {

    public static List<ScrapedVideogame> filterGames(List<ScrapedVideogame> scrapedGames) {
        return scrapedGames.stream()
                .filter((game) -> game.getName().equalsIgnoreCase(game.getSearchString()) && game.getPrice() != -1)
                .toList();
    }
}
