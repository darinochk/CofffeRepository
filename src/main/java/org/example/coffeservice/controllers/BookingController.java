package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.BookingRequestDTO;
import org.example.coffeservice.dto.response.BookingResponseDTO;
import org.example.coffeservice.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/user")
    public List<BookingResponseDTO> getBookingsByUser(Authentication authentication) {
        return bookingService.getBookingsByUser();
    }

    @GetMapping("/desk/{deskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponseDTO> getBookingsByDesk(@PathVariable Long deskId) {
        try {
            return bookingService.getBookingsByDesk(deskId).stream()
                    .map(booking -> bookingService.convertToDTO(booking))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения бронирований для стола с id " + deskId, e);
        }
    }

    @PostMapping("/create")
    public BookingResponseDTO createBooking(@RequestBody BookingRequestDTO bookingRequest, Authentication authentication) {
        return bookingService.createBooking(bookingRequest);
    }

    @PutMapping("/update/{id}")
    public BookingResponseDTO updateBooking(@PathVariable Long id, @RequestBody BookingRequestDTO bookingRequest) {
        return bookingService.updateBooking(id, bookingRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
