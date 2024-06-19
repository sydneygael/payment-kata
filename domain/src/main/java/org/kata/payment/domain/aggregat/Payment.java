package org.kata.payment.domain.aggregat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kata.payment.domain.state.NewState;
import org.kata.payment.domain.state.PaymentState;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.Money;
import org.kata.payment.domain.valueobject.PaymentId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class Payment {

    private PaymentId id;
    private PaymentType paymentType;
    private PaymentState state = new NewState();
    private List<Item> items = new ArrayList<>();
    private boolean stateModified;
    private PaymentStatus status = PaymentStatus.NEW;

    public void authorize() {
        state.authorized(this);
        status = state.getStatus();
        stateModified = true;
    }

    public void capture() {
        state.captured(this);
        status = state.getStatus();
        stateModified = true;
    }

    public void cancel() {
        state.canceled(this);
        status = state.getStatus();
        stateModified = true;
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
        private PaymentState state = new NewState();
        private List<Item> items = new ArrayList<>();
        private PaymentStatus status = PaymentStatus.NEW;

        private Builder() {
        }

        public Builder id(PaymentId id) {
            this.id = id;
            return this;
        }

        public Builder paymentType(PaymentType paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public Builder state(PaymentState state) {
            this.state = state;
            return this;
        }

        public Builder items(List<Item> items) {
            this.items = items;
            return this;
        }

        public Builder status(PaymentStatus status) {
            this.status = status;
            return this;
        }

        public Payment build() {
            var payment = new Payment();
            payment.id = id;
            payment.paymentType = paymentType;
            payment.state = state;
            payment.items = items != null ? items : new ArrayList<>();
            payment.status = status;
            payment.stateModified = false; // Set stateModified to false by default
            return payment;
        }
    }
}
