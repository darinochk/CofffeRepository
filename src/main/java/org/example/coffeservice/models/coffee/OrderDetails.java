package org.example.coffeservice.models.coffee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Booking booking;

    private double amount;

    @Column(nullable = false)
    private String status;


    @OneToMany(mappedBy = "orderDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
}
