package org.example.coffeservice.dto.request.coffee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsRequestDTO {
    @NotNull(message = "ID бронирования не может быть null")
    private Long bookingId;
}
