package org.example.coffeservice.controllers;

import org.example.coffeservice.models.Review;
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
    public List<Review> getAllReviews() {
        try {
            return reviewService.getAllReviews();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reviews");
        }
    }

    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUser(@PathVariable Long userId) {
        try {
            return reviewService.getReviewsByUser(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reviews for user");
        }
    }

    @PostMapping("/create")
    public Review createReview(@RequestBody Review review) {
        try {
            return reviewService.createReview(review);
        } catch (Exception e) {
            throw new RuntimeException("Error creating review");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok("Review deleted successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting review");
        }
    }
}
