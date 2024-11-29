import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PendingOrdersGUI {
    public PendingOrdersGUI() {
        JFrame frame = new JFrame("Pending and Processing Orders");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());


        String[] columnNames = {"Order ID", "Customer Name", "VIP Status", "Special Request", "Status", "Order Date"};
        List<String> pendingOrders = OrderFiles.readOrdersFromFile("pendingOrders.txt");
        List<String> processingOrders = OrderFiles.readOrdersFromFile("processingOrders.txt");


        int totalRows = pendingOrders.size() + processingOrders.size();
        Object[][] data = new Object[totalRows][7];

        int row = 0;

        for (String order : pendingOrders) {
            String[] orderDetails = parseOrderDetails(order);
            data[row] = new Object[]{
                    orderDetails[0], // Order ID
                    orderDetails[1], // Customer Name
                    orderDetails[2], // VIP Status
                    orderDetails[3], // Special Request
                    "Pending",       // Status
                    orderDetails[5]  // Order Date
            };
            row++;
        }

        for (String order : processingOrders) {
            String[] orderDetails = parseOrderDetails(order);
            data[row] = new Object[]{
                    orderDetails[0], // Order ID
                    orderDetails[1], // Customer Name
                    orderDetails[2], // VIP Status
                    orderDetails[3], // Special Request
                    "Processing",    // Status
                    orderDetails[5]  // Order Date
            };
            row++;
        }

        JTable orderTable = new JTable(data, columnNames);
        orderTable.setFont(new Font("Arial", Font.PLAIN, 14));
        orderTable.setRowHeight(25);
        orderTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(orderTable);

        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.setFont(new Font("Arial", Font.BOLD, 14));
        backToMenuButton.addActionListener(e -> {
            frame.dispose();
            new MenuGUI();
        });


        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backToMenuButton, BorderLayout.SOUTH);


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private String[] parseOrderDetails(String order) {

        String[] parts = new String[6];

        parts[0] = extractValue(order, "orderId");          // Order ID
        parts[1] = extractValue(order, "customerName");     // Customer Name
        parts[2] = extractValue(order, "isVIP");            // VIP Status
        parts[3] = extractValue(order, "Special Request");  // Special Request
        parts[4] = extractValue(order, "items");            // Items
        parts[5] = extractValue(order, "orderDate");        // Order Date

        return parts;
    }


    private String extractValue(String order, String field) {
        String pattern = field + "='(.*?)'";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(order);
        return matcher.find() ? matcher.group(1) : "N/A";
    }
}
