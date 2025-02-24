package org.example.coffeservice.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment_session")
public class PaymentSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String description;
    private Date orderDate;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public PaymentSession() {}

    public PaymentSession(String fullName, String email, String description, Date orderDate, double amount, Transaction transaction) {
        this.fullName = fullName;
        this.email = email;
        this.description = description;
        this.orderDate = orderDate;
        this.amount = amount;
        this.transaction = transaction;
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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
