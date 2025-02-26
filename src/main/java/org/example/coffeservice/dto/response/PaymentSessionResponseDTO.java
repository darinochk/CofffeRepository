package org.example.coffeservice.dto.response;

import java.util.Date;

public class PaymentSessionResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String description;
    private Date orderDate;
    private double amount;
    private Long transactionId;
    private String transactionStatus;

    public PaymentSessionResponseDTO() {}

    public PaymentSessionResponseDTO(Long id, String fullName, String email, String description, Date orderDate, double amount, Long transactionId, String transactionStatus) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.description = description;
        this.orderDate = orderDate;
        this.amount = amount;
        this.transactionId = transactionId;
        this.transactionStatus = transactionStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
