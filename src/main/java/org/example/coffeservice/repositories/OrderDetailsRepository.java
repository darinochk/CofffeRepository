package org.example.coffeservice.repositories;

import org.example.coffeservice.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    // Поиск всех позиций для конкретного заказа
//    List<OrderDetails> findByOrderId(Long orderId);  // Поиск по order_id
}
