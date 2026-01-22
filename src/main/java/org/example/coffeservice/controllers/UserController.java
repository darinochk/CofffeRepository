package org.example.coffeservice.controllers;

import java.util.List;
import org.example.coffeservice.dto.request.user.UserRequestDTO;
import org.example.coffeservice.dto.response.coffee.OrderDetailsResponseDTO;
import org.example.coffeservice.dto.response.user.UserResponseDTO;
import org.example.coffeservice.services.OrderService;
import org.example.coffeservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired private UserService userService;

  @Autowired private OrderService orderService;

  @GetMapping("/")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponseDTO> getAllUsers() {
    try {
      return userService.getAllUsers();
    } catch (Exception exception) {
      throw new RuntimeException("Error retrieving users", exception);
    }
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponseDTO getUserById(@PathVariable Long id) {
    try {
      return userService.getUserById(id);
    } catch (Exception exception) {
      throw new RuntimeException("Error retrieving user by id", exception);
    }
  }

  @GetMapping("/orderDetailsByBooking/{bookingId}")
  @PreAuthorize("hasRole('USER')")
  public List<OrderDetailsResponseDTO> getOrderDetailsByBooking(@PathVariable Long bookingId) {
    try {
      return orderService.getOrderDetailsByBookingId(bookingId);
    } catch (Exception exception) {
      throw new RuntimeException(
          "Error retrieving OrderDetails for booking ID " + bookingId, exception);
    }
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponseDTO createUser(@RequestBody UserRequestDTO userRequest) {
    try {
      return userService.createUser(userRequest);
    } catch (Exception exception) {
      throw new RuntimeException("Error creating user", exception);
    }
  }

  @PutMapping("/update")
  public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userRequest) {
    try {
      UserResponseDTO updatedUser = userService.updateUser(userRequest);
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException exception) {
      return ResponseEntity.badRequest().body(null);
    } catch (Exception exception) {
      return ResponseEntity.status(500).body(null);
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    try {
      userService.deleteUser(id);
      return ResponseEntity.ok("User deleted successfully.");
    } catch (IllegalArgumentException exception) {
      return ResponseEntity.badRequest().body(exception.getMessage());
    } catch (Exception exception) {
      return ResponseEntity.status(500).body("Error deleting user");
    }
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponseDTO> searchUsers(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String lastname,
      @RequestParam(required = false) String email) {
    try {
      return userService.searchUsers(name, lastname, email);
    } catch (Exception exception) {
      throw new RuntimeException("Error searching for users", exception);
    }
  }
}
