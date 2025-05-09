import model.*;
import service.*;
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
            } catch (InterruptedException e) {
            }
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
                if (price < 0)
                    throw new IllegalArgumentException();
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
        System.out.println("10. Search Product");
        System.out.println("11. Wishlist");
    }

    private static void displayWishlistMenu() {
        System.out.println("\n--- Wishlist Menu ---");
        System.out.println("1. View Wishlist");
        System.out.println("2. Add to Wishlist");
        System.out.println("3. Remove from Wishlist");
        System.out.println("4. Back to Customer Menu");
    }

    private static Product findProductById(List<Product> products, int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // Manual binary search implementation
    private static int binarySearchByName(List<Product> products, String targetName) {
        int low = 0;
        int high = products.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            String midName = products.get(mid).getName();
            int cmp = midName.compareTo(targetName);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1; // Not found
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Product> products = ProductController.loadProducts(); // Loading products from file
        Queue<Customer> checkoutQueue = new LinkedList<>();

        // If no products are loaded (file was empty or doesn't exist), preload some
        // default products
        if (products.isEmpty()) {
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
        }

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

            // Check if the user exists, if not, register them
            if (UserController.checkUserExistence(userName)) {
                System.out.println("Welcome back, " + userName + "!");
            } else {
                UserController.registerUser(userName);
                System.out.println("You have been registered.");
            }

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
                        ProductController.saveProducts(products); // Save products to file
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
                        System.out.println("Product list filtered.");
                        break;
                    case 3:
                        int addId = validateIntegerInput(scanner, "Enter product ID to add: ", 1, products.size());
                        int quantity = validateIntegerInput(scanner, "Enter quantity: ", 1, 100);
                        Product selectedProduct = findProductById(products, addId);
                        if (selectedProduct != null) {
                            customer.getCart().addProduct(selectedProduct, quantity);
                        } else {
                            System.out.println("Product ID not found.");
                        }
                        break;
                    case 4:
                        customer.getCart().viewCart();
                        break;
                    case 5:
                        int removeId = validateIntegerInput(scanner, "Enter product ID to remove: ", 1,
                                products.size());
                        Product removeProduct = findProductById(products, removeId);
                        if (removeProduct != null) {
                            customer.getCart().removeProduct(removeProduct);
                        } else {
                            System.out.println("Product ID not found.");
                        }
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
                    case 10:
                        System.out.print("Enter product name to search: ");
                        String searchName = scanner.nextLine();
                        // Ensure list is sorted by name before binary search
                        products.sort(Comparator.comparing(Product::getName));
                        int index = binarySearchByName(products, searchName);
                        if (index != -1) {
                            System.out.println("Product found:");
                            System.out.println(products.get(index));
                        } else {
                            System.out.println("Product not found.");
                        }
                        break;
                    case 11:
                        boolean wishlistRunning = true;
                        displayWishlistMenu();
                        while (wishlistRunning) {
                            int wishlistChoice = validateIntegerInput(scanner, "Enter wishlist choice: ", 1, 4);
                            switch (wishlistChoice) {
                                case 1:
                                    customer.viewWishlist();
                                    break;
                                case 2:
                                    int addWishId = validateIntegerInput(scanner,
                                            "Enter product ID to add to wishlist: ", 1, products.size());
                                    Product wishAddProduct = findProductById(products, addWishId);
                                    if (wishAddProduct != null) {
                                        customer.addToWishlist(wishAddProduct);
                                    } else {
                                        System.out.println("Product ID not found.");
                                    }
                                    break;
                                case 3:
                                    int removeWishId = validateIntegerInput(scanner,
                                            "Enter product ID to remove from wishlist: ", 1, products.size());
                                    Product wishRemoveProduct = findProductById(products, removeWishId);
                                    if (wishRemoveProduct != null) {
                                        customer.removeFromWishlist(wishRemoveProduct);
                                    } else {
                                        System.out.println("Product ID not found.");
                                    }
                                    break;
                                case 4:
                                    wishlistRunning = false;
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }
                        }
                        break;

                    default:
                        System.out.println("Invalid choice. (Enter 0 to see menu)");
                }
            }
        }
        scanner.close();
    }
}