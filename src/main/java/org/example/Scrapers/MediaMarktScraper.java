package org.example.Scrapers;

import org.example.Entities.ScrapedVideogame;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class MediaMarktScraper extends Scraper {
    @Override
    public String getSearchUrl(String searchTerm) {
        return null;
    }

    @Override
    public ArrayList<ScrapedVideogame> parseDocument(Document document) {
        return null;
    }

    @Override
    public ArrayList<ScrapedVideogame> scrapeSite(String searchTerm) {
        return new ArrayList<>();
    }
}
