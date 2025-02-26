package org.example.coffeservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequestDTO {

    @NotNull(message = "ID блюда не может быть null")
    private Long foodId;

    @NotNull(message = "ID деталей заказа не может быть null")
    private Long orderDetailsId;

    @Min(value = 1, message = "Количество должно быть не меньше 1")
    private int quantity;

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public Long getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Long orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
