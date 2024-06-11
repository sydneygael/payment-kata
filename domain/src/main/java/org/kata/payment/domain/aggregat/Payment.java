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

import java.util.ArrayList;
import java.util.List;



@EqualsAndHashCode
@NoArgsConstructor
public class Payment {

    @Getter
    @Setter
    private PaymentId id;
    @Getter
    @Setter
    private PaymentType paymentType;
    @Getter
    @Setter
    private PaymentState state;
    @Getter
    @Setter
    private List<Item> items;
    @Setter
    private boolean stateModified;

    public void authorize() {
        state.authorized(this);
        stateModified = true;
    }

    public void capture() {
        state.captured(this);
        stateModified = true;
    }

    public void cancel() {
        state.canceled(this);
        stateModified = true;
    }

    public PaymentStatus getPaymentStatus() {
        return state.getStatus();
    }

    public Money totalAmount() {
        double total = items.stream()
                .mapToDouble(item -> item.price().amount() * item.quantity())
                .sum();
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

        public Payment build() {
            var payment = new Payment();
            payment.setId(id);
            payment.setPaymentType(paymentType);
            payment.setState(state);
            payment.setItems(items);
            payment.setStateModified(false); // Set stateModified to false by default
            return payment;
        }
    }
}
