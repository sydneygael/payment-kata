package org.kata.payment.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.Money;

import java.io.Serializable;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
public class ItemEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_price", nullable = false)
    private Double price;

    @Column(name = "item_quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    // Constructor to convert domain object to JPA entity
    public ItemEntity(Item item) {
        this.name = item.name();
        this.price = item.price().amount();
        this.quantity = item.quantity();
    }

    // Method to convert JPA entity to domain object
    public Item toDomain() {
        return new Item(name, new Money(price), quantity);
    }
}

