package org.kata.payment.domain.state;

public enum PaymentEvent {
    NEW,
    AUTHORIZE,
    CAPTURE,
    CANCEL
}
