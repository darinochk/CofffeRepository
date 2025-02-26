package org.example.coffeservice.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class BookingRequestDTO {

    @NotNull(message = "ID стола не может быть null")
    private Long deskId;

    @NotNull(message = "Дата начала не может быть null")
    private Date startDate;

    @NotNull(message = "Дата окончания не может быть null")
    private Date endDate;

    @NotBlank(message = "Статус не может быть пустым")
    private String status;

    public Long getDeskId() {
        return deskId;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
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
