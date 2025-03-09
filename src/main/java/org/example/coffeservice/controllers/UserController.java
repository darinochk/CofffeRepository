package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.user.UserRequestDTO;
import org.example.coffeservice.dto.response.coffee.OrderDetailsResponseDTO;
import org.example.coffeservice.dto.response.user.UserResponseDTO;
import org.example.coffeservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving users");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        try {
            return userService.getUserById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by id");
        }
    }

    @GetMapping("/orderDetailsByBooking/{bookingId}")
    @PreAuthorize("hasRole('USER')")
    public List<OrderDetailsResponseDTO> getOrderDetailsByBooking(@PathVariable Long bookingId) {
        try {
            return userService.getOrderDetailsByBookingId(bookingId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving OrderDetails for booking ID " + bookingId, e);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO createUser(@RequestBody UserRequestDTO userRequest) {
        try {
            return userService.createUser(userRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userRequest) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(userRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user");
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> searchUsers(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) String lastname,
                                             @RequestParam(required = false) String email) {
        try {
            return userService.searchUsers(name, lastname, email);
        } catch (Exception e) {
            throw new RuntimeException("Error searching for users");
        }
    }
}
