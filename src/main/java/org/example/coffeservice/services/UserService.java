package org.example.coffeservice.services;

import java.util.List;
import java.util.stream.Collectors;
import org.example.coffeservice.dto.request.user.UserRequestDTO;
import org.example.coffeservice.dto.response.user.UserResponseDTO;
import org.example.coffeservice.models.user.Role;
import org.example.coffeservice.models.user.User;
import org.example.coffeservice.repositories.UserRepository;
import org.example.coffeservice.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  public List<UserResponseDTO> getAllUsers() {
    try {
      List<User> users = userRepository.findAll();
      return users.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException("Error retrieving all users", exception);
    }
  }

  public UserResponseDTO getUserById(Long id) {
    try {
      User user =
          userRepository
              .findById(id)
              .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
      return convertToResponseDTO(user);
    } catch (Exception exception) {
      throw new RuntimeException("Error retrieving user by id " + id, exception);
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

      String roleStr = userRequest.getRole().name();
      try {
        user.setRole(Role.valueOf(roleStr.toUpperCase()));
      } catch (IllegalArgumentException exception) {
        throw new IllegalArgumentException("Invalid role: " + roleStr);
      }

      User savedUser = userRepository.save(user);
      return convertToResponseDTO(savedUser);
    } catch (Exception exception) {
      throw new RuntimeException("Error creating user", exception);
    }
  }

  public UserResponseDTO updateUser(UserRequestDTO userRequest) {
    try {
      User currentUser = SecurityUtils.getCurrentUser();

      currentUser.setFirstName(userRequest.getFirstName());
      currentUser.setLastName(userRequest.getLastName());
      currentUser.setPhone(userRequest.getPhone());

      User updatedUser = userRepository.save(currentUser);
      return convertToResponseDTO(updatedUser);
    } catch (Exception exception) {
      throw new RuntimeException("Error updating user", exception);
    }
  }

  public void deleteUser(Long id) {
    try {
      User currentUser = SecurityUtils.getCurrentUser();

      if (!currentUser.getId().equals(id)) {
        throw new IllegalArgumentException("You can only delete your own profile.");
      }

      userRepository.deleteById(id);
    } catch (Exception exception) {
      throw new RuntimeException("Error deleting user with id " + id, exception);
    }
  }

  public List<UserResponseDTO> searchUsers(String name, String lastname, String email) {
    try {
      List<User> users =
          userRepository
              .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                  name, lastname, email);
      return users.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException("Error searching for users", exception);
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
        user.isLocked());
  }
}
