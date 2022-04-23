package billing_system;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class generateBill {
    private JComboBox<String> meterInput;
    private JComboBox<String> monthInput;
    private JPanel main;
    private JButton generateButton;
    private JButton printButton;
    private JTextArea billInfo;

    generateBill() {
        connectToMySQL getData = new connectToMySQL();
        ResultSet resultSet;

        try {
            resultSet = getData.statement.executeQuery("select * from customer_info");
            while (resultSet.next()) {
                meterInput.addItem(resultSet.getString("meter"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        generateButton.addActionListener(e -> {
            String month = (String) monthInput.getSelectedItem();
            String meter = (String) meterInput.getSelectedItem();
            billInfo.setText("Symbiosis Power Limited\nELECTRICITY BILL FOR THE MONTH OF " + month + ", 2022\n\n\n");
            try {
                ResultSet getInfo = getData.statement.executeQuery("select * from customer_info where meter=" + meter);
                if (getInfo.next()) {
                    billInfo.append("Customer Name\t:\t" + getInfo.getString("name") + "\n");
                    billInfo.append("Meter Number\t:\t" + getInfo.getString("meter") + "\n");
                    billInfo.append("Address\t\t:\t" + getInfo.getString("address") + "\n");
                    billInfo.append("City\t\t:\t" + getInfo.getString("city") + "\n");
                    billInfo.append("State\t\t:\t" + getInfo.getString("state") + "\n");
                    billInfo.append("E-Mail\t\t:\t" + getInfo.getString("email") + "\n");
                    billInfo.append("Phone Number\t:\t" + getInfo.getString("phone") + "\n");
                    billInfo.append("\n\n\n");
                }

                getInfo = getData.statement.executeQuery("select * from tax");
                if (getInfo.next()) {
                    billInfo.append("Meter Location\t:\t" + getInfo.getString("meter_location") + "\n");
                    billInfo.append("Meter Type\t:\t" + getInfo.getString("meter_type") + "\n");
                    billInfo.append("Phase Code\t:\t" + getInfo.getString("phase_code") + "\n");
                    billInfo.append("Bill Type\t:\t" + getInfo.getString("bill_type") + "\n");
                    billInfo.append("Days\t\t:\t" + getInfo.getString("days") + "\n");
                    billInfo.append("\n\n\n");
                    billInfo.append("Meter Rent\t:\t" + getInfo.getString("meter_rent") + "\n");
                    billInfo.append("MCB Rent\t:\t" + getInfo.getString("mcb_rent") + "\n");
                    billInfo.append("Service Tax\t:\t" + getInfo.getString("service_rent") + "\n");
                    billInfo.append("GST@9%\t\t:\t" + getInfo.getString("gst") + "\n");
                    billInfo.append("\n\n\n");
                }

                getInfo = getData.statement.executeQuery("select * from bill where meter_number='" + meter + "' and month='" + month + "'");
                if (getInfo.next()) {
                    billInfo.append("Meter Number\t:\t" + getInfo.getString("meter_number") + "\n");
                    billInfo.append("Month\t\t:\t" + getInfo.getString("month") + "\n");
                    billInfo.append("Units Consumed\t:\t" + getInfo.getString("units") + "\n");
                    billInfo.append("Total Charges\t:\t" + getInfo.getString("amount") + "\n");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            billInfo.append("BILL GENERATED ON " + formatter.format(date));
        });
        printButton.addActionListener(e -> {
            try {
                billInfo.print();
            } catch (PrinterException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public static void drawWindow() {
        JFrame frame = new JFrame("Electricity Billing System");
        frame.setContentPane(new generateBill().main);
        frame.pack();
        frame.setSize(470, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Image imageIcon = Toolkit.getDefaultToolkit().getImage("./icons/lightning.png");
        frame.setIconImage(imageIcon);
    }
}
