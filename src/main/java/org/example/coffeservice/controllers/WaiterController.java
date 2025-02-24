package org.example.coffeservice.controllers;

import org.example.coffeservice.models.Booking;
import org.example.coffeservice.services.WaiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/waiter")
public class WaiterController {

    @Autowired
    private WaiterService waiterService;

    @PutMapping("/confirm/{id}")
    @PreAuthorize("hasRole('WAITER')")
    public Booking confirmBooking(@PathVariable Long id) {
        return waiterService.confirmBooking(id);
    }
}
