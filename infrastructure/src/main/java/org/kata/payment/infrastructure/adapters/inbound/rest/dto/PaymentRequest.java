package org.kata.payment.infrastructure.adapters.inbound.rest.dto;

import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.Money;
import org.kata.payment.domain.valueobject.PaymentId;

import java.util.List;

public record PaymentRequest(
        Payment.PaymentType paymentType,
        Payment.PaymentStatus paymentStatus,
        List<ItemRequest> items
) {
    public Payment toDomain() {
        return Payment.builder()
                .id(new PaymentId())
                .paymentType(paymentType)
                .status(paymentStatus)
                .items(items != null ? items.stream().map(ItemRequest::toDomain).toList() : List.of())
                .build();
    }

    public static record ItemRequest(String name, double price, int quantity) {
        public Item toDomain() {
            return new Item(name, new Money(price), quantity);
        }
    }
}
