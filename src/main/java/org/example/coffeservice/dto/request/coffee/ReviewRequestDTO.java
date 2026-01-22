package org.example.coffeservice.dto.request.coffee;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coffeservice.utils.Constants;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
  @Min(value = Constants.MIN_RATING, message = "Рейтинг должен быть не меньше 1")
  @Max(value = Constants.MAX_RATING, message = "Рейтинг должен быть не больше 5")
  private int rating;

  @NotBlank(message = "Текст отзыва не может быть пустым")
  private String reviewText;

  @NotNull(message = "Дата отзыва не может быть null")
  private Date reviewDate;
}
