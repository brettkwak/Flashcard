package com.flashcards.controller;

import com.flashcards.model.FlashcardSet;
import com.flashcards.model.User;
import com.flashcards.repository.FlashcardSetRepository;
import com.flashcards.repository.UserRepository;
import java.security.Principal;
import java.util.Collection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final FlashcardSetRepository flashcardSetRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HomeController(FlashcardSetRepository flashcardSetRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.flashcardSetRepository = flashcardSetRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("User Not Found"));
            List<FlashcardSet> userSets = flashcardSetRepository.findByUser(user);
            model.addAttribute("flashcardSets", userSets);
        } else {
            model.addAttribute("flashcardSets", Collections.emptyList());
        }

        return "index";
    }

    @GetMapping("/cards")
    public String cards() {
        return "cards";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(username);

        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPassword(hashedPassword);

        newUser.setRole("USER");

        userRepository.save(newUser);

        return "redirect:/login";
    }
}
