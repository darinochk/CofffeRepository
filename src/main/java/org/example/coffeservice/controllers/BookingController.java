package org.example.coffeservice.controllers;

import java.util.List;
import org.example.coffeservice.dto.request.coffee.BookingRequestDTO;
import org.example.coffeservice.dto.response.coffee.BookingResponseDTO;
import org.example.coffeservice.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

  @Autowired private BookingService bookingService;

  @GetMapping("/")
  @PreAuthorize("hasRole('ADMIN')")
  public List<BookingResponseDTO> getAllBookings() {
    return bookingService.getAllBookings();
  }

  @GetMapping("/user")
  public List<BookingResponseDTO> getBookingsByUser() {
    return bookingService.getBookingsByUser();
  }

  @GetMapping("/desk/{deskId}")
  @PreAuthorize("hasRole('ADMIN')")
  public List<BookingResponseDTO> getBookingsByDesk(@PathVariable Long deskId) {
    return bookingService.getBookingsByDesk(deskId);
  }

  @PostMapping("/create")
  public BookingResponseDTO createBooking(@RequestBody BookingRequestDTO bookingRequest) {
    return bookingService.createBooking(bookingRequest);
  }

  @PutMapping("/update/{id}")
  public BookingResponseDTO updateBooking(
      @PathVariable Long id, @RequestBody BookingRequestDTO bookingRequest) {
    return bookingService.updateBooking(id, bookingRequest);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteBooking(@PathVariable Long id) {
    bookingService.deleteBooking(id);
  }
}
