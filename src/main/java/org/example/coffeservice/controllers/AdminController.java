package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.user.UserRequestDTO;
import org.example.coffeservice.dto.response.coffee.BookingResponseDTO;
import org.example.coffeservice.dto.response.coffee.DeskResponseDTO;
import org.example.coffeservice.dto.response.coffee.OrderDetailsResponseDTO;
import org.example.coffeservice.dto.response.coffee.ReviewResponseDTO;
import org.example.coffeservice.dto.response.user.UserResponseDTO;
import org.example.coffeservice.models.user.User;
import org.example.coffeservice.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllBookings() {
        try {
            List<BookingResponseDTO> bookings = adminService.getAllBookings();
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return handleError("Error retrieving bookings");
        }
    }

    @GetMapping("/desks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllDesks() {
        try {
            List<DeskResponseDTO> desks = adminService.getAllDesks();
            return ResponseEntity.ok(desks);
        } catch (Exception e) {
            return handleError("Error retrieving desks");
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<UserResponseDTO> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return handleError("Error retrieving users");
        }
    }

    @DeleteMapping("/deleteBooking/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteBooking(@PathVariable Long id) {
        try {
            adminService.deleteBooking(id);
            return ResponseEntity.ok("Booking deleted successfully");
        } catch (Exception e) {
            return handleError("Error deleting booking");
        }
    }

    @DeleteMapping("/deleteDesk/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteDesk(@PathVariable Long id) {
        try {
            adminService.deleteDesk(id);
            return ResponseEntity.ok("Desk deleted successfully");
        } catch (Exception e) {
            return handleError("Error deleting desk");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerAdmin(@RequestBody UserRequestDTO userRequest) {
        UserResponseDTO newAdmin = adminService.createAdmin(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAdmin);
    }

    @DeleteMapping("/blockUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            adminService.blockUser(id);
            return ResponseEntity.ok("User blocked successfully");
        } catch (Exception e) {
            return handleError("Error of blocking this user");
        }
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        try {
            UserResponseDTO newUser = adminService.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return handleError("Error adding user");
        }
    }

    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            UserResponseDTO updatedUser = adminService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return handleError("Error updating user");
        }
    }

    @PutMapping("/assignRole/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> assignRoleToUser(@PathVariable Long userId, @RequestParam String role) {
        try {
            UserResponseDTO updatedUser = adminService.assignRoleToUser(userId, role);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return handleError("Error assigning role");
        }
    }

    @PutMapping("/blockUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> blockUser(@PathVariable Long id) {
        try {
            UserResponseDTO updatedUser = adminService.blockUser(id);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return handleError("Error blocking user");
        }
    }

    @PutMapping("/unblockUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> unblockUser(@PathVariable Long id) {
        try {
            UserResponseDTO updatedUser = adminService.unblockUser(id);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return handleError("Error unblocking user");
        }
    }

    @PutMapping("/moderateReview/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> moderateReview(@PathVariable Long reviewId, @RequestParam String newText) {
        try {
            ReviewResponseDTO updatedReview = adminService.moderateReview(reviewId, newText);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            return handleError("Error moderating review");
        }
    }

    @PutMapping("/confirmBooking/{id}")
    @PreAuthorize("hasRole('WAITER')")
    public BookingResponseDTO confirmBooking(@PathVariable Long id) {
        return adminService.confirmBooking(id);
    }

    @PutMapping("/confirmOrderDetails/{orderDetailsId}")
    @PreAuthorize("hasRole('WAITER')")
    public OrderDetailsResponseDTO confirmOrderDetails(@PathVariable Long orderDetailsId) {
        return adminService.confirmOrderDetails(orderDetailsId);
    }

    private ResponseEntity<Object> handleError(String message) {
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
