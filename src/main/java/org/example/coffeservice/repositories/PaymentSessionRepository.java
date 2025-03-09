package org.example.coffeservice.repositories;

import org.example.coffeservice.models.payment.PaymentSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSessionRepository extends JpaRepository<PaymentSession, Long> {
    PaymentSession findByEmail(String email);
}
