package org.example.coffeservice.controllers;

import org.example.coffeservice.models.Booking;
import org.example.coffeservice.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> getAllBookings() {
        try {
            return bookingService.getAllBookings();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving bookings");
        }
    }

    @GetMapping("/user")
    public List<Booking> getBookingsByUser(Authentication authentication) {
        try {
            return bookingService.getBookingsByUser();
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Error retrieving user bookings");
        }
    }

    @GetMapping("/desk/{deskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> getBookingsByDesk(@PathVariable Long deskId) {
        try {
            return bookingService.getBookingsByDesk(deskId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving desk bookings");
        }
    }

    @PostMapping("/create")
    public Booking createBooking(@RequestBody Booking booking, Authentication authentication) {
        try {
            return bookingService.createBooking(booking);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Error creating booking");
        }
    }

    @PutMapping("/update/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        try {
            return bookingService.updateBooking(id, booking);
        } catch (Exception e) {
            throw new RuntimeException("Error updating booking");
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting booking");
        }
    }
}
