import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.List;

public class OrderFiles {

    public static void saveOrdersToFile(PriorityQueue<Order> pendingOrders, List<Order> processingOrders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pendingOrders.txt"))) {
            for (Order order : pendingOrders) {
                writer.write(order.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to pendingOrders.txt: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("processingOrders.txt"))) {
            for (Order order : processingOrders) {
                writer.write(order.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to processingOrders.txt: " + e.getMessage());
        }
    }

    public static List<String> readOrdersFromFile(String filename) {
        List<String> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orders.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from " + filename + ": " + e.getMessage());
        }
        return orders;
    }
}
