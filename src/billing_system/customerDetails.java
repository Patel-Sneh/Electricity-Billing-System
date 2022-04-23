package billing_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class customerDetails {
    DefaultTableModel defaultTableModel = new DefaultTableModel();
    String[] header = new String[]{"Customer Name", "Meter Number", "Address", "City", "State", "E-Mail", "Phone Number"};
    String[] data = new String[7];
    private JPanel main;
    private JButton printButton;
    private JTable table;

    public customerDetails() {
        defaultTableModel.setColumnIdentifiers(header);
        table.setModel(defaultTableModel);
        connectToMySQL connection = new connectToMySQL();
        try {
            ResultSet resultSet = connection.statement.executeQuery("select * from customer_info");
            while (resultSet.next()) {
                data[0] = resultSet.getString("name");
                data[1] = resultSet.getString("meter");
                data[2] = resultSet.getString("address");
                data[3] = resultSet.getString("city");
                data[4] = resultSet.getString("state");
                data[5] = resultSet.getString("email");
                data[6] = resultSet.getString("phone");
                defaultTableModel.addRow(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printButton.requestFocus();
        printButton.addActionListener(e -> {
            try {
                table.print();
            } catch (PrinterException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public static void drawWindow() {
        JFrame frame = new JFrame("Customer Details");
        frame.setContentPane(new customerDetails().main);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Image imageIcon = Toolkit.getDefaultToolkit().getImage("./icons/lightning.png");
        frame.setIconImage(imageIcon);
    }
}
