package org.kata.payment.infrastructure.adapter.in.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.usecase.CreatePayment;
import org.kata.payment.domain.usecase.GetAllPayments;
import org.kata.payment.domain.usecase.ModifyPayment;
import org.kata.payment.domain.usecase.ReadPayment;
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

    private final CreatePayment createPayment;
    private final ReadPayment readPayment;
    private final ModifyPayment modifyPayment;
    private final GetAllPayments getAllPayments;
    private final PaymentRestMapper mapper;

    public PaymentController(CreatePayment createPayment,
                             ReadPayment readPayment,
                             ModifyPayment modifyPayment,
                             GetAllPayments getAllPayments,
                             PaymentRestMapper mapper) {
        this.createPayment = createPayment;
        this.readPayment = readPayment;
        this.modifyPayment = modifyPayment;
        this.getAllPayments = getAllPayments;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        var payment = createPayment.execute(mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(payment));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payment by ID")
    public ResponseEntity<PaymentResponse> getPayment(
            @Parameter(description = "ID of the payment to be retrieved")
            @PathVariable String id) {
        var payment = readPayment.execute(new PaymentId(id));
        return ResponseEntity.ok(mapper.toResponse(payment));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing payment")
    public ResponseEntity<PaymentResponse> updatePayment(
            @Parameter(description = "ID of the payment to be updated")
            @PathVariable String id,
            @RequestBody PaymentRequest request) {
        var payment = modifyPayment.execute(mapper.toDomain(request, id));
        return ResponseEntity.ok(mapper.toResponse(payment));
    }

    @GetMapping
    @Operation(summary = "Get all payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        var responses = getAllPayments.execute().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
