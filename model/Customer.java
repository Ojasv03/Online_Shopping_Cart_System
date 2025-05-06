package model;

import service.ShoppingCart;

public class Customer extends User {
    private ShoppingCart cart;

    public Customer(String name) {
        super(name, Role.CUSTOMER);
        this.cart = new ShoppingCart();
    }

    public ShoppingCart getCart() {
        return cart;
    }
}
