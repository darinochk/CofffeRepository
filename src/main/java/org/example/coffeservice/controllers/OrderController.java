package org.example.coffeservice.controllers;

import java.util.List;
import org.example.coffeservice.dto.request.coffee.OrderRequestDTO;
import org.example.coffeservice.dto.response.coffee.OrderResponseDTO;
import org.example.coffeservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired private OrderService orderService;

  @GetMapping("/")
  @PreAuthorize("hasRole('ADMIN')")
  public List<OrderResponseDTO> getAllOrders() {
    try {
      return orderService.getAllOrders();
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения заказов", exception);
    }
  }

  @PostMapping("/create")
  public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO orderRequest) {
    try {
      return orderService.createOrder(orderRequest);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка создания заказа", exception);
    }
  }

  @PutMapping("/update/{id}")
  public OrderResponseDTO updateOrder(
      @PathVariable Long id, @RequestBody OrderRequestDTO orderRequest) {
    try {
      return orderService.updateOrder(id, orderRequest);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка обновления заказа", exception);
    }
  }

  @DeleteMapping("/delete/{id}")
  public void deleteOrder(@PathVariable Long id) {
    try {
      orderService.deleteOrder(id);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка удаления заказа", exception);
    }
  }
}
