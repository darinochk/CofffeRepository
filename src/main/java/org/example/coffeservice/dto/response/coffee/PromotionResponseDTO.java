package org.example.coffeservice.dto.response.coffee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
}
