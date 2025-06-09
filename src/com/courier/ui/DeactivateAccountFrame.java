package com.courier.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DeactivateAccountFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextArea reasonArea;
    private JButton deactivateButton;

    public DeactivateAccountFrame() {
        initComponents();
        setupUI();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void setupUI() {
        // Main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(16, 25, 16, 25));

        JLabel deactivateTitle = new JLabel("Deactivate Account");
        deactivateTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        deactivateTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(deactivateTitle);
        
        JSeparator line1 = new JSeparator();
        line1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line1.setForeground(new Color(200, 200, 200));
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(line1);
        
        JSeparator line2 = new JSeparator();
        line2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line2.setForeground(new Color(200, 200, 200));
        headerPanel.add(Box.createVerticalStrut(2));
        headerPanel.add(line2);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Warning message
        JLabel warningLabel = new JLabel("Warning: This action cannot be undone!");
        warningLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        warningLabel.setForeground(Color.RED);
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(warningLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Form fields
        emailField = createFormField("Email*");
        passwordField = createPasswordField("Password*");
        
        // Reason text area
        reasonArea = new JTextArea();
        reasonArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        reasonArea.setText("Reason for deactivation*");
        reasonArea.setForeground(Color.GRAY);
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Deactivate Button
        deactivateButton = new JButton("Deactivate Account");
        deactivateButton.setBackground(new Color(220, 53, 69)); // Red color
        deactivateButton.setForeground(Color.WHITE);
        deactivateButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        deactivateButton.setMaximumSize(new Dimension(200, 40));
        deactivateButton.setBorderPainted(false);
        deactivateButton.setFocusPainted(false);
        deactivateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add components to content panel
        contentPanel.add(emailField);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(passwordField);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(scrollPane);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(deactivateButton);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Add action listener
        deactivateButton.addActionListener(e -> handleDeactivate());
    }

    private JTextField createFormField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setEchoChar((char) 0); // Show placeholder text
        return field;
    }

    private void handleDeactivate() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String reason = reasonArea.getText();

        if (email.isEmpty() || email.equals("Email*") ||
            password.isEmpty() || password.equals("Password*") ||
            reason.isEmpty() || reason.equals("Reason for deactivation*")) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to deactivate your account? This action cannot be undone.",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Here you would implement the actual account deactivation logic
                JOptionPane.showMessageDialog(this,
                    "Your account has been deactivated. You will be logged out.",
                    "Account Deactivated",
                    JOptionPane.INFORMATION_MESSAGE);
                System.exit(0); // Close the application
            }
        }
    }

    public JPanel getMainPanel() {
        return (JPanel) getContentPane();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DeactivateAccountFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new DeactivateAccountFrame().setVisible(true);
        });
    }
}
