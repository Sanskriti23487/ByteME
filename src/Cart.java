import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Item, Integer> Cartitems;
    private String specialInstructions;
    private static int orderCounter = 1;

    public Cart() {
        this.Cartitems = new HashMap<>();
        this.specialInstructions = "";
    }

    public boolean addItem(Item item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (!item.isAvailable()) {
            throw new RuntimeException("Item '" + item.getName() + "' is not available.");
        }
        if (item.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient quantity available for '" + item.getName() + "'. Requested: "
                    + quantity + ", Available: " + item.getQuantity());
        }
        Cartitems.put(item, quantity);
        item.decrementQuantity(quantity);
        System.out.println("Added " + quantity + " of " + item.getName() + " to cart.");
        return true;
    }



    public void modifyItemQuantity(Item item, int newQuantity) {
        if (Cartitems.containsKey(item)) {
            int currentQuantity = Cartitems.get(item);
            if (newQuantity > 0 && item.getQuantity() + currentQuantity >= newQuantity) {
                item.decrementQuantity(currentQuantity - newQuantity);
                Cartitems.put(item, newQuantity);
                System.out.println("Updated " + item.getName() + " quantity to " + newQuantity + " in cart.");
            } else {
                System.out.println("Not enough stock.");
            }
        } else {
            System.out.println("Item is not in the cart.");
        }
    }

    public void removeItem(Item item) {
        if (Cartitems.containsKey(item)) {
            int quantity = Cartitems.remove(item);
            item.incrementQuantity(quantity);
            System.out.println("Removed " + item.getName() + " from cart.");
        } else {
            System.out.println("Item is not in the cart.");
        }
    }

    public double viewTotal() {
        double total = 0.0;
        for (Map.Entry<Item, Integer> entry : Cartitems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            total += item.getPrice() * quantity;
        }
        System.out.println("Total Price: Rs" + total);
        return total;
    }

    public void addSpecialInstructions(String instructions) {
        this.specialInstructions = instructions;
        System.out.println("Special instructions added: " + instructions);
    }

    public void viewCart() {
        if (Cartitems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Cart items:");
        for (Map.Entry<Item, Integer> entry : Cartitems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(item.getName() + " - Quantity: " + quantity + " - Price: Rs" + item.getPrice() * quantity);
        }

        System.out.println("Special Instructions: " + specialInstructions);
    }

    public Order checkout(boolean isVIP, Customer customer, Admin admin) {
        double total = viewTotal();
        if (total == 0) {
            System.out.println("Your cart is empty. Please add items to checkout.");
            return null;
        }

        if (!customer.hasEnoughMoney(total)) {
            System.out.println("Insufficient funds. Cannot proceed with checkout.");
            return null;
        }

        customer.deductMoney(total);

        admin.addToBank(total);

        String orderId = String.format("%03d", orderCounter++);
        Order order = new Order(orderId, customer, isVIP, specialInstructions, new HashMap<>(Cartitems), total);


        Main.pendingOrders.add(order);




        Cartitems.clear();
        specialInstructions = "";
        System.out.println("Order placed successfully. Order ID: " + orderId);
        return order;
    }




}
