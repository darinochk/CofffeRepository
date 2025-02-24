package org.example.coffeservice.controllers;

import org.example.coffeservice.models.Order;
import org.example.coffeservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAllOrders() {
        try {
            return orderService.getAllOrders();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving orders");
        }
    }

    @PostMapping("/create")
    public Order createOrder(@RequestBody Order order) {
        try {
            return orderService.createOrder(order);
        } catch (Exception e) {
            throw new RuntimeException("Error creating order");
        }
    }

    @PutMapping("/update/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            return orderService.updateOrder(id, order);
        } catch (Exception e) {
            throw new RuntimeException("Error updating order");
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting order");
        }
    }
}
