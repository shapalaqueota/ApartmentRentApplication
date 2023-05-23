package com.aprent.ApartmentRent.service;

import com.aprent.ApartmentRent.models.Listings;
import com.aprent.ApartmentRent.models.Users;
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

    public List<Listings> findByUsers(Users users) {
        return listingsRepository.findByUsers(users);
    }

    public void deleteById(Long id) {
        listingsRepository.deleteById(id);
    }
    public Optional<Listings> findById(Long id) {
        return listingsRepository.findById(id);
    }

    public void save(Listings listings) {
        listingsRepository.save(listings);
    }




}