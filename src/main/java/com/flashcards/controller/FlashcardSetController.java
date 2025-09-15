package com.flashcards.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FlashcardSetController {

    @GetMapping("/sets/create")
    public String showCreateForm(){
        return "create-set";
    }

}
