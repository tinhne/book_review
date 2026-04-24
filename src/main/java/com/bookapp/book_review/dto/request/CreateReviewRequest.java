package com.bookapp.book_review.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReviewRequest {
    @NotNull(message = "BookId is required")
    private UUID bookId;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 5000, message = "content from 10 - 5000 rows")
    private String content;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating min is 1")
    @Max(value = 5, message = "Rating max is 5")
    private Integer rating;
}
