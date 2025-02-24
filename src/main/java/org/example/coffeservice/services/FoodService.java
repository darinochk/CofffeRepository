package org.example.coffeservice.services;

import org.example.coffeservice.models.Food;
import org.example.coffeservice.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<Food> getAllFood() {
        try {
            return foodRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving food items", e);
        }
    }

    public Food createFood(Food food) {
        try {
            return foodRepository.save(food);
        } catch (Exception e) {
            throw new RuntimeException("Error creating food item", e);
        }
    }

    public Food updateFood(Long id, Food food) {
        try {
            Food existingFood = foodRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Food not found with id " + id));

            existingFood.setName(food.getName());
            existingFood.setPrice(food.getPrice());
            existingFood.setFoodType(food.getFoodType());
            return foodRepository.save(existingFood);
        } catch (Exception e) {
            throw new RuntimeException("Error updating food item with id " + id, e);
        }
    }

    public void deleteFood(Long id) {
        try {
            foodRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting food item with id " + id, e);
        }
    }
}
