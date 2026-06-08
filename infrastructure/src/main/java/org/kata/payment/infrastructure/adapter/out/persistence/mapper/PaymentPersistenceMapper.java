package org.kata.payment.infrastructure.adapter.out.persistence.mapper;

import org.kata.payment.domain.model.Item;
import org.kata.payment.domain.model.Money;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.infrastructure.adapter.out.persistence.entity.ItemEntity;
import org.kata.payment.infrastructure.adapter.out.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentPersistenceMapper {

    public PaymentEntity toEntity(Payment payment) {
        var entity = new PaymentEntity();
        entity.setId(parseDbId(payment.getId()));
        entity.setPaymentType(payment.getPaymentType());
        entity.setStatus(payment.getStatus());
        var items = payment.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .toList();
        entity.setItems(items);
        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        return Payment.builder()
                .id(new PaymentId(String.valueOf(entity.getId())))
                .paymentType(entity.getPaymentType())
                .status(entity.getStatus())
                .items(entity.getItems().stream().map(this::toItemDomain).toList())
                .build();
    }

    private ItemEntity toItemEntity(Item item, PaymentEntity payment) {
        var entity = new ItemEntity();
        entity.setName(item.name());
        entity.setPrice(item.price().amount());
        entity.setQuantity(item.quantity());
        entity.setPayment(payment);
        return entity;
    }

    private Item toItemDomain(ItemEntity entity) {
        return new Item(entity.getName(), new Money(entity.getPrice()), entity.getQuantity());
    }

    private Long parseDbId(PaymentId paymentId) {
        if (paymentId == null) return null;
        try {
            return Long.valueOf(paymentId.id());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
