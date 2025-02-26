package org.example.coffeservice.dto.response;

import java.util.Date;

public class BookingResponseDTO {

    private Long id;
    private String userName;
    private String deskLocation;
    private Date startDate;
    private Date endDate;
    private String status;

    public BookingResponseDTO(Long id, String userName, String deskLocation, Date startDate, Date endDate, String status) {
        this.id = id;
        this.userName = userName;
        this.deskLocation = deskLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeskLocation() {
        return deskLocation;
    }

    public void setDeskLocation(String deskLocation) {
        this.deskLocation = deskLocation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
