package com.bookapp.book_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @AllArgsConstructor
public class BookResponse {
    private UUID id;
    private String title;
    private String author;
    private String coverUrl;
    private String categoryName;
    private Double averageRating;
    private int reviewCount;
    private LocalDateTime createdAt;
}
