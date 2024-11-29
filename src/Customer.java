import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;


public class Customer {
    private String name;
    private boolean isVIP;
    private double money;
    private Cart cart;
    private List<Order> orderHistory;
    private String password;

    public Customer(String name, String password) {
        this.name = name;
        this.isVIP = false;
        this.money = 150.0;
        this.cart = new Cart();
        this.orderHistory = new ArrayList<>();
        this.password=password;

    }
    public Customer(String name, String password, boolean isVIP, double money) { //for file
        this.name = name;
        this.password = password;
        this.isVIP = isVIP;
        this.money = money;
        this.cart = new Cart();
        this.orderHistory = new ArrayList<>();
    }


    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
            System.out.println(name + "'s password has been set.");
        } else {
            System.out.println("Password cannot be empty.");
        }
    }

    public String getPassword() {
        return password;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void becomeVIP() {
        if (!isVIP && money >= 30) {
            money -= 30;
            isVIP = true;
            System.out.println(name + " is now a VIP. Remaining balance: " + money);
        } else if (isVIP) {
            System.out.println(name + " is already a VIP.");
        } else {
            System.out.println(name + " does not have enough money to become a VIP.");
        }
    }

    public void viewAllItems() {
        System.out.println("Available Menu Items:");
        for (Item item : Menu.menu) {
            if (item.isAvailable()) {
                System.out.println(item.toString());
            }
        }
    }

    public void searchItem(String keyword) {
        List<Item> searchResults = new ArrayList<>();

        for (Item item : Menu.menu) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(item);
            }
        }

        if (searchResults.isEmpty()) {
            System.out.println("No items found for keyword: " + keyword);
        } else {
            System.out.println("Search Results for keyword '" + keyword + "':");
            for (Item item : searchResults) {
                System.out.println(item.toString());
            }
        }
    }

    public void filterByCategory(Item.Category category) {
        List<Item> filteredItems = new ArrayList<>();

        for (Item item : Menu.menu) {
            if (item.getCategory() == category) {
                filteredItems.add(item);
            }
        }

        if (filteredItems.isEmpty()) {
            System.out.println("No items found in category: " + category);
        } else {
            System.out.println("Items in category '" + category + "':");
            for (Item item : filteredItems) {
                System.out.println(item.toString());
            }
        }
    }

    public void sortByPrice(boolean ascending) {
        List<Item> sortedItems = new ArrayList<>(Menu.menu);

        if (ascending) {
            sortedItems.sort((item1, item2) -> Double.compare(item1.getPrice(), item2.getPrice()));
        } else {
            sortedItems.sort((item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));
        }

        System.out.println("Items sorted by price (" + (ascending ? "Ascending" : "Descending") + "):");
        for (Item item : sortedItems) {
            System.out.println(item.toString());
        }
    }

    public String getName() {
        return name;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public double getMoney() {
        return money;
    }

    public boolean hasEnoughMoney(double amount) {
        return money >= amount;
    }

    public void deductMoney(double amount) {
        if (hasEnoughMoney(amount)) {
            money -= amount;
            System.out.println(this.getName() +" has left Rs."+ this.money);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public void cancelOrder(Order order, Admin admin) {
        if (order.getStatus() == Order.OrderStatus.PLACED) {
            Main.pendingOrders.remove(order);

            Main.cancelledOrders.add(order);

            order.cancelOrder(order,admin);
            System.out.println("Order ID: " + order.getOrderId() + " has been canceled.");

        } else {
            System.out.println("Order " + order.getOrderId() + " cannot be canceled at this stage.");
        }
    }



    public void viewOrderHistory() {
        if (orderHistory == null ||orderHistory.isEmpty()) {
            System.out.println("No past orders available.");
        } else {
            System.out.println("Order History:");
            for (Order order : orderHistory) {
                System.out.println(order);
            }
        }
    }

    public void viewItemReviews(Item item) {
        List<String> reviews = item.getReviews();
        if (reviews.isEmpty()) {
            System.out.println("No reviews available for item: " + item.getName());
        } else {
            System.out.println("Reviews for " + item.getName() + ":");
            for (String review : reviews) {
                System.out.println("- " + review);
            }
        }

    }

    public void reviewItem(Item item, String review) {
        item.addReview(review);
        System.out.println("Review added for " + item.getName() + ": " + review);
    }


    public void reorder(String pastOrderId) {

        if (orderHistory == null || orderHistory.isEmpty()) {
            System.out.println("No past orders to reorder.");
            return;
        }

        Order pastOrder = null;
        for (Order order : orderHistory) {
            if (order.getOrderId().equals(pastOrderId)) {
                pastOrder = order;
                break;
            }
        }

        if (pastOrder == null) {
            System.out.println("Order ID " + pastOrderId + " not found in your order history.");
            return;
        }

        String newOrderId = pastOrderId + "1";


        Order newOrder = new Order(newOrderId, this, this.isVIP, pastOrder.getDetails(), pastOrder.getItems(), pastOrder.getTotal());

        orderHistory.add(newOrder);
        Main.pendingOrders.add(newOrder);

        System.out.println("Reordered successfully with new Order ID: " + newOrderId);
    }

    public void addToBalance(double amount) {
        if (amount > 0) {
            this.money += amount;
        }
    }

    public Cart getCart(){
        return this.cart;
    }

    public void saveOrderHistoryToFile() {
        String filename = name + "_order_history.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Order order : orderHistory) {
                writer.write(order.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving order history for customer " + name + ": " + e.getMessage());
        }
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
        saveOrderHistoryToFile();
    }






}
