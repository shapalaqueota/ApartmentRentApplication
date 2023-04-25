package com.aprent.ApartmentRent.repos;

import com.aprent.ApartmentRent.models.Listings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingsRepository extends JpaRepository<Listings, Long> {
}