package com.aprent.ApartmentRent.repos;

import com.aprent.ApartmentRent.models.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByEmailAndPassword(String email, String password);
    Users findByUsernameAndPassword(String username, String password);
    Users findByUsername(String username);

    Users findByEmail(String email);
}
