package org.example.Scrapers;

import org.example.Entities.ScrapedVideogame;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class InstantGamingScraper extends Scraper {

    private static String SEARCH_URL = "https://www.instant-gaming.com/de/suche/?q=";
    private static String PRODUCT_DIV = "div.item.force-badge";
    private static String PRICE_DIV = "div.price";
    private static String TITLE_SPAN = "span.title";


    @Override
    public String getSearchUrl(String searchTerm) {
        return SEARCH_URL + searchTerm;
    }

    @Override
    public ArrayList<ScrapedVideogame> parseDocument(Document document) {
        ArrayList<ScrapedVideogame> matchingProducts = new ArrayList<>();

        Elements elements = document.select(PRODUCT_DIV);
        Elements titles = document.select(PRODUCT_DIV + " " + TITLE_SPAN);

        for (int i = 0; i < titles.size(); i++) {
            ScrapedVideogame videogame = new ScrapedVideogame();
            videogame.setWebsite("Instant Gaming");

            Element title = elements.get(i).selectFirst(TITLE_SPAN);
            videogame.setName(title != null ? title.text() : "Unknown Title");

            Element priceElement = elements.get(i).selectFirst(PRICE_DIV);
            float price = priceElement != null ?
                    Float.parseFloat(priceElement.text().substring(0, priceElement.text().length() - 1)) :
                    -1;
            videogame.setPrice(price);


            System.out.println(videogame.toString());
            System.out.println();

            matchingProducts.add(videogame);
        }

        return matchingProducts;
    }



}
