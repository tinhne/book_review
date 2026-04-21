package com.bookapp.book_review.repository;

import com.bookapp.book_review.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    // Join fetch category to avoid N+1 query when getList
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category " +
            "WHERE (:search IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:categoryId IS NULL OR b.category.id = :categoryId)")
    Page<Book> findAllWithFilter(
            @Param("search") String search,
            @Param("categoryId") UUID categoryId,
            Pageable pageable
    );

    @Query("SELECT b FROM Book B LEFT JOIN FETCH b.category where b.id = :id")
    Optional<Book> findByIdWithCategory(@Param("id") UUID id);

    boolean existsByIsbn(String isbn);
}
