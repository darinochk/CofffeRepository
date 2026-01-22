package org.example.coffeservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.example.coffeservice.dto.request.coffee.FoodRequestDTO;
import org.example.coffeservice.dto.response.coffee.FoodResponseDTO;
import org.example.coffeservice.services.FoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FoodController.class)
class FoodControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private FoodService foodService;

  @Autowired private ObjectMapper objectMapper;

  private FoodResponseDTO foodResponseDTO;
  private FoodRequestDTO foodRequestDTO;

  @BeforeEach
  void setUp() {
    foodResponseDTO =
        FoodResponseDTO.builder().id(1L).name("Cappuccino").price(5.50).foodType("DRINK").build();

    foodRequestDTO =
        FoodRequestDTO.builder().name("Cappuccino").price(5.50).foodType("DRINK").build();
  }

  @Test
  void testGetAllFood_Success() throws Exception {
    // Given
    List<FoodResponseDTO> foods = Arrays.asList(foodResponseDTO);
    when(foodService.getAllFood()).thenReturn(foods);

    // When & Then
    mockMvc
        .perform(get("/food/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].name").value("Cappuccino"))
        .andExpect(jsonPath("$[0].price").value(5.50));

    verify(foodService, times(1)).getAllFood();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testCreateFood_Success() throws Exception {
    // Given
    when(foodService.createFood(any(FoodRequestDTO.class))).thenReturn(foodResponseDTO);

    // When & Then
    mockMvc
        .perform(
            post("/food/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodRequestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Cappuccino"));

    verify(foodService, times(1)).createFood(any(FoodRequestDTO.class));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testUpdateFood_Success() throws Exception {
    // Given
    when(foodService.updateFood(eq(1L), any(FoodRequestDTO.class))).thenReturn(foodResponseDTO);

    // When & Then
    mockMvc
        .perform(
            put("/food/update/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodRequestDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));

    verify(foodService, times(1)).updateFood(eq(1L), any(FoodRequestDTO.class));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testDeleteFood_Success() throws Exception {
    // Given
    doNothing().when(foodService).deleteFood(1L);

    // When & Then
    mockMvc.perform(delete("/food/delete/1").with(csrf())).andExpect(status().isOk());

    verify(foodService, times(1)).deleteFood(1L);
  }

  @Test
  @WithMockUser(roles = "VISITOR")
  void testCreateFood_Forbidden() throws Exception {
    // Given - USER role doesn't have ADMIN access

    // When & Then
    mockMvc
        .perform(
            post("/food/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodRequestDTO)))
        .andExpect(status().isForbidden());

    verify(foodService, never()).createFood(any(FoodRequestDTO.class));
  }

  @Test
  void testGetAllFood_EmptyList() throws Exception {
    // Given
    when(foodService.getAllFood()).thenReturn(Arrays.asList());

    // When & Then
    mockMvc
        .perform(get("/food/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());

    verify(foodService, times(1)).getAllFood();
  }
}
