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
public class ReviewResponseDTO {
  private Long id;
  private String userName;
  private int rating;
  private String reviewText;
  private Date reviewDate;
}
