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

import java.util.ArrayList;
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
        assertEquals(Payment.PaymentStatus.NEW, payment.getStatus());
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
        assertEquals(Payment.PaymentStatus.CAPTURED, payment.getStatus());
    }

    @Test
    void testChangeToCapturedWhenNotAuthorized() {
        var payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new NewState())
                .build();
        assertThrows(IllegalStateException.class, payment::capture);
    }

    @Test
    void testChangeToCanceledWhenNotCaptured() {
        var payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new NewState())
                .build();
        payment.cancel();
        assertEquals(Payment.PaymentStatus.CANCELED, payment.getStatus());
    }

    @Test
    void testChangeToCanceledWhenCaptured() {
        var payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new CapturedState())
                .build();
        assertThrows(IllegalStateException.class, payment::cancel);
    }

    @Test
    void testChangeToCanceledWhenCanceled() {
        var payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.emptyList())
                .state(new CanceledState())
                .build();
        assertThrows(IllegalStateException.class, payment::cancel);
    }

    @Test
    void testShouldNotChangePurchaseOrderWhenModified() {
        var payment = Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(new ArrayList<>())
                .state(new NewState())
                .build();

        // WHEN
        payment.authorize();

        // THEN
        assertThrows(IllegalStateException.class, () -> payment.addItem(new Item("item", new Money(10.0), 1)));
    }

}


