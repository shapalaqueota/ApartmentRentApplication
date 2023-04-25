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
            // Пользователь аутентифицирован
            String username = authentication.getName();
            model.addAttribute("authentication", authentication);
            model.addAttribute("username", username);
        } else {
            // Пользователь не аутентифицирован
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
        userRepository.save(users); // сохраняем пользователя в базе данных
        model.addAttribute("message", "Registration successful!");
        return "login"; // переходим на страницу входа
    }


    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        // Выводим форму для входа на страницу
        model.addAttribute("user", new Users());
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute("user") Users users, Model model) {
        // Вызываем метод findByLoginAndPassword() из сервиса
        Users foundUsers = userService.findByLoginAndPassword(users.getName(), users.getPassword());

        if (foundUsers == null) {
            // Если пользователь не найден, показываем сообщение об ошибке
            model.addAttribute("error", "Invalid login or password");
            return "login";
        } else {
            // Если пользователь найден, перенаправляем на главную страницу
            return "redirect:/";
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

        // Получаем текущего пользователя из базы данных
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUsername(authentication.getName());
        listing.setUser(user);

        // Сохраняем объявление в базу данных
        listingsService.save(listing);

        return "redirect:/";
    }

}
