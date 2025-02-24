package org.example.coffeservice.services;

import org.example.coffeservice.models.Event;
import org.example.coffeservice.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        try {
            return eventRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving events", e);
        }
    }

    public Event createEvent(Event event) {
        try {
            return eventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error creating event", e);
        }
    }

    public Event updateEvent(Long id, Event event) {
        try {
            Event existingEvent = eventRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with id " + id));

            existingEvent.setName(event.getName());
            existingEvent.setDescription(event.getDescription());
            existingEvent.setStartDate(event.getStartDate());
            existingEvent.setEndDate(event.getEndDate());
            return eventRepository.save(existingEvent);
        } catch (Exception e) {
            throw new RuntimeException("Error updating event with id " + id, e);
        }
    }

    public void deleteEvent(Long id) {
        try {
            eventRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting event with id " + id, e);
        }
    }
}
