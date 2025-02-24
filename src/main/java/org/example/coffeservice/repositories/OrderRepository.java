package org.example.coffeservice.repositories;

import org.example.coffeservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDetailsId(Long orderDetailsId);
    List<Order> findByFoodId(Long foodId);
}
