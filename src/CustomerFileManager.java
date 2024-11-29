import java.io.*;
import java.util.*;

public class CustomerFileManager {
    private static final String FILE_NAME = "customers.txt";

    public static void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(customer.getName() + "," + customer.getPassword());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to customer file: " + e.getMessage());
        }
    }

    public static List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    String password = parts[1];
                    customers.add(new Customer(name, password));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customer file: " + e.getMessage());
        }
        return customers;
    }
}
