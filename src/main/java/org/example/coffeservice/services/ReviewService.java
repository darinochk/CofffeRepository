package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.coffee.ReviewRequestDTO;
import org.example.coffeservice.dto.response.coffee.ReviewResponseDTO;
import org.example.coffeservice.models.coffee.Review;
import org.example.coffeservice.models.user.User;
import org.example.coffeservice.repositories.ReviewRepository;
import org.example.coffeservice.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

  @Autowired private ReviewRepository reviewRepository;

  public List<ReviewResponseDTO> getAllReviews() {
    try {
      List<Review> reviews = reviewRepository.findAll();
      return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения отзывов", exception);
    }
  }

  public List<ReviewResponseDTO> getReviewsByUser(Long userId) {
    try {
      List<Review> reviews = reviewRepository.findByUserId(userId);
      return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения отзывов для пользователя с id " + userId, exception);
    }
  }

  public ReviewResponseDTO createReview(ReviewRequestDTO reviewRequest) {
    try {
      User currentUser = SecurityUtils.getCurrentUser();

      Review review = new Review();
      review.setUser(currentUser);
      review.setRating(reviewRequest.getRating());
      review.setReviewText(reviewRequest.getReviewText());
      review.setReviewDate(reviewRequest.getReviewDate());

      Review savedReview = reviewRepository.save(review);
      return convertToDTO(savedReview);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка создания отзыва", exception);
    }
  }

  public void deleteReview(Long id) {
    try {
      Review existingReview =
          reviewRepository
              .findById(id)
              .orElseThrow(() -> new IllegalArgumentException("Отзыв не найден с id " + id));

      User currentUser = SecurityUtils.getCurrentUser();

      if (!existingReview.getUser().equals(currentUser)) {
        throw new IllegalArgumentException("Вы можете удалять только свои отзывы.");
      }

      reviewRepository.deleteById(id);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка удаления отзыва с id " + id, exception);
    }
  }

  private ReviewResponseDTO convertToDTO(Review review) {
    String userName = review.getUser().getFirstName() + " " + review.getUser().getLastName();
    return new ReviewResponseDTO(
        review.getId(),
        userName,
        review.getRating(),
        review.getReviewText(),
        review.getReviewDate());
  }
}
