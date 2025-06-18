package com.courier.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DispatchFrame.java — Dispatch Management Frame
 */
public class DispatchFrame extends JFrame {

    /* ─────  THEME  ───── */
    private static final Color HEADER_BG    = new Color(0x6D8CAE);
    private static final Color NAV_BG       = new Color(0x10273B);
    private static final Color NAV_SELECTED = new Color(0x4A78A4);
    private static final Color NAV_TEXT     = new Color(0xE7EFF7);
    private static final Color ORANGE_BTN   = new Color(0xE67E22);
    private static final Color MAIN_BG      = Color.WHITE;

    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 32);
    private static final Font NAV_FONT   = new Font("SansSerif", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);

    /* ─────  FIELDS  ───── */
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     mainView   = new JPanel(cardLayout);
    private final Map<String, NavItem> navButtons = new HashMap<>();
    
    // Form fields
    private JTextField nameField, addressField, phoneField;
    private JTextField courierWeightField, courierTypeField, amountField;
    
    // Database connection
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/cms?user=root";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Change as needed
    private static Connection connection = null;

    /* ─────  CONSTRUCTOR  ───── */
    public DispatchFrame() {
        setTitle("nepXpress — Dispatch");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        add(createMainView(), BorderLayout.CENTER);

        selectView("Dispatch");
    }

    private JComponent createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        JLabel title = new JLabel("nepXpress", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(TITLE_FONT);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(HEADER_BG);
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(NAV_SELECTED);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> handleLogout());
        rightPanel.add(logoutBtn);
        
        header.add(title, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            // TODO: Show login frame
        }
    }

    /* ───────────────── SIDEBAR  ───────────────── */
    private JComponent createSidebar() {
        JPanel side = new JPanel();
        side.setBackground(NAV_BG);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(220, 0));

        JLabel userLbl = new JLabel("  User");
        userLbl.setForeground(Color.WHITE);
        userLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        userLbl.setBorder(new EmptyBorder(16,16,32,0));
        side.add(userLbl);

        side.add(makeNav("Dashboard"));
        side.add(makeNav("Dispatch"));
        side.add(makeNav("Parcels"));
        side.add(makeNav("Track Parcels"));
        side.add(makeNav("Complaints"));
        side.add(makeNav("Contact"));

        side.add(Box.createVerticalGlue());
        return side;
    }

    private NavItem makeNav(String label) {
        NavItem item = new NavItem(label);
        navButtons.put(label, item);
        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectView(label);
            }
        });
        return item;
    }

    private void selectView(String label) {
        if (!label.equals("Dispatch")) {
            navigateToFrame(label);
        } else {
            cardLayout.show(mainView, label);
            navButtons.values().forEach(n -> n.setSelected(false));
            navButtons.get(label).setSelected(true);
        }
    }
    
    private void navigateToFrame(String frameName) {
        SwingUtilities.invokeLater(() -> {
            JFrame newFrame = null;
            switch (frameName) {
                case "Dashboard":
                    newFrame = new DashboardFrame();
                    break;
                case "Parcels":
                    newFrame = new ParcelsFrame();
                    break;
                case "Track Parcels":
                    newFrame = new TrackParcelsFrame();
                    break;
                case "Complaints":
                    newFrame = new ComplaintsFrame();
                    break;
                case "Contact":
                    newFrame = new ContactsFrame();
                    break;
            }
            
            if (newFrame != null) {
                newFrame.setVisible(true);
                this.dispose();
            }
        });
    }

    /* ───────────────── MAIN VIEW ───────────────── */
    private JComponent createMainView() {
        JPanel dispatchPanel = createDispatchPanel();
        mainView.add(dispatchPanel, "Dispatch");
        return mainView;
    }

    private JPanel createDispatchPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(MAIN_BG);

        // Title
        JLabel title = new JLabel("Create Dispatch");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(20, 20, 20, 20));
        container.add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(MAIN_BG);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Customer Details
        JPanel customerPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        customerPanel.setBackground(MAIN_BG);
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));

        nameField = createStyledTextField(20);
        addressField = createStyledTextField(20);
        phoneField = createStyledTextField(20);

        customerPanel.add(new JLabel("Name:"));
        customerPanel.add(nameField);
        customerPanel.add(new JLabel("Address:"));
        customerPanel.add(addressField);
        customerPanel.add(new JLabel("Phone:"));
        customerPanel.add(phoneField);

        // Courier Details
        JPanel courierPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        courierPanel.setBackground(MAIN_BG);
        courierPanel.setBorder(BorderFactory.createTitledBorder("Courier Details"));

        courierWeightField = createStyledTextField(20);
        courierTypeField = createStyledTextField(20);
        amountField = createStyledTextField(20);

        courierPanel.add(new JLabel("Weight (kg):"));
        courierPanel.add(courierWeightField);
        courierPanel.add(new JLabel("Type:"));
        courierPanel.add(courierTypeField);
        courierPanel.add(new JLabel("Amount:"));
        courierPanel.add(amountField);

        // Submit Button
        JButton submitButton = new JButton("Create Dispatch");
        submitButton.setBackground(ORANGE_BTN);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(new CreateDispatchListener());

        // Add components to form
        formPanel.add(customerPanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(courierPanel);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(submitButton);

        container.add(formPanel, BorderLayout.CENTER);
        return container;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    /* ───────────────── CREATE DISPATCH LISTENER ───────────────── */
    private class CreateDispatchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Validate input
            if (nameField.getText().trim().isEmpty() ||
                addressField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                courierWeightField.getText().trim().isEmpty() ||
                courierTypeField.getText().trim().isEmpty() ||
                amountField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(DispatchFrame.this,
                    "Please fill in all fields",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                if (connection != null) {
                    String query = "INSERT INTO dispatches (name, address, phone, courier_weight, courier_type, amount) VALUES (?, ?, ?, ?, ?, ?)";
                    
                    PreparedStatement pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, nameField.getText().trim());
                    pstmt.setString(2, addressField.getText().trim());
                    pstmt.setString(3, phoneField.getText().trim());
                    pstmt.setString(4, courierWeightField.getText().trim());
                    pstmt.setString(5, courierTypeField.getText().trim());
                    pstmt.setDouble(6, Double.parseDouble(amountField.getText().trim()));
                    
                    int result = pstmt.executeUpdate();
                    pstmt.close();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(DispatchFrame.this,
                            "Dispatch created successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(DispatchFrame.this,
                            "Failed to create dispatch",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(DispatchFrame.this,
                    "Please enter a valid amount",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(DispatchFrame.this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        courierWeightField.setText("");
        courierTypeField.setText("");
        amountField.setText("");
    }

    /* ───────────────── NAV ITEM CLASS ───────────────── */
    private class NavItem extends JPanel {
        private final JLabel lbl;
        NavItem(String text) {
            setLayout(new FlowLayout(FlowLayout.LEFT,12,8));
            setOpaque(true);
            setBackground(NAV_BG);
            lbl = new JLabel(text);
            lbl.setForeground(NAV_TEXT);
            lbl.setFont(NAV_FONT);
            add(lbl);
        }
        void setSelected(boolean sel) {
            setBackground(sel ? NAV_SELECTED : NAV_BG);
        }
    }

    /* ─────────────────── MAIN ─────────────────── */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            new DispatchFrame().setVisible(true);
        });
    }
}