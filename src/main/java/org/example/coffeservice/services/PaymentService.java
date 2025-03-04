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

    public PaymentSession initPaymentSession(Long orderDetailsId) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
                .orElseThrow(() -> new IllegalArgumentException("Order details not found with id " + orderDetailsId));

        Booking booking = bookingRepository.findById(orderDetails.getBooking().getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for order details id " + orderDetailsId));

        Transaction transaction = new Transaction("NEW");
        Transaction savedTransaction = transactionRepository.save(transaction);

        PaymentSession paymentSession = new PaymentSession();
        User user = booking.getUser();
        paymentSession.setFullName(user.getFirstName() + " " + user.getLastName());
        paymentSession.setEmail(user.getEmail());
        paymentSession.setDescription("Payment for order details id " + orderDetailsId);
        paymentSession.setOrderDate(booking.getStartDate());
        paymentSession.setAmount(orderDetails.getAmount());
        paymentSession.setTransaction(savedTransaction);

        return paymentSessionRepository.save(paymentSession);
    }

    public PaymentSession processPayment(Long paymentSessionId, String accountNumber) {
        PaymentSession paymentSession = paymentSessionRepository.findById(paymentSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Payment session not found with id " + paymentSessionId));

        Transaction transaction = paymentSession.getTransaction();
        double orderAmount = paymentSession.getAmount();
        double accountBalance = getMockAccountBalance(accountNumber);

        if (accountBalance >= orderAmount) {
            transaction.setStatus("SUCCESS");
        } else {
            transaction.setStatus("FAIL");
        }
        transactionRepository.save(transaction);

        return paymentSession;
    }

    public PaymentSession processPartialPayment(Long paymentSessionId, String accountNumber, int totalInstallments) {
        PaymentSession paymentSession = paymentSessionRepository.findById(paymentSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Payment session not found with id " + paymentSessionId));

        Transaction transaction = paymentSession.getTransaction();
        if (paymentSession.getInstallments() == 0) {
            paymentSession.setInstallments(totalInstallments);
            paymentSession.setRemainingInstallments(totalInstallments);
        }

        if (paymentSession.getRemainingInstallments() <= 0) {
            throw new IllegalStateException("All installments have already been paid.");
        }

        double installmentAmount = paymentSession.getAmount() / paymentSession.getInstallments();
        double accountBalance = getMockAccountBalance(accountNumber);

        if (accountBalance >= installmentAmount) {
            paymentSession.setRemainingInstallments(paymentSession.getRemainingInstallments() - 1);
            if (paymentSession.getRemainingInstallments() == 0) {
                transaction.setStatus("SUCCESS");
            } else {
                transaction.setStatus("IN_PROGRESS");
            }
        } else {
            transaction.setStatus("FAIL");
        }

        transactionRepository.save(transaction);
        paymentSessionRepository.save(paymentSession);

        return paymentSession;
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
