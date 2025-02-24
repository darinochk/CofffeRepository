package org.example.coffeservice.services;

import org.example.coffeservice.models.Review;
import org.example.coffeservice.models.User;
import org.example.coffeservice.repositories.ReviewRepository;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Review> getAllReviews() {
        try {
            return reviewRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reviews", e);
        }
    }

    public List<Review> getReviewsByUser(Long userId) {
        try {
            return reviewRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reviews for user with id " + userId, e);
        }
    }

    public Review createReview(Review review) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            review.setUser(currentUser);
            return reviewRepository.save(review);
        } catch (Exception e) {
            throw new RuntimeException("Error creating review", e);
        }
    }

    public void deleteReview(Long id) {
        try {
            Review existingReview = reviewRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Review not found with id " + id));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            if (!existingReview.getUser().equals(currentUser)) {
                throw new IllegalArgumentException("You can only delete your own reviews.");
            }

            reviewRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting review with id " + id, e);
        }
    }
}
