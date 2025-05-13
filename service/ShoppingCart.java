package service;

import model.Product;
import java.util.*;

public class ShoppingCart {
    private Map<Product, Integer> cartItems = new HashMap<>();

    public void addProduct(Product product, int quantity, boolean admin) {
        if (product.getStock() >= quantity) {
            cartItems.put(product, cartItems.getOrDefault(product, 0) + quantity);
            product.setStock(product.getStock() - quantity);
            if (!admin) {
                System.out.printf("%d x %s added to cart.%n", quantity, product.getName());
            }
        } else {
            if (!admin) {
                System.out.printf("Error: Only %d units of %s available.%n", 
                    product.getStock(), product.getName());
            }
        }
    }

    public void removeProduct(Product product) {
        if (cartItems.containsKey(product)) {
            int quantity = cartItems.get(product);
            product.setStock(product.getStock() + quantity);
            cartItems.remove(product);
            System.out.println(product.getName() + " removed. Stock updated.");
        } else {
            System.out.println("Product not found in cart.");
        }
    }

    public void viewCart() {
        if (cartItems.isEmpty()) {
            System.out.println("+------------------------+");
            System.out.println("|     Your cart is empty |");
            System.out.println("+------------------------+");
            return;
        }
        
        System.out.println("\n+--------------------- Cart ---------------------+");
        System.out.println("| Product              | Qty |    Subtotal  |");
        System.out.println("|----------------------+-----+--------------|");
        
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            double subtotal = p.getPrice() * qty;
            System.out.printf("| %-20s | %3d | Rs %8.2f |%n", 
                p.getName(), qty, subtotal);
            total += subtotal;
        }
        
        System.out.println("|----------------------+-----+--------------|");
        System.out.printf("| TOTAL                |     | Rs %8.2f |%n", total);
        System.out.println("+-------------------------------------------+");
    }

    public void checkout() {
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty. Add items before checkout.");
        } else {
            viewCart();
            System.out.println("Checkout complete. Thank you for shopping!");
            cartItems.clear();
        }
    }
}
