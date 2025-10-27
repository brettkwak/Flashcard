package com.flashcards.controller;

import com.flashcards.model.FlashcardSet;
import com.flashcards.repository.FlashcardSetRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final FlashcardSetRepository flashcardSetRepository;

    public HomeController(FlashcardSetRepository flashcardSetRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<FlashcardSet> sets = flashcardSetRepository.findAll();
        model.addAttribute("flashcardSets", sets);
        return "index";
    }

    @GetMapping("/cards")
    public String cards() {
        return "cards";
    }
}
