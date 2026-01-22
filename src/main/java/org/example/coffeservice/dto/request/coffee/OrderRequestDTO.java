package org.example.coffeservice.dto.request.coffee;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coffeservice.utils.Constants;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
  @NotNull(message = "ID блюда не может быть null")
  private Long foodId;

  @NotNull(message = "ID деталей заказа не может быть null")
  private Long orderDetailsId;

  @Min(value = Constants.MIN_QUANTITY, message = "Количество должно быть не меньше 1")
  private int quantity;
}
