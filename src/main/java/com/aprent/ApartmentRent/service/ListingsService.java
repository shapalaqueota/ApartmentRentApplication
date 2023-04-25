package com.aprent.ApartmentRent.service;

import com.aprent.ApartmentRent.models.Listings;
import com.aprent.ApartmentRent.repos.ListingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListingsService {

    @Autowired
    private ListingsRepository listingsRepository;

    public List<Listings> findAll() {
        return listingsRepository.findAll();
    }

    public Optional<Listings> findById(Long id) {
        return listingsRepository.findById(id);
    }

    public void save(Listings listings) {
        listingsRepository.save(listings);
    }

    public void delete(Listings listings) {
        listingsRepository.delete(listings);
    }
}