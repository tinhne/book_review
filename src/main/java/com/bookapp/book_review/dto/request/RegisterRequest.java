package com.bookapp.book_review.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Field username")
    @Size(min = 3, max = 50, message = "user must 3-50 character")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must consist of letters, digits, and underscores only")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must 8-100 character")
    private String password;
}
