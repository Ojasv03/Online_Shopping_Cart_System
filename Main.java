import model.*;
//import service.ShoppingCart;
import java.util.*;

public class Main {
    private static final String ADMIN_PASSWORD = "admin123";

    private static void printProductTable(List<Product> products) {
        System.out.println("+-----+----------------------+-------------+----------+");
        System.out.println("| ID  | Product Name         | Price       | Stock    |");
        System.out.println("+-----+----------------------+-------------+----------+");
        products.forEach(p -> System.out.println(p));
        System.out.println("+-----+----------------------+-------------+----------+");
    }

    private static void simulateProgressBar() {
        System.out.print("Processing: [");
        int width = 40;
        for (int i = 0; i < width; i++) {
            System.out.print("=");
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {}
        }
        System.out.println("]");
    }

    private static int validateIntegerInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = Integer.parseInt(scanner.nextLine());
                if (input < min || input > max) {
                    System.out.printf("Input must be between %d and %d.%n", min, max);
                    continue;
                }
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    private static double validatePriceInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter product price: ");
                double price = Double.parseDouble(scanner.nextLine());
                if (price < 0) throw new IllegalArgumentException();
                return price;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Price cannot be negative.");
            }
        }
    }

    private static void displayAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("0. Show Menu");
        System.out.println("1. View Products");
        System.out.println("8. Add Product");
        System.out.println("10. Process Checkout Queue");
        System.out.println("7. Exit");
    }

    private static void displayCustomerMenu() {
        System.out.println("\n--- Customer Menu ---");
        System.out.println("0. Show Menu");
        System.out.println("1. View Products");
        System.out.println("2. Filter Products");
        System.out.println("3. Add to Cart");
        System.out.println("4. View Cart");
        System.out.println("5. Remove from Cart");
        System.out.println("6. Checkout");
        System.out.println("7. Exit");
        System.out.println("9. Enter Checkout Queue");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Product> products = new ArrayList<>();
        Queue<Customer> checkoutQueue = new LinkedList<>();

        // Preloaded products with stock
        products.add(new Product(1, "Laptop", 99999.99, 10));
        products.add(new Product(2, "Phone", 49949.00, 15));
        products.add(new Product(3, "Headphones", 7999.99, 20));
        products.add(new Product(4, "Book", 1299.00, 30));
        products.add(new Product(5, "Tablet", 29999.00, 5));
        products.add(new Product(6, "Smartwatch", 15999.50, 8));
        products.add(new Product(7, "Backpack", 2499.99, 12));
        products.add(new Product(8, "Charger", 899.00, 25));
        products.add(new Product(9, "Pen Drive", 499.99, 50));
        products.add(new Product(10, "Bluetooth Speaker", 3499.75, 18));

        // Role-based authentication
        System.out.print("Are you an admin? (y/n): ");
        String roleInput = scanner.nextLine().trim().toLowerCase();
        boolean isAdmin = roleInput.equals("y");
        boolean isCustomer = !isAdmin;

        String userName = "Admin";
        Customer customer = null;

        if (isAdmin) {
            System.out.print("Enter admin password: ");
            String password = scanner.nextLine();
            if (!ADMIN_PASSWORD.equals(password)) {
                System.out.println("Incorrect password! Switching to customer mode.");
                isAdmin = false;
                isCustomer = true;
            }
        }

        if (isCustomer) {
            System.out.print("Enter your name: ");
            userName = scanner.nextLine();
            customer = new Customer(userName);
            customer.greet();
        } else {
            System.out.println("Welcome, Admin!");
        }

        // Display menu ONCE before loop
        if (isAdmin) {
            displayAdminMenu();
        } else {
            displayCustomerMenu();
        }

        boolean running = true;
        while (running) {
            System.out.print("\nEnter choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            if (isAdmin) {
                switch (choice) {
                    case 0:
                        displayAdminMenu();
                        break;
                    case 1:
                        System.out.println("\n--- Product List ---");
                        printProductTable(products);
                        break;
                    case 8:
                        System.out.print("Enter product name: ");
                        String prodName = scanner.nextLine();
                        double prodPrice = validatePriceInput(scanner);
                        int stock = validateIntegerInput(scanner, "Enter stock quantity: ", 0, 1000);
                        products.add(new Product(products.size() + 1, prodName, prodPrice, stock));
                        System.out.println("Product added successfully!");
                        break;
                    case 10:
                        if (checkoutQueue.isEmpty()) {
                            System.out.println("No customers in queue.");
                        } else {
                            Customer nextCustomer = checkoutQueue.poll();
                            System.out.println("Processing checkout for: " + nextCustomer.getName());
                            simulateProgressBar();
                            nextCustomer.getCart().checkout();
                        }
                        break;
                    case 7:
                        System.out.println("Goodbye, Admin!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. (Enter 0 to see menu)");
                }
            } else { // Customer
                switch (choice) {
                    case 0:
                        displayCustomerMenu();
                        break;
                    case 1:
                        System.out.println("\n--- Product List ---");
                        printProductTable(products);
                        break;
                    case 2:
                        int sortChoice = validateIntegerInput(scanner,
                                "Filter by: 1. Name  2. Price\nEnter choice: ", 1, 2);
                        if (sortChoice == 1) {
                            products.sort(Comparator.comparing(Product::getName));
                        } else {
                            products.sort(Comparator.comparingDouble(Product::getPrice));
                        }
                        System.out.println("Filtered list sorted.");
                        break;
                    case 3:
                        int addId = validateIntegerInput(scanner,
                                "Enter product ID to add: ", 1, products.size());
                        int quantity = validateIntegerInput(scanner,
                                "Enter quantity: ", 1, 100);
                        Product selectedProduct = products.get(addId - 1);
                        customer.getCart().addProduct(selectedProduct, quantity);
                        break;
                    case 4:
                        customer.getCart().viewCart();
                        break;
                    case 5:
                        int removeId = validateIntegerInput(scanner,
                                "Enter product ID to remove: ", 1, products.size());
                        Product removeProduct = products.get(removeId - 1);
                        customer.getCart().removeProduct(removeProduct);
                        break;
                    case 6:
                        customer.getCart().viewCart();
                        int confirm = validateIntegerInput(scanner,
                                "Confirm checkout? (1=Yes, 0=No): ", 0, 1);
                        if (confirm == 1) {
                            simulateProgressBar();
                            customer.getCart().checkout();
                        }
                        break;
                    case 7:
                        System.out.println("Goodbye, " + customer.getName() + "!");
                        running = false;
                        break;
                    case 9:
                        checkoutQueue.add(customer);
                        System.out.println(customer.getName() + " added to checkout queue.");
                        break;
                    default:
                        System.out.println("Invalid choice. (Enter 0 to see menu)");
                }
            }
        }
        scanner.close();
    }
}
