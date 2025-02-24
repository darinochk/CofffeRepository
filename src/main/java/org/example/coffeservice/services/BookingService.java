package org.example.coffeservice.services;

import org.example.coffeservice.models.Booking;
import org.example.coffeservice.models.OrderDetails;
import org.example.coffeservice.models.User;
import org.example.coffeservice.repositories.BookingRepository;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<Booking> getAllBookings() {
        try {
            return bookingRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all bookings", e);
        }
    }

    public List<Booking> getBookingsByUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            return bookingRepository.findByUserId(currentUser.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving bookings for the user", e);
        }
    }

    public List<Booking> getBookingsByDesk(Long deskId) {
        try {
            return bookingRepository.findByDeskId(deskId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving bookings for desk with id " + deskId, e);
        }
    }

    public Booking createBooking(Booking booking) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            booking.setUser(currentUser);

            if (isDeskAvailable(booking.getDesk().getId(), booking.getStartDate(), booking.getEndDate())) {
                Booking savedBooking = bookingRepository.save(booking);

                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setBooking(savedBooking);
                orderDetails.setAmount(0.0); // Default value for now
                orderDetails.setStatus(null); // Default value for now

                orderDetailsRepository.save(orderDetails);

                return savedBooking;
            } else {
                throw new IllegalArgumentException("The desk is already booked for the selected time.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating booking", e);
        }
    }

    private boolean isDeskAvailable(Long deskId, Date startDate, Date endDate) {
        try {
            List<Booking> existingBookings = bookingRepository.findByDeskIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(deskId, startDate, endDate);
            return existingBookings.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Error checking desk availability", e);
        }
    }

    public Booking updateBooking(Long id, Booking booking) {
        try {
            Booking existingBooking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + id));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            if (!existingBooking.getUser().equals(currentUser)) {
                throw new IllegalArgumentException("You can only update your own bookings.");
            }

            existingBooking.setDesk(booking.getDesk());
            existingBooking.setStartDate(booking.getStartDate());
            existingBooking.setEndDate(booking.getEndDate());
            existingBooking.setStatus(booking.getStatus());

            return bookingRepository.save(existingBooking);
        } catch (Exception e) {
            throw new RuntimeException("Error updating booking with id " + id, e);
        }
    }

    public void deleteBooking(Long id) {
        try {
            Booking existingBooking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + id));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            if (!existingBooking.getUser().equals(currentUser)) {
                throw new IllegalArgumentException("You can only delete your own bookings.");
            }

            bookingRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting booking with id " + id, e);
        }
    }
}
