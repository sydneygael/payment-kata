package org.kata.payment.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.domain.exception.PaymentNotFoundException;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.port.out.PaymentRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReadPaymentTest {

    private ReadPayment readPayment;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        readPayment = new ReadPayment(paymentRepository);
    }

    @Test
    void execute_returnsPaymentWhenFound() {
        var paymentId = new PaymentId();
        var payment = Payment.builder()
                .id(paymentId)
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        var result = readPayment.execute(paymentId);

        verify(paymentRepository).findById(paymentId);
        assertEquals(payment, result);
    }

    @Test
    void execute_throwsWhenNotFound() {
        var paymentId = new PaymentId();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> readPayment.execute(paymentId));
    }
}
