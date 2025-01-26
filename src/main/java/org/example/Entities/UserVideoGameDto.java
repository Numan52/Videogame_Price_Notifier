package org.example.Entities;

public class UserVideoGameDto {
    private String email;
    private String gameTitle;
    private float priceThreshold;

    public UserVideoGameDto(String email, String gameTitle, float priceThreshold) {
        this.email = email;
        this.gameTitle = gameTitle;
        this.priceThreshold = priceThreshold;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public float getPriceThreshold() {
        return priceThreshold;
    }

    public void setPriceThreshold(float priceThreshold) {
        this.priceThreshold = priceThreshold;
    }
}
