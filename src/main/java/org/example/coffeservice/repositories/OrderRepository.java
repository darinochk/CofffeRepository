package org.example.coffeservice.repositories;

import java.util.List;
import org.example.coffeservice.models.coffee.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByOrderDetailsId(Long orderDetailsId);

  List<Order> findByFoodId(Long foodId);
}
