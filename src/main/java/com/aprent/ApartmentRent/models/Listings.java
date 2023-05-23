package com.aprent.ApartmentRent.models;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
public class Listings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String location;

    private boolean hidden;

    public static final List<String> LOCATION_LIST = Arrays.asList("Almaty", "Astana", "Aktobe",
            "Atyrau", "Aktau", "Balkhash", "Karaganda", "Kokshetau", "Kostanay",
            "Kyzylorda", "Pavlodar", "Semey", "Shymkent", "Taraz", "Turkestan");


    private int price;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn
    private Users users;


    public List<String> getPossibleLocations() {
        return LOCATION_LIST;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Users getUser() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
