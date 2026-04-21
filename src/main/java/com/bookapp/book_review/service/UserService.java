package com.bookapp.book_review.service;

import com.bookapp.book_review.dto.response.UserResponse;
import com.bookapp.book_review.exception.ResourceNotFoundException;
import com.bookapp.book_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getMe(String username) {
        return userRepository.findByUsername(username)
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getCreatedAt()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
}
