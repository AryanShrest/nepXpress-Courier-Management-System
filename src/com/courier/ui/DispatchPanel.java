package com.courier.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DispatchPanel extends JPanel {
    private static final Color ORANGE_BTN = new Color(0xE67E22);
    private static final Color MAIN_BG = Color.WHITE;
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);

    // Form fields
    private JTextField nameField, addressField, phoneField;
    private JTextField courierWeightField, courierTypeField, amountField;

    public DispatchPanel() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MAIN_BG);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Dispatch");
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));
        
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setBackground(MAIN_BG);
        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(LABEL_FONT);
        JTextField dateField = new JTextField(10);
        dateField.setText("4/22/2025");
        dateField.setFont(LABEL_FONT);
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        datePanel.add(dateLabel);
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(dateField);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(datePanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(MAIN_BG);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(MAIN_BG);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Left column
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridy = 1;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridy = 2;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        // Left input fields
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.gridy = 0;
        nameField = createStyledTextField(20);
        formPanel.add(nameField, gbc);
        gbc.gridy = 1;
        addressField = createStyledTextField(20);
        formPanel.add(addressField, gbc);
        gbc.gridy = 2;
        phoneField = createStyledTextField(20);
        formPanel.add(phoneField, gbc);
        
        // Right column
        gbc.gridx = 2; gbc.weightx = 0.0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Courier Weight:"), gbc);
        gbc.gridy = 1;
        formPanel.add(new JLabel("Courier Type:"), gbc);
        gbc.gridy = 2;
        formPanel.add(new JLabel("Amount:"), gbc);
        
        // Right input fields
        gbc.gridx = 3; gbc.weightx = 1.0;
        gbc.gridy = 0;
        courierWeightField = createStyledTextField(20);
        formPanel.add(courierWeightField, gbc);
        gbc.gridy = 1;
        courierTypeField = createStyledTextField(20);
        formPanel.add(courierTypeField, gbc);
        gbc.gridy = 2;
        amountField = createStyledTextField(20);
        formPanel.add(amountField, gbc);
        
        // Create button
        JPanel createButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createButtonPanel.setBackground(MAIN_BG);
        createButtonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JButton createBtn = new JButton("Create");
        createBtn.setBackground(ORANGE_BTN);
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(LABEL_FONT);
        createBtn.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        createBtn.setFocusPainted(false);
        createBtn.addActionListener(e -> {
            if (validateForm()) {
                JOptionPane.showMessageDialog(this, 
                    "Dispatch created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        });
        createButtonPanel.add(createBtn);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(MAIN_BG);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(createButtonPanel, BorderLayout.SOUTH);
        
        container.add(centerPanel, BorderLayout.CENTER);
        return container;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }

    private boolean validateForm() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        String amountText = amountField.getText().trim();
        
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields (Name, Address, Phone)",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!amountText.isEmpty()) {
            try {
                Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid amount",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        return true;
    }

    private void clearFields() {
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        courierWeightField.setText("");
        courierTypeField.setText("");
        amountField.setText("");
    }
} 