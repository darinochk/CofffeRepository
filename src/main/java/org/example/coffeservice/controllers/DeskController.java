package org.example.coffeservice.controllers;

import java.util.List;
import org.example.coffeservice.dto.request.coffee.DeskRequestDTO;
import org.example.coffeservice.dto.response.coffee.DeskResponseDTO;
import org.example.coffeservice.services.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/desks")
public class DeskController {

  @Autowired private DeskService deskService;

  @GetMapping("/")
  public List<DeskResponseDTO> getAllDesks() {
    try {
      return deskService.getAllDesks();
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения столов", exception);
    }
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  public DeskResponseDTO createDesk(@RequestBody DeskRequestDTO deskRequest) {
    try {
      return deskService.createDesk(deskRequest);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка создания стола", exception);
    }
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public DeskResponseDTO updateDesk(
      @PathVariable Long id, @RequestBody DeskRequestDTO deskRequest) {
    try {
      return deskService.updateDesk(id, deskRequest);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка обновления стола", exception);
    }
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteDesk(@PathVariable Long id) {
    try {
      deskService.deleteDesk(id);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка удаления стола", exception);
    }
  }
}
