package com.aprent.ApartmentRent.models;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Table(name = "listings")
@Entity
public class Listings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    public static final List<String> LOCATION_LIST = Arrays.asList("Almaty", "Astana", "Aktobe",
            "Atyrau", "Aktau", "Balkhash", "Karaganda", "Kokshetau", "Kostanay",
            "Kyzylorda", "Pavlodar", "Semey", "Shymkent", "Taraz", "Turkestan");




    @Column(name = "price")
    private double price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
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

    public void setPrice(double price) {
        this.price = price;
    }

    public Users getUser() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }
}
