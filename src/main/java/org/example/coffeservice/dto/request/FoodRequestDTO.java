package org.example.coffeservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class FoodRequestDTO {

    @NotBlank(message = "Название блюда не может быть пустым")
    private String name;

    @Min(value = 0, message = "Цена должна быть неотрицательной")
    private double price;

    @NotBlank(message = "Тип блюда не может быть пустым")
    private String foodType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
