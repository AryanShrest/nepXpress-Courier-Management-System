/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.courier.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 *
 * @author kabin
 */
public class TrackParcelsFrame extends JFrame {

    private JTextField trackingField;
    private JButton searchButton;
    private JPanel resultPanel;
    private Connection conn;

    /**
     * Creates new form java
     */
    public TrackParcelsFrame() {
        setTitle("nepXpress — Track Parcels");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setupUI();
    }

    private void setupUI() {
        // Main container with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(16, 25, 16, 25));

        JLabel headerLabel = new JLabel("Track Parcels");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(headerLabel);
        
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(200, 200, 200));
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(separator);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel trackingLabel = new JLabel("Enter Tracking Number:");
        trackingLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        trackingField = new JTextField(30);
        trackingField.setPreferredSize(new Dimension(trackingField.getPreferredSize().width, 35));

        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(66, 133, 244));
        searchButton.setForeground(Color.WHITE);
        searchButton.setPreferredSize(new Dimension(100, 35));
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);

        searchPanel.add(trackingLabel);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(trackingField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchButton);

        // Add search panel to content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        // Result panel
        resultPanel = new JPanel();
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(new EmptyBorder(0, 25, 25, 25));
        contentPanel.add(resultPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add search functionality
        searchButton.addActionListener(e -> {
            String trackingNumber = trackingField.getText().trim();
            if (!trackingNumber.isEmpty()) {
                searchParcel(trackingNumber);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please enter a tracking number",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        setContentPane(mainPanel);
    }

    private void searchParcel(String trackingNumber) {
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                // Query for parcel details
                String query = "SELECT p.*, u.first_name, u.last_name, " +
                             "CONCAT(u.first_name, ' ', u.last_name) as sender_name " +
                             "FROM parcels p " +
                             "JOIN users u ON p.sender_id = u.user_id " +
                             "WHERE p.tracking_number = ?";
                
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, trackingNumber);
                ResultSet rs = pstmt.executeQuery();

                // Clear previous results
                resultPanel.removeAll();
                resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

                if (rs.next()) {
                    // Create a panel for parcel details
                    JPanel detailsPanel = new JPanel();
                    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                    detailsPanel.setBackground(Color.WHITE);
                    detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                    ));

                    // Add parcel details
                    addDetailRow(detailsPanel, "Tracking Number", rs.getString("tracking_number"));
                    addDetailRow(detailsPanel, "Sender", rs.getString("sender_name"));
                    addDetailRow(detailsPanel, "Receiver", rs.getString("receiver_name"));
                    addDetailRow(detailsPanel, "Status", rs.getString("status"));
                    addDetailRow(detailsPanel, "Weight", rs.getString("weight") + " kg");

                    resultPanel.add(detailsPanel);
                    resultPanel.add(Box.createVerticalStrut(20));

                    // Add tracking history
                    addTrackingHistory(trackingNumber);
                } else {
                    JLabel noResultLabel = new JLabel("No parcel found with tracking number: " + trackingNumber);
                    noResultLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    resultPanel.add(noResultLabel);
                }

                rs.close();
                pstmt.close();
                resultPanel.revalidate();
                resultPanel.repaint();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error searching parcel: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(Color.WHITE);
        
        JLabel labelLbl = new JLabel(label + ": ");
        labelLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelLbl.setPreferredSize(new Dimension(120, 20));
        
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        row.add(labelLbl);
        row.add(valueLbl);
        panel.add(row);
        panel.add(Box.createVerticalStrut(10));
    }

    private void addTrackingHistory(String trackingNumber) throws SQLException {
        String query = "SELECT * FROM tracking_history " +
                      "WHERE parcel_id = (SELECT parcel_id FROM parcels WHERE tracking_number = ?) " +
                      "ORDER BY created_at DESC";
        
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, trackingNumber);
        ResultSet rs = pstmt.executeQuery();

        // Create tracking history panel
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel historyLabel = new JLabel("Tracking History");
        historyLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        historyPanel.add(historyLabel);
        historyPanel.add(Box.createVerticalStrut(15));

        boolean hasHistory = false;
        while (rs.next()) {
            hasHistory = true;
            JPanel historyRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            historyRow.setBackground(Color.WHITE);
            
            JLabel dateLabel = new JLabel(rs.getTimestamp("created_at").toString());
            dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            JLabel statusLabel = new JLabel(rs.getString("status"));
            statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            JLabel locationLabel = new JLabel(rs.getString("location"));
            locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            historyRow.add(dateLabel);
            historyRow.add(Box.createHorizontalStrut(20));
            historyRow.add(statusLabel);
            historyRow.add(Box.createHorizontalStrut(20));
            historyRow.add(locationLabel);
            
            historyPanel.add(historyRow);
            historyPanel.add(Box.createVerticalStrut(10));
        }

        if (!hasHistory) {
            JLabel noHistoryLabel = new JLabel("No tracking history available");
            noHistoryLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            historyPanel.add(noHistoryLabel);
        }

        rs.close();
        pstmt.close();
        resultPanel.add(historyPanel);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            new TrackParcelsFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
