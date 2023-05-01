package com.aprent.ApartmentRent.service;

import com.aprent.ApartmentRent.models.Users;
import com.aprent.ApartmentRent.repos.UserRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository2 userRepository;

    public Users findByLoginAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<Users> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
