package com.bookapp.book_review.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class healthController {
    @GetMapping
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "service", "book-review"
        );
    }
}
