package com.bookapp.book_review.dto.response;

import com.bookapp.book_review.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AdminUserResponse {
    private UUID id;
    private String username;
    private String email;
    private Role role;
    private boolean enabled;
    private int reviewCount;
    private LocalDateTime createdAt;
}
