package com.bookapp.book_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @AllArgsConstructor
public class ReviewResponse {
    private UUID     id;
    private UUID bookId;
    private String bookTitle;
    private UUID userId;
    private String username;
    private String content;
    private int rating;
    private int likeCount;
    private boolean likedByCurrentUser;  //toggle icon like
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
