package org.example.coffeservice.dto.response;

public class OrderResponseDTO {

    private Long id;
    private String foodName;
    private int quantity;
    private double totalPrice;
    private Long orderDetailsId;

    public OrderResponseDTO(Long id, String foodName, int quantity, double totalPrice, Long orderDetailsId) {
        this.id = id;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDetailsId = orderDetailsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Long orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }
}
