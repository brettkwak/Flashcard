package com.flashcards.dto;

public class FlashcardDTO {
    public Long id;
    public String front;
    public String back;

    // Constructor
    public FlashcardDTO() {

    }

    public FlashcardDTO(String front, String back) {
        this.front = front;
        this.back = back;
    }

    // Gets, Sets
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFront() { return front; }
    public void setFront(String front) { this.front = front; }
    public String getBack() { return back; }
    public void setBack(String back) { this.back = back; }

}
