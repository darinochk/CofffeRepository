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
import org.example.coffeservice.repositories.FoodRepository;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.example.coffeservice.repositories.OrderRepository;
import org.example.coffeservice.utils.Constants;
import org.example.coffeservice.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

  @Autowired private OrderRepository orderRepository;

  @Autowired private OrderDetailsRepository orderDetailsRepository;

  @Autowired private FoodRepository foodRepository;

  @Autowired private BookingRepository bookingRepository;

  public List<OrderResponseDTO> getAllOrders() {
    try {
      List<Order> orders = orderRepository.findAll();
      return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения всех заказов", exception);
    }
  }

  public OrderDetailsResponseDTO createOrderDetails(OrderDetailsRequestDTO request) {
    Booking booking =
        bookingRepository
            .findById(request.getBookingId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Бронировние не найдено с id " + request.getBookingId()));

    OrderDetails orderDetails =
        orderDetailsRepository.saveAndFlush(
            OrderDetails.builder()
                .amount(Constants.DEFAULT_AMOUNT)
                .booking(booking)
                .status(Constants.STATUS_PENDING)
                .build());

    return OrderDetailsResponseDTO.builder()
        .orderDetailsId(orderDetails.getId())
        .totalAmount(orderDetails.getAmount())
        .build();
  }

  public List<OrderDetailsResponseDTO> getOrderDetailsByBookingId(Long bookingId) {
    List<OrderDetails> orderDetailsList = orderDetailsRepository.findByBookingId(bookingId);

    if (orderDetailsList.isEmpty()) {
      throw new IllegalArgumentException("No OrderDetails found for booking ID " + bookingId);
    }

    return orderDetailsList.stream()
        .map(this::convertToOrderDetailsResponseDTO)
        .collect(Collectors.toList());
  }

  private OrderDetailsResponseDTO convertToOrderDetailsResponseDTO(OrderDetails orderDetails) {
    List<Order> orders = orderRepository.findByOrderDetailsId(orderDetails.getId());
    return new OrderDetailsResponseDTO(
        orderDetails.getId(),
        orderDetails.getAmount(),
        orders.stream()
            .map(
                order ->
                    new OrderResponseDTO(
                        order.getId(),
                        order.getFood().getName(),
                        order.getQuantity(),
                        OrderUtils.calculateTotalPrice(order),
                        order.getOrderDetails().getId()))
            .collect(Collectors.toList()));
  }

  public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
    try {
      Food food =
          foodRepository
              .findById(orderRequest.getFoodId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Блюдо не найдено с id " + orderRequest.getFoodId()));
      OrderDetails orderDetails =
          orderDetailsRepository
              .findById(orderRequest.getOrderDetailsId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Детали заказа не найдены с id " + orderRequest.getOrderDetailsId()));

      Order order = new Order();
      order.setFood(food);
      order.setOrderDetails(orderDetails);
      order.setQuantity(orderRequest.getQuantity());

      Order savedOrder = orderRepository.save(order);
      return convertToDTO(savedOrder);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка создания заказа", exception);
    }
  }

  public OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderRequest) {
    try {
      Order existingOrder =
          orderRepository
              .findById(id)
              .orElseThrow(() -> new IllegalArgumentException("Заказ не найден с id " + id));

      existingOrder.setQuantity(orderRequest.getQuantity());
      Order updatedOrder = orderRepository.save(existingOrder);
      return convertToDTO(updatedOrder);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка обновления заказа с id " + id, exception);
    }
  }

  public void deleteOrder(Long id) {
    try {
      orderRepository.deleteById(id);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка удаления заказа с id " + id, exception);
    }
  }

  public List<Order> getOrdersByOrderDetailsId(Long orderDetailsId) {
    try {
      return orderRepository.findByOrderDetailsId(orderDetailsId);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения заказов по id деталей заказа", exception);
    }
  }

  public void updateOrderDetails(OrderDetails orderDetails) {
    try {
      orderDetailsRepository.save(orderDetails);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка обновления деталей заказа", exception);
    }
  }

  private OrderResponseDTO convertToDTO(Order order) {
    String foodName = order.getFood().getName();
    int quantity = order.getQuantity();
    double totalPrice = OrderUtils.calculateTotalPrice(order);
    Long orderDetailsId = order.getOrderDetails().getId();
    return new OrderResponseDTO(order.getId(), foodName, quantity, totalPrice, orderDetailsId);
  }
}
