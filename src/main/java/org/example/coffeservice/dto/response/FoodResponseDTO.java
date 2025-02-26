package org.example.coffeservice.dto.response;

public class FoodResponseDTO {

    private Long id;
    private String name;
    private double price;
    private String foodType;

    public FoodResponseDTO(Long id, String name, double price, String foodType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.foodType = foodType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
