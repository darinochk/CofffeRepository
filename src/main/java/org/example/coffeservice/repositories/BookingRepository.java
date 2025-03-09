package org.example.coffeservice.repositories;

import org.example.coffeservice.models.coffee.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByDeskId(Long deskId);
    List<Booking> findByDeskIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long deskId, Date startDate, Date endDate);
}
