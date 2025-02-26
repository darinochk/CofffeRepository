package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.PromotionRequestDTO;
import org.example.coffeservice.dto.response.PromotionResponseDTO;
import org.example.coffeservice.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/")
    public List<PromotionResponseDTO> getAllPromotions() {
        try {
            return promotionService.getAllPromotions();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения акций", e);
        }
    }

    @PostMapping("/create")
    public PromotionResponseDTO createPromotion(@RequestBody PromotionRequestDTO promotionRequest) {
        try {
            return promotionService.createPromotion(promotionRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания акции", e);
        }
    }

    @PutMapping("/update/{id}")
    public PromotionResponseDTO updatePromotion(@PathVariable Long id, @RequestBody PromotionRequestDTO promotionRequest) {
        try {
            return promotionService.updatePromotion(id, promotionRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления акции с id " + id, e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deletePromotion(@PathVariable Long id) {
        try {
            promotionService.deletePromotion(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления акции с id " + id, e);
        }
    }
}
