package org.example.coffeservice.services;

import org.example.coffeservice.dto.response.BookingResponseDTO;
import org.example.coffeservice.dto.response.DeskResponseDTO;
import org.example.coffeservice.dto.response.UserResponseDTO;
import org.example.coffeservice.dto.response.ReviewResponseDTO;
import org.example.coffeservice.models.Booking;
import org.example.coffeservice.models.Desk;
import org.example.coffeservice.models.Review;
import org.example.coffeservice.models.User;
import org.example.coffeservice.repositories.BookingRepository;
import org.example.coffeservice.repositories.DeskRepository;
import org.example.coffeservice.repositories.ReviewRepository;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<BookingResponseDTO> getAllBookings() {
        try {
            List<Booking> bookings = bookingRepository.findAll();
            return bookings.stream()
                    .map(this::convertBookingToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all bookings", e);
        }
    }

    public List<DeskResponseDTO> getAllDesks() {
        try {
            List<Desk> desks = deskRepository.findAll();
            return desks.stream()
                    .map(this::convertDeskToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all desks", e);
        }
    }

    public List<UserResponseDTO> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(this::convertUserToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all users", e);
        }
    }

    public void deleteBooking(Long id) {
        try {
            bookingRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting booking with id " + id, e);
        }
    }

    public void deleteDesk(Long id) {
        try {
            deskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting desk with id " + id, e);
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user with id " + id, e);
        }
    }

    public UserResponseDTO addUser(User user) {
        try {
            user.setRole("VISITOR");
            User savedUser = userRepository.save(user);
            return convertUserToDTO(savedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error adding new user", e);
        }
    }

    public UserResponseDTO updateUser(Long id, User user) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhone(user.getPhone());
            User updatedUser = userRepository.save(existingUser);
            return convertUserToDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user with id " + id, e);
        }
    }

    public UserResponseDTO assignRoleToUser(Long userId, String role) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));
            if (!role.equals("VISITOR") && !role.equals("ADMIN") && !role.equals("WAITER")) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
            user.setRole(role);
            User updatedUser = userRepository.save(user);
            return convertUserToDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error assigning role to user with id " + userId, e);
        }
    }

    public UserResponseDTO blockUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            user.setLocked(true);
            User updatedUser = userRepository.save(user);
            return convertUserToDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error blocking user with id " + id, e);
        }
    }

    public UserResponseDTO unblockUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            user.setLocked(false);
            User updatedUser = userRepository.save(user);
            return convertUserToDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error unblocking user with id " + id, e);
        }
    }

    public ReviewResponseDTO moderateReview(Long reviewId, String newText) {
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException("Review not found with id " + reviewId));
            review.setReviewText(newText);
            Review updatedReview = reviewRepository.save(review);
            return convertReviewToDTO(updatedReview);
        } catch (Exception e) {
            throw new RuntimeException("Error moderating review with id " + reviewId, e);
        }
    }

    private BookingResponseDTO convertBookingToDTO(Booking booking) {
        String userName = booking.getUser().getFirstName() + " " + booking.getUser().getLastName();
        String deskLocation = booking.getDesk().getLocation();
        return new BookingResponseDTO(booking.getId(), userName, deskLocation,
                booking.getStartDate(), booking.getEndDate(), booking.getStatus());
    }

    private DeskResponseDTO convertDeskToDTO(Desk desk) {
        return new DeskResponseDTO(desk.getId(), desk.getDeskNumber(), desk.getCapacity(), desk.getLocation());
    }

    private UserResponseDTO convertUserToDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhone(), user.getRole(), user.isLocked());
    }

    private ReviewResponseDTO convertReviewToDTO(Review review) {
        String userName = review.getUser().getFirstName() + " " + review.getUser().getLastName();
        return new ReviewResponseDTO(review.getId(), userName, review.getRating(), review.getReviewText(), review.getReviewDate());
    }
}
