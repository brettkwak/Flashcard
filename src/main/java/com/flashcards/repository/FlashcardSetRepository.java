package com.flashcards.repository;
import com.flashcards.model.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {}