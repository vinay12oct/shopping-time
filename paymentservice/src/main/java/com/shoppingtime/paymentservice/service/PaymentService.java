package com.shoppingtime.paymentservice.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.shoppingtime.paymentservice.dto.PaymentDTO;
import com.shoppingtime.paymentservice.entity.Payment;

public interface PaymentService {
	ResponseEntity<Payment> createPayment(PaymentDTO paymentDTO);
    Payment getPaymentById(Long transactionId);
    List<Payment> getPaymentsByOrderId(Long orderId);
    
}

