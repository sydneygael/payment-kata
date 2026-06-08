package org.kata.payment.domain.state;

import org.kata.payment.domain.model.Payment;

public sealed interface PaymentState
        permits PaymentState.New, PaymentState.Authorized, PaymentState.Captured, PaymentState.Canceled {

    Payment.PaymentStatus status();

    record New() implements PaymentState {
        public Payment.PaymentStatus status() { return Payment.PaymentStatus.NEW; }
    }

    record Authorized() implements PaymentState {
        public Payment.PaymentStatus status() { return Payment.PaymentStatus.AUTHORIZED; }
    }

    record Captured() implements PaymentState {
        public Payment.PaymentStatus status() { return Payment.PaymentStatus.CAPTURED; }
    }

    record Canceled() implements PaymentState {
        public Payment.PaymentStatus status() { return Payment.PaymentStatus.CANCELED; }
    }
}
