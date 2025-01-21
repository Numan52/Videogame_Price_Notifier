package org.example.Scrapers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.Entities.ScrapedVideogame;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Scraper {
    private OkHttpClient client = new OkHttpClient();

    public abstract String getSearchUrl(String searchTerm);
    public abstract ArrayList<ScrapedVideogame> parseDocument(Document document, String searchTerm);

    public ArrayList<ScrapedVideogame> scrapeSite(String searchTerm) throws IOException {
        ArrayList<ScrapedVideogame> matchingProducts = new ArrayList<>();
        String link = getSearchUrl(searchTerm);

        Request request = new Request.Builder()
                .url(link)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String html = response.body().string();
                Document document = Jsoup.parse(html);
                matchingProducts = parseDocument(document, searchTerm);

                return matchingProducts;
            } else {
                // Handle the error if response body is null
                System.err.println("Error: Response body is null.");
            }
        } catch (IOException e) {
            System.err.println("Error during request execution: " + e.getMessage());
            e.printStackTrace();
        }
        return matchingProducts;
    }
}
