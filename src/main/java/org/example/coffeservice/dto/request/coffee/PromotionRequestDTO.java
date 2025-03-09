package org.example.coffeservice.dto.request.coffee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRequestDTO {
    @NotBlank(message = "Название акции не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Дата начала не может быть null")
    private Date startDate;

    @NotNull(message = "Дата окончания не может быть null")
    private Date endDate;
}
