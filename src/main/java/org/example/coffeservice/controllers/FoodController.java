package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.coffee.FoodRequestDTO;
import org.example.coffeservice.dto.response.coffee.FoodResponseDTO;
import org.example.coffeservice.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/")
    public List<FoodResponseDTO> getAllFood() {
        try {
            return foodService.getAllFood();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения списка блюд", e);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public FoodResponseDTO createFood(@RequestBody FoodRequestDTO foodRequest) {
        try {
            return foodService.createFood(foodRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания блюда", e);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public FoodResponseDTO updateFood(@PathVariable Long id, @RequestBody FoodRequestDTO foodRequest) {
        try {
            return foodService.updateFood(id, foodRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления блюда", e);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFood(@PathVariable Long id) {
        try {
            foodService.deleteFood(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления блюда", e);
        }
    }
}
