package com.aprent.ApartmentRent.models;

import jakarta.persistence.*;

@Entity
public class ListingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listings listing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Listings getListing() {
        return listing;
    }

    public void setListing(Listings listing) {
        this.listing = listing;
    }
}
