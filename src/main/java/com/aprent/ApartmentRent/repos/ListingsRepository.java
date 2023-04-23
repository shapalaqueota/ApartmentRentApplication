package com.aprent.ApartmentRent.repos;

import com.aprent.ApartmentRent.models.Listings;
import org.springframework.data.repository.CrudRepository;

public interface ListingsRepository extends CrudRepository<Listings, Long> {
}
