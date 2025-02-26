package org.example.coffeservice.controllers;

import org.example.coffeservice.dto.response.PaymentSessionResponseDTO;
import org.example.coffeservice.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/{orderDetailsId}")
    public PaymentSessionResponseDTO payForOrder(@PathVariable Long orderDetailsId,
                                                 @RequestParam String accountNumber) {
        return paymentService.processPayment(orderDetailsId, accountNumber);
    }
}
