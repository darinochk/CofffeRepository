package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.coffee.EventRequestDTO;
import org.example.coffeservice.dto.response.coffee.EventResponseDTO;
import org.example.coffeservice.models.coffee.Event;
import org.example.coffeservice.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<EventResponseDTO> getAllEvents() {
        try {
            List<Event> events = eventRepository.findAll();
            return events.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения событий", e);
        }
    }

    public EventResponseDTO createEvent(EventRequestDTO eventRequest) {
        try {
            Event event = new Event();
            event.setName(eventRequest.getName());
            event.setDescription(eventRequest.getDescription());
            event.setStartDate(eventRequest.getStartDate());
            event.setEndDate(eventRequest.getEndDate());
            Event savedEvent = eventRepository.save(event);
            return convertToDTO(savedEvent);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания события", e);
        }
    }

    public EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequest) {
        try {
            Event existingEvent = eventRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Событие не найдено с id " + id));
            existingEvent.setName(eventRequest.getName());
            existingEvent.setDescription(eventRequest.getDescription());
            existingEvent.setStartDate(eventRequest.getStartDate());
            existingEvent.setEndDate(eventRequest.getEndDate());
            Event updatedEvent = eventRepository.save(existingEvent);
            return convertToDTO(updatedEvent);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления события с id " + id, e);
        }
    }

    public void deleteEvent(Long id) {
        try {
            eventRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления события с id " + id, e);
        }
    }

    public EventResponseDTO convertToDTO(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate()
        );
    }
}
