package com.aprent.ApartmentRent.repos;

import com.aprent.ApartmentRent.models.Listings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingsRepository extends JpaRepository<Listings, Long> {

}