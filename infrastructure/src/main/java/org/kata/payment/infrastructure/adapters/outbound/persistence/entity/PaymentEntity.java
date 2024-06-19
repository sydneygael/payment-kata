package org.kata.payment.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.PaymentId;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class PaymentEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5747736168032629998L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private Payment.PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private Payment.PaymentStatus status;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> items;

    public PaymentEntity(Payment payment) {
        this.id = payment.getId() != null ? Long.valueOf(payment.getId().id()) : null;
        this.paymentType = payment.getPaymentType();
        this.status = payment.getStatus();
        this.items = payment.getItems().stream()
                .map(ItemEntity::new)
                .peek(itemEntity -> itemEntity.setPayment(this))
                .toList();
    }

    public Payment toDomain() {
        return Payment.builder()
                .id(new PaymentId(String.valueOf(id)))
                .paymentType(paymentType)
                .status(status) // Use status instead of state
                .items(items.stream().map(ItemEntity::toDomain).toList())
                .build();
    }
}
