package org.kata.payment.domain.model;

import org.kata.payment.domain.state.PaymentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Payment {

    private static final Logger log = LoggerFactory.getLogger(Payment.class);

    private PaymentId id;
    private PaymentType paymentType;
    private PaymentState state = new PaymentState.New();
    private List<Item> items = new ArrayList<>();
    private boolean stateModified;
    private PaymentStatus status = PaymentStatus.NEW;

    public Payment() {}

    public PaymentId getId() { return id; }
    public PaymentType getPaymentType() { return paymentType; }
    public PaymentState getState() { return state; }
    public List<Item> getItems() { return items; }
    public boolean isStateModified() { return stateModified; }
    public PaymentStatus getStatus() { return status; }

    public void authorize() {
        state = switch (state) {
            case PaymentState.New _ -> new PaymentState.Authorized();
            default -> throw new IllegalStateException("Cannot authorize from state " + state.status());
        };
        status = state.status();
        stateModified = true;
        log.info("Payment {} transitioned to AUTHORIZED", id);
    }

    public void capture() {
        state = switch (state) {
            case PaymentState.Authorized _ -> new PaymentState.Captured();
            default -> throw new IllegalStateException("Cannot capture from state " + state.status());
        };
        status = state.status();
        stateModified = true;
        log.info("Payment {} transitioned to CAPTURED", id);
    }

    public void cancel() {
        state = switch (state) {
            case PaymentState.New _, PaymentState.Authorized _ -> new PaymentState.Canceled();
            default -> throw new IllegalStateException("Cannot cancel from state " + state.status());
        };
        status = state.status();
        stateModified = true;
        log.info("Payment {} transitioned to CANCELED", id);
    }

    public Money totalAmount() {
        BigDecimal total = items.stream()
                .map(item -> item.price().amount().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Money(total);
    }

    public void addItem(Item item) {
        if (!stateModified) {
            items.add(item);
        } else {
            throw new IllegalStateException("Cannot add items after the state has been modified");
        }
    }

    public void updateDetails(PaymentType paymentType, List<Item> items) {
        this.paymentType = paymentType;
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public enum PaymentType {
        CREDIT_CARD, GIFT_CARD, PAYPAL
    }

    public enum PaymentStatus {
        NEW, AUTHORIZED, CAPTURED, CANCELED
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private PaymentId id;
        private PaymentType paymentType;
        private List<Item> items = new ArrayList<>();
        private PaymentStatus status = PaymentStatus.NEW;

        private Builder() {}

        public Builder id(PaymentId id) { this.id = id; return this; }
        public Builder paymentType(PaymentType paymentType) { this.paymentType = paymentType; return this; }
        public Builder items(List<Item> items) { this.items = items; return this; }
        public Builder status(PaymentStatus status) { this.status = status; return this; }

        public Payment build() {
            var payment = new Payment();
            payment.id = id;
            payment.paymentType = paymentType;
            payment.status = status;
            payment.state = stateFrom(status);
            payment.items = items != null ? items : new ArrayList<>();
            payment.stateModified = false;
            return payment;
        }

        private static PaymentState stateFrom(PaymentStatus status) {
            return switch (status) {
                case NEW        -> new PaymentState.New();
                case AUTHORIZED -> new PaymentState.Authorized();
                case CAPTURED   -> new PaymentState.Captured();
                case CANCELED   -> new PaymentState.Canceled();
            };
        }
    }
}
