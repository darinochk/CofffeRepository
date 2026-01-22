package org.example.coffeservice.dto.response.payment;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSessionResponseDTO {
  private Long id;
  private String fullName;
  private String email;
  private String description;
  private Date orderDate;
  private double amount;
  private Long transactionId;
  private String transactionStatus;
}
