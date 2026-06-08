package org.kata.payment.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.port.out.PaymentRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetAllPaymentsTest {

    private GetAllPayments getAllPayments;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        getAllPayments = new GetAllPayments(paymentRepository);
    }

    @Test
    void execute_returnsAllPayments() {
        var payment = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .build();

        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        var result = getAllPayments.execute();

        verify(paymentRepository).findAll();
        assertEquals(List.of(payment), result);
    }
}
