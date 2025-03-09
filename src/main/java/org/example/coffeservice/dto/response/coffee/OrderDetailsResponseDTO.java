package org.example.coffeservice.dto.response.coffee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsResponseDTO {
    private Long orderDetailsId;
    private double totalAmount;
    private List<OrderResponseDTO> orders;
}
