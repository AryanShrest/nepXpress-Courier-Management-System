package com.courier.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

public class ViewData extends JFrame {
    private JComboBox<String> tableSelector;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;

    public ViewData() {
        setTitle("View Database Records");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setupUI();
        loadTableNames();
    }

    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table selector panel
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Select Table:");
        tableSelector = new JComboBox<>();
        tableSelector.setPreferredSize(new Dimension(200, 30));
        selectorPanel.add(label);
        selectorPanel.add(tableSelector);

        // Table
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);

        // Add components to main panel
        mainPanel.add(selectorPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add action listener to table selector
        tableSelector.addActionListener(e -> loadTableData());

        setContentPane(mainPanel);
    }

    private void loadTableNames() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SHOW TABLES FROM cms";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    tableSelector.addItem(rs.getString(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading table names: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData() {
        String selectedTable = (String) tableSelector.getSelectedItem();
        if (selectedTable == null) return;

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM " + selectedTable;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData metaData = rs.getMetaData();
                
                // Clear existing data
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);

                // Set column names
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    tableModel.addColumn(metaData.getColumnName(i));
                }

                // Add data rows
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading table data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ViewData().setVisible(true);
        });
    }
} 