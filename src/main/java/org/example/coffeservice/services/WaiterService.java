package org.example.coffeservice.services;

import org.example.coffeservice.dto.response.BookingResponseDTO;
import org.example.coffeservice.dto.response.OrderDetailsResponseDTO;
import org.example.coffeservice.dto.response.OrderResponseDTO;
import org.example.coffeservice.models.Booking;
import org.example.coffeservice.models.Order;
import org.example.coffeservice.models.OrderDetails;
import org.example.coffeservice.repositories.BookingRepository;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.example.coffeservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaiterService {

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
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
                .orElseThrow(() -> new IllegalArgumentException("OrderDetails not found with id " + orderDetailsId));
        orderDetails.setStatus("CONFIRMED");

        List<Order> orders = orderRepository.findByOrderDetailsId(orderDetailsId);
        double totalAmount = orders.stream()
                .mapToDouble(order -> order.getQuantity() * order.getFood().getPrice())
                .sum();
        orderDetails.setAmount(totalAmount);
        OrderDetails savedDetails = orderDetailsRepository.save(orderDetails);

        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());

        return new OrderDetailsResponseDTO(savedDetails.getId(), savedDetails.getAmount(), orderDTOs);
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
