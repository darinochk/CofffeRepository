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
public class BookingResponseDTO {
  private Long id;
  private String userName;
  private String deskLocation;
  private Date startDate;
  private Date endDate;
  private String status;
}
