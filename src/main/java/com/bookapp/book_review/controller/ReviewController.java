package com.bookapp.book_review.controller;

import com.bookapp.book_review.dto.request.CreateReviewRequest;
import com.bookapp.book_review.dto.request.UpdateReviewRequest;
import com.bookapp.book_review.dto.response.PageResponse;
import com.bookapp.book_review.dto.response.ReviewResponse;
import com.bookapp.book_review.entity.User;
import com.bookapp.book_review.repository.UserRepository;
import com.bookapp.book_review.service.ReviewService;
import com.bookapp.book_review.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<ReviewResponse>> getByBook(
            @PathVariable UUID bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        UUID currentUserId = null;
        if(userDetails != null) {
            currentUserId = userRepository.findByUsername(userDetails.getUsername())
                    .map(User::getId).orElse(null);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok((reviewService.getByBook(bookId, currentUserId, pageable)));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @Valid @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(201).body(reviewService.create(request, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reviewService.update(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ReviewResponse> toggleLike(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reviewService.toggleLike(id, userDetails.getUsername()));
    }
}
