package org.example.coffeservice.dto.response.coffee;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
