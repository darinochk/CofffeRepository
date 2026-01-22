package org.example.coffeservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.example.coffeservice.dto.request.user.UserRequestDTO;
import org.example.coffeservice.dto.response.user.UserResponseDTO;
import org.example.coffeservice.models.user.Role;
import org.example.coffeservice.services.OrderService;
import org.example.coffeservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private UserService userService;

  @MockBean private OrderService orderService;

  @Autowired private ObjectMapper objectMapper;

  private UserResponseDTO userResponseDTO;
  private UserRequestDTO userRequestDTO;

  @BeforeEach
  void setUp() {
    userResponseDTO = UserResponseDTO.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("john.doe@example.com")
        .phone("+1234567890")
        .role(Role.VISITOR)
        .locked(false)
        .build();

    userRequestDTO = UserRequestDTO.builder()
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
  @WithMockUser(roles = "ADMIN")
  void testGetAllUsers_Success() throws Exception {
    // Given
    List<UserResponseDTO> users = Arrays.asList(userResponseDTO);
    when(userService.getAllUsers()).thenReturn(users);

    // When & Then
    mockMvc.perform(get("/users/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[0].lastName").value("Doe"));

    verify(userService, times(1)).getAllUsers();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testGetUserById_Success() throws Exception {
    // Given
    when(userService.getUserById(1L)).thenReturn(userResponseDTO);

    // When & Then
    mockMvc.perform(get("/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.firstName").value("John"));

    verify(userService, times(1)).getUserById(1L);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testCreateUser_Success() throws Exception {
    // Given
    when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

    // When & Then
    mockMvc.perform(post("/users/create")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.firstName").value("John"));

    verify(userService, times(1)).createUser(any(UserRequestDTO.class));
  }

  @Test
  @WithMockUser
  void testUpdateUser_Success() throws Exception {
    // Given
    when(userService.updateUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

    // When & Then
    mockMvc.perform(put("/users/update")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));

    verify(userService, times(1)).updateUser(any(UserRequestDTO.class));
  }

  @Test
  @WithMockUser
  void testDeleteUser_Success() throws Exception {
    // Given
    doNothing().when(userService).deleteUser(1L);

    // When & Then
    mockMvc.perform(delete("/users/delete/1")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("User deleted successfully."));

    verify(userService, times(1)).deleteUser(1L);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testSearchUsers_Success() throws Exception {
    // Given
    List<UserResponseDTO> users = Arrays.asList(userResponseDTO);
    when(userService.searchUsers("John", null, null)).thenReturn(users);

    // When & Then
    mockMvc.perform(get("/users/search")
            .param("name", "John"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].firstName").value("John"));

    verify(userService, times(1)).searchUsers("John", null, null);
  }

  @Test
  @WithMockUser(roles = "VISITOR")
  void testGetAllUsers_Forbidden() throws Exception {
    // Given - USER role doesn't have ADMIN access

    // When & Then
    mockMvc.perform(get("/users/"))
        .andExpect(status().isForbidden());

    verify(userService, never()).getAllUsers();
  }
}

