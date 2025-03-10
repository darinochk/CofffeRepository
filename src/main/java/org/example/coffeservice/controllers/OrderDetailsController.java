package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.coffee.OrderDetailsRequestDTO;
import org.example.coffeservice.dto.request.coffee.OrderRequestDTO;
import org.example.coffeservice.dto.response.coffee.OrderDetailsResponseDTO;
import org.example.coffeservice.dto.response.coffee.OrderResponseDTO;
import org.example.coffeservice.models.coffee.Order;
import org.example.coffeservice.models.coffee.OrderDetails;
import org.example.coffeservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order-details")
public class OrderDetailsController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public OrderDetailsResponseDTO createOrderDetails(@RequestBody OrderDetailsRequestDTO orderDetailsRequest) {
        try {
            return orderService.createOrderDetails(orderDetailsRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания деталей заказа", e);
        }
    }

    @GetMapping("/get/{bookingId}")
    public List<OrderDetails> getOrderDetails(@PathVariable Long bookingId) {
        try {
            return orderService.getOrderDetailsByBookingId(bookingId);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания деталей заказа", e);
        }
    }

    @GetMapping("/confirm_details/{orderDetailsId}")
    public OrderDetailsResponseDTO confirmDetails(@PathVariable Long orderDetailsId) {
        try {
            List<Order> orders = orderService.getOrdersByOrderDetailsId(orderDetailsId);

            double totalAmount = 0.0;
            List<OrderResponseDTO> orderDTOs = orders.stream()
                    .map(this::convertOrderToDTO)
                    .collect(Collectors.toList());

            for (Order order : orders) {
                totalAmount += order.getQuantity() * order.getFood().getPrice();
            }

            if (!orders.isEmpty()) {
                OrderDetails orderDetails = orders.get(0).getOrderDetails();
                orderDetails.setAmount(totalAmount);
                orderService.updateOrderDetails(orderDetails);
            }

            return new OrderDetailsResponseDTO(orderDetailsId, totalAmount, orderDTOs);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения или обработки деталей заказа", e);
        }
    }

    private OrderResponseDTO convertOrderToDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getFood().getName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderDetails().getId()
        );
    }
}
