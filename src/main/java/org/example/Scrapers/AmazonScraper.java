package org.example.Scrapers;

import org.example.Entities.ScrapedVideogame;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class AmazonScraper extends Scraper {

    private static String SEARCH_URL = "https://www.amazon.de/s?k=";
    private static String productDiv = "div.puisg-col.puisg-col-4-of-12.puisg-col-8-of-16.puisg-col-12-of-20.puisg-col-12-of-24.puis-list-col-right";
    private static String titleSpan = "span.a-size-medium.a-color-base.a-text-normal";
    private static String priceSpan = "span.a-price > span.a-offscreen";
    private static String altPriceSpan = "div.a-row.a-size-base.a-color-secondary > span.a-color-base"; // sold by other vendors

    @Override
    public String getSearchUrl(String searchTerm) {
        return SEARCH_URL + searchTerm;
    }

    @Override
    public ArrayList<ScrapedVideogame> parseDocument(Document document , String searchTerm) {
        ArrayList<ScrapedVideogame> matchingProducts = new ArrayList<>();

        Elements elements = document.select(productDiv);
        Elements titles = document.select(productDiv + " " + titleSpan);

        for (int i = 0; i < titles.size(); i++) {
            ScrapedVideogame videogame = new ScrapedVideogame();
            videogame.setWebsite("Amazon");
            videogame.setSearchString(searchTerm);

            Element title = elements.get(i).selectFirst(titleSpan);
            videogame.setName(title != null ? title.text() : "Unknown Title");

            Element price = elements.get(i).selectFirst(priceSpan);
            if (price == null) {
                Element altPrice = elements.get(i).selectFirst(altPriceSpan);
                videogame.setPrice(altPrice != null ? Float.parseFloat(altPrice.text()) : -1);
            } else {
                videogame.setPrice(Float.parseFloat(price.text()));
            }



            matchingProducts.add(videogame);
        }

        return matchingProducts;
    }


}
