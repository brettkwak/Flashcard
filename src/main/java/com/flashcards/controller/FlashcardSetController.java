package com.flashcards.controller;

import com.flashcards.dto.FlashcardDTO;
import com.flashcards.dto.FlashcardSetDTO;
import com.flashcards.model.Flashcard;
import com.flashcards.model.FlashcardSet;
import com.flashcards.model.User;
import com.flashcards.repository.FlashcardSetRepository;
import com.flashcards.repository.UserRepository;
import com.flashcards.service.FlashcardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;

import java.util.List;
import java.io.IOException;

@Controller
public class FlashcardSetController {

    private FlashcardSetRepository flashcardSetRepository;
    private final UserRepository userRepository;

    public FlashcardSetController(FlashcardSetRepository flashcardSetRepository,
                                  UserRepository userRepository,
                                  FlashcardMapper flashcardMapper
                                  ) {
        this.flashcardSetRepository = flashcardSetRepository;
        this.userRepository = userRepository;
        this.flashcardMapper = flashcardMapper;
    }

    @GetMapping("/sets/create")
    public String showCreateForm(){
        return "create-set";
    }

    @PostMapping("/sets/create")
    public String createSet(@ModelAttribute FlashcardSet flashcardSet, Principal principal) {
        String username = principal.getName();
        flashcardSet.getFlashcards().removeIf(card ->
                card == null ||
                        card.getFront() == null || card.getFront().trim().isEmpty() ||
                        card.getBack() == null || card.getBack().trim().isEmpty()
        );

        for (Flashcard card : flashcardSet.getFlashcards()) {
            card.setFlashcardSet(flashcardSet);
        }

        User user = userRepository.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("User Not Found"));
        flashcardSet.setUser(user);

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


        FlashcardSetDTO dto = flashcardMapper.toDTO(set);

        model.addAttribute("flashcardSet", dto);
        return "edit-set";
    }

    @PostMapping("/sets/{id}/edit")
    public String updateSet(@PathVariable Long id, @ModelAttribute FlashcardSetDTO formData) {
        FlashcardSet existingSet = flashcardSetRepository.findById(id).orElse(null);
        if (existingSet == null) return "redirect:/";

        existingSet.setTitle(formData.getTitle());
        existingSet.setDescription(formData.getDescription());

        if (formData.getIdsToDelete() != null && !formData.getIdsToDelete().isEmpty()) {
            for (Long cardId : formData.getIdsToDelete()) {
                existingSet.getFlashcards().removeIf(c -> c.getId().equals(cardId));
            }
        }

        for (FlashcardDTO dto : formData.getCards()) {
            if (dto.getId() != null) {
                existingSet.getFlashcards().stream()
                        .filter(c -> c.getId().equals(dto.getId()))
                        .findFirst()
                        .ifPresent(card -> {
                            card.setFront(dto.getFront());
                            card.setBack(dto.getBack());
                        });
            } else {
                Flashcard newCard = new Flashcard(dto.getFront(), dto.getBack());
                existingSet.addFlashcard(newCard);
            }

        }

        flashcardSetRepository.save(existingSet);
        return "redirect:/sets/" + id;

    }

    @PostMapping("/sets/{id}/delete")
    public String deleteSet(@PathVariable long id) {
        FlashcardSet existingSet = flashcardSetRepository.findById(id).orElse(null);
        if (existingSet != null) {
            flashcardSetRepository.delete(existingSet);
        }
        return "redirect:/";
    }

    @Autowired
    private FlashcardMapper flashcardMapper;

    @GetMapping("/sets/{id}/study")
    public String studySet(@PathVariable Long id, Model model) {
        FlashcardSet set = flashcardSetRepository.findById(id).orElse(null);
        if (set == null) return "redirect:/";

        FlashcardSetDTO setDTO = flashcardMapper.toDTO(set);

        model.addAttribute("flashcardSet", setDTO);
        return "study";
    }

    @GetMapping("/sets/{id}/export")
    public ResponseEntity<FlashcardSetDTO> exportSet(@PathVariable Long id) {
        FlashcardSet set = flashcardSetRepository.findById(id).orElse(null);

        if (set == null) {
            return ResponseEntity.notFound().build();
        }

        FlashcardSetDTO dto = flashcardMapper.toDTO(set);

        String filename = "flashcards-" + id + ".json";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }

    @GetMapping("/sets/import")
    public String showImportForm() {
        return "import-set";
    }

    @PostMapping("/sets/import")
    public String processImport(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/sets/import?error=empty";
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            FlashcardSetDTO dto = mapper.readValue(file.getInputStream(),
                    FlashcardSetDTO.class);

            FlashcardSet set = flashcardMapper.toEntity(dto);

            flashcardSetRepository.save(set);

            return "redirect:/";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/sets/import?error=processing";
        }
    }


}
