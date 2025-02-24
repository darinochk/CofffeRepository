package org.example.coffeservice.services;

import org.example.coffeservice.models.Booking;
import org.example.coffeservice.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaiterService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + bookingId));
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }
}
