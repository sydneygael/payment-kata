package org.kata.payment.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.kata.payment.domain.model.Payment;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "payments")
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

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private List<ItemEntity> items;

    public PaymentEntity() {}

    public Long getId() { return id; }
    public Payment.PaymentType getPaymentType() { return paymentType; }
    public Payment.PaymentStatus getStatus() { return status; }
    public List<ItemEntity> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setPaymentType(Payment.PaymentType paymentType) { this.paymentType = paymentType; }
    public void setStatus(Payment.PaymentStatus status) { this.status = status; }
    public void setItems(List<ItemEntity> items) { this.items = items; }
}
