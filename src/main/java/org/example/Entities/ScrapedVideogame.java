package org.example.Entities;

public class ScrapedVideogame {
    private String name;
    private float price;
    private String website;


    public ScrapedVideogame() {
    }

    public ScrapedVideogame(String name, float price, String website) {
        this.name = name;
        this.price = price;
        this.website = website;
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
                "name='" + name + '\'' +
                ", price=" + price +
                ", website='" + website + '\'' +
                '}';
    }
}
