package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.FoodRequestDTO;
import org.example.coffeservice.dto.response.FoodResponseDTO;
import org.example.coffeservice.models.Food;
import org.example.coffeservice.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<FoodResponseDTO> getAllFood() {
        try {
            List<Food> foods = foodRepository.findAll();
            return foods.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения списка блюд", e);
        }
    }

    public FoodResponseDTO createFood(FoodRequestDTO foodRequest) {
        try {
            Food food = new Food();
            food.setName(foodRequest.getName());
            food.setPrice(foodRequest.getPrice());
            food.setFoodType(foodRequest.getFoodType());
            Food savedFood = foodRepository.save(food);
            return convertToDTO(savedFood);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания блюда", e);
        }
    }

    public FoodResponseDTO updateFood(Long id, FoodRequestDTO foodRequest) {
        try {
            Food existingFood = foodRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено с id " + id));
            existingFood.setName(foodRequest.getName());
            existingFood.setPrice(foodRequest.getPrice());
            existingFood.setFoodType(foodRequest.getFoodType());
            Food updatedFood = foodRepository.save(existingFood);
            return convertToDTO(updatedFood);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления блюда с id " + id, e);
        }
    }

    public void deleteFood(Long id) {
        try {
            foodRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления блюда с id " + id, e);
        }
    }

    private FoodResponseDTO convertToDTO(Food food) {
        return new FoodResponseDTO(food.getId(), food.getName(), food.getPrice(), food.getFoodType());
    }
}
