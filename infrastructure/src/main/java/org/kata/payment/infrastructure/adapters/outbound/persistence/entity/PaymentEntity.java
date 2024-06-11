package org.kata.payment.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.PaymentId;

import java.util.List;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private Payment.PaymentType paymentType;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> items;

    // Method to convert JPA entity to domain entity
    public Payment toDomain() {
        Payment payment = new Payment();
        payment.setId(new PaymentId(String.valueOf(id)));
        payment.setPaymentType(paymentType);
        // Map other attributes...
        if (items != null) {
            List<Item> itemList = items.stream()
                    .map(ItemEntity::toDomain)
                    .toList();
            payment.setItems(itemList);
        }
        return payment;
    }
}

