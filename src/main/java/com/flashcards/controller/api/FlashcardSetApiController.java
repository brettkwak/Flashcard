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

    // GET single set /api/v1/sets/{id}
    @GetMapping("/{id}")
    public ResponseEntity<FlashcardSetDTO> getSetById(@PathVariable Long id, Principal principal) {
        FlashcardSet set = flashcardSetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Set not found"));

        if (!set.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).build(); // 403 Forbidden
        }

        return ResponseEntity.ok(flashcardMapper.toDTO(set));
    }

    // POST /api/v1/sets
    @PostMapping
    public ResponseEntity<FlashcardSetDTO> createSet(@RequestBody FlashcardSetDTO dto, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        FlashcardSet set = flashcardMapper.toEntity(dto);
        set.setUser(user);

        FlashcardSet savedSet = flashcardSetRepository.save(set);
        FlashcardSetDTO savedDto = flashcardMapper.toDTO(savedSet);

        java.net.URI location = org.springframework.web.servlet.support.ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSet.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedDto);
    }

    // DELETE /api/v1/sets/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSet(@PathVariable Long id, Principal principal) {
        FlashcardSet existingSet = flashcardSetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Set not found"));

        if (!existingSet.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).build();
        }

        flashcardSetRepository.delete(existingSet);
        return ResponseEntity.noContent().build();
    }
}
