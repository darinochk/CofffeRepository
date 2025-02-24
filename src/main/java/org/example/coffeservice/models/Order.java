package org.example.coffeservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "orderr")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Food cannot be null")
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @NotNull(message = "Order details cannot be null")
    @ManyToOne
    @JoinColumn(name = "order_details_id")
    private OrderDetails orderDetails;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    public Order() {}

    public Order(Food food, OrderDetails orderDetails, int quantity) {
        this.food = food;
        this.orderDetails = orderDetails;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        if (food != null) {
            return this.quantity * food.getPrice();
        }
        return 0.0;
    }
}
