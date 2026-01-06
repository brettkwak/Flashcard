package com.flashcards.dto;

public class FlashcardDTO {
    public String front;
    public String back;

    // Constructor
    public FlashcardDTO(String front, String back) {
        this.front = front;
        this.back = back;
    }

    // Gets, Sets
    public String getFront() { return front; }
    public void setFront(String front) { this.front = front; }
    public String getBack() { return back; }
    public void getBack(String back) { this.back = back; }

}
