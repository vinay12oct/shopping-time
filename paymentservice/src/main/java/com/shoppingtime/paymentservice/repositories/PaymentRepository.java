package com.shoppingtime.paymentservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingtime.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
	List<Payment> findByOrderId(Long orderId);
	boolean existsByTransactionId(Long transactionId);

}
