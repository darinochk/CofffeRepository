package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.user.UserRequestDTO;
import org.example.coffeservice.dto.response.user.UserResponseDTO;
import org.example.coffeservice.models.user.Role;
import org.example.coffeservice.models.user.User;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all users", e);
        }
    }

    public UserResponseDTO getUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            return convertToResponseDTO(user);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by id " + id, e);
        }
    }

    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        try {
            User user = new User();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());
            user.setPhone(userRequest.getPhone());
            user.setLocked(userRequest.isLocked());
            user.setRole(userRequest.getRole() != null ? userRequest.getRole() : Role.VISITOR);

            User savedUser = userRepository.save(user);
            return convertToResponseDTO(savedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    public UserResponseDTO updateUser(UserRequestDTO userRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

            currentUser.setFirstName(userRequest.getFirstName());
            currentUser.setLastName(userRequest.getLastName());
            currentUser.setPhone(userRequest.getPhone());

            User updatedUser = userRepository.save(currentUser);
            return convertToResponseDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    public void deleteUser(Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (!currentUser.getId().equals(id)) {
                throw new IllegalArgumentException("You can only delete your own profile.");
            }

            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user with id " + id, e);
        }
    }

    public List<UserResponseDTO> searchUsers(String name, String lastname, String email) {
        try {
            List<User> users = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, lastname, email);
            return users.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching for users", e);
        }
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with provided email not found."));
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.isLocked()
        );
    }
}
