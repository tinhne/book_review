package com.bookapp.book_review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateBookRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 225, message = "Name max 255 character")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author name max 255 character")
    private String author;

    @Size(max = 20, message = "ISBN max 20 character")
    private String isbn;

    private String description;

    @Size(max = 500, message = "ImageUrl max 500 character")
    private String coverUrl;

    private UUID categoryId;
}
