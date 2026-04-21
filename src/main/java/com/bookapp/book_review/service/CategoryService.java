package com.bookapp.book_review.service;

import com.bookapp.book_review.dto.response.CategoryResponse;
import com.bookapp.book_review.entity.Category;
import com.bookapp.book_review.exception.BadRequestException;
import com.bookapp.book_review.exception.ResourceNotFoundException;
import com.bookapp.book_review.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getSlug()))
                .toList();
    }

    @Transactional
    public CategoryResponse create(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new BadRequestException("Category already exists");
        }
        String slug = name.toLowerCase().replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9\\-]", "");
        Category saved = categoryRepository.save(
                Category.builder().name(name).slug(slug).build()
        );
        return new CategoryResponse(saved.getId(), saved.getName(), saved.getSlug());
    }

    @Transactional
    public void delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category does not exist");
        }
        categoryRepository.deleteById(id);
    }
}
