package com.flashcards.service;

import com.flashcards.dto.FlashcardDTO;
import com.flashcards.dto.FlashcardSetDTO;
import com.flashcards.model.Flashcard;
import com.flashcards.model.FlashcardSet;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

public class FlashcardMapper {

    // Entity -> DTO
    public FlashcardSetDTO toDTO(FlashcardSet set) {
        FlashcardSetDTO dto = new FlashcardSetDTO();
        dto.setTitle(set.getTitle());
        dto.setDescription(set.getDescription());

        dto.setCards(set.getFlashcards().steram()
                .map(card -> new FlashcardDTO(card.getFront(), card.getBack()))
                .collect(Collectors.toList()));

        return dto;
    }

    // DTO -> Entity
    public FlashcardSet toEntity(FlashcardsetDTO dto) {
        FlashcardSet set = new FlashcardSet();
        set.setTitle(dto.getTitle());
        set.setDescription(dto.getDescription());

        for (FlashcardDTO cardDTO : dto.getCards()) {
            Flashcard card = new Flashcard();
            card.setFront(cardDTO.getFront());
            card.setBack(cardDTO.getBack());
            set.addFlashcard(card);
        }

        return set;
    }
}
