package com.bookapp.book_review.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateBookRequest {
    @Size(max = 255, message = "Bookname max 255 character")
    private String title;

    @Size(max = 255, message = "Author name max 255 character")
    private String author;

    private String description;

    @Size(max = 500, message = "ImageUrl max 500 character")
    private String coverUrl;

    private UUID categoryId;
}
