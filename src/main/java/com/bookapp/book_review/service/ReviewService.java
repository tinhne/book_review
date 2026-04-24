package com.bookapp.book_review.service;

import com.bookapp.book_review.dto.request.CreateReviewRequest;
import com.bookapp.book_review.dto.request.UpdateReviewRequest;
import com.bookapp.book_review.dto.response.PageResponse;
import com.bookapp.book_review.dto.response.ReviewResponse;
import com.bookapp.book_review.entity.Book;
import com.bookapp.book_review.entity.Review;
import com.bookapp.book_review.entity.ReviewLike;
import com.bookapp.book_review.entity.User;
import com.bookapp.book_review.exception.BadRequestException;
import com.bookapp.book_review.exception.ForbiddenException;
import com.bookapp.book_review.exception.ResourceNotFoundException;
import com.bookapp.book_review.repository.BookRepository;
import com.bookapp.book_review.repository.ReviewLikeRepository;
import com.bookapp.book_review.repository.ReviewRepository;
import com.bookapp.book_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getByBook(UUID bookId, UUID currentUserId, Pageable pageable) {
        if (!bookRepository.existsById(bookId)) throw new ResourceNotFoundException("Book not found");

        Page<Review> page = reviewRepository.findByBookId(bookId, pageable);

        // get tat ca reviewId roi query 1 lan -> know user liked
        Set<UUID> reviewIds = page.getContent().stream()
                .map(Review::getId).collect(Collectors.toSet());

        Set<UUID> likeIds = (currentUserId != null && !reviewIds.isEmpty())
                ? reviewLikeRepository.findLikedReviewIds(currentUserId, reviewIds)
                : Collections.emptySet();

        return PageResponse.of(page.map(r -> toResponse(r, likeIds.contains(r.getId()))));
    }

    @Transactional
    public ReviewResponse create(CreateReviewRequest request, String username) {
        User user =  userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (reviewRepository.existsByUserIdAndBookId(user.getId(), book.getId())) {
            throw new BadRequestException("You reviewed this book");
        }

        Review review = Review.builder()
                .user(user)
                .book(book)
                .content(request.getContent())
                .rating(request.getRating())
                .likeCount(0)
                .build();

        Review saved = reviewRepository.save(review);
        updateBookStats(book);

        return toResponse(saved, false);
    }

    @Transactional
    public ReviewResponse update(UUID reviewId, UpdateReviewRequest request, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review không tồn tại"));

        // Chỉ chủ review mới được sửa
        if (!review.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("Bạn không có quyền sửa review này");
        }

        if (request.getContent() != null) review.setContent(request.getContent());
        if (request.getRating() != null) {
            review.setRating(request.getRating());
            updateBookStats(review.getBook());
        }

        User user = userRepository.findByUsername(username).orElseThrow();
        boolean liked = reviewLikeRepository.existsByUserIdAndReviewId(user.getId(), reviewId);

        return toResponse(reviewRepository.save(review), liked);
    }

    @Transactional
    public void delete(UUID reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review không tồn tại"));

        boolean isOwner = review.getUser().getUsername().equals(username);
        // Admin cũng có thể xóa — kiểm tra ở controller bằng @PreAuthorize
        if (!isOwner) {
            throw new ForbiddenException("Bạn không có quyền xóa review này");
        }

        Book book = review.getBook();

        reviewLikeRepository.deleteAllByReviewId(reviewId);

        reviewRepository.delete(review);
        updateBookStats(book);
    }

    @Transactional
    public ReviewResponse toggleLike(UUID reviewId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review không tồn tại"));

        // Không cho like review của chính mình
        if (review.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Bạn không thể like review của chính mình");
        }

        var existingLike = reviewLikeRepository.findByUserIdAndReviewId(user.getId(), reviewId);

        if (existingLike.isPresent()) {
            // Unlike
            reviewLikeRepository.delete(existingLike.get());
            review.setLikeCount(Math.max(0, review.getLikeCount() - 1));
        } else {
            // Like
            ReviewLike like = ReviewLike.builder().user(user).review(review).build();
            reviewLikeRepository.save(like);
            review.setLikeCount(review.getLikeCount() + 1);
        }

        return toResponse(reviewRepository.save(review), existingLike.isEmpty());
    }

    // update avg rating và review count on book sau mỗi change
    private void updateBookStats(Book book) {
        double avg = reviewRepository.calculateAverageRating(book.getId());
        int count = reviewRepository.countByBookId(book.getId());
        book.setAverageRating(Math.round(avg * 10.0) / 10.0);  // làm tròn 1 chữ số thập phân
        book.setReviewCount(count);
        bookRepository.save(book);
    }

    private ReviewResponse toResponse(Review r, boolean liked) {
        return new ReviewResponse(
                r.getId(),
                r.getBook().getId(), r.getBook().getTitle(),
                r.getUser().getId(), r.getUser().getUsername(),
                r.getContent(), r.getRating(),
                r.getLikeCount(), liked,
                r.getCreatedAt(), r.getUpdatedAt()
        );
    }
}
