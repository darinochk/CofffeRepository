package org.example.coffeservice.controllers;

import org.example.coffeservice.models.User;
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
    public List<User> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving users");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable Long id) {
        try {
            return userService.getUserById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by id");
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@RequestBody User user) {
        try {
            return userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user);
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
    public List<User> searchUsers(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String lastname,
                                  @RequestParam(required = false) String email) {
        try {
            return userService.searchUsers(name, lastname, email);
        } catch (Exception e) {
            throw new RuntimeException("Error searching for users");
        }
    }
}
