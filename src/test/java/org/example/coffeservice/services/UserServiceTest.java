package org.example.coffeservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.coffeservice.dto.request.user.UserRequestDTO;
import org.example.coffeservice.dto.response.user.UserResponseDTO;
import org.example.coffeservice.models.user.Role;
import org.example.coffeservice.models.user.User;
import org.example.coffeservice.repositories.UserRepository;
import org.example.coffeservice.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  private User testUser;
  private UserRequestDTO userRequestDTO;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setFirstName("John");
    testUser.setLastName("Doe");
    testUser.setEmail("john.doe@example.com");
    testUser.setPassword("password123");
    testUser.setPhone("+1234567890");
    testUser.setRole(Role.VISITOR);
    testUser.setLocked(false);

    userRequestDTO =
        UserRequestDTO.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .password("password123")
            .phone("+1234567890")
            .locked(false)
            .role(Role.VISITOR)
            .build();
  }

  @Test
  void testGetAllUsers_Success() {
    // Given
    List<User> users = Arrays.asList(testUser);
    when(userRepository.findAll()).thenReturn(users);

    // When
    List<UserResponseDTO> result = userService.getAllUsers();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("John", result.get(0).getFirstName());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  void testGetUserById_Success() {
    // Given
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

    // When
    UserResponseDTO result = userService.getUserById(1L);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("John", result.getFirstName());
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  void testGetUserById_NotFound() {
    // Given
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  void testCreateUser_Success() {
    // Given
    when(userRepository.save(any(User.class))).thenReturn(testUser);

    // When
    UserResponseDTO result = userService.createUser(userRequestDTO);

    // Then
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testCreateUser_InvalidRole() {
    // Given
    UserRequestDTO invalidRequest =
        UserRequestDTO.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .password("password123")
            .phone("+1234567890")
            .locked(false)
            .role(null)
            .build();

    // When & Then
    assertThrows(RuntimeException.class, () -> userService.createUser(invalidRequest));
  }

  @Test
  void testUpdateUser_Success() {
    // Given
    UserRequestDTO updateRequest =
        UserRequestDTO.builder().firstName("Jane").lastName("Smith").phone("+9876543210").build();

    try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
      securityUtilsMock.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
      when(userRepository.save(any(User.class))).thenReturn(testUser);

      // When
      UserResponseDTO result = userService.updateUser(updateRequest);

      // Then
      assertNotNull(result);
      verify(userRepository, times(1)).save(any(User.class));
    }
  }

  @Test
  void testDeleteUser_Success() {
    // Given
    try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
      securityUtilsMock.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
      doNothing().when(userRepository).deleteById(1L);

      // When
      userService.deleteUser(1L);

      // Then
      verify(userRepository, times(1)).deleteById(1L);
    }
  }

  @Test
  void testDeleteUser_DifferentUser() {
    // Given
    User otherUser = new User();
    otherUser.setId(2L);

    try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
      securityUtilsMock.when(SecurityUtils::getCurrentUser).thenReturn(otherUser);

      // When & Then
      assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
      verify(userRepository, never()).deleteById(anyLong());
    }
  }

  @Test
  void testSearchUsers_Success() {
    // Given
    List<User> users = Arrays.asList(testUser);
    when(userRepository
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                "John", null, null))
        .thenReturn(users);

    // When
    List<UserResponseDTO> result = userService.searchUsers("John", null, null);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(userRepository, times(1))
        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            "John", null, null);
  }

  @Test
  void testLoadUserByUsername_Success() {
    // Given
    when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

    // When
    User result = userService.loadUserByUsername("john.doe@example.com");

    // Then
    assertNotNull(result);
    assertEquals("john.doe@example.com", result.getEmail());
    verify(userRepository, times(1)).findByEmail("john.doe@example.com");
  }

  @Test
  void testLoadUserByUsername_NotFound() {
    // Given
    when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        org.springframework.security.core.userdetails.UsernameNotFoundException.class,
        () -> userService.loadUserByUsername("notfound@example.com"));
  }
}
