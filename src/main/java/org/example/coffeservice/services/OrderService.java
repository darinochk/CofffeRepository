package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.coffee.OrderDetailsRequestDTO;
import org.example.coffeservice.dto.request.coffee.OrderRequestDTO;
import org.example.coffeservice.dto.response.coffee.OrderDetailsResponseDTO;
import org.example.coffeservice.dto.response.coffee.OrderResponseDTO;
import org.example.coffeservice.models.coffee.Booking;
import org.example.coffeservice.models.coffee.Food;
import org.example.coffeservice.models.coffee.Order;
import org.example.coffeservice.models.coffee.OrderDetails;
import org.example.coffeservice.repositories.BookingRepository;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.example.coffeservice.repositories.OrderRepository;
import org.example.coffeservice.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public List<OrderResponseDTO> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return orders.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения всех заказов", e);
        }
    }

    public OrderDetailsResponseDTO createOrderDetails(OrderDetailsRequestDTO request) {
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(() -> new IllegalArgumentException("Бронировние не найдено с id " + request.getBookingId()));

        OrderDetails orderDetails = orderDetailsRepository.saveAndFlush(OrderDetails.builder().amount(0).booking(booking).status("PENDING").build());

        return OrderDetailsResponseDTO.builder().orderDetailsId(orderDetails.getId()).totalAmount(orderDetails.getAmount()).build();
    }

    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        try {
            Food food = foodRepository.findById(request.getFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено с id " + request.getFoodId()));
            OrderDetails orderDetails = orderDetailsRepository.findById(request.getOrderDetailsId())
                    .orElseThrow(() -> new IllegalArgumentException("Детали заказа не найдены с id " + request.getOrderDetailsId()));

            Order order = new Order();
            order.setFood(food);
            order.setOrderDetails(orderDetails);
            order.setQuantity(request.getQuantity());

            Order savedOrder = orderRepository.save(order);
            return convertToDTO(savedOrder);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания заказа", e);
        }
    }

    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO request) {
        try {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Заказ не найден с id " + id));

            existingOrder.setQuantity(request.getQuantity());
            Order updatedOrder = orderRepository.save(existingOrder);
            return convertToDTO(updatedOrder);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления заказа с id " + id, e);
        }
    }

    public void deleteOrder(Long id) {
        try {
            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления заказа с id " + id, e);
        }
    }

    public List<Order> getOrdersByOrderDetailsId(Long orderDetailsId) {
        try {
            return orderRepository.findByOrderDetailsId(orderDetailsId);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения заказов по id деталей заказа", e);
        }
    }

    public void updateOrderDetails(OrderDetails orderDetails) {
        try {
            orderDetailsRepository.save(orderDetails);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления деталей заказа", e);
        }
    }

    private OrderResponseDTO convertToDTO(Order order) {
        String foodName = order.getFood().getName();
        int quantity = order.getQuantity();
        double totalPrice = order.getTotalPrice();
        Long orderDetailsId = order.getOrderDetails().getId();
        return new OrderResponseDTO(order.getId(), foodName, quantity, totalPrice, orderDetailsId);
    }
}
