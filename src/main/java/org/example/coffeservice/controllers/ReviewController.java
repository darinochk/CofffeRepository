package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.ReviewRequestDTO;
import org.example.coffeservice.dto.response.ReviewResponseDTO;
import org.example.coffeservice.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public List<ReviewResponseDTO> getAllReviews() {
        try {
            return reviewService.getAllReviews();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения отзывов", e);
        }
    }

    @GetMapping("/user/{userId}")
    public List<ReviewResponseDTO> getReviewsByUser(@PathVariable Long userId) {
        try {
            return reviewService.getReviewsByUser(userId);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения отзывов для пользователя", e);
        }
    }

    @PostMapping("/create")
    public ReviewResponseDTO createReview(@RequestBody ReviewRequestDTO reviewRequest) {
        try {
            return reviewService.createReview(reviewRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания отзыва", e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok("Отзыв успешно удалён.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка удаления отзыва");
        }
    }
}
