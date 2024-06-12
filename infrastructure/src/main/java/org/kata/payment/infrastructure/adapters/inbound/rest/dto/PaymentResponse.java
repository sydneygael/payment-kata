package org.kata.payment.infrastructure.adapters.inbound.rest.dto;

import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.Item;

import java.util.List;

public record PaymentResponse(
        String id,
        Payment.PaymentType paymentType,
        Payment.PaymentStatus paymentStatus,
        List<ItemResponse> items
) {
    public static PaymentResponse fromDomain(Payment payment) {
        return new PaymentResponse(
                payment.getId().id(),
                payment.getPaymentType(),
                payment.getStatus(),
                payment.getItems() != null ? payment.getItems().stream().map(ItemResponse::fromDomain).toList() : List.of()
        );
    }


    public record ItemResponse(String name, double price, int quantity) {
        public static ItemResponse fromDomain(Item item) {
            return new ItemResponse(item.name(), item.price().amount(), item.quantity());
        }
    }
}
