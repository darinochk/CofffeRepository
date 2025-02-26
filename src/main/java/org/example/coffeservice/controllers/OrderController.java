package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.OrderRequestDTO;
import org.example.coffeservice.dto.response.OrderResponseDTO;
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
    public List<OrderResponseDTO> getAllOrders() {
        try {
            return orderService.getAllOrders();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения заказов", e);
        }
    }

    @PostMapping("/create")
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO orderRequest) {
        try {
            return orderService.createOrder(orderRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания заказа", e);
        }
    }

    @PutMapping("/update/{id}")
    public OrderResponseDTO updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequest) {
        try {
            return orderService.updateOrder(id, orderRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления заказа", e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления заказа", e);
        }
    }
}
