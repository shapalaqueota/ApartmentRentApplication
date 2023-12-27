package com.aprent.ApartmentRent.controllers;


import com.aprent.ApartmentRent.models.Booking;
import com.aprent.ApartmentRent.models.ListingImage;
import com.aprent.ApartmentRent.models.Listings;
import com.aprent.ApartmentRent.models.Users;
import com.aprent.ApartmentRent.repos.BookingRepository;
import com.aprent.ApartmentRent.repos.UserRepository2;
import com.aprent.ApartmentRent.service.ListingsService;
import com.aprent.ApartmentRent.service.S3Service;
import com.aprent.ApartmentRent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController {


    @GetMapping("/")
    public String index(){
        return "index";
    }

    private final UserRepository2 userRepository;



    public MainController(UserRepository2 userRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }


    @Autowired
    private ListingsService listingsService;
    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        List<Listings> listings = listingsService.findAll()
                .stream()
                .filter(listings1 -> !listings1.isHidden())
                .toList();
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




    @Autowired
    private S3Service s3Service;
    @PostMapping("/listings/add-listings")
    public String addListing(@RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("location") String location,
                             @RequestParam("price") int price,
                             @RequestParam("files") MultipartFile[] files,
                             Authentication authentication) throws IOException {
        Listings listing = new Listings();
        listing.setTitle(title);
        listing.setDescription(description);
        listing.setLocation(location);
        listing.setPrice(price);


        Users user = userRepository.findByEmail(authentication.getName());
        listing.setUser(user);
        listingsService.save(listing);

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileKey = s3Service.generateFileKey(listing.getId(), file.getOriginalFilename());
                s3Service.uploadFile(fileKey, file);
                String fileUrl = "https://chicken-bucket.hb.kz-ast.vkcs.cloud/" + fileKey;

                ListingImage image = new ListingImage();
                image.setImageUrl(fileUrl);
                image.setListing(listing);
                listing.getImages().add(image); // Добавляем изображение в список
            }
        }

        listingsService.save(listing);

        return "redirect:/";
    }


    @GetMapping("/house-details")
    public String showHouseDetails(@RequestParam("id") Long id, Model model) {
        Optional<Listings> listingOptional = listingsService.findById(id);
        if (listingOptional.isPresent()) {
            model.addAttribute("listing", listingOptional.get());
        } else {
            // Обработка случая, когда объявление не найдено.
            // Например, перенаправление на другую страницу.
            model.addAttribute("errorMessage", "Listing not found");
            return "home"; // Перенаправление на страницу ошибки
        }
        return "house-details"; // Ваш шаблон Thymeleaf для деталей дома
    }


    private final BookingRepository bookingRepository;

    @PostMapping("/listings/{listingId}/book")
    public String bookListing(@PathVariable("listingId") Long listingId,
                              @RequestParam("startDate") LocalDate startDate,
                              @RequestParam("endDate") LocalDate endDate,
                              Authentication authentication,Model model) {

        // объект юзер со всей инфой
        Users user = userRepository.findByEmail(authentication.getName());
        // айдишка хаты которой мы бронируем
        Optional<Listings> listingOptional = listingsService.findById(listingId);
        if (listingOptional.isPresent()) {
            Listings listing = listingOptional.get();

            // процесс букинга, исопльзуем сеттеры
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setListing(listing);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);

            // сохраняем в бдшке
            bookingRepository.save(booking);

            model.addAttribute("bookingConfirmed", true);

            return "redirect:/confirmation"; // перенаправления на страницу с подтверждением брони
        }

        return "redirect:/"; // перенаправления на главную при ошибке
    }


    @GetMapping("/confirmation")
    public String showConfirmationPage(Model model) {
        // отображение страницы с подтверждением бронирования
        model.addAttribute("confirmationCheck", false); // Флаг, указывающий, что бронирование еще не подтверждено
        return "confirmation";
    }

    @PostMapping("/confirmation")
    public String confirmBooking(@RequestParam("confirmationCheck") boolean confirmationCheck) {
        if (confirmationCheck) {
            // бронирование подтверждено
            return "redirect:/my-bookings"; // перенаправление на страницу с бронированиями
        } else {
            // бронирование отменено или произошла ошибка. Перенаправление на главную страницу.
            return "redirect:/";
        }
    }

    @GetMapping("/my-bookings")
    public String showMyBookings(Model model,Authentication authentication) {
        Users user = userRepository.findByEmail(authentication.getName());
        List<Booking> bookings = bookingRepository.findByUser(user);
        model.addAttribute("bookings", bookings);
        return "my-bookings";
    }

    @GetMapping("/my-ads")
    public String showMyAds(Model model, Authentication authentication) {
        Users user = userRepository.findByEmail(authentication.getName());
        List<Listings> ads = user.getListings()
                .stream()
                .filter(ad -> !ad.isHidden()) // скрывается объявление которое помечено как isHidden
                .collect(Collectors.toList());
        model.addAttribute("ads", ads);
        return "my-ads";
    }

    @PostMapping("/delete-ad")
    public String deleteAd(@RequestParam("adId") Long adId) {
        Optional<Listings> adOptional = listingsService.findById(adId);
        if (adOptional.isPresent()) {
            Listings ad = adOptional.get();
            ad.setHidden(true); // типо удаление объявления, но на самом деле скрываем его просто
            listingsService.save(ad);
        }
        return "redirect:/my-ads";
    }


}
