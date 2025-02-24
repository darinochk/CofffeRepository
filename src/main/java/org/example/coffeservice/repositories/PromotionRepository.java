package org.example.coffeservice.repositories;

import org.example.coffeservice.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByName(String name);
    List<Promotion> findByStartDateAfter(Date startDate);
}
