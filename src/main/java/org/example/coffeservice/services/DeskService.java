package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.DeskRequestDTO;
import org.example.coffeservice.dto.response.DeskResponseDTO;
import org.example.coffeservice.models.Desk;
import org.example.coffeservice.repositories.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeskService {

    @Autowired
    private DeskRepository deskRepository;

    public List<DeskResponseDTO> getAllDesks() {
        try {
            List<Desk> desks = deskRepository.findAll();
            return desks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения столов", e);
        }
    }

    public DeskResponseDTO createDesk(DeskRequestDTO deskRequest) {
        try {
            Desk desk = new Desk();
            desk.setDeskNumber(deskRequest.getDeskNumber());
            desk.setCapacity(deskRequest.getCapacity());
            desk.setLocation(deskRequest.getLocation());
            Desk savedDesk = deskRepository.save(desk);
            return convertToDTO(savedDesk);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания стола", e);
        }
    }

    public DeskResponseDTO updateDesk(Long id, DeskRequestDTO deskRequest) {
        try {
            Desk existingDesk = deskRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Стол не найден с id " + id));
            existingDesk.setDeskNumber(deskRequest.getDeskNumber());
            existingDesk.setCapacity(deskRequest.getCapacity());
            existingDesk.setLocation(deskRequest.getLocation());
            Desk updatedDesk = deskRepository.save(existingDesk);
            return convertToDTO(updatedDesk);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления стола с id " + id, e);
        }
    }

    public void deleteDesk(Long id) {
        try {
            deskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления стола с id " + id, e);
        }
    }

    public DeskResponseDTO convertToDTO(Desk desk) {
        return new DeskResponseDTO(desk.getId(), desk.getDeskNumber(), desk.getCapacity(), desk.getLocation());
    }
}
