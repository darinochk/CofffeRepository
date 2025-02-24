package org.example.coffeservice.controllers;

import org.example.coffeservice.models.Food;
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
    public List<Food> getAllFood() {
        try {
            return foodService.getAllFood();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving food items");
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Food createFood(@RequestBody Food food) {
        try {
            return foodService.createFood(food);
        } catch (Exception e) {
            throw new RuntimeException("Error creating food item");
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Food updateFood(@PathVariable Long id, @RequestBody Food food) {
        try {
            return foodService.updateFood(id, food);
        } catch (Exception e) {
            throw new RuntimeException("Error updating food item");
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFood(@PathVariable Long id) {
        try {
            foodService.deleteFood(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting food item");
        }
    }
}
