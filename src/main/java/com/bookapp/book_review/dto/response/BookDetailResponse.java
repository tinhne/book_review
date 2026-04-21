package com.bookapp.book_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data @AllArgsConstructor
public class BookDetailResponse {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String coverUrl;
    private CategoryResponse category;
    private double averageRating;
    private int reviewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
