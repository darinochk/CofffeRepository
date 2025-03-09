package org.example.coffeservice.dto.response.coffee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private String foodName;
    private int quantity;
    private double totalPrice;
    private Long orderDetailsId;
}
