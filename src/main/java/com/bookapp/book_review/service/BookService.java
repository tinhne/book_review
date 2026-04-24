package com.bookapp.book_review.service;

import com.bookapp.book_review.dto.request.CreateBookRequest;
import com.bookapp.book_review.dto.request.UpdateBookRequest;
import com.bookapp.book_review.dto.response.BookDetailResponse;
import com.bookapp.book_review.dto.response.BookResponse;
import com.bookapp.book_review.dto.response.CategoryResponse;
import com.bookapp.book_review.dto.response.PageResponse;
import com.bookapp.book_review.entity.Book;
import com.bookapp.book_review.entity.Category;
import com.bookapp.book_review.exception.BadRequestException;
import com.bookapp.book_review.exception.ResourceNotFoundException;
import com.bookapp.book_review.repository.BookRepository;
import com.bookapp.book_review.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public PageResponse<BookResponse> getBooks(String search, UUID categoryId, Pageable pageable) {
        String normalizedSearch = StringUtils.hasText(search) ? search.trim() : "";
        Page<Book> page = bookRepository.findAllWithFilter(normalizedSearch, categoryId, pageable);
        return PageResponse.of(page.map(this::toBookResponse));
    }

    @Transactional(readOnly = true)
    public BookDetailResponse getBook(UUID id) {
        Book book = bookRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book is not found"));
        return toBookDetailResponse(book);
    }

    @Transactional
    public BookDetailResponse create(CreateBookRequest createBookRequest) {
        if (StringUtils.hasText(createBookRequest.getTitle()) && bookRepository.existsByIsbn(createBookRequest.getIsbn())) {
            throw new BadRequestException("ISBN already exists");
        }

        Category category = null;
        if (createBookRequest.getCategoryId() != null) {
            category = categoryRepository.findById(createBookRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));
        }

        Book book = Book.builder()
                .title(createBookRequest.getTitle())
                .isbn(createBookRequest.getIsbn())
                .category(category)
                .description(createBookRequest.getDescription())
                .author(createBookRequest.getAuthor())
                .coverUrl(createBookRequest.getCoverUrl())
                .build();

        return toBookDetailResponse(bookRepository.save(book));
    }

    @Transactional
    public BookDetailResponse update(UUID id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book is not found"));
        if (StringUtils.hasText(request.getTitle())) {
            book.setTitle(request.getTitle());
        }
        if (StringUtils.hasText(request.getDescription())) {
            book.setDescription(request.getDescription());
        }
        if (StringUtils.hasText(request.getAuthor())) {book.setAuthor(request.getAuthor());}
        if (StringUtils.hasText(request.getCoverUrl())) {book.setCoverUrl(request.getCoverUrl());}
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));
            book.setCategory(category);
        }
        return toBookDetailResponse(bookRepository.save(book));
    }

    @Transactional
    public void delete(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book is not found");
        }
        bookRepository.deleteById(id);
    }

    @Transactional
    public void refreshStats(UUID id) {
        bookRepository.findById(id).ifPresent(book -> {
            // avgRating và reviewCount tính thẳng từ DB, không cache trong memory
        });
    }

    // ---- Mappers ----
    private BookResponse toBookResponse(Book book) {
        return new BookResponse(
                book.getId(), book.getAuthor(), book.getTitle(),
                book.getCoverUrl(),
                book.getCategory() != null ? book.getCategory().getName() : null,
                book.getAverageRating(), book.getReviewCount(), book.getCreatedAt()
        );
    }

    private BookDetailResponse toBookDetailResponse(Book book) {
        CategoryResponse categoryResponse = book.getCategory() != null
                ? new CategoryResponse(book.getCategory().getId(),
                book.getCategory().getName(), book.getCategory().getSlug())
                : null;
        return new BookDetailResponse(
                book.getId(), book.getTitle(),book.getAuthor(),
                book.getIsbn(),book.getDescription(), book.getCoverUrl(), categoryResponse,
                book.getAverageRating(),book.getReviewCount(),book.getCreatedAt(), book.getUpdatedAt()
        );
    }
}
