package org.kata.payment.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.application.port.out.PaymentRepository;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.state.NewState;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentApplicationServiceTest {

    private PaymentApplicationService paymentService;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentApplicationService(paymentRepository);
    }

    @Test
    void testCreatePayment() {
        var payment = Payment.builder()
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .id(new PaymentId("1"))
                .state(new NewState())
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
