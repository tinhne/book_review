package com.bookapp.book_review.repository;

import com.bookapp.book_review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    // JOIN FETCH user để không bị N+1 khi hiển thị danh sách review
    @Query  ("SELECT r FROM Review r JOIN FETCH r.user WHERE r.book.id = :bookId")
    Page<Review> findByBookId(@Param("bookId") UUID bookId, Pageable pageable);

    // Review của 1 user cho 1 cuốn sách
    Optional<Review> findByUserIdAndBookId(UUID userId, UUID bookId);

    boolean existsByUserIdAndBookId(UUID userId, UUID bookId);

    // Tính lại avg rating sau mỗi lần thêm/sửa/xóa review
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.book.id = :bookId")
    double calculateAverageRating(@Param("bookId") UUID bookId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.book.id = :bookId")
    int countByBookId(@Param("bookId") UUID bookId);
}
