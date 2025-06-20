package nepxpress.view;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
// import cashcollection.DBConnection; // Removed: Not found in project

public class RiderDashboard extends JFrame {
    private String userId;
    private String riderFullName = "";

    // New constructor accepting userId
    public RiderDashboard(String userId) {
        this(); // Call the default constructor
        this.userId = userId;
        // Fetch rider full name from DB
        try {
            int uid = Integer.parseInt(userId);
            nepxpress.database.RiderDAO riderDAO = new nepxpress.database.RiderDAO();
            nepxpress.database.RiderInfo riderInfo = riderDAO.getRiderByUserId(uid);
            if (riderInfo != null) {
                this.riderFullName = riderInfo.getFullName();
            } else {
                this.riderFullName = "User " + userId;
            }
        } catch (Exception e) {
            this.riderFullName = "User " + userId;
        }
    }
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton activeButton;
    private Map<String, JPanel> panelCache;
    private Color primaryColor = new Color(16, 42, 67);    // #102A43
    private Color secondaryColor = new Color(63, 124, 172); // #3F7CAC
    private Color hoverColor = new Color(28, 66, 98);      // Darker shade for hover
    private Color activeColor = new Color(40, 90, 130);    // Active menu item color
    private JButton reportIssueSidebarButton; // Reference to Report Issue sidebar button

    public RiderDashboard() {
        setupFrame();
        panelCache = new HashMap<>();
        initializeUI();
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Always maximize window
    }

    private void setupFrame() {
        setTitle("Rider Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void initializeUI() {
        add(createSidebar(), BorderLayout.WEST);
        add(createTopbar(), BorderLayout.NORTH);
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(243, 244, 246)); // Light gray background
        add(mainPanel, BorderLayout.CENTER);
        
        // Show deliveries panel by default
        showDeliveriesPanel();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(primaryColor);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        
        // Dashboard title
        JLabel dashboardTitle = new JLabel("Rider Dashboard");
        dashboardTitle.setForeground(Color.WHITE);
        dashboardTitle.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dashboardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(dashboardTitle);
        
        // Add menu items
        String[] menuItems = {
            "My Deliveries",
            "My Pickup",
            "Cash Collected",
            "Report Issue",
            "Daily Summary"
        };

        for (String item : menuItems) {
            JButton btn = createMenuButton(item);
            if (item.equals("Report Issue")) {
                reportIssueSidebarButton = btn;
            }
            sidebar.add(btn);
        }

        return sidebar;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(primaryColor);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        
        // Custom styling
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(primaryColor);
                }
            }
        });
        
        // Click handler
        button.addActionListener(e -> {
            if (activeButton != null) {
                activeButton.setBackground(primaryColor);
            }
            button.setBackground(activeColor);
            activeButton = button;
            
            switch (text) {
                case "My Deliveries":
                    showDeliveriesPanel();
                    break;
                case "My Pickup":
                    showPickupPanel();
                    break;
                case "Cash Collected":
                    showCashCollectedPanel();
                    break;
                case "Report Issue":
                    showReportIssuePanel();
                    break;
                case "Daily Summary":
                    showDailySummaryPanel();
                    break;
            }
        });
        
        return button;
    }

    private JPanel createTopbar() {
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(secondaryColor);
        topbar.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Welcome text
        String welcomeText = riderFullName.isEmpty() ? "Welcome, User" : ("Welcome, " + riderFullName);
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        // Logout button with new color and functionality
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Professional red color
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Add hover effect
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(200, 35, 51)); // Darker red on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(220, 53, 69)); // Back to original red
            }
        });

        // Add logout functionality
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                dispose(); // Close the dashboard window
                new RegisterView().setVisible(true); // Open login page
            }
        });
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(secondaryColor);
        rightPanel.add(logoutButton);
        
        topbar.add(welcomeLabel, BorderLayout.WEST);
        topbar.add(rightPanel, BorderLayout.EAST);
        
        return topbar;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(40, 90, 130));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showDeliveriesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(243, 244, 246));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("My Deliveries");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Parcel ID", "Customer Name", "Address", "Status", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };

        // --- Load data from database ---
        boolean dataLoaded = false;
        // TODO: Integrate actual DB connection here in the future
        // Sample data used for now
        if (!dataLoaded) {
            model.addRow(new Object[]{"DLV1001", "Ramesh Shrestha", "Baneshwor, KTM", "Pending", "Actions"});
            model.addRow(new Object[]{"DLV1002", "Sita Gurung", "Patan, KTM", "Delivered", "Actions"});
            model.addRow(new Object[]{"DLV1003", "Bikash Lama", "Bhaktapur", "Pending", "Actions"});
            model.addRow(new Object[]{"DLV1004", "Sunita Karki", "Kalanki, KTM", "Failed", "Actions"});
            model.addRow(new Object[]{"DLV1005", "Niraj Thapa", "Lagankhel, KTM", "Delivered", "Actions"});
        }

        // Add hardcoded sample records if no data loaded
        if (!dataLoaded) {
            model.addRow(new Object[]{"DLV1001", "Ramesh Shrestha", "Baneshwor, KTM", "Pending", "Actions"});
            model.addRow(new Object[]{"DLV1002", "Sita Gurung", "Patan, KTM", "Delivered", "Actions"});
            model.addRow(new Object[]{"DLV1003", "Bikash Lama", "Bhaktapur", "Pending", "Actions"});
            model.addRow(new Object[]{"DLV1004", "Sunita Karki", "Kalanki, KTM", "Failed", "Actions"});
            model.addRow(new Object[]{"DLV1005", "Niraj Thapa", "Lagankhel, KTM", "Delivered", "Actions"});
        }

        JTable table = new HoverTable(model);
    table.setRowHeight(40);
    table.setShowGrid(true);
    table.setGridColor(new Color(229, 231, 235));
    table.getTableHeader().setBackground(new Color(243, 244, 246));
    table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    table.setSelectionBackground(new Color(200, 220, 240));
    // Set status color renderer
    int statusColIdx = 3; // "Status" column index
    table.getColumnModel().getColumn(statusColIdx).setCellRenderer(new StatusCellRenderer());
    if (columns[columns.length - 1].equals("Actions")) {
        TableColumn actionColumn = table.getColumnModel().getColumn(columns.length - 1);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
    }
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());

    // --- Professional search bar panel ---
    JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    searchBarPanel.setBackground(new Color(243, 244, 246));
    PlaceholderTextField searchField = new PlaceholderTextField("Search by Merchant Name, Parcel ID or Customer name");
    searchField.setPreferredSize(new Dimension(380, 32));
    searchField.setFont(new Font("Arial", Font.PLAIN, 14));
    searchField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 210, 220)),
        BorderFactory.createEmptyBorder(6, 12, 6, 12)
    ));
    searchField.setBackground(Color.WHITE);
    JButton searchButton = new JButton("Search");
    searchButton.setFont(new Font("Arial", Font.BOLD, 13));
    searchButton.setBackground(secondaryColor);
    searchButton.setForeground(Color.WHITE);
    searchButton.setFocusPainted(false);
    searchButton.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
    searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    searchBarPanel.add(searchField);
    searchBarPanel.add(Box.createHorizontalStrut(8));
    searchBarPanel.add(searchButton);
    panel.add(searchBarPanel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);

    Runnable doSearch = () -> {
        String text = searchField.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
        table.setRowSorter(sorter);
    };
    searchButton.addActionListener(e -> doSearch.run());
    searchField.addActionListener(e -> doSearch.run());
    updateMainPanel(panel);
    }

    private void showCashCollectedPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(243, 244, 246));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Cash Collected");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Parcel ID", "Customer Name", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // --- Load data from database ---
        boolean dataLoaded = false;
        // TODO: Integrate actual DB connection here in the future
        // Sample data used for now
        if (!dataLoaded) {
            model.addRow(new Object[]{"DLV1002", "Sita Gurung", "1200", "Collected"});
            model.addRow(new Object[]{"DLV1005", "Niraj Thapa", "1800", "Collected"});
            model.addRow(new Object[]{"DLV1006", "Asha KC", "950", "Delivered"});
            model.addRow(new Object[]{"DLV1007", "Bimal Rai", "2100", "Collected"});
            model.addRow(new Object[]{"DLV1008", "Rita Sharma", "700", "Delivered"});
        }
        // Add hardcoded sample records if no data loaded
        if (!dataLoaded) {
            model.addRow(new Object[]{"DLV1002", "Sita Gurung", "1200", "Collected"});
            model.addRow(new Object[]{"DLV1005", "Niraj Thapa", "1800", "Collected"});
            model.addRow(new Object[]{"DLV1006", "Asha KC", "950", "Delivered"});
            model.addRow(new Object[]{"DLV1007", "Bimal Rai", "2100", "Collected"});
            model.addRow(new Object[]{"DLV1008", "Rita Sharma", "700", "Delivered"});
        }

        JTable table = new HoverTable(model);
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(new Color(229, 231, 235));
        table.getTableHeader().setBackground(new Color(243, 244, 246));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setSelectionBackground(new Color(200, 220, 240));
        int statusColIdx = 3; // "Status" column index
        table.getColumnModel().getColumn(statusColIdx).setCellRenderer(new StatusCellRenderer());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // --- Search bar panel ---
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchBarPanel.setBackground(new Color(243, 244, 246));
        PlaceholderTextField searchField = new PlaceholderTextField("Search by Parcel ID or Customer Name");
        searchField.setPreferredSize(new Dimension(320, 32));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        searchField.setBackground(Color.WHITE);
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 13));
        searchButton.setBackground(secondaryColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBarPanel.add(searchField);
        searchBarPanel.add(Box.createHorizontalStrut(8));
        searchBarPanel.add(searchButton);
        panel.add(searchBarPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        Runnable doSearch = () -> {
            String text = searchField.getText().trim();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            if (text.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
            table.setRowSorter(sorter);
        };
        searchButton.addActionListener(e -> doSearch.run());
        searchField.addActionListener(e -> doSearch.run());
        updateMainPanel(panel);
    }

    private void showReportIssuePanel(String parcelId) {
        if (reportIssueSidebarButton != null) {
            reportIssueSidebarButton.setBackground(activeColor);
            activeButton = reportIssueSidebarButton;
        }
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(243, 244, 246));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Report Issue");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;

        // Parcel ID
        formPanel.add(createFormLabel("Parcel ID"), gbc);
        JTextField parcelIdField = createFormInput();
        if (parcelId != null) parcelIdField.setText(parcelId);
        formPanel.add(parcelIdField, gbc);

        // Issue Type
        formPanel.add(createFormLabel("Issue Type"), gbc);
        String[] issueTypes = {
            "Select Issue Type",
            "Customer not available",
            "Address not found",
            "Package damaged",
            "Payment issue",
            "Vehicle breakdown",
            "Other"
        };
        JComboBox<String> issueTypeCombo = new JComboBox<>(issueTypes);
        issueTypeCombo.setBackground(Color.WHITE);
        formPanel.add(issueTypeCombo, gbc);

        // Description
        formPanel.add(createFormLabel("Description"), gbc);
        JTextArea description = new JTextArea(5, 20);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        formPanel.add(new JScrollPane(description), gbc);

        // Submit button
        JButton submitButton = new JButton("Submit Report");
        styleButton(submitButton);
        submitButton.setMaximumSize(new Dimension(200, 40));
        submitButton.setPreferredSize(new Dimension(200, 40));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String pid = parcelIdField.getText().trim();
            String issue = (String) issueTypeCombo.getSelectedItem();
            String desc = description.getText().trim();
            if (pid.isEmpty() || issue.equals("Select Issue Type") || desc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all fields and select an issue type.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Get rider ID from user ID
                    int uid = Integer.parseInt(userId);
                    nepxpress.database.RiderDAO riderDAO = new nepxpress.database.RiderDAO();
                    nepxpress.database.RiderInfo riderInfo = riderDAO.getRiderByUserId(uid);
                    
                    if (riderInfo != null) {
                        // Create issue title with parcel ID
                        String issueTitle = issue + " - Parcel: " + pid;
                        
                        // Save to database
                        nepxpress.database.RiderIssueDAO issueDAO = new nepxpress.database.RiderIssueDAO();
                        boolean success = issueDAO.reportIssue(riderInfo.getId(), issueTitle, desc);
                        
                        if (success) {
                            JOptionPane.showMessageDialog(panel, "Issue reported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            showDeliveriesPanel();
                        } else {
                            JOptionPane.showMessageDialog(panel, "Failed to report issue. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Could not find rider information. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(formPanel, BorderLayout.CENTER);
        updateMainPanel(panel);
    }

    private void showReportIssuePanel() {
        showReportIssuePanel(null);
    }

    private void showPickupPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(243, 244, 246));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Pickup");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panel.add(title, BorderLayout.NORTH);

        // Search Bar
        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setOpaque(false);
        PlaceholderTextField searchField = new PlaceholderTextField("Search by pickup ID, status, or merchant name");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        searchField.setPreferredSize(new Dimension(450, 38));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 223, 228)),
            BorderFactory.createEmptyBorder(0, 12, 0, 0)
        ));
        JButton searchButton = new JButton("\uD83D\uDD0D");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        searchButton.setFocusPainted(false);
        searchButton.setBackground(new Color(236, 242, 248));
        searchButton.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchButton, BorderLayout.EAST);
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        panel.add(searchBarPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Table
        String[] columns = {"Pickup ID", "Merchant Name", "Address", "Status", "Statas"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only the "Statas" (Acknowledge) button is editable for click
            }
        };

        // Sample data - replace with DB fetch
        model.addRow(new Object[]{"PKP00156", "Smart Deals", "Kalanki, KTM", "Assigned", "Acknowledge"});
        model.addRow(new Object[]{"PKP00157", "Trendy Store", "Patan, KTM", "Pending", "Acknowledge"});

        JTable table = new HoverTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        table.setRowHeight(44);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(236, 242, 248));
        table.getTableHeader().setForeground(new Color(40, 90, 130));
        table.getTableHeader().setOpaque(true);

        // Custom rounded green button for "Acknowledge"
        class RoundedGreenButton extends JButton {
            public RoundedGreenButton(String text) {
                super(text);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 17));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(8, 28, 8, 28));
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(76, 175, 80)); // #4CAF50
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                super.paintComponent(g);
                g2.dispose();
            }
            @Override
            public void paintBorder(Graphics g) {
                // No border
            }
        }
        // Button renderer for "Acknowledge"
        table.getColumn("Statas").setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            RoundedGreenButton btn = new RoundedGreenButton(value != null ? value.toString() : "Acknowledge");
            btn.setOpaque(false);
            return btn;
        });
        table.getColumn("Statas").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private final RoundedGreenButton btn = new RoundedGreenButton("Acknowledge");
            {
                btn.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        JOptionPane.showMessageDialog(panel, "Pickup acknowledged for: " + table.getValueAt(row, 0));
                    }
                });
            }
            @Override
            public Component getTableCellEditorComponent(JTable tbl, Object value, boolean isSelected, int row, int column) {
                btn.setText(value != null ? value.toString() : "Acknowledge");
                return btn;
            }
            @Override
            public Object getCellEditorValue() {
                return "Acknowledge";
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        // Search logic
        Runnable doSearch = () -> {
            String text = searchField.getText().trim();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            if (text.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
            table.setRowSorter(sorter);
        };
        searchButton.addActionListener(e -> doSearch.run());
        searchField.addActionListener(e -> doSearch.run());

        updateMainPanel(panel);
    }

    private void showDailySummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(236, 242, 248)); // Subtle blue-gray background
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header with title and date
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(236, 242, 248));

        JLabel title = new JLabel("\uD83D\uDCC8  Daily Summary"); // Unicode chart icon
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(40, 90, 130));
        headerPanel.add(title, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(107, 114, 128));
        headerPanel.add(dateLabel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Stats grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 28, 28));
        statsGrid.setBackground(new Color(236, 242, 248));
        statsGrid.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        statsGrid.add(createStatCardWithIcon("Deliveries Completed", "12", new Color(34, 197, 94), "\u2714"));
        statsGrid.add(createStatCardWithIcon("Pending Deliveries", "5", new Color(234, 179, 8), "\u23F3"));
        statsGrid.add(createStatCardWithIcon("Pickups Completed", "8", new Color(34, 197, 94), "\uD83D\uDE9A"));
        statsGrid.add(createStatCardWithIcon("Pending Pickups", "3", new Color(234, 179, 8), "\u23F3"));
        statsGrid.add(createStatCardWithIcon("Cash Collected", "Rs. 7,000", new Color(34, 197, 94), "\uD83D\uDCB0"));
        statsGrid.add(createStatCardWithIcon("Pending Collection", "Rs. 4,250", new Color(234, 179, 8), "\u23F3"));

        panel.add(statsGrid, BorderLayout.CENTER);
        updateMainPanel(panel);
    }

    // Stat card with icon
    private JPanel createStatCardWithIcon(String title, String value, Color valueColor, String icon) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 8, 8, new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));
        card.setPreferredSize(new Dimension(210, 120));
        card.setMaximumSize(new Dimension(240, 140));
        card.setMinimumSize(new Dimension(180, 100));

        // Horizontal panel for icon and title
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(valueColor.darker());
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(new Color(107, 114, 128));
        topPanel.add(iconLabel);
        topPanel.add(titleLabel);

        // Value label
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 44));
        valueLabel.setForeground(valueColor);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createSearchPanel(JTextField searchField) {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(243, 244, 246));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        return searchPanel;
    }

    private JTable createStyledTable(String[] columns, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(new Color(229, 231, 235));
        table.getTableHeader().setBackground(new Color(243, 244, 246));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setSelectionBackground(new Color(229, 231, 235));
        
        // Add action buttons if it's the last column
        if (columns[columns.length - 1].equals("Actions")) {
            TableColumn actionColumn = table.getColumnModel().getColumn(columns.length - 1);
            actionColumn.setCellRenderer(new ButtonRenderer());
            actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        }

        return table;
    }

    private void updateMainPanel(JPanel newPanel) {
        mainPanel.removeAll();
        mainPanel.add(newPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Custom button renderer for table
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton markButton;
        private JButton reportButton;
        private String markLabel;

        public ButtonRenderer() {
            this("Mark Delivered");
        }
        public ButtonRenderer(String markLabel) {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            this.markLabel = markLabel;
            markButton = new JButton(markLabel);
            reportButton = new JButton("Report Issue");
            styleButton(markButton);
            styleButton(reportButton);
            add(markButton);
            add(reportButton);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            markButton.setText(markLabel);
            return this;
        }
    }

    // Custom button editor for table
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton markButton;
        private JButton reportButton;
        private JTable currentTable;
        private int currentRow;
        private String markLabel;
        private String pickedUpStatus;
        private String pickedUpMessage;

        public ButtonEditor(JCheckBox checkBox) {
            this(checkBox, "Mark Delivered", "Delivered", "Parcel marked as Delivered!");
        }
        public ButtonEditor(JCheckBox checkBox, String markLabel, String pickedUpStatus, String pickedUpMessage) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            this.markLabel = markLabel;
            this.pickedUpStatus = pickedUpStatus;
            this.pickedUpMessage = pickedUpMessage;
            markButton = new JButton(markLabel);
            reportButton = new JButton("Report Issue");
            styleButton(markButton);
            styleButton(reportButton);

            markButton.addActionListener(e -> {
                if (currentTable != null) {
                    DefaultTableModel model = (DefaultTableModel) currentTable.getModel();
                    int statusCol = model.getColumnCount() - 2; // Status is second last column
                    model.setValueAt(pickedUpStatus, currentRow, statusCol);
                    JOptionPane.showMessageDialog(panel, pickedUpMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                fireEditingStopped();
            });
            reportButton.addActionListener(e -> {
                final String parcelId;
                if (currentTable != null) {
                    DefaultTableModel model = (DefaultTableModel) currentTable.getModel();
                    parcelId = model.getValueAt(currentRow, 0).toString();
                } else {
                    parcelId = null;
                }
                fireEditingStopped();
                // Show Report Issue panel and pre-fill Parcel ID
                SwingUtilities.invokeLater(() -> showReportIssuePanel(parcelId));
            });
            panel.add(markButton);
            panel.add(reportButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.currentTable = table;
            this.currentRow = row;
            markButton.setText(markLabel);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(55, 65, 81));
        return label;
    }

    private JTextField createFormInput() {
        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(300, 35));
        input.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return input;
    }

    public static void main(String[] args) {
        // Ensure deliveries table exists before launching dashboard
        // TODO: Replace with actual DB connection logic
        // cashcollection.DBConnection.createDeliveriesTableIfNotExists();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            RiderDashboard dashboard = new RiderDashboard();
            dashboard.setVisible(true);
            dashboard.setExtendedState(JFrame.MAXIMIZED_BOTH); // Make full screen
        });
    }

    class HoverTable extends JTable {
        private int hoveredRow = -1;
        public HoverTable(DefaultTableModel model) {
            super(model);
            // Set modern font and row height
            setFont(new Font("Segoe UI", Font.PLAIN, 17));
            setRowHeight(44);
            getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
            getTableHeader().setBackground(new Color(236, 242, 248));
            getTableHeader().setForeground(new Color(40, 90, 130));
            getTableHeader().setOpaque(true);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int row = rowAtPoint(e.getPoint());
                    if (row != hoveredRow) {
                        hoveredRow = row;
                        repaint();
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    hoveredRow = -1;
                    repaint();
                }
            });
        }
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
                if (row == hoveredRow) {
                    c.setBackground(new Color(220, 236, 251)); // Professional light blue
                } else if (row % 2 == 0) {
                    c.setBackground(new Color(248, 250, 252)); // Subtle striped row
                } else {
                    c.setBackground(Color.WHITE);
                }
            }
            c.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            return c;
        }
    }

    // Utility class for placeholder text in JTextField
    class PlaceholderTextField extends JTextField {
        private String placeholder;
        private Color placeholderColor = new Color(180, 180, 180);
        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setForeground(Color.BLACK);
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    repaint();
                }
                public void focusLost(java.awt.event.FocusEvent e) {
                    repaint();
                }
            });
            getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(placeholderColor);
                g2.setFont(getFont());
                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int y = insets.top + fm.getAscent() + 2;
                g2.drawString(placeholder, insets.left + 2, y);
                g2.dispose();
            }
        }
    }

    // Custom cell renderer for status coloring
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = value != null ? value.toString().toLowerCase() : "";
            switch (status) {
                case "pending":
                    c.setForeground(new Color(234, 179, 8)); // Yellow
                    break;
                case "delivered":
                case "picked up":
                case "collected":
                    c.setForeground(new Color(34, 197, 94)); // Green
                    break;
                case "failed":
                case "cancelled":
                    c.setForeground(new Color(220, 53, 69)); // Red
                    break;
                default:
                    c.setForeground(new Color(55, 65, 81)); // Default dark gray
            }
            return c;
        }
    }

    // Example method to fetch deliveries from the database
    public void fetchDeliveriesFromDB() {
        // TODO: Integrate actual DB connection here in the future
        // This is a placeholder for DB fetch logic.
    }
}