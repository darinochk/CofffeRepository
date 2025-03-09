package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.response.coffee.BookingResponseDTO;
import org.example.coffeservice.dto.response.coffee.OrderDetailsResponseDTO;
import org.example.coffeservice.dto.response.coffee.OrderResponseDTO;
import org.example.coffeservice.services.WaiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiter")
public class WaiterController {

    @Autowired
    private WaiterService waiterService;

    @GetMapping("/orders")
    @PreAuthorize("hasRole('WAITER')")
    public List<OrderResponseDTO> getAllOrders() {
        return waiterService.getAllOrders();
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('WAITER')")
    public List<BookingResponseDTO> getAllBookings() {
        return waiterService.getAllBookings();
    }

    @PutMapping("/confirmBooking/{id}")
    @PreAuthorize("hasRole('WAITER')")
    public BookingResponseDTO confirmBooking(@PathVariable Long id) {
        return waiterService.confirmBooking(id);
    }

    @PutMapping("/confirmOrderDetails/{orderDetailsId}")
    @PreAuthorize("hasRole('WAITER')")
    public OrderDetailsResponseDTO confirmOrderDetails(@PathVariable Long orderDetailsId) {
        return waiterService.confirmOrderDetails(orderDetailsId);
    }
}
