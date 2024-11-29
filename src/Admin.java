import java.util.*;

public class Admin {

    private double bank;

    public Admin() {
        this.bank = 0.0;
    }


    public void addItemToMenu(Item item) {
        Menu.menu.add(item);
        System.out.println("Item added to menu: " + item.getName());
        Menu.saveMenuToFile();

    }

    public void removeItemFromMenu(Item item) {
        List<Order> ordersWithItem = new ArrayList<>();

        for (Order order : Main.pendingOrders) {
            if (order.getItems().containsKey(item)) {
                ordersWithItem.add(order);
            }
        }

        if (!ordersWithItem.isEmpty()) {
            System.out.println("The following orders containing " + item.getName() + " have been denied:");
            for (Order order : ordersWithItem) {
                order.cancelOrder(order, this);
                System.out.println("Order #" + order.getOrderId() + " denied.");
            }

        }

        // Remove the item from the menu
        Menu.menu.remove(item);
        System.out.println("Item removed from menu: " + item.getName());
        Menu.saveMenuToFile();
    }


    public void updateItemPrice(Item item, double newPrice) {
        item.updatePrice(newPrice);
        System.out.println("Updated price of " + item.getName() + " to " + newPrice);
        Menu.saveMenuToFile();
    }

    public void updateItemQuantity(Item item, int newQuantity) {
        item.updateQuantity(newQuantity);
        System.out.println("Updated quantity of " + item.getName() + " to " + newQuantity);
        Menu.saveMenuToFile();
    }

    public void makeItemUnavailable(Item item, boolean val) {
        item.updateAvailability(val);
        System.out.println( item.getName() + "is now unavaialbe");
        Menu.saveMenuToFile();


    }

    public void processOrder() {
        new Thread(() -> {
            Order currentOrder = Main.pendingOrders.poll();

            System.out.println(currentOrder.toString());


            try {
                if (currentOrder.getStatus() == Order.OrderStatus.PLACED) {
                    currentOrder.updateStatus(Order.OrderStatus.ACCEPTED);
                    System.out.println("Order " + currentOrder.getOrderId() + " has been accepted.");
                    Main.processingOrders.add(currentOrder);

                    Thread.sleep(30_000);

                    if (currentOrder.getStatus() == Order.OrderStatus.ACCEPTED) {
                        currentOrder.updateStatus(Order.OrderStatus.PREPARING);
                    }
                }

                if (currentOrder.getStatus() == Order.OrderStatus.PREPARING) {
                    Thread.sleep(30_000);

                    currentOrder.updateStatus(Order.OrderStatus.OUT_FOR_DELIVERY);
                }

                if (currentOrder.getStatus() == Order.OrderStatus.OUT_FOR_DELIVERY) {
                    Thread.sleep(30_000);

                    currentOrder.updateStatus(Order.OrderStatus.DELIVERED);
                    System.out.println("Order " + currentOrder.getOrderId() + " has been delivered.");
                    Main.completedOrders.add(currentOrder);
                    currentOrder.getCustomerName().addOrderToHistory(currentOrder);
                    OrderFileManager.saveOrderHistory(currentOrder.getCustomerName(), currentOrder);

                }

            } catch (InterruptedException e) {
                System.out.println("Order processing was interrupted.");

            }
        }
    ).start();
}

    public void CancelOrder() {
        Order currentOrder = Main.pendingOrders.poll();

        System.out.println(currentOrder.toString());
        if (currentOrder.getStatus() == Order.OrderStatus.PLACED) {
            currentOrder.updateStatus(Order.OrderStatus.DENIED);
            currentOrder.cancelOrder(currentOrder,this );
        }

    }


    public void viewPendingOrders() {
        if (Main.pendingOrders.isEmpty()) {
            System.out.println("No pending orders.");
            return;
        }

        System.out.println("Pending Orders:");
        System.out.println("------------------------------");
        for (Order order : Main.pendingOrders) {
            System.out.println(order.toString());
            System.out.println("------------------------------");
        }
    }

    public void addToBank(double amount) {
        bank += amount;
        System.out.println("Bank balance updated. Current balance: Rs" + bank);
    }

    public double getBankBalance() {
        return bank;
    }

    public void deductFromBank(double amount) {
        if (bank >= amount) {
            bank -= amount;
        }
    }


    public void generateDailySalesReport() {
        double totalSales = 0;
        int totalOrders = Main.processingOrders.size();
        Map<Item, Integer> itemPopularity = new HashMap<>();

        for (Order order : Main.processingOrders) {
            totalSales += order.getTotal();

            System.out.println("Order ID: " + order.getOrderId());
            Map<Item, Integer> itemsMap = order.getItems();

            for (Map.Entry<Item, Integer> entry : itemsMap.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();

                System.out.println("Item: " + item.getName() + ", Quantity: " + quantity);

                itemPopularity.put(item, itemPopularity.getOrDefault(item, 0) + quantity);
            }
        }

        System.out.println("Item Popularity Map: ");
        for (Map.Entry<Item, Integer> entry : itemPopularity.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
        }


        Item mostPopularItem = null;
        int maxCount = 0;
        for (Map.Entry<Item, Integer> entry : itemPopularity.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPopularItem = entry.getKey();
            }
        }

        System.out.println("\n--- Daily Sales Report ---");
        System.out.println("Total Orders Processed: " + totalOrders);
        System.out.println("Total Sales: Rs" + totalSales);
        System.out.println("Most Popular Item: " + (mostPopularItem != null ? mostPopularItem.getName() : "None") +
                " (" + maxCount + " times)");
        System.out.println("------------------------------");
    }

    public void clearDailyData(){
        Main.processingOrders.clear();

    }

}

