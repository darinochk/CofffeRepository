package org.example.coffeservice.repositories;

import java.util.Date;
import java.util.List;
import org.example.coffeservice.models.coffee.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
  List<Promotion> findByName(String name);

  List<Promotion> findByStartDateAfter(Date startDate);
}
