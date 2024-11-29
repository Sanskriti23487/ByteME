import java.io.*;
import java.util.*;

public class OrderFileManager {
    private static final String ORDER_HISTORY_DIR = "order_histories";

    static {
        File dir = new File(ORDER_HISTORY_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void saveOrderHistory(Customer customer, Order order) {
        String fileName = ORDER_HISTORY_DIR + "/" + customer.getName() + "_orders.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) { // Enable append mode
            writer.write(order.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving order history: " + e.getMessage());
        }
    }

    public static List<String> loadOrderHistory(Customer customer) {
        String fileName = ORDER_HISTORY_DIR + "/" + customer.getName() + "_orders.txt";
        List<String> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orders.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No order history found for user: " + customer.getName());
        } catch (IOException e) {
            System.err.println("Error loading order history: " + e.getMessage());
        }
        return orders;
    }




}
