package org.example.coffeservice.controllers;

import org.example.coffeservice.models.Desk;
import org.example.coffeservice.services.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/desks")
public class DeskController {

    @Autowired
    private DeskService deskService;

    @GetMapping("/")
    public List<Desk> getAllDesks() {
        try {
            return deskService.getAllDesks();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving desks");
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Desk createDesk(@RequestBody Desk desk) {
        try {
            return deskService.createDesk(desk);
        } catch (Exception e) {
            throw new RuntimeException("Error creating desk");
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Desk updateDesk(@PathVariable Long id, @RequestBody Desk desk) {
        try {
            return deskService.updateDesk(id, desk);
        } catch (Exception e) {
            throw new RuntimeException("Error updating desk");
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDesk(@PathVariable Long id) {
        try {
            deskService.deleteDesk(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting desk");
        }
    }
}
