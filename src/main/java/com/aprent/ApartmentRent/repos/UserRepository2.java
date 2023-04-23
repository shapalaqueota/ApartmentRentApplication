package com.aprent.ApartmentRent.repos;

import com.aprent.ApartmentRent.models.Users;
import org.springframework.stereotype.Repository;
import java.util.stream.StreamSupport;

@Repository
public
interface UserRepository2 extends UserRepository {

    @Override
    default Users findByUsernameAndPassword(String username, String password) {
        return StreamSupport.stream(findAll().spliterator(), false)
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }


}