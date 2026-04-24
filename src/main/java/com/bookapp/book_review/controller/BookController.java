package com.bookapp.book_review.controller;

import com.bookapp.book_review.dto.request.CreateBookRequest;
import com.bookapp.book_review.dto.request.UpdateBookRequest;
import com.bookapp.book_review.dto.response.BookDetailResponse;
import com.bookapp.book_review.dto.response.BookResponse;
import com.bookapp.book_review.dto.response.PageResponse;
import com.bookapp.book_review.entity.Book;
import com.bookapp.book_review.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Validated
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Max(value = 50, message = "Size maximum is 50") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
            ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(bookService.getBooks(search, categoryId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDetailResponse> getBook(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDetailResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        return ResponseEntity.status(201).body(bookService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDetailResponse> updateBook(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
