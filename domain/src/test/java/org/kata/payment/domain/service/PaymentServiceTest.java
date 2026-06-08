package org.kata.payment.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.port.out.PaymentRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentService paymentService;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentService(paymentRepository);
    }

    @Test
    void testCreatePayment() {
        var payment = Payment.builder()
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .id(new PaymentId("1"))
                .items(Collections.emptyList())
                .build();

        when(paymentRepository.save(payment)).thenReturn(payment);

        var created = paymentService.createPayment(payment);

        verify(paymentRepository).save(payment);
        assertEquals(payment, created);
    }

    @Test
    void testReadingPayment() {
        var paymentId = new PaymentId();
        var payment = Payment.builder().id(paymentId).paymentType(Payment.PaymentType.CREDIT_CARD).items(Collections.emptyList()).build();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        var retrieved = paymentService.readingPayment(paymentId);

        verify(paymentRepository).findById(paymentId);
        assertEquals(payment, retrieved);
    }

    @Test
    void testModifyPayment() {
        var paymentId = new PaymentId();
        var payment = Payment.builder()
                .id(paymentId)
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .build();

        when(paymentRepository.findById(any())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any())).thenReturn(payment);

        var modified = paymentService.modifyPayment(payment);

        verify(paymentRepository, times(1)).save(payment);
        assertEquals(payment, modified);
    }
}
