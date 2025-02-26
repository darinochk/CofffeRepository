package org.example.coffeservice.services;

import org.example.coffeservice.models.*;
import org.example.coffeservice.dto.response.PaymentSessionResponseDTO;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.example.coffeservice.repositories.BookingRepository;
import org.example.coffeservice.repositories.PaymentSessionRepository;
import org.example.coffeservice.repositories.TransactionRepository;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentSessionRepository paymentSessionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public PaymentSessionResponseDTO processPayment(Long orderDetailsId, String accountNumber) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
                .orElseThrow(() -> new IllegalArgumentException("Order details not found with id " + orderDetailsId));

        Booking booking = bookingRepository.findById(orderDetails.getBooking().getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for order details id " + orderDetailsId));

        double orderAmount = orderDetails.getAmount();
        double accountBalance = getMockAccountBalance(accountNumber);

        if (accountBalance < orderAmount) {
            throw new IllegalArgumentException("Insufficient funds. Account balance is " + accountBalance);
        }

        Transaction transaction = new Transaction("IN_PROCESS");
        Transaction savedTransaction = transactionRepository.save(transaction);

        PaymentSession paymentSession = new PaymentSession();
        User user = booking.getUser();
        paymentSession.setFullName(user.getFirstName() + " " + user.getLastName());
        paymentSession.setEmail(user.getEmail());
        paymentSession.setDescription("Payment for order details id " + orderDetailsId);
        paymentSession.setOrderDate(booking.getStartDate());
        paymentSession.setAmount(orderAmount);
        paymentSession.setTransaction(savedTransaction);

        paymentSessionRepository.save(paymentSession);

        completeTransaction(savedTransaction);

        return convertToDTO(paymentSession);
    }

    private void completeTransaction(Transaction transaction) {
        transaction.setStatus("COMPLETED");
        transactionRepository.save(transaction);
    }

    private double getMockAccountBalance(String accountNumber) {
        return 1000.0;
    }

    private PaymentSessionResponseDTO convertToDTO(PaymentSession session) {
        Transaction transaction = session.getTransaction();
        return new PaymentSessionResponseDTO(
                session.getId(),
                session.getFullName(),
                session.getEmail(),
                session.getDescription(),
                session.getOrderDate(),
                session.getAmount(),
                (transaction != null ? transaction.getId() : null),
                (transaction != null ? transaction.getStatus() : null)
        );
    }
}
