package com.flashcards.repository;
import com.flashcards.model.FlashcardSet;
import com.flashcards.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {

    List<FlashcardSet> findByUser(User user);

}