package org.example.Scrapers;

import org.example.Entities.ScrapedVideogame;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SteamScraper extends Scraper {

    private static String SEARCH_URL = "https://store.steampowered.com/search/?term=";
    private static String PRODUCT_DIV = "div.responsive_search_name_combined";
    private static String PRICE_DIV = "div.discount_final_price";
    private static String TITLE_SPAN = "span.title";
    private static int NUM_TITLES = 5;

    @Override
    public String getSearchUrl(String searchTerm) {
        return SEARCH_URL + searchTerm;
    }

    @Override
    public ArrayList<ScrapedVideogame> parseDocument(Document document) {
        ArrayList<ScrapedVideogame> matchingProducts = new ArrayList<>();

        Elements elements = document.select(PRODUCT_DIV);
        Elements titles = document.select(PRODUCT_DIV + " " + TITLE_SPAN);

        System.out.println(elements);

        for (int i = 0; i < Math.min(NUM_TITLES, titles.size()); i++) {
            ScrapedVideogame videogame = new ScrapedVideogame();
            videogame.setWebsite("Steam");

            Element title = elements.get(i).selectFirst(TITLE_SPAN);
            videogame.setName(title != null ? title.text() : "Unknown Title");

            Element priceElement = elements.get(i).selectFirst(PRICE_DIV);
            if (priceElement != null) {
                String priceString = priceElement.text().replace(",", ".");
                float price = Float.parseFloat(priceString.substring(0, priceString.length() - 1));
                videogame.setPrice(price);
            } else {
                videogame.setPrice(-1);
            }




            System.out.println(videogame.toString());
            System.out.println();

            matchingProducts.add(videogame);
        }

        return matchingProducts;
    }


}
