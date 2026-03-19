package com.flashcards.controller.api;

import com.flashcards.dto.FlashcardSetDTO;
import com.flashcards.model.FlashcardSet;
import com.flashcards.model.User;
import com.flashcards.repository.FlashcardSetRepository;
import com.flashcards.repository.UserRepository;
import com.flashcards.service.FlashcardMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sets")
public class FlashcardSetApiController {

    private final FlashcardSetRepository flashcardSetRepository;
    private final UserRepository userRepository;
    private final FlashcardMapper flashcardMapper;

    public FlashcardSetApiController(FlashcardSetRepository flashcardSetRepository,
                                     UserRepository userRepository,
                                     FlashcardMapper flashcardMapper) {
        this.flashcardSetRepository = flashcardSetRepository;
        this.userRepository = userRepository;
        this.flashcardMapper = flashcardMapper;
    }

    // GET /api/v1/sets
    @GetMapping
    public ResponseEntity<List<FlashcardSetDTO>> getUserSets(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FlashcardSetDTO> userSets = flashcardSetRepository.findByUser(user)
                .stream()
                .map(flashcardMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userSets);
    }
}
