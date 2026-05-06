package com.bookapp.book_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AdminReviewResponse {
    private UUID id;
    private String username;
    private String bookTitle;
    private String content;
    private int rating;
    private int likeCount;
    private LocalDateTime createdAt;
}
