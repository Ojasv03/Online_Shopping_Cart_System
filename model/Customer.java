package model;

import service.ShoppingCart;

public class Customer extends User {
    private ShoppingCart cart;

    public Customer(String name) {
        super(name);
        this.cart = new ShoppingCart();
    }

    // Getter for cart
    public ShoppingCart getCart() {
        return cart;
    }
}
