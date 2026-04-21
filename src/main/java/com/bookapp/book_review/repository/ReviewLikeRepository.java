package com.bookapp.book_review.repository;

import com.bookapp.book_review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, UUID> {
    Optional<ReviewLike> findByUserIdAndReviewId(UUID userId, UUID reviewId);

    boolean existsByUserIdAndReviewId(UUID userId, UUID reviewId);

    // Lấy tất cả reviewId mà user đã like — dùng để set flag likedByCurrentUser
    @Query("SELECT rl.review.id FROM ReviewLike rl WHERE rl.user.id = :userId AND rl.review.id IN :reviewIds")
    Set<UUID> findLikedReviewIds(@Param("userId") UUID userId, @Param("reviewIds") Set<UUID> reviewIds);
}
