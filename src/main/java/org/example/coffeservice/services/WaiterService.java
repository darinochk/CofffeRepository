package org.example.coffeservice.services;

import org.example.coffeservice.dto.response.coffee.BookingResponseDTO;
import org.example.coffeservice.dto.response.coffee.OrderDetailsResponseDTO;
import org.example.coffeservice.dto.response.coffee.OrderResponseDTO;
import org.example.coffeservice.models.coffee.Booking;
import org.example.coffeservice.models.coffee.Order;
import org.example.coffeservice.models.coffee.OrderDetails;
import org.example.coffeservice.repositories.BookingRepository;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.example.coffeservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WaiterService {

    private static final Logger logger = LoggerFactory.getLogger(WaiterService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + bookingId));

        booking.setStatus("CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        return convertToDTO(savedBooking);
    }

    public OrderDetailsResponseDTO confirmOrderDetails(Long orderDetailsId) {
        logger.info("Confirming order details with ID: {}", orderDetailsId);

        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
                .orElseThrow(() -> {
                    logger.error("OrderDetails not found with ID: {}", orderDetailsId);
                    return new IllegalArgumentException("OrderDetails not found with id " + orderDetailsId);
                });

        if ("CONFIRMED".equals(orderDetails.getStatus())) {
            logger.warn("OrderDetails with ID {} is already confirmed.", orderDetailsId);
            throw new IllegalStateException("OrderDetails already confirmed.");
        }

        orderDetails.setStatus("CONFIRMED");
        List<Order> orders = orderRepository.findByOrderDetailsId(orderDetailsId);

        if (orders.isEmpty()) {
            logger.warn("No orders found for OrderDetails with ID: {}", orderDetailsId);
            throw new IllegalStateException("No orders found for these order details.");
        }

        double totalAmount = orders.stream()
                .mapToDouble(order -> order.getQuantity() * order.getFood().getPrice())
                .sum();
        orderDetails.setAmount(totalAmount);

        OrderDetails savedDetails = orderDetailsRepository.save(orderDetails);
        logger.info("OrderDetails with ID {} confirmed successfully.", orderDetailsId);

        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
        return new OrderDetailsResponseDTO(savedDetails.getId(), savedDetails.getAmount(), orderDTOs);
    }

    public List<OrderDetailsResponseDTO> getAllOrderDetails() {
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        return orderDetailsList.stream()
                .map(this::convertToOrderDetailsResponseDTO)
                .collect(Collectors.toList());
    }

    private OrderDetailsResponseDTO convertToOrderDetailsResponseDTO(OrderDetails orderDetails) {
        List<Order> orders = orderRepository.findByOrderDetailsId(orderDetails.getId());
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
        return new OrderDetailsResponseDTO(orderDetails.getId(), orderDetails.getAmount(), orderDTOs);
    }


    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getFood().getName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderDetails().getId()
        );
    }

    private BookingResponseDTO convertToDTO(Booking booking) {
        String userName = booking.getUser().getFirstName() + " " + booking.getUser().getLastName();
        String deskLocation = booking.getDesk().getLocation();
        return new BookingResponseDTO(
                booking.getId(),
                userName,
                deskLocation,
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus()
        );
    }
}
