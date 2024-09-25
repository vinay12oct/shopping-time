package com.shoppingtime.paymentservice.controller;
import java.util.List;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingtime.paymentservice.dto.PaymentDTO;
import com.shoppingtime.paymentservice.entity.Payment;
import com.shoppingtime.paymentservice.exceptions.PaymentNotFoundException;
import com.shoppingtime.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Create a new payment with validation and return ResponseEntity
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDTO paymentDTO) {
         ResponseEntity<Payment> createPayment = paymentService.createPayment(paymentDTO);
         Payment payment = createPayment.getBody();
        return new ResponseEntity<>(payment, HttpStatus.CREATED);  // Return Payment with 201 status
    }

    // Get a payment by ID and handle if the payment is not found
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return new ResponseEntity<>(payment, HttpStatus.OK);  // Return Payment with 200 status
    }

    // Get payments by orderId and handle if no payments are found
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsByOrderId(@PathVariable Long orderId) {
        List<Payment> payments = paymentService.getPaymentsByOrderId(orderId);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException("No payments found for order ID: " + orderId);
        }
        return new ResponseEntity<>(payments, HttpStatus.OK);  // Return list of payments with 200 status
    }
}
