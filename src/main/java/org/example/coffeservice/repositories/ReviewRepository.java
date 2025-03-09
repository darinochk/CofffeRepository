package org.example.coffeservice.repositories;

import org.example.coffeservice.models.coffee.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(Long userId);
    List<Review> findByRating(int rating);
}
