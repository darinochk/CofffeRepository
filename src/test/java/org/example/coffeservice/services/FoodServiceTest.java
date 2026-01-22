package org.example.coffeservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.coffeservice.dto.request.coffee.FoodRequestDTO;
import org.example.coffeservice.dto.response.coffee.FoodResponseDTO;
import org.example.coffeservice.models.coffee.Food;
import org.example.coffeservice.repositories.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

  @Mock private FoodRepository foodRepository;

  @InjectMocks private FoodService foodService;

  private Food testFood;
  private FoodRequestDTO foodRequestDTO;

  @BeforeEach
  void setUp() {
    testFood = Food.builder()
        .id(1L)
        .name("Cappuccino")
        .price(5.50)
        .foodType("DRINK")
        .build();

    foodRequestDTO = FoodRequestDTO.builder()
        .name("Cappuccino")
        .price(5.50)
        .foodType("DRINK")
        .build();
  }

  @Test
  void testGetAllFood_Success() {
    // Given
    List<Food> foods = Arrays.asList(testFood);
    when(foodRepository.findAll()).thenReturn(foods);

    // When
    List<FoodResponseDTO> result = foodService.getAllFood();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Cappuccino", result.get(0).getName());
    assertEquals(5.50, result.get(0).getPrice());
    verify(foodRepository, times(1)).findAll();
  }

  @Test
  void testCreateFood_Success() {
    // Given
    when(foodRepository.save(any(Food.class))).thenReturn(testFood);

    // When
    FoodResponseDTO result = foodService.createFood(foodRequestDTO);

    // Then
    assertNotNull(result);
    assertEquals("Cappuccino", result.getName());
    assertEquals(5.50, result.getPrice());
    assertEquals("DRINK", result.getFoodType());
    verify(foodRepository, times(1)).save(any(Food.class));
  }

  @Test
  void testUpdateFood_Success() {
    // Given
    FoodRequestDTO updateRequest = FoodRequestDTO.builder()
        .name("Latte")
        .price(6.00)
        .foodType("DRINK")
        .build();

    Food updatedFood = Food.builder()
        .id(1L)
        .name("Latte")
        .price(6.00)
        .foodType("DRINK")
        .build();

    when(foodRepository.findById(1L)).thenReturn(Optional.of(testFood));
    when(foodRepository.save(any(Food.class))).thenReturn(updatedFood);

    // When
    FoodResponseDTO result = foodService.updateFood(1L, updateRequest);

    // Then
    assertNotNull(result);
    assertEquals("Latte", result.getName());
    assertEquals(6.00, result.getPrice());
    verify(foodRepository, times(1)).findById(1L);
    verify(foodRepository, times(1)).save(any(Food.class));
  }

  @Test
  void testUpdateFood_NotFound() {
    // Given
    when(foodRepository.findById(1L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(RuntimeException.class, () -> foodService.updateFood(1L, foodRequestDTO));
    verify(foodRepository, times(1)).findById(1L);
    verify(foodRepository, never()).save(any(Food.class));
  }

  @Test
  void testDeleteFood_Success() {
    // Given
    doNothing().when(foodRepository).deleteById(1L);

    // When
    foodService.deleteFood(1L);

    // Then
    verify(foodRepository, times(1)).deleteById(1L);
  }

  @Test
  void testGetAllFood_EmptyList() {
    // Given
    when(foodRepository.findAll()).thenReturn(Arrays.asList());

    // When
    List<FoodResponseDTO> result = foodService.getAllFood();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(foodRepository, times(1)).findAll();
  }
}

