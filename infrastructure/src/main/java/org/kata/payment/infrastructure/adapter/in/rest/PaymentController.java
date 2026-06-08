package org.kata.payment.infrastructure.adapter.in.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kata.payment.application.port.in.ManagePayment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.infrastructure.adapter.in.rest.dto.PaymentRequest;
import org.kata.payment.infrastructure.adapter.in.rest.dto.PaymentResponse;
import org.kata.payment.infrastructure.adapter.in.rest.mapper.PaymentRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payments", description = "API for managing payments")
public class PaymentController {

    private final ManagePayment managePayment;
    private final PaymentRestMapper mapper;

    public PaymentController(ManagePayment managePayment, PaymentRestMapper mapper) {
        this.managePayment = managePayment;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        var payment = managePayment.createPayment(mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(payment));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payment by ID")
    public ResponseEntity<PaymentResponse> getPayment(
            @Parameter(description = "ID of the payment to be retrieved")
            @PathVariable String id) {
        var payment = managePayment.readingPayment(new PaymentId(id));
        return ResponseEntity.ok(mapper.toResponse(payment));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing payment")
    public ResponseEntity<PaymentResponse> updatePayment(
            @Parameter(description = "ID of the payment to be updated")
            @PathVariable String id,
            @RequestBody PaymentRequest request) {
        var payment = managePayment.modifyPayment(mapper.toDomain(request, id));
        return ResponseEntity.ok(mapper.toResponse(payment));
    }

    @GetMapping
    @Operation(summary = "Get all payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        var responses = managePayment.getAllPayments().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
