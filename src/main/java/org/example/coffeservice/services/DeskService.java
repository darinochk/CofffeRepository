package org.example.coffeservice.services;

import org.example.coffeservice.models.Desk;
import org.example.coffeservice.repositories.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService {

    @Autowired
    private DeskRepository deskRepository;

    public List<Desk> getAllDesks() {
        try {
            return deskRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving desks", e);
        }
    }

    public Desk createDesk(Desk desk) {
        try {
            return deskRepository.save(desk);
        } catch (Exception e) {
            throw new RuntimeException("Error creating desk", e);
        }
    }

    public Desk updateDesk(Long id, Desk desk) {
        try {
            Desk existingDesk = deskRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Desk not found with id " + id));
            existingDesk.setDeskNumber(desk.getDeskNumber());
            existingDesk.setCapacity(desk.getCapacity());
            existingDesk.setLocation(desk.getLocation());
            return deskRepository.save(existingDesk);
        } catch (Exception e) {
            throw new RuntimeException("Error updating desk with id " + id, e);
        }
    }

    public void deleteDesk(Long id) {
        try {
            deskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting desk with id " + id, e);
        }
    }
}
