package org.example.coffeservice.dto.response;

import java.util.List;

public class OrderDetailsResponseDTO {

    private Long orderDetailsId;
    private double totalAmount;
    private List<OrderResponseDTO> orders;

    public OrderDetailsResponseDTO(Long orderDetailsId, double totalAmount, List<OrderResponseDTO> orders) {
        this.orderDetailsId = orderDetailsId;
        this.totalAmount = totalAmount;
        this.orders = orders;
    }

    public Long getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Long orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderResponseDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderResponseDTO> orders) {
        this.orders = orders;
    }
}
