import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;

public class Main {
    public static PriorityQueue<Order> pendingOrders = new PriorityQueue<>(new OrderComparator());
    public static List<Order> completedOrders = new ArrayList<>();
    public static List<Order> processingOrders = new ArrayList<>();

    public static List<Order> cancelledOrders = new ArrayList<>();
    public static List<Customer> customers = new ArrayList<>();
    public static Admin admin = new Admin();


    private static LocalDateTime currentDate = LocalDateTime.now();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("---------Menu---------");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminMenu(scanner);
                    break;

                case 2:
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    Customer existingCustomer = null;

                    List<Customer> allCustomers = CustomerFileManager.loadCustomers();

                    for (Customer c : allCustomers) {
                        if (c.getName().equalsIgnoreCase(name)) {
                            existingCustomer = c;
                            break;
                        }
                    }

                    if (existingCustomer == null) {
                        System.out.println("Customer not found, registering new customer.");
                        Customer newCustomer = new Customer(name, password);
                        CustomerFileManager.saveCustomer(newCustomer);
                        customers.add(newCustomer);
                        System.out.println("Registration successful!");
                        customerMenu(newCustomer);
                    } else {
                        if (existingCustomer.validatePassword(password)) {
                            System.out.println("Login successful! Welcome back, " + name);
                            customerMenu(existingCustomer);
                        } else {
                            System.out.println("Invalid password. Please try again.");
                        }
                    }
                    break;


                case 3:
                    System.out.println("Exiting the program.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);

        scanner.close();
    }

    private static void adminMenu(Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Welcome to the Admin Section.");
            System.out.println("Please choose an option:");
            System.out.println("A. Menu Management");
            System.out.println("B. Order Management");
            System.out.println("C. Report Generation");
            System.out.println("D. Exit");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    menuManagement(scanner);
                    break;
                case "B":
                    orderManagement(scanner);
                    break;
                case "C":
                    generateReport(scanner, admin);
                    break;
                case "D":
                    exit = true;
                    System.out.println("Exiting Admin Section.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void menuManagement(Scanner scanner) {
        boolean backToAdminMenu = false;

        while (!backToAdminMenu) {
            System.out.println("\n--- Menu Management ---");

            System.out.println("1. Add New Items");
            System.out.println("2. Remove Items");
            System.out.println("3. Update Item Price");
            System.out.println("4. Update Item Availability");
            System.out.println("5. Update Item Quantity");
            System.out.println("6. View Final Menu(GUI)");
            System.out.println("7. Back to Admin Menu");

            int option = scanner.nextInt();
            scanner.nextLine();



            switch (option) {
                case 1:
                    System.out.println("Adding new items...");
                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter item price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();

                    Item.Category category = null;
                    while (category == null) {
                        System.out.print("Enter item category (SNACKS, MEALS, BEVERAGES, DESSERTS, APPETIZERS): ");
                        String categoryInput = scanner.nextLine().toUpperCase();

                        try {
                            category = Item.Category.valueOf(categoryInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid category. Please enter a valid category.");
                        }
                    }

                    System.out.print("Enter item quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    Item newItem = new Item(name, price, category, quantity);

                    admin.addItemToMenu(newItem);


                    break;
                case 2:
                    System.out.println("Enter item to be removed: ");
                    String itemName = scanner.nextLine();
                    Item itemToRemove = null;

                    for (Item item : Menu.menu) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToRemove = item;
                            break;
                        }
                    }

                    if (itemToRemove != null) {
                        admin.removeItemFromMenu(itemToRemove);
                    } else {
                        System.out.println("Item not found in menu: " + itemName);
                    }

                    break;
                case 3:

                    System.out.println("Enter item name to be updated: ");
                    String itemname = scanner.nextLine();
                    Item itemUd = null;

                    for (Item item : Menu.menu) {
                        if (item.getName().equalsIgnoreCase(itemname)) {
                            itemUd = item;
                            break;
                        }
                    }

                    if (itemUd != null) {
                        System.out.print("Enter item's new price: ");
                        double newprice = scanner.nextDouble();
                        admin.updateItemPrice(itemUd, newprice);
                    } else {
                        System.out.println("Item not found in menu: " + itemname);
                    }
                    break;


                case 4:
                    System.out.println("Enter item name to update availability: ");
                    String iname = scanner.nextLine();
                    Item iUd = null;

                    for (Item item : Menu.menu) {
                        if (item.getName().equalsIgnoreCase(iname)) {
                            iUd = item;
                            break;
                        }
                    }

                    if (iUd != null) {
                        System.out.println("Current availability: " + (iUd.isAvailable() ? "Available" : "Unavailable"));

                        boolean newAvailability = !iUd.isAvailable();
                        admin.makeItemUnavailable(iUd, newAvailability);
                        System.out.println("Updated availability for item: " + iname + " to " + (newAvailability ? "Available" : "Unavailable"));
                    } else {
                        System.out.println("Item not found in menu: " + iname);
                    }
                    break;

                case 5:

                    System.out.println("Enter item name to be updated: ");
                    String itemn = scanner.nextLine();
                    Item itm = null;

                    for (Item item : Menu.menu) {
                        if (item.getName().equalsIgnoreCase(itemn)) {
                            itm = item;
                            break;
                        }
                    }

                    if (itm != null) {
                        System.out.print("Enter item's new quantiy: ");
                        int newq = scanner.nextInt();
                        admin.updateItemQuantity(itm, newq);
                    } else {
                        System.out.println("Item not found in menu: " + itemn);
                    }
                    break;

                case 6:
                    Menu.saveMenuToFile();
                    System.out.println("Opening the menu in GUI...");
                    SwingUtilities.invokeLater(() -> {
                        MenuGUI.main(new String[]{});
                    });
                    break;

                case 7:
                    backToAdminMenu = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void orderManagement(Scanner scanner) {
        boolean backToAdminMenu = false;

        while (!backToAdminMenu) {
            System.out.println("\n--- Order Management ---");
            System.out.println("1. View Pending Orders(CLI and GUI)");
            System.out.println("2. Accept the First Order in List");
            System.out.println("3. Deny the First Order in List");
            System.out.println("4. Back to Admin Menu");

            int option = scanner.nextInt();
            scanner.nextLine();



            switch (option) {
                case 1:
                    admin.viewPendingOrders();

                    OrderFiles.saveOrdersToFile(Main.pendingOrders, Main.processingOrders);
                    new PendingOrdersGUI();


                    break;
                case 2:

                    System.out.println("Accepting the first order...");
                    admin.processOrder();
                    break;
                case 3:
                    System.out.println("Denying the first order...");
                    admin.CancelOrder();
                    break;
                case 4:
                    backToAdminMenu = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void generateReport(Scanner scanner, Admin admin) {
        System.out.println("Generating Daily Sales Report for " + currentDate.toLocalDate() + "...");
        admin.generateDailySalesReport();

        System.out.print("Do you want to move to the next day? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            for (Order order : new ArrayList<>(pendingOrders)) {
                order.cancelOrder(order, admin);
            }

            admin.clearDailyData();
            currentDate = currentDate.plusDays(1);
            System.out.println("Data cleared. Moved to the next day: " + currentDate.toLocalDate());

        } else {
            System.out.println("Returning to the main menu without clearing data or moving the date.");
        }
    }


    private static void customerMenu(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Welcome, " + customer.getName() + "!");
            System.out.println("Please choose an option:");
            System.out.println("A. Become VIP");
            System.out.println("B. View menu");
            System.out.println("C. Add/View review");
            System.out.println("D. Track order");
            System.out.println("E. Create new order");
            System.out.println("F. Show order history");
            System.out.println("G. Exit");


            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    customer.becomeVIP();
                    break;
                case "B":
                    viewMenu(scanner,customer);
                    break;
                case "C":
                    manageReviews(scanner,customer);
                    break;
                case "D":
                    trackOrder(scanner, customer);
                    break;
                case "E":
                    createOrder(scanner, customer);
                    break;
                case "F":
                    customer.viewOrderHistory();
                    break;
                case "G":
                    exit = true;
                    System.out.println("Exiting customer menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }



    private static void viewMenu(Scanner scanner, Customer customer) {

        boolean backtoCustomerMenu= false;

        while(!backtoCustomerMenu) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. View available items(CLI and GUI)");
            System.out.println("2. Search in menu");
            System.out.println("3. Filter by category");
            System.out.println("4. Sort by price");
            System.out.println("5. Back to Customer Menu");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    customer.viewAllItems();

                    Menu.saveMenuToFile();
                    System.out.println("Opening the menu in GUI...");
                    SwingUtilities.invokeLater(() -> {
                        MenuGUI.main(new String[]{});
                    });

                    break;
                case 2:

                    System.out.println("Enter item to search: ");
                    String itemName = scanner.nextLine();
                    customer.searchItem(itemName);
                    break;
                case 3:

                    Item.Category category = null;
                    while (category == null) {
                        System.out.print("Enter item category to search (SNACKS, MEALS, BEVERAGES, DESSERTS, APPETIZERS): ");
                        String categoryInput = scanner.nextLine().toUpperCase();

                        try {
                            category = Item.Category.valueOf(categoryInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid category. Please enter a valid category.");
                        }
                    }
                    customer.filterByCategory(category);
                    break;
                case 4:
                    System.out.println("Sort items by price:");
                    System.out.print("Enter 'A' for ascending or 'D' for descending: ");
                    String sortChoice = scanner.nextLine().trim().toUpperCase();

                    boolean ascending;
                    if (sortChoice.equals("A")) {
                        ascending = true;
                    } else if (sortChoice.equals("D")) {
                        ascending = false;
                    } else {
                        System.out.println("Invalid choice. Please enter 'A' or 'D'.");
                        break;
                    }

                    customer.sortByPrice(ascending);
                    break;


                case 5:
                    backtoCustomerMenu=true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        }
    }



    private static void manageReviews(Scanner scanner, Customer customer) {

        boolean backtoCustomerMenu= false;

        while(!backtoCustomerMenu) {
            System.out.println("\n--- Review Management ---");
            System.out.println("1. Add item review");
            System.out.println("2. View item reviews");
            System.out.println("3. Back to Customer Menu");


            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Enter item name to review: ");

                    String itemName = scanner.nextLine();
                    Item itemToreview = null;

                    for (Item item : Menu.menu) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToreview = item;
                            break;
                        }
                    }

                    if (itemToreview != null) {
                        System.out.print("Enter your review: ");
                        String reviewText = scanner.nextLine();
                        customer.reviewItem(itemToreview,reviewText);

                    } else {
                        System.out.println("Item not found in menu: " + itemName);
                    }

                    break;
                case 2:
                    System.out.print("Enter item name to view reviews: ");
                    String reviewItemName = scanner.nextLine();

                    Item itemreview = null;

                    for (Item item : Menu.menu) {
                        if (item.getName().equalsIgnoreCase(reviewItemName)) {
                            itemreview = item;
                            break;
                        }
                    }

                    if (itemreview != null) {
                        customer.viewItemReviews(itemreview);

                    }
                    else {
                        System.out.println("Item not found in menu: " + reviewItemName);
                    }
                    break;

                case 3:
                    backtoCustomerMenu=true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void trackOrder(Scanner scanner, Customer customer) {

        boolean backtoCustomerMenu= false;

        while(!backtoCustomerMenu) {
            System.out.println("\n--- Track Order ---");
            System.out.println("1. Check order status");
            System.out.println("2. Cancel order");
            System.out.println("3. View all order history");
            System.out.println("4. Back to Customer Menu");


            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("Checking your order status...");
                    boolean orderFound = false;

                    for (Order order : Main.processingOrders) {
                        if (order.getCustomerName().equals(customer)) {
                            System.out.println("Order ID: " + order.getOrderId() + " - Status:" + order.getStatus());
                            orderFound = true;
                        }
                    }

                    for (Order order : Main.pendingOrders) {
                        if (order.getCustomerName().equals(customer)) {
                            System.out.println("Order ID: " + order.getOrderId() + " - Status:" + order.getStatus());
                            orderFound = true;
                        }
                    }

                    if (!orderFound) {
                        System.out.println("No orders found for this customer.");
                    }

                    OrderFiles.saveOrdersToFile(Main.pendingOrders, Main.processingOrders);
                    new PendingOrdersGUI();
                    break;
                case 2:
                    System.out.print("Enter order ID to cancel: ");
                    String orderIdToCancel = scanner.nextLine();

                    Order orderToCancel = null;
                    for (Order order : Main.pendingOrders) {
                        System.out.println(order.getOrderId());
                        System.out.println(order.getCustomerName().getName());
                        if (order.getOrderId().equals(orderIdToCancel) && order.getCustomerName().equals(customer)) {

                            orderToCancel = order;


                            break;
                        }
                    }

                    if (orderToCancel != null) {
                        customer.cancelOrder(orderToCancel,admin);
                    } else {
                        System.out.println("Order not found or it is already accepted by the shop.");
                    }
                    break;

                case 3:
                    customer.viewOrderHistory();
                    break;

                case 4:
                    backtoCustomerMenu=true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");

            }
        }
    }

    private static void createOrder(Scanner scanner, Customer customer) {

        boolean backtoCustomerMenu= false;
        while(!backtoCustomerMenu){
            System.out.println("\n--- Create New Order ---");
            System.out.println("1. Reorder");
            System.out.println("2. Make a new cart");
            System.out.println("3. Back to Customer Menu");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Enter the order ID to reorder: ");
                    String orderId = scanner.nextLine();

                    Order previousOrder = null;
                    for (Order order : completedOrders) {
                        System.out.println(order.getOrderId());
                        if (order.getOrderId().equals(orderId)) {
                            previousOrder = order;
                            break;
                        }
                    }

                    if (previousOrder != null) {
                        customer.reorder(orderId);
                        System.out.println("Order has been added to your cart.");
                    } else {
                        System.out.println("Invalid order ID. Please check your order history.");
                    }
                    break;
                case 2:
                    System.out.println("Creating a new cart...");
                    Cart cart = customer.getCart();
                    boolean orderPlaced = false;

                    while (!orderPlaced) {
                        System.out.println("\nCart Menu:");
                        System.out.println("1. Add item to cart");
                        System.out.println("2. Remove item from cart");
                        System.out.println("3. Modify item quantity in cart");
                        System.out.println("4. Add special instructions");
                        System.out.println("5. Place order");
                        System.out.println("0. Go back to main menu");
                        System.out.print("Enter your choice: ");
                        int cartChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (cartChoice) {
                            case 1:
                                System.out.print("Enter item name: ");
                                String itemName = scanner.nextLine();
                                System.out.print("Enter quantity: ");
                                int quantity = scanner.nextInt();
                                scanner.nextLine();

                                Item item = Menu.findItemInMenu(itemName);
                                if (item != null) {
                                    cart.addItem(item, quantity);
                                } else {
                                    System.out.println("Item not found.");
                                }
                                break;

                            case 2:
                                System.out.print("Enter item name to remove: ");
                                String removeItemName = scanner.nextLine();
                                Item removeItem = Menu.findItemInMenu(removeItemName);
                                if (removeItem != null) {
                                    cart.removeItem(removeItem);
                                } else {
                                    System.out.println("Item not found in cart.");
                                }
                                break;

                            case 3:
                                System.out.print("Enter item name to modify quantity: ");
                                String modifyItemName = scanner.nextLine();
                                Item modifyItem = Menu.findItemInMenu(modifyItemName);
                                if (modifyItem != null) {
                                    System.out.print("Enter new quantity: ");
                                    int newQuantity = scanner.nextInt();
                                    scanner.nextLine();
                                    cart.modifyItemQuantity(modifyItem, newQuantity);
                                } else {
                                    System.out.println("Item not found in cart.");
                                }
                                break;

                            case 4:
                                System.out.print("Enter special instructions: ");
                                String instructions = scanner.nextLine();
                                cart.addSpecialInstructions(instructions);
                                break;

                            case 5:
                                Order order = cart.checkout(customer.isVIP(), customer, admin);
                                if (order != null) {
                                    orderPlaced = true;
                                    System.out.println("Order placed successfully.");
                                }
                                break;

                            case 0:
                                orderPlaced = true;
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }
                    break;
                case 3:
                    backtoCustomerMenu=true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                }
        }


    }


    private static Customer findCustomer(String name) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }
}

class OrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return Boolean.compare(o2.isVIP(), o1.isVIP());
    }
}
