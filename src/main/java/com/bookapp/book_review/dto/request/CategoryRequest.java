package com.bookapp.book_review.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Tên category không được để trống")
    private String name;
}
