package org.example.coffeservice.repositories;

import org.example.coffeservice.models.coffee.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Food findByName(String name);
    List<Food> findByFoodType(String foodType);
}
