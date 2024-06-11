package org.kata.payment.domain.aggregat;

import lombok.*;
import org.kata.payment.domain.state.NewState;
import org.kata.payment.domain.state.PaymentState;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.Money;
import org.kata.payment.domain.valueobject.PaymentId;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    private PaymentId id;
    private PaymentType paymentType;
    @Builder.Default
    private PaymentState state = new NewState();
    private List<Item> items;

    public void authorize() {
        state.authorized(this);
    }

    public void capture() {
        state.captured(this);
    }

    public void cancel() {
        state.canceled(this);
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

    public enum PaymentType {
        CREDIT_CARD, GIFT_CARD, PAYPAL
    }

    public enum PaymentStatus {
        NEW, AUTHORIZED, CAPTURED, CANCELED
    }
}
