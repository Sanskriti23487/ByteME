import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Menu {
    public static List<Item> menu = new ArrayList<>();

    static {
        menu.add(new Item("Burger", 50, Item.Category.SNACKS, 5));
        menu.add(new Item("Pizza", 100, Item.Category.MEALS, 6));
        menu.add(new Item("Coke", 40, Item.Category.BEVERAGES, 4));
        menu.add(new Item("Pasta", 65, Item.Category.MEALS, 6));
        menu.add(new Item("Coffee", 30, Item.Category.BEVERAGES, 3));
        menu.add(new Item("Sandwich", 45, Item.Category.SNACKS, 5));
        menu.add(new Item("French Fries", 40, Item.Category.APPETIZERS, 2));


    }

    public static Item findItemInMenu(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        for (Item item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        throw new RuntimeException("Item '" + itemName + "' not found in the menu.");
    }


    public static void saveMenuToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("menu.txt"))) {
            for (Item item : menu) {
                String line = item.getName() + "|" +
                        item.getPrice() + "|" +
                        item.getCategory() + "|" +
                        item.isAvailable() + "|" +
                        item.getQuantity();
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Menu saved to menu.txt.");
        } catch (IOException e) {
            System.err.println("Error saving menu: " + e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    public static void loadMenuFromFile() {
        menu.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("menu.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                Item.Category category = Item.Category.valueOf(parts[2]);
                boolean isAvailable = Boolean.parseBoolean(parts[3]);
                int quantity = Integer.parseInt(parts[4]);

                Item item = new Item(name, price, category, quantity);
                item.updateAvailability(isAvailable); // Ensure availability is accurate
                menu.add(item);
            }
            System.out.println("Menu loaded from menu.txt.");
        } catch (IOException e) {
            System.err.println("Error loading menu: " + e.getMessage());
        }
    }



}
