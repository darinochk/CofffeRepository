package org.example.coffeservice.controllers;

import org.example.coffeservice.models.PaymentSession;
import org.example.coffeservice.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/{orderDetailsId}")
    public PaymentSession payForOrder(@PathVariable Long orderDetailsId, @RequestParam String accountNumber) {
        return paymentService.processPayment(orderDetailsId, accountNumber);
    }
}
