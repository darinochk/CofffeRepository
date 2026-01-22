package org.example.coffeservice.repositories;

import java.util.Date;
import java.util.List;
import org.example.coffeservice.models.coffee.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findByName(String name);

  List<Event> findByStartDateAfter(Date startDate);
}
