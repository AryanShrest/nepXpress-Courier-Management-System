package com.courier.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize database first
        if (!initializeDatabase()) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Database connection failed. Would you like to retry?\n" +
                "Make sure the 'cms' database exists.",
                "Database Error",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);
            
            if (choice == JOptionPane.YES_OPTION) {
                // Try to create database if it doesn't exist
                if (createDatabase()) {
                    if (!initializeDatabase()) {
                        showDatabaseError();
                    }
                } else {
                    showDatabaseError();
                }
            }
        }

        add(createHeader(),  BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        add(createMainView(),BorderLayout.CENTER);

        selectView("Dispatch"); // default selection
    }

    private void showDatabaseError() {
        JOptionPane.showMessageDialog(this,
            "Could not connect to database. Please check:\n" +
            "1. MySQL Server is running\n" +
            "2. Database 'cms' exists\n" +
            "3. Username 'root' with empty password is correct",
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private boolean createDatabase() {
        try {
            // Connect to MySQL server without database
            Connection rootConnection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?user=root");
            Statement stmt = rootConnection.createStatement();
            
            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS cms");
            
            stmt.close();
            rootConnection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ───────────────── DATABASE INITIALIZATION ───────────────── */
    private boolean initializeDatabase() {
        try {
            // Close existing connection if any
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            
            // Establish new connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Create table if not exists
            String createTable = "CREATE TABLE IF NOT EXISTS dispatches (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "address TEXT NOT NULL," +
                    "phone VARCHAR(20) NOT NULL," +
                    "courier_weight VARCHAR(50)," +
                    "courier_type VARCHAR(100)," +
                    "amount DECIMAL(10,2)," +
                    "dispatch_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            Statement stmt = connection.createStatement();
            stmt.execute(createTable);
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ───────────────── HEADER ───────────────── */
    private JComponent createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(15, 0, 15, 0));
        JLabel title = new JLabel("nepXpress", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(TITLE_FONT);
        header.add(title, BorderLayout.CENTER);
        return header;
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

        side.add(makeNav("Dashboard", "dashboard"));
        side.add(makeNav("Dispatch",  "dispatch"));
        side.add(makeNav("Parcels",   "parcel"));
        side.add(makeNav("Track Parcels", "search"));
        side.add(makeNav("Complaints", "complaint"));
        side.add(makeNav("Contact",   "contact"));

        side.add(Box.createVerticalGlue());
        return side;
    }

    private NavItem makeNav(String label, String iconKey) {
        NavItem item = new NavItem(label, iconKey);
        navButtons.put(label, item);
        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectView(label);
            }
        });
        return item;
    }

    private void selectView(String label) {
        cardLayout.show(mainView, label);
        navButtons.values().forEach(n -> n.setSelected(false));
        navButtons.get(label).setSelected(true);
        
        // Navigate to other frames
        if (!label.equals("Dispatch")) {
            navigateToFrame(label);
        }
    }
    
    private void navigateToFrame(String frameName) {
        SwingUtilities.invokeLater(() -> {
            switch (frameName) {
                case "Dashboard":
                    new DashboardFrame().setVisible(true);
                    this.dispose();
                    break;
                case "Parcels":
                    cardLayout.show(mainView, "Parcels");
                    navButtons.values().forEach(n -> n.setSelected(false));
                    navButtons.get("Parcels").setSelected(true);
                    break;
                case "Track Parcels":
                    // TODO: Implement TrackParcelsFrame
                    break;
                case "Complaints":
                    // TODO: Implement ComplaintsFrame
                    break;
                case "Contact":
                    // TODO: Implement ContactFrame
                    break;
            }
        });
    }

    /* ───────────────── MAIN VIEW ───────────────── */
    private JComponent createMainView() {
        JPanel dispatchPanel = createDispatchPanel();
        mainView.add(dispatchPanel, "Dispatch");
        mainView.add(placeholder("Dashboard"), "Dashboard");
        mainView.add(placeholder("Parcels"), "Parcels");
        mainView.add(placeholder("Track Parcels"), "Track Parcels");
        mainView.add(placeholder("Complaints"), "Complaints");
        mainView.add(placeholder("Contact"), "Contact");
        return mainView;
    }

    private JPanel placeholder(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(title + " view", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 22));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    /* ───────────────── DISPATCH PANEL ───────────────── */
    private JPanel createDispatchPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(MAIN_BG);

        // Header with title and date
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
        
        container.add(headerPanel, BorderLayout.NORTH);
        
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
        createBtn.addActionListener(new CreateDispatchListener());
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

    /* ───────────────── CREATE DISPATCH LISTENER ───────────────── */
    private class CreateDispatchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            String weight = courierWeightField.getText().trim();
            String type = courierTypeField.getText().trim();
            String amountText = amountField.getText().trim();
            
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(DispatchFrame.this, 
                    "Please fill in all required fields (Name, Address, Phone)",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                double amount = amountText.isEmpty() ? 0.0 : Double.parseDouble(amountText);
                
                // Check database connection
                if (connection == null || connection.isClosed()) {
                    if (!initializeDatabase()) {
                        JOptionPane.showMessageDialog(DispatchFrame.this,
                            "Lost database connection. Please try again.",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                String sql = "INSERT INTO dispatches (name, address, phone, courier_weight, courier_type, amount) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, address);
                pstmt.setString(3, phone);
                pstmt.setString(4, weight);
                pstmt.setString(5, type);
                pstmt.setDouble(6, amount);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(DispatchFrame.this, 
                        "Dispatch created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                }
                
                pstmt.close();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(DispatchFrame.this, 
                    "Please enter a valid amount",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(DispatchFrame.this, 
                    "Database error: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
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
        NavItem(String text, String iconKey) {
            setLayout(new FlowLayout(FlowLayout.LEFT,12,8));
            setOpaque(true);
            setBackground(NAV_BG);
            lbl = new JLabel(text);
            lbl.setForeground(NAV_TEXT);
            lbl.setFont(NAV_FONT);
            add(new JLabel(IconFactory.get(iconKey)));
            add(lbl);
        }
        void setSelected(boolean sel) {
            setBackground(sel ? NAV_SELECTED : NAV_BG);
        }
    }

    /* ───────────────── ICON FACTORY ───────────────── */
    private static class IconFactory {
        private static final Map<String, ImageIcon> cache = new HashMap<>();
        static ImageIcon get(String key) {
            return cache.computeIfAbsent(key, IconFactory::make);
        }
        private static ImageIcon make(String key) {
            switch (key) {
                case "dashboard":      return drawHouse();
                case "dispatch":       return drawTruck();
                case "parcel":         return drawBox();
                case "search":         return drawMagnifier();
                case "complaint":      return drawCubeExcl();
                case "contact":        return drawIdCard();
                default:               return placeholder();
            }
        }

        private static ImageIcon placeholder() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.LIGHT_GRAY); g.fillRect(0,0,32,32);
            g.setColor(Color.DARK_GRAY);  g.drawLine(0,0,32,32); g.drawLine(32,0,0,32);
            g.dispose();
            return new ImageIcon(img);
        }

        private static ImageIcon drawHouse() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawPolygon(new int[]{4,16,28}, new int[]{16,4,16},3);
            g.drawRect(8,16,16,12);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawTruck() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawRect(4,12,14,8);
            g.drawRect(18,16,10,6);
            g.fillOval(8,22,6,6);
            g.fillOval(20,22,6,6);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawBox() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawRect(6,6,20,20);
            g.drawLine(6,16,26,16);
            g.drawLine(16,6,16,26);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawMagnifier() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawOval(4,4,16,16);
            g.drawLine(16,16,26,26);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawCubeExcl() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawRect(6,6,20,20);
            g.drawLine(16,10,16,20);
            g.fillOval(15,23,2,2);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawIdCard() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawRect(4,8,24,16);
            g.drawOval(6,10,8,8);
            g.drawLine(16,12,26,12);
            g.drawLine(16,16,26,16);
            g.dispose();
            return new ImageIcon(img);
        }
        private static Graphics2D prep(BufferedImage img) {
            Graphics2D g = img.createGraphics();
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            return g;
        }
    }

    /* ─────────────────── MAIN ─────────────────── */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DispatchFrame().setVisible(true));
    }
}