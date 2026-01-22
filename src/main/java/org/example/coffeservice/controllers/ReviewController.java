package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.coffee.ReviewRequestDTO;
import org.example.coffeservice.dto.response.coffee.ReviewResponseDTO;
import org.example.coffeservice.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

  @Autowired private ReviewService reviewService;

  @GetMapping("/")
  public List<ReviewResponseDTO> getAllReviews() {
    try {
      return reviewService.getAllReviews();
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения отзывов", exception);
    }
  }

  @GetMapping("/user/{userId}")
  public List<ReviewResponseDTO> getReviewsByUser(@PathVariable Long userId) {
    try {
      return reviewService.getReviewsByUser(userId);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения отзывов для пользователя", exception);
    }
  }

  @PostMapping("/create")
  public ReviewResponseDTO createReview(@RequestBody ReviewRequestDTO reviewRequest) {
    try {
      return reviewService.createReview(reviewRequest);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка создания отзыва", exception);
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteReview(@PathVariable Long id) {
    try {
      reviewService.deleteReview(id);
      return ResponseEntity.ok("Отзыв успешно удалён.");
    } catch (IllegalArgumentException exception) {
      return ResponseEntity.badRequest().body(exception.getMessage());
    } catch (Exception exception) {
      return ResponseEntity.status(500).body("Ошибка удаления отзыва");
    }
  }
}
