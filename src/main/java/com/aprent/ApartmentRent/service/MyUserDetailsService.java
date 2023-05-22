package com.aprent.ApartmentRent.service;

import com.aprent.ApartmentRent.models.Users;
import com.aprent.ApartmentRent.repos.UserRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<Users> userOptional = loadUserByUsernameOrEmail(usernameOrEmail);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
        }
        Users user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getAuthorities(user));
    }

    @Transactional
    public Optional<Users> loadUserByUsernameOrEmail(String usernameOrEmail) {
        Optional<Users> userOptional = Optional.ofNullable(userService.findByUsername(usernameOrEmail));
        if (userOptional.isEmpty()) {
            userOptional = Optional.ofNullable(userService.findByEmail(usernameOrEmail));
        }
        return userOptional;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Users user) {
        String[] userRoles = user.getRoles().stream().map((role) -> role.name()).toArray(String[]::new);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }
}


