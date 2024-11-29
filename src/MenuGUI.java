import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuGUI {
    public MenuGUI() {
        Menu.loadMenuFromFile();

        JFrame frame = new JFrame("Canteen Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);

        List<Item> menu = Menu.menu;

        String[] columnNames = {"Name", "Price", "Category", "Availability"};

        String[][] data = new String[menu.size()][4];
        for (int i = 0; i < menu.size(); i++) {
            Item item = menu.get(i);
            data[i][0] = item.getName();
            data[i][1] = String.format("%.2f", item.getPrice());
            data[i][2] = item.getCategory().toString();
            data[i][3] = item.isAvailable() ? "Available" : "Not Available";
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);

        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel byteMeLabel = new JLabel("Byte Me - Canteen Menu", SwingConstants.CENTER);
        byteMeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(byteMeLabel, BorderLayout.NORTH);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton switchToOrdersButton = new JButton("View Pending Orders");
        switchToOrdersButton.addActionListener(e -> {
            frame.dispose();
            new PendingOrdersGUI();
        });
        mainPanel.add(switchToOrdersButton, BorderLayout.SOUTH);

        frame.add(mainPanel);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MenuGUI();
    }
}
