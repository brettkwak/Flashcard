package com.flashcards.service;

import com.flashcards.model.Flashcard;
import com.flashcards.model.FlashcardSet;
import com.flashcards.dto.FlashcardSetDTO;
import com.flashcards.dto.FlashcardDTO;
import com.flashcards.repository.FlashcardSetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlashcardSetService {

    private final FlashcardSetRepository flashcardSetRepository;

    public FlashcardSetService(FlashcardSetRepository flashcardSetRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
    }

    @Transactional
    public void updateFlashcardSet(FlashcardSet existingSet, FlashcardSetDTO formData) {

        existingSet.setTitle(formData.getTitle());
        existingSet.setDescription(formData.getDescription());
        existingSet.setFrontFirst(formData.isFrontFirst());

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
    }
}
