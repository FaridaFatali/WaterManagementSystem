package org.example;

import java.sql.*;
import javax.swing.*;
import java.util.Vector;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.table.DefaultTableModel;

public class Water {
    private JTable table1;
    JTextField nameData = new JTextField();
    JTextField locationData = new JTextField();
    JComboBox<String> litres = new JComboBox<>();
    JComboBox<String> quantity = new JComboBox<>();
    JFrame waterF = new JFrame();

    public Water() {
        waterF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel waterPanel = new JPanel();
        waterF.setContentPane(waterPanel);
        waterF.pack();
        waterF.setLocationRelativeTo(null);
        waterF.setVisible(true);

        tableData();

        JButton addRecordButton = new JButton("Add Record");
        addRecordButton.addActionListener(e -> {
            if (nameData.getText().equals("") || locationData.getText().equals("") || litres.getSelectedItem() == null || quantity.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Please Fill All Fields to add Record.");
            } else {
                try {
                    String sql = "insert into water" + "(NAME, LOCATION, LITRES, QUANTITY, PRICE)" + "values (?, ?, ?, ?, ?)";

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern", "root", "root");
                    PreparedStatement statement = connection.prepareStatement(sql);

                    int price = Integer.parseInt("" + litres.getSelectedItem()) * Integer.parseInt("" + quantity.getSelectedItem()) * 10;
                    statement.setString(1, nameData.getText());
                    statement.setString(2, locationData.getText());
                    statement.setString(3, "" + litres.getSelectedItem());
                    statement.setString(4, "" + quantity.getSelectedItem());
                    statement.setInt(5, price);

                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "ITEM ADDED SUCCESSFULLY!");
                    nameData.setText("");
                    locationData.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }

                tableData();
            }
        });

        JButton updateRecordButton = new JButton("Update Record");
        updateRecordButton.addActionListener(e -> {
            try {
                int price = Integer.parseInt("" + litres.getSelectedItem()) * Integer.parseInt("" + quantity.getSelectedItem()) * 10;
                String sql = "UPDATE water " +
                        "SET LOCATION = " + locationData.getText() + "', LITRES='" + litres.getSelectedItem() +
                        "', QUANTITY='" + quantity.getSelectedItem() + "', PRICE=" + price +
                        " WHERE NAME= '" + nameData.getText() + "'";
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern", "root", "root");
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Updated successfully!");
            } catch (Exception ex1) {
                System.out.println(ex1);
            }
            tableData();
        });

        JTable table1 = new JTable();
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel dm = (DefaultTableModel) table1.getModel();
                int selectedRow = table1.getSelectedRow();
                nameData.setText(dm.getValueAt(selectedRow, 0).toString());
                locationData.setText(dm.getValueAt(selectedRow, 1).toString());
            }
        });
    }

    public void tableData() {
        try {
            String a = "Select* from water";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost3306/intern", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(a);
            table1.setModel(buildTableModel(rs));
        } catch (Exception ex2) {
            JOptionPane.showMessageDialog(null, ex2.getMessage());
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();

        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
