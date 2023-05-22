package com.aprent.ApartmentRent.repos;

import com.aprent.ApartmentRent.models.Booking;
import com.aprent.ApartmentRent.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(Users user);
}
