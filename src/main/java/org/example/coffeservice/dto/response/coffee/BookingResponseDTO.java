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
public class BookingResponseDTO {
    private Long id;
    private String userName;
    private String deskLocation;
    private Date startDate;
    private Date endDate;
    private String status;
}
