package org.example.coffeservice.dto.response;

public class DeskResponseDTO {

    private Long id;
    private int deskNumber;
    private int capacity;
    private String location;

    public DeskResponseDTO(Long id, int deskNumber, int capacity, String location) {
        this.id = id;
        this.deskNumber = deskNumber;
        this.capacity = capacity;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
