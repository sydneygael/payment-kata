package org.kata.payment.infrastructure.adapter.in.rest.mapper;

import org.kata.payment.domain.model.Item;
import org.kata.payment.domain.model.Money;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.infrastructure.adapter.in.rest.dto.PaymentRequest;
import org.kata.payment.infrastructure.adapter.in.rest.dto.PaymentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentRestMapper {

    public Payment toDomain(PaymentRequest request) {
        return Payment.builder()
                .id(new PaymentId())
                .paymentType(request.paymentType())
                .status(request.paymentStatus())
                .items(toItems(request.items()))
                .build();
    }

    public Payment toDomain(PaymentRequest request, String id) {
        return Payment.builder()
                .id(new PaymentId(id))
                .paymentType(request.paymentType())
                .status(request.paymentStatus())
                .items(toItems(request.items()))
                .build();
    }

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId().id(),
                payment.getPaymentType(),
                payment.getStatus(),
                payment.getItems() != null
                        ? payment.getItems().stream().map(this::toItemResponse).toList()
                        : List.of()
        );
    }

    private List<Item> toItems(List<PaymentRequest.ItemRequest> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(i -> new Item(i.name(), new Money(i.price()), i.quantity()))
                .toList();
    }

    private PaymentResponse.ItemResponse toItemResponse(Item item) {
        return new PaymentResponse.ItemResponse(item.name(), item.price().amount(), item.quantity());
    }
}
