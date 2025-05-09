package model;

import service.ShoppingCart;
import java.util.LinkedList;
import java.util.List;


public class Customer extends User {
    private ShoppingCart cart;
    private LinkedList<Product> wishlist = new LinkedList<>();

    public Customer(String name) {
        super(name, Role.CUSTOMER);
        this.cart = new ShoppingCart();
    }

    public ShoppingCart getCart() {
        return cart;
    }
    // Wishlist methods
    public List<Product> getWishlist() {
        return wishlist;
    }

    public void addToWishlist(Product product) {
        if (!wishlist.contains(product)) {
            wishlist.add(product);
            System.out.println(product.getName() + " added to wishlist.");
        } else {
            System.out.println(product.getName() + " is already in your wishlist.");
        }
    }

    public void removeFromWishlist(Product product) {
        if (wishlist.remove(product)) {
            System.out.println(product.getName() + " removed from wishlist.");
        } else {
            System.out.println(product.getName() + " is not in your wishlist.");
        }
    }

    public void viewWishlist() {
        if (wishlist.isEmpty()) {
            System.out.println("Your wishlist is empty.");
        } else {
            System.out.println("Your Wishlist:");
            for (Product p : wishlist) {
                System.out.println("- " + p.getName());
            }
        }
    }
}
