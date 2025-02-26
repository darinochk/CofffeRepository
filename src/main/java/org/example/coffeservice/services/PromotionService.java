package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.PromotionRequestDTO;
import org.example.coffeservice.dto.response.PromotionResponseDTO;
import org.example.coffeservice.models.Promotion;
import org.example.coffeservice.repositories.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public List<PromotionResponseDTO> getAllPromotions() {
        try {
            List<Promotion> promotions = promotionRepository.findAll();
            return promotions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения списка акций", e);
        }
    }

    public PromotionResponseDTO createPromotion(PromotionRequestDTO request) {
        try {
            Promotion promotion = new Promotion();
            promotion.setName(request.getName());
            promotion.setDescription(request.getDescription());
            promotion.setStartDate(request.getStartDate());
            promotion.setEndDate(request.getEndDate());
            Promotion savedPromotion = promotionRepository.save(promotion);
            return convertToDTO(savedPromotion);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания акции", e);
        }
    }

    public PromotionResponseDTO updatePromotion(Long id, PromotionRequestDTO request) {
        try {
            Promotion existingPromotion = promotionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Акция не найдена с id " + id));
            existingPromotion.setName(request.getName());
            existingPromotion.setDescription(request.getDescription());
            existingPromotion.setStartDate(request.getStartDate());
            existingPromotion.setEndDate(request.getEndDate());
            Promotion updatedPromotion = promotionRepository.save(existingPromotion);
            return convertToDTO(updatedPromotion);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления акции с id " + id, e);
        }
    }

    public void deletePromotion(Long id) {
        try {
            promotionRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления акции с id " + id, e);
        }
    }

    private PromotionResponseDTO convertToDTO(Promotion promotion) {
        return new PromotionResponseDTO(
                promotion.getId(),
                promotion.getName(),
                promotion.getDescription(),
                promotion.getStartDate(),
                promotion.getEndDate()
        );
    }
}
