package com.flashcards.controller;

import com.flashcards.model.Flashcard;
import com.flashcards.model.FlashcardSet;
import com.flashcards.repository.FlashcardSetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FlashcardSetController {

    private FlashcardSetRepository flashcardSetRepository;

    public FlashcardSetController(FlashcardSetRepository flashcardSetRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
    }

    @GetMapping("/sets/create")
    public String showCreateForm(){
        return "create-set";
    }

    @PostMapping("/sets/create")
    public String createSet(@ModelAttribute FlashcardSet flashcardSet) {
        for (Flashcard card : flashcardSet.getFlashcards()) {
            card.setFlashcardSet(flashcardSet);
        }
        flashcardSetRepository.save(flashcardSet);
        return "redirect:/";
    }

}
