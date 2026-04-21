package com.bookapp.book_review.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Field Username")
    private String username;

    @NotBlank(message = "Field Password")
    private String password;
}
