import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private String orderId;
    private Customer customerName;
    private boolean isVIP;
    private String details;
    private Map<Item, Integer> items;
    private OrderStatus status;
    private Double total;
    private LocalDateTime orderDate;

    public Order(String orderId, Customer customerName, boolean isVIP, String details, Map<Item, Integer> items, Double total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.isVIP = isVIP;
        this.details = details;
        this.items = items;
        this.status = OrderStatus.PLACED;
        this.total = total;
        this.orderDate = LocalDateTime.now();
    }

    public enum OrderStatus {
        PLACED,
        ACCEPTED,
        PREPARING,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED,
        DENIED
    }

    public boolean isVIP() {
        return isVIP;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomerName() {
        return customerName;
    }

    public String getDetails() {
        return details;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void addItems(Item item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void updateStatus(OrderStatus newStatus) {
        if (this.status != OrderStatus.CANCELLED) {
            this.status = newStatus;
        } else {
            System.out.println("Cannot update a cancelled order.");
        }
    }

    public Double getTotal() {
        return total;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getFormattedOrderDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return orderDate.format(formatter);
    }

    public void cancelOrder(Order order, Admin admin) {
        order.updateStatus(OrderStatus.CANCELLED);

        Customer customer = order.getCustomerName();
        double refundAmount = order.getTotal();
        customer.addToBalance(refundAmount);
        System.out.println("Refund of Rs " + refundAmount + " has been added back to " + customer.getName() + "'s account.");

        admin.deductFromBank(refundAmount);

        for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            item.updateQuantity(item.getQuantity() + quantity);

            if (item.getQuantity() > 0) {
                item.updateAvailability(true);
            }
        }

        Main.cancelledOrders.add(order);
        Main.pendingOrders.remove(order);

        System.out.println("Order #" + order.getOrderId() + " has been cancelled.");
    }

    public String getItemsString() {
        StringBuilder itemsString = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            itemsString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(", ");
        }
        if (itemsString.length() > 0) {
            itemsString.setLength(itemsString.length() - 2); // Remove trailing comma and space
        }
        return itemsString.toString();
    }



    @Override
    public String toString() {
        StringBuilder itemsString = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            itemsString.append(entry.getKey().getName())
                    .append(":")
                    .append(entry.getValue())
                    .append(","); // Name:Quantity format
        }
        if (itemsString.length() > 0) {
            itemsString.deleteCharAt(itemsString.length() - 1); // Remove trailing comma
        }


        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName.getName() + '\'' +
                ", isVIP=" + isVIP +
                ", Special Request='" + details + '\'' +
                ", items={" + itemsString + '}' +
                ", status=" + status +
                ", orderDate='" + getFormattedOrderDate() + '\'' +
                '}';
    }


}
