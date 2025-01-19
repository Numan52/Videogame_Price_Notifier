package org.example.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVideogame> subbedGames;


    public User() {
    }

    public User(String email, List<UserVideogame> subbedGames) {
        this.email = email;
        this.subbedGames = subbedGames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserVideogame> getSubbedGames() {
        return subbedGames;
    }

    public void setSubbedGames(List<UserVideogame> subbedGames) {
        this.subbedGames = subbedGames;
    }
}
