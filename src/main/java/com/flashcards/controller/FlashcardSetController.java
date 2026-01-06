package com.flashcards.controller;

import com.flashcards.model.Flashcard;
import com.flashcards.model.FlashcardSet;
import com.flashcards.repository.FlashcardSetRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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

    @GetMapping("/sets/{id}")
    public String viewSet(@PathVariable Long id, Model model) {
        FlashcardSet flashcardSet = flashcardSetRepository.findById(id).orElse(null);

        if (flashcardSet == null) {
            return "redirect:/";
        }

        model.addAttribute("flashcardSet", flashcardSet);
        return "view-set";
    }

    @GetMapping("/sets/{id}/edit")
    public String showEditSetForm(@PathVariable Long id, Model model) {
        FlashcardSet set = flashcardSetRepository.findById(id).orElse(null);
        if (set == null) return "redirect:/";

        model.addAttribute("flashcardSet", set);
        return "edit-set";
    }

    @PostMapping("/sets/{id}/edit")
    public String updateSet(@PathVariable Long id, @ModelAttribute FlashcardSet formData) {
        FlashcardSet existingSet = flashcardSetRepository.findById(id).orElse(null);
        if (existingSet != null) {
            existingSet.setTitle(formData.getTitle());
            existingSet.setDescription(formData.getDescription());

            List<Flashcard> submittedCards = formData.getFlashcards();
            List<Flashcard> existingCards = existingSet.getFlashcards();

            for (int i=0; i < existingCards.size(); i++) {
                if (i < submittedCards.size()) {
                    existingCards.get(i).setFront(submittedCards.get(i).getFront());
                    existingCards.get(i).setBack(submittedCards.get(i).getBack());
                }
            }

            flashcardSetRepository.save(existingSet);

            return "redirect:/sets/{id}";

        }

        return "redirect:/";

    }

    @GetMapping("/sets/{id}/study")
    public String studySet(@PathVariable Long id, Model model) {
        FlashcardSet set = flashcardSetRepository.findById(id).orElse(null);
        if (set == null) return "redirect:/";

        model.addAttribute("flashcardSet", set);
        return "study";
    }

}
