package org.example.coffeservice.services;

import org.example.coffeservice.models.Order;
import org.example.coffeservice.models.OrderDetails;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.example.coffeservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all orders", e);
        }
    }

    public Order createOrder(Order order) {
        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error creating order", e);
        }
    }

    public Order updateOrder(Long id, Order order) {
        try {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with id " + id));

            existingOrder.setQuantity(order.getQuantity());
            return orderRepository.save(existingOrder);
        } catch (Exception e) {
            throw new RuntimeException("Error updating order with id " + id, e);
        }
    }

    public void deleteOrder(Long id) {
        try {
            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting order with id " + id, e);
        }
    }

    public List<Order> getOrdersByOrderDetailsId(Long orderDetailsId) {
        try {
            return orderRepository.findByOrderDetailsId(orderDetailsId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving orders by OrderDetailsId", e);
        }
    }

    public void updateOrderDetails(OrderDetails orderDetails) {
        try {
            orderDetailsRepository.save(orderDetails);
        } catch (Exception e) {
            throw new RuntimeException("Error updating OrderDetails", e);
        }
    }
}
