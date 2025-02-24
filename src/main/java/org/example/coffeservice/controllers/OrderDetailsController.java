package org.example.coffeservice.controllers;

import org.example.coffeservice.models.Order;
import org.example.coffeservice.models.OrderDetails;
import org.example.coffeservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-details")
public class OrderDetailsController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/confirm_details/{orderDetailsId}")
    public OrderDetailsResponse confirmDetails(@PathVariable Long orderDetailsId) {
        try {
            List<Order> orders = orderService.getOrdersByOrderDetailsId(orderDetailsId);

            double totalAmount = 0.0;
            for (Order order : orders) {
                double itemCost = order.getQuantity() * order.getFood().getPrice();
                totalAmount += itemCost;
            }
            if (!orders.isEmpty()) {
                OrderDetails orderDetails = orders.get(0).getOrderDetails();
                orderDetails.setAmount(totalAmount);
                orderService.updateOrderDetails(orderDetails);
            }

            return new OrderDetailsResponse(orders, totalAmount);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving or processing order details");
        }
    }

    public static class OrderDetailsResponse {
        private List<Order> orders;
        private double totalAmount;

        public OrderDetailsResponse(List<Order> orders, double totalAmount) {
            this.orders = orders;
            this.totalAmount = totalAmount;
        }

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }
}
