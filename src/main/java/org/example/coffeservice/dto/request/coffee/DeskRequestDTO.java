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
public class DeskRequestDTO {
    @Min(value = 1, message = "Номер стола должен быть не меньше 1")
    private int deskNumber;

    @Min(value = 1, message = "Вместимость должна быть не меньше 1")
    private int capacity;

    @NotBlank(message = "Местоположение не может быть пустым")
    private String location;
}
