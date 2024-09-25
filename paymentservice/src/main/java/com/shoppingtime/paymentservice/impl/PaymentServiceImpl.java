package com.shoppingtime.paymentservice.impl;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shoppingtime.paymentservice.dto.PaymentDTO;
import com.shoppingtime.paymentservice.entity.Payment;
import com.shoppingtime.paymentservice.exceptions.PaymentNotFoundException;
import com.shoppingtime.paymentservice.repositories.PaymentRepository;
import com.shoppingtime.paymentservice.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ResponseEntity<Payment> createPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setOrderId(paymentDTO.getOrderId());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());

        // Asynchronous task for generating unique random transaction ID with a delay
     
            try {
                // Simulate delay of 5 seconds
                Thread.sleep(5000);

                // Generate a unique transaction ID
                

            } catch (InterruptedException e) {
                // Handle the exception if the thread is interrupted
                Thread.currentThread().interrupt();
            }
        
        Long transactionId = generateUniqueTransactionId();

        // Set the transaction ID and update the payment status
        payment.setTransactionId(transactionId);
        payment.setStatus("COMPLETED"); // Mark the status as completed
        paymentRepository.save(payment);

        return new ResponseEntity<>(payment, HttpStatus.ACCEPTED);
    }

    // Method to generate a unique random transaction ID as Long
    private Long generateUniqueTransactionId() {
        Long transactionId;

        // Loop to ensure the generated transaction ID is unique
        do {
            transactionId = (long) (Math.random() * Long.MAX_VALUE); // Generate random Long
        } while (paymentRepository.existsByTransactionId(transactionId)); // Check if the ID already exists in the database

        return transactionId;
    }
    @Override
    public Payment getPaymentById(Long transactionId) {
        // Validate that the transactionId is not null or invalid
        if (ObjectUtils.isEmpty(transactionId)) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty.");
        }

        // Retrieve the payment or throw an exception if not found
        return paymentRepository.findById(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for transaction ID: " + transactionId));
    }

    @Override
    public List<Payment> getPaymentsByOrderId(Long orderId) {
        // Validate that the orderId is not null or invalid
        if (ObjectUtils.isEmpty(orderId)) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }

        // Retrieve payments or throw an exception if none are found
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException("No payments found for order ID: " + orderId);
        }

        return payments;
    }
}