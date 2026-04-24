package com.bookapp.book_review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "review_likes",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_user_review_like",
                columnNames = {"user_id", "review_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
