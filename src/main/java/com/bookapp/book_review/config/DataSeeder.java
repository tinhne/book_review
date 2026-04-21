package com.bookapp.book_review.config;

import com.bookapp.book_review.entity.Category;
import com.bookapp.book_review.entity.User;
import com.bookapp.book_review.enums.Role;
import com.bookapp.book_review.repository.CategoryRepository;
import com.bookapp.book_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {
    @Bean
    CommandLineRunner seedData(UserRepository userRepo,
                               CategoryRepository categoryRepo,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Tạo admin nếu chưa có
            if (!userRepo.existsByUsername("admin")) {
                userRepo.save(User.builder()
                        .username("admin")
                        .email("admin@bookreview.com")
                        .passwordHash(passwordEncoder.encode("Admin@123456"))
                        .role(Role.ADMIN)
                        .enabled(true)
                        .build());
                log.info("Đã tạo tài khoản admin");
            }

            // Tạo categories mặc định
            List<String[]> categories = List.of(
                    new String[]{"Tiểu thuyết", "tieu-thuyet"},
                    new String[]{"Khoa học", "khoa-hoc"},
                    new String[]{"Lịch sử", "lich-su"},
                    new String[]{"Kinh tế", "kinh-te"},
                    new String[]{"Tâm lý học", "tam-ly-hoc"},
                    new String[]{"Kỹ năng sống", "ky-nang-song"},
                    new String[]{"Thiếu nhi", "thieu-nhi"}
            );

            for (String[] cat : categories) {
                if (!categoryRepo.existsByName(cat[0])) {
                    categoryRepo.save(Category.builder()
                            .name(cat[0]).slug(cat[1]).build());
                }
            }
        };
    }
}
