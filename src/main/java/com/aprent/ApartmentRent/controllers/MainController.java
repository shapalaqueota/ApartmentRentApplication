package com.aprent.ApartmentRent.controllers;


import com.aprent.ApartmentRent.models.Listings;
import com.aprent.ApartmentRent.models.Users;
import com.aprent.ApartmentRent.repos.UserRepository2;
import com.aprent.ApartmentRent.service.ListingsService;
import com.aprent.ApartmentRent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private final UserRepository2 userRepository;

    public MainController(UserRepository2 userRepository) {
        this.userRepository = userRepository;
    }


    @Autowired
    private ListingsService listingsService;
    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        List<Listings> listings = listingsService.findAll();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("authentication", authentication);
            model.addAttribute("username", username);
        } else {

        }
        model.addAttribute("listings", listings);
        return "home";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerSubmit(@RequestParam("username") String username, @RequestParam("password") String password,
                                 @RequestParam("email") String email,Model model) {
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(passwordEncoder.encode(password));
        users.setEmail(email);
        userRepository.save(users);
        model.addAttribute("message", "Registration successful!");
        return "login"; // переходим на страницу входа
    }


    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new Users());
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute("user") Users users, Model model) {
        Optional<Users> foundUsersOptional = userService.findByEmailAndPassword(users.getEmail(), users.getPassword());
        if (foundUsersOptional.isPresent()) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }


    @GetMapping("/listings/add-listings")
    public String showAddListingsPage(Model model) {
        model.addAttribute("listing", new Listings());
        return "add-listings";
    }

    @PostMapping("/listings/add-listings")
    public String addListing(@RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("location") String location,
                             @RequestParam("price") double price) {
        Listings listing = new Listings();
        listing.setTitle(title);
        listing.setDescription(description);
        listing.setLocation(location);
        listing.setPrice(price);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUsername(authentication.getName());
        listing.setUser(user);

        listingsService.save(listing);

        return "redirect:/";
    }


    @GetMapping("/house-details")
    public String showHouseDetails(@RequestParam("id") Long id, Model model) {
        Optional<Listings> listing = listingsService.findById(id);
        model.addAttribute("listing", listing);
        return "/house-details";
    }



}
