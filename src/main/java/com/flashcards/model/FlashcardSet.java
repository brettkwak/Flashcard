package com.flashcards.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FlashcardSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "flashcardSet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Flashcard> flashcards = new ArrayList<>();


    // Gets, Sets
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle() { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Flashcard> getFlashcards() { return flashcards; }
    public void setFlashcards(List<Flashcard> flashcards) { this.flashcards = flashcards; }


    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
        flashcard.setFlashcardSet(this);
    }

}
