package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.request.coffee.EventRequestDTO;
import org.example.coffeservice.dto.response.coffee.EventResponseDTO;
import org.example.coffeservice.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public List<EventResponseDTO> getAllEvents() {
        try {
            return eventService.getAllEvents();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения событий", e);
        }
    }

    @PostMapping("/create")
    public EventResponseDTO createEvent(@RequestBody EventRequestDTO eventRequest) {
        try {
            return eventService.createEvent(eventRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания события", e);
        }
    }

    @PutMapping("/update/{id}")
    public EventResponseDTO updateEvent(@PathVariable Long id, @RequestBody EventRequestDTO eventRequest) {
        try {
            return eventService.updateEvent(id, eventRequest);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления события с id " + id, e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления события с id " + id, e);
        }
    }
}
