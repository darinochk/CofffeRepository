package org.example.coffeservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class DeskRequestDTO {

    @Min(value = 1, message = "Номер стола должен быть не меньше 1")
    private int deskNumber;

    @Min(value = 1, message = "Вместимость должна быть не меньше 1")
    private int capacity;

    @NotBlank(message = "Местоположение не может быть пустым")
    private String location;

    public int getDeskNumber() {
        return deskNumber;
    }

    public void setDeskNumber(int deskNumber) {
        this.deskNumber = deskNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
