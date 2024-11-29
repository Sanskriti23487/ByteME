import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class LoginOrderTest {

    @Test
    public void testCustomerLoginandCart() {
        System.out.println("Starting Customer Login Test...");

        Customer testCustomer = new Customer("JohnDoe", "password123");
        CustomerFileManager.saveCustomer(testCustomer);
        System.out.println("Saved test customer: " + testCustomer.getName());

        List<Customer> allCustomers = CustomerFileManager.loadCustomers();
        System.out.println("Loaded customers from file. Total customers: " + allCustomers.size());
        Customer existingCustomer = null;

        for (Customer c : allCustomers) {
            if (c.getName().equalsIgnoreCase("JohnDoe")) {
                existingCustomer = c;
                break;
            }
        }

        System.out.println("Validating customer existence...");
        assertNotNull(existingCustomer, "Customer should exist.");
        System.out.println("Validating correct password...");
        assertTrue(existingCustomer.validatePassword("password123"), "Password should match for successful login.");
        System.out.println("Validating incorrect password...");
        assertFalse(existingCustomer.validatePassword("wrongPassword"), "Password should not match for unsuccessful login.");

        System.out.println("Customer Login Test Passed!");


        System.out.println("------------------------");


        Cart cart = testCustomer.getCart();

        String itemName = "Pizza";
        System.out.println("Attempting to find item '" + itemName + "' in the menu...");
        Item item = Menu.findItemInMenu(itemName);
        assertNotNull(item, "Item '" + itemName + "' exists in the menu.");

        System.out.println("Attempting to add '" + item.getName() + "' to the cart with quantity 5...");
        assertDoesNotThrow(() -> cart.addItem(item, 5), "Item should be added to the cart successfully.");

        System.out.println("------------------------");

        String itemName2 = "Chowmein";
        System.out.println("Attempting to find non-existent item '" + itemName2 + "'...");
        RuntimeException itemNotFoundException = assertThrows(RuntimeException.class, () -> {
            Menu.findItemInMenu(itemName2);
        });
        assertEquals("Item 'Chowmein' not found in the menu.", itemNotFoundException.getMessage());

        String itemName3 = "Burger";
        System.out.println("Attempting to find item '" + itemName3 + "' in the menu...");
        Item item3 = Menu.findItemInMenu(itemName3);
        assertNotNull(item3, "Item '" + itemName3 + "' exists in the menu.");

        System.out.println("Attempting to add '" + item3.getName() + "' to the cart with quantity 30...");
        RuntimeException insufficientStockException = assertThrows(RuntimeException.class, () -> {
            cart.addItem(item3, 30);
        });
        assertEquals("Insufficient quantity available for 'Burger'. Requested: 30, Available: " + item3.getQuantity(),
                insufficientStockException.getMessage());

        System.out.println("Test for handling unavailable or insufficient items passed!");
    }
}



