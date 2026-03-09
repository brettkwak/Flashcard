package com.flashcards.controller;

import com.flashcards.model.FlashcardSet;
import com.flashcards.model.User;
import com.flashcards.repository.FlashcardSetRepository;
import com.flashcards.repository.UserRepository;
import java.security.Principal;
import java.util.Collection;
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

    public HomeController(FlashcardSetRepository flashcardSetRepository,
                          UserRepository userRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
        this.userRepository = userRepository;
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


}
