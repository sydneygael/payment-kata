package org.kata.payment.domain;

import org.junit.jupiter.api.Test;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.state.AuthorizedState;
import org.kata.payment.domain.state.CanceledState;
import org.kata.payment.domain.state.CapturedState;
import org.kata.payment.domain.state.NewState;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.Money;
import org.kata.payment.domain.valueobject.PaymentId;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTest {

    @Test
    void testNewTransactionStatus() {
        var payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .build();
        assertEquals(Payment.PaymentStatus.NEW, payment.getPaymentStatus());
    }

    @Test
    void testChangeToCapturedWhenAuthorized() {
        var payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new AuthorizedState())
                .build();
        payment.capture();
        assertEquals(Payment.PaymentStatus.CAPTURED, payment.getPaymentStatus());
    }

    @Test
    void testChangeToCapturedWhenNotAuthorized() {
        Payment payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new NewState())
                .build();
        assertThrows(IllegalStateException.class, payment::capture);
    }

    @Test
    void testChangeToCanceledWhenNotCaptured() {
        Payment payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new NewState())
                .build();
        payment.cancel();
        assertEquals(Payment.PaymentStatus.CANCELED, payment.getPaymentStatus());
    }

    @Test
    void testChangeToCanceledWhenCaptured() {
        Payment payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new CapturedState())
                .build();
        assertThrows(IllegalStateException.class, payment::cancel);
    }

    @Test
    void testChangeToCanceledWhenCanceled() {
        Payment payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new CanceledState())
                .build();
        assertThrows(IllegalStateException.class, payment::cancel);
    }

    @Test
    void testChangePurchaseOrderWhenModified() {
        Payment payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new NewState())
                .build();

        // Simulate modifying the transaction (e.g., adding or removing items)
        payment.getItems().add(new Item("item", new Money(10.0), 1));

        // Attempt to change the purchase order
        assertThrows(IllegalStateException.class, payment::authorize);
    }

}


