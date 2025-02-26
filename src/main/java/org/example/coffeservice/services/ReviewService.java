package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.ReviewRequestDTO;
import org.example.coffeservice.dto.response.ReviewResponseDTO;
import org.example.coffeservice.models.Review;
import org.example.coffeservice.models.User;
import org.example.coffeservice.repositories.ReviewRepository;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ReviewResponseDTO> getAllReviews() {
        try {
            List<Review> reviews = reviewRepository.findAll();
            return reviews.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения отзывов", e);
        }
    }

    public List<ReviewResponseDTO> getReviewsByUser(Long userId) {
        try {
            List<Review> reviews = reviewRepository.findByUserId(userId);
            return reviews.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения отзывов для пользователя с id " + userId, e);
        }
    }

    public ReviewResponseDTO createReview(ReviewRequestDTO reviewRequest) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("Пользователь не найден");
            }

            Review review = new Review();
            review.setUser(currentUser);
            review.setRating(reviewRequest.getRating());
            review.setReviewText(reviewRequest.getReviewText());
            review.setReviewDate(reviewRequest.getReviewDate());

            Review savedReview = reviewRepository.save(review);
            return convertToDTO(savedReview);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания отзыва", e);
        }
    }

    public void deleteReview(Long id) {
        try {
            Review existingReview = reviewRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Отзыв не найден с id " + id));

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("Пользователь не найден");
            }

            if (!existingReview.getUser().equals(currentUser)) {
                throw new IllegalArgumentException("Вы можете удалять только свои отзывы.");
            }

            reviewRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления отзыва с id " + id, e);
        }
    }

    private ReviewResponseDTO convertToDTO(Review review) {
        String userName = review.getUser().getFirstName() + " " + review.getUser().getLastName();
        return new ReviewResponseDTO(review.getId(), userName, review.getRating(), review.getReviewText(), review.getReviewDate());
    }
}
