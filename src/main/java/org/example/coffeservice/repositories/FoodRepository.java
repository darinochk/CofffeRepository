package org.example.coffeservice.repositories;

import java.util.List;
import org.example.coffeservice.models.coffee.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
  Food findByName(String name);

  List<Food> findByFoodType(String foodType);
}
