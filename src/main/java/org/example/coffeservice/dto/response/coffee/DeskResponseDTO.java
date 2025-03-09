package org.example.coffeservice.dto.response.coffee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeskResponseDTO {
    private Long id;
    private int deskNumber;
    private int capacity;
    private String location;
}
