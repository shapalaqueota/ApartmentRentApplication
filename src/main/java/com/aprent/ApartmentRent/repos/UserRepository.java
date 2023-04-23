package com.aprent.ApartmentRent.repos;

import com.aprent.ApartmentRent.models.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {
    Users findByUsernameAndPassword(String username, String password);
    Users findByUsername(String username);
}
