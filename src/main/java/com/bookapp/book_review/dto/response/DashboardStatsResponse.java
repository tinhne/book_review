package com.bookapp.book_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class DashboardStatsResponse {
    private long totalBooks;
    private long totalUsers;
    private long totalReviews;
    private long totalCategories;
    private double averageRatingAllBooks;
}
