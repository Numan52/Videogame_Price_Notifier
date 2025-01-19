package org.example.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "user_videogames")
public class UserVideogame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull @NaturalId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull @NaturalId
    private String videogame;

    private float priceThreshold;


    public UserVideogame(@NotNull User user, @NotNull String videogame, float priceThreshold) {
        this.user = user;
        this.videogame = videogame;
        this.priceThreshold = priceThreshold;
    }


    public UserVideogame() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    @NotNull
    public String getVideogame() {
        return videogame;
    }

    public void setVideogame(@NotNull String videogame) {
        this.videogame = videogame;
    }

    public float getPriceThreshold() {
        return priceThreshold;
    }

    public void setPriceThreshold(float priceThreshold) {
        this.priceThreshold = priceThreshold;
    }
}
