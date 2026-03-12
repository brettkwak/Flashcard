package com.flashcards.dto;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetDTO {
    private Long id;
    private String title;
    private String description;
    private List<FlashcardDTO> cards = new ArrayList<>();
    private List<Long> idsToDelete = new ArrayList<>();
    private boolean  frontFirst = true;

    // Gets, Sets
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<FlashcardDTO> getCards() { return cards; }
    public void setCards(List<FlashcardDTO> cards) { this.cards = cards; }
    public List<Long> getIdsToDelete() { return idsToDelete; }
    public void setIdsToDelete(List<Long> idsToDelete) { this.idsToDelete = idsToDelete; }
    public boolean isFrontFirst() {
        return frontFirst;
    }
    public void setFrontFirst(boolean frontFirst) {
        this.frontFirst = frontFirst;
    }

    public void addCard(FlashcardDTO card) {
        this.cards.add(card);
    }

}
