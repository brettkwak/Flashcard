package com.flashcards.dto;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetDTO {
    private String title;
    private String description;
    private List<FlashcardDTO> cards = new ArrayList<>();

    // Gets, Sets
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<FlashcardDTO> getCards() { return cards; }
    public void setCards(List<FlashcardDTO> cards) { this.cards = cards; }

    public void addCard(FlashcardDTO card) {
        this.cards.add(card);
    }

}
