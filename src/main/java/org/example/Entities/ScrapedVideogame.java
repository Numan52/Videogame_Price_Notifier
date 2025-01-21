package org.example.Entities;

public class ScrapedVideogame {
    private String searchString;
    private String name;
    private float price;
    private String website;


    public ScrapedVideogame() {
    }

    public ScrapedVideogame(String searchString, String name, float price, String website) {
        this.searchString = searchString;
        this.name = name;
        this.price = price;
        this.website = website;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    @Override
    public String toString() {
        return "ScrapedVideogame{" +
                "searchString='" + searchString + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", website='" + website + '\'' +
                '}';
    }
}
