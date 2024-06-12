package org.kata.payment.infrastructure.adapters.inbound.rest;

import org.kata.payment.application.ports.input.ManagePayment;
import org.kata.payment.application.services.exception.PaymentNotFoundException;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.PaymentId;
import org.kata.payment.infrastructure.adapters.inbound.rest.dto.PaymentRequest;
import org.kata.payment.infrastructure.adapters.inbound.rest.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final ManagePayment managePayment;

    public PaymentController(ManagePayment managePayment) {
        this.managePayment = managePayment;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        var createdPayment = managePayment.createPayment(paymentRequest.toDomain());
        return ResponseEntity.ok(PaymentResponse.fromDomain(createdPayment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String id) throws PaymentNotFoundException {
        var payment = managePayment.readingPayment(new PaymentId(id));
        return ResponseEntity.ok(PaymentResponse.fromDomain(payment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable("id") String id,
                                                         @RequestBody PaymentRequest paymentRequest) throws PaymentNotFoundException {
        var payment = paymentRequest.toDomain();
        payment.setId(new PaymentId(id));
        var updatedPayment = managePayment.modifyPayment(payment);
        return ResponseEntity.ok(PaymentResponse.fromDomain(updatedPayment));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<Payment> payments = managePayment.getAllPayments();
        List<PaymentResponse> paymentResponses = payments.stream()
                .map(PaymentResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(paymentResponses);
    }
}
