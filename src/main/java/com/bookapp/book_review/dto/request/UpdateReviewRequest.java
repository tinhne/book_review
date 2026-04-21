package com.bookapp.book_review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateReviewRequest {
    @Size(min = 10, max = 5000, message = "Content from 10-5000 character")
    private String content;

    @Min(value = 1, message = "Rating min is 1")
    @Max(value = 5, message = "Rating max is 5")
    private Integer rating;
}
