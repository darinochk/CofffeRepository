package org.example.coffeservice.repositories;

import org.example.coffeservice.models.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<Desk, Long> {
    Desk findByDeskNumber(int deskNumber);
}
