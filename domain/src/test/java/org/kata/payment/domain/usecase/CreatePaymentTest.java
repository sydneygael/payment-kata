package org.kata.payment.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.port.out.PaymentRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreatePaymentTest {

    private CreatePayment createPayment;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        createPayment = new CreatePayment(paymentRepository);
    }

    @Test
    void execute_savesAndReturnsPayment() {
        var payment = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .build();

        when(paymentRepository.save(payment)).thenReturn(payment);

        var result = createPayment.execute(payment);

        verify(paymentRepository).save(payment);
        assertEquals(payment, result);
    }
}
