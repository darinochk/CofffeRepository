package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.coffee.PromotionRequestDTO;
import org.example.coffeservice.dto.response.coffee.PromotionResponseDTO;
import org.example.coffeservice.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

  @Autowired private PromotionService promotionService;

  @GetMapping("/")
  public List<PromotionResponseDTO> getAllPromotions() {
    try {
      return promotionService.getAllPromotions();
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения акций", exception);
    }
  }

  @PostMapping("/create")
  public PromotionResponseDTO createPromotion(@RequestBody PromotionRequestDTO promotionRequest) {
    try {
      return promotionService.createPromotion(promotionRequest);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка создания акции", exception);
    }
  }

  @PutMapping("/update/{id}")
  public PromotionResponseDTO updatePromotion(
      @PathVariable Long id, @RequestBody PromotionRequestDTO promotionRequest) {
    try {
      return promotionService.updatePromotion(id, promotionRequest);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка обновления акции с id " + id, exception);
    }
  }

  @DeleteMapping("/delete/{id}")
  public void deletePromotion(@PathVariable Long id) {
    try {
      promotionService.deletePromotion(id);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка удаления акции с id " + id, exception);
    }
  }
}
