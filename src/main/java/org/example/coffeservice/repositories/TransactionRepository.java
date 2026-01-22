package org.example.coffeservice.repositories;

import java.util.List;
import org.example.coffeservice.models.payment.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByStatus(String status);
}
