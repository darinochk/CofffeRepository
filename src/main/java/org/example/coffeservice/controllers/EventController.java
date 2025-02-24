package org.example.coffeservice.controllers;

import org.example.coffeservice.models.Event;
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
    public List<Event> getAllEvents() {
        try {
            return eventService.getAllEvents();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving events");
        }
    }

    @PostMapping("/create")
    public Event createEvent(@RequestBody Event event) {
        try {
            return eventService.createEvent(event);
        } catch (Exception e) {
            throw new RuntimeException("Error creating event");
        }
    }

    @PutMapping("/update/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        try {
            return eventService.updateEvent(id, event);
        } catch (Exception e) {
            throw new RuntimeException("Error updating event");
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting event");
        }
    }
}
