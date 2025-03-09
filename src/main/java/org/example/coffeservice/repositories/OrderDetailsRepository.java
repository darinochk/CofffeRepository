package org.example.coffeservice.repositories;

import org.example.coffeservice.models.coffee.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findByBookingId(Long bookingId);
}
