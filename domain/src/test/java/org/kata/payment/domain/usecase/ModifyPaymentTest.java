package org.kata.payment.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.port.out.PaymentRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ModifyPaymentTest {

    private ModifyPayment modifyPayment;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        modifyPayment = new ModifyPayment(paymentRepository);
    }

    @Test
    void execute_noStatusChange_updatesDetailsAndSaves() {
        var paymentId = new PaymentId();
        var existing = Payment.builder()
                .id(paymentId)
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.NEW)
                .items(Collections.emptyList())
                .build();

        var incoming = Payment.builder()
                .id(paymentId)
                .paymentType(Payment.PaymentType.GIFT_CARD)
                .status(Payment.PaymentStatus.NEW)
                .items(Collections.emptyList())
                .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(existing)).thenReturn(existing);

        var result = modifyPayment.execute(incoming);

        verify(paymentRepository, times(1)).save(existing);
        assertEquals(existing, result);
    }

    @Test
    void execute_authorizesPayment_whenStatusChangesToAuthorized() {
        var paymentId = new PaymentId();
        var existing = Payment.builder()
                .id(paymentId)
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.NEW)
                .items(Collections.emptyList())
                .build();

        var incoming = Payment.builder()
                .id(paymentId)
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.AUTHORIZED)
                .items(Collections.emptyList())
                .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(existing)).thenReturn(existing);

        modifyPayment.execute(incoming);

        assertEquals(Payment.PaymentStatus.AUTHORIZED, existing.getStatus());
        verify(paymentRepository).save(existing);
    }
}
