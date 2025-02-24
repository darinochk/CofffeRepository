package org.example.coffeservice.services;

import org.example.coffeservice.models.User;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all users", e);
        }
    }

    public User getUserById(Long id) {
        try {
            return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by id " + id, e);
        }
    }

    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    public User updateUser(User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setPhone(user.getPhone());

            return userRepository.save(currentUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    public void deleteUser(Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            if (!currentUser.getId().equals(id)) {
                throw new IllegalArgumentException("You can only delete your own profile.");
            }

            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user with id " + id, e);
        }
    }

    public List<User> searchUsers(String name, String lastname, String email) {
        try {
            return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, lastname, email);
        } catch (Exception e) {
            throw new RuntimeException("Error searching for users", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User appUser = userRepository.findByEmail(email);

            if (appUser != null) {
                var springUser = org.springframework.security.core.userdetails.User.withUsername(appUser.getEmail())
                        .password(appUser.getPassword())
                        .roles(appUser.getRole())
                        .build();

                return springUser;
            }

            throw new UsernameNotFoundException("User not found with email " + email);
        } catch (Exception e) {
            throw new RuntimeException("Error loading user by username", e);
        }
    }
}
