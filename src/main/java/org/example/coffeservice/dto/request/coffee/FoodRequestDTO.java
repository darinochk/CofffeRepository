package org.example.coffeservice.dto.request.coffee;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequestDTO {
    @NotBlank(message = "Название блюда не может быть пустым")
    private String name;

    @Min(value = 0, message = "Цена должна быть неотрицательной")
    private double price;

    @NotBlank(message = "Тип блюда не может быть пустым")
    private String foodType;
}
