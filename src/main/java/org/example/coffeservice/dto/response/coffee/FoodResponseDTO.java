package org.example.coffeservice.dto.response.coffee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponseDTO {
    private Long id;
    private String name;
    private double price;
    private String foodType;
}
