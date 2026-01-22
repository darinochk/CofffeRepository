package org.example.coffeservice.services;

import java.util.List;
import java.util.stream.Collectors;
import org.example.coffeservice.dto.request.coffee.PromotionRequestDTO;
import org.example.coffeservice.dto.response.coffee.PromotionResponseDTO;
import org.example.coffeservice.models.coffee.Promotion;
import org.example.coffeservice.repositories.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {

  @Autowired private PromotionRepository promotionRepository;

  public List<PromotionResponseDTO> getAllPromotions() {
    try {
      List<Promotion> promotions = promotionRepository.findAll();
      return promotions.stream().map(this::convertToDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения списка акций", exception);
    }
  }

  public PromotionResponseDTO createPromotion(PromotionRequestDTO promotionRequest) {
    try {
      Promotion promotion = new Promotion();
      promotion.setName(promotionRequest.getName());
      promotion.setDescription(promotionRequest.getDescription());
      promotion.setStartDate(promotionRequest.getStartDate());
      promotion.setEndDate(promotionRequest.getEndDate());
      Promotion savedPromotion = promotionRepository.save(promotion);
      return convertToDTO(savedPromotion);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка создания акции", exception);
    }
  }

  public PromotionResponseDTO updatePromotion(Long id, PromotionRequestDTO promotionRequest) {
    try {
      Promotion existingPromotion =
          promotionRepository
              .findById(id)
              .orElseThrow(() -> new IllegalArgumentException("Акция не найдена с id " + id));
      existingPromotion.setName(promotionRequest.getName());
      existingPromotion.setDescription(promotionRequest.getDescription());
      existingPromotion.setStartDate(promotionRequest.getStartDate());
      existingPromotion.setEndDate(promotionRequest.getEndDate());
      Promotion updatedPromotion = promotionRepository.save(existingPromotion);
      return convertToDTO(updatedPromotion);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка обновления акции с id " + id, exception);
    }
  }

  public void deletePromotion(Long id) {
    try {
      promotionRepository.deleteById(id);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка удаления акции с id " + id, exception);
    }
  }

  private PromotionResponseDTO convertToDTO(Promotion promotion) {
    return new PromotionResponseDTO(
        promotion.getId(),
        promotion.getName(),
        promotion.getDescription(),
        promotion.getStartDate(),
        promotion.getEndDate());
  }
}
