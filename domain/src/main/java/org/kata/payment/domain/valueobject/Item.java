package org.kata.payment.domain.valueobject;

public record Item(String name, Money price, Integer quantity) {

    public Item {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty");
        }
        if (price == null || price.amount() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}


