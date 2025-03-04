package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.response.PaymentSessionResponseDTO;
import org.example.coffeservice.models.PaymentSession;
import org.example.coffeservice.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/init/{orderDetailsId}")
    public PaymentSession initPayment(@PathVariable Long orderDetailsId) {
        return paymentService.initPaymentSession(orderDetailsId);
    }

    @PostMapping("/pay/{paymentSessionId}")
    public PaymentSession payForOrder(@PathVariable Long paymentSessionId, @RequestParam String accountNumber) {
        return paymentService.processPayment(paymentSessionId, accountNumber);
    }

    @PostMapping("/pay/partial/{paymentSessionId}")
    public PaymentSession payPartial(@PathVariable Long paymentSessionId, @RequestParam String accountNumber, @RequestParam int totalInstallments) {
        return paymentService.processPartialPayment(paymentSessionId, accountNumber, totalInstallments);
    }
}