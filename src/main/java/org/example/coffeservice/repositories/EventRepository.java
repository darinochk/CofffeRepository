package org.example.coffeservice.repositories;

import org.example.coffeservice.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByName(String name);
    List<Event> findByStartDateAfter(Date startDate);
}
