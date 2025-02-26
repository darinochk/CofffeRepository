package org.example.coffeservice.services;

import org.example.coffeservice.dto.response.BookingResponseDTO;
import org.example.coffeservice.models.Booking;
import org.example.coffeservice.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaiterService {

    @Autowired
    private BookingRepository bookingRepository;

    public BookingResponseDTO confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + bookingId));

        booking.setStatus("CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        return convertToDTO(savedBooking);
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
