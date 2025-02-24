package org.example.coffeservice.services;

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

    public List<Booking> getAllBookings() {
        try {
            return bookingRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all bookings", e);
        }
    }

    public List<Desk> getAllDesks() {
        try {
            return deskRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all desks", e);
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
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

    public User addUser(User user) {
        try {
            user.setRole("VISITOR");
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error adding new user", e);
        }
    }

    public User updateUser(Long id, User user) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhone(user.getPhone());
            return userRepository.save(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user with id " + id, e);
        }
    }

    public User assignRoleToUser(Long userId, String role) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));

            if (!role.equals("VISITOR") && !role.equals("ADMIN") && !role.equals("WAITER")) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }

            user.setRole(role);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error assigning role to user with id " + userId, e);
        }
    }

    public User blockUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            user.setLocked(true);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error blocking user with id " + id, e);
        }
    }

    public User unblockUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            user.setLocked(false);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error unblocking user with id " + id, e);
        }
    }

    public Review moderateReview(Long reviewId, String newText) {
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException("Review not found with id " + reviewId));

            review.setReviewText(newText);
            return reviewRepository.save(review);
        } catch (Exception e) {
            throw new RuntimeException("Error moderating review with id " + reviewId, e);
        }
    }
}
