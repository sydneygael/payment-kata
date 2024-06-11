package org.kata.payment.application.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.application.ports.output.Payments;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.state.NewState;
import org.kata.payment.domain.valueobject.PaymentId;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentApplicationServiceTest {

    private PaymentApplicationService paymentService;
    private Payments payments;

    @BeforeEach
    void setUp() {
        payments = mock(Payments.class);
        paymentService = new PaymentApplicationService(payments);
    }

    @Test
    void testCreatePayment() {
        // GIVEN
        var payment = Payment.builder()
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .id(new PaymentId())
                .state(new NewState())
                .items(Collections.emptyList())
                .build();

        when(payments.save(payment)).thenReturn(payment);

        // WHEN
        var createdPayment = paymentService.createPayment(payment);

        // THEN
        verify(payments).save(payment);
        assertEquals(payment, createdPayment);
    }

    @Test
    void testReadingPayment() {
        //GIVEN
        var paymentId = new PaymentId();
        var payment = new Payment();
        when(payments.findPaymentById(paymentId.id())).thenReturn(Optional.of(payment));

        // WHEN
        var retrievedPayment = paymentService.readingPayment(paymentId);

        // THEN
        verify(payments).findPaymentById(paymentId.id());
        assertEquals(payment, retrievedPayment);
    }

    @Test
    void testModifyPayment() {

        Payment payment = new Payment();
        payment.setId(new PaymentId());
        when(payments.findPaymentById(any())).thenReturn(Optional.of(payment));
        when(payments.save(any())).thenReturn(payment);

        // WHEN
        Payment modifiedPayment = paymentService.modifyPayment(payment);

        // THEN
        verify(payments,times(1)).save(payment);
        assertEquals(payment, modifiedPayment);

    }
}
