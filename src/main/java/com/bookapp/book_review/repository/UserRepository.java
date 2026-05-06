package com.bookapp.book_review.repository;

import com.bookapp.book_review.entity.User;
import com.bookapp.book_review.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Tìm kiếm user theo username hoặc email — dùng cho admin
    @Query("SELECT u FROM User u WHERE " +
            "(:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findAllWithSearch(@Param("search") String search, Pageable pageable);

    // Đếm theo role — dùng cho dashboard stats
    long countByRole(Role role);
}
