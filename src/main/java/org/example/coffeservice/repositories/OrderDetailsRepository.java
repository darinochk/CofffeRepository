package org.example.coffeservice.repositories;

import java.util.List;
import org.example.coffeservice.models.coffee.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
  List<OrderDetails> findByBookingId(Long bookingId);
}
