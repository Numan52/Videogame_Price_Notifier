package org.example.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Videogame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;


    public Videogame(){

    }


    public Videogame(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Videogame{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
