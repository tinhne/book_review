package com.bookapp.book_review.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static <T> PageResponse<T> of(Page<T> page) {
        PageResponse<T> res = new PageResponse<>();
        res.setContent(page.getContent());
        res.setPage(page.getNumber());
        res.setSize(page.getSize());
        res.setTotalElements(page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        res.setLast(page.isLast());
        return res;
    }}
