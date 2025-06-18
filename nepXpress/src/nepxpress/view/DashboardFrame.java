package nepxpress.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Dashboardframe.java — NetBeans‑ready Swing UI
 * ------------------------------------------------------------
 * • Reproduces the screenshot colours & layout.
 * • Adds programmatically‑generated icons so there are NO more
 *   grey "X" placeholders.
 * • Every sidebar item (Dashboard, Dispatch, … Contact) now
 *   switches the main view via CardLayout, so they are truly
 *   "functional".
 *
 *  How to run in NetBeans:
 *     1. Place this file under src/dashboard/.
 *     2. Right‑click project → Properties → Run → Main Class →
 *        dashboard.Dashboardframe
 *     3. Press F6.
 */
public class DashboardFrame extends JFrame {
    private String userId;
    private String userFullName = "User";
    private JLabel userLbl; // move to class field for sidebar personalization

    /* ─────  THEME  ───── */
    private static final Color HEADER_BG    = new Color(0x436C8E);
    private static final Color NAV_BG       = new Color(0x10273B);
    private static final Color NAV_SELECTED = new Color(0x4A78A4);
    private static final Color NAV_TEXT     = new Color(0xE7EFF7);
    private static final Color CARD_BORDER  = NAV_SELECTED;

    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 26);
    private static final Font NAV_FONT   = new Font("SansSerif", Font.BOLD, 14);
    private static final Font COUNT_FONT = new Font("SansSerif", Font.BOLD, 22);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);

    /* ─────  FIELDS  ───── */
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     mainView   = new JPanel(cardLayout);
    private final Map<String, NavItem> navButtons = new HashMap<>();

    /* ─────  CONSTRUCTOR  ───── */
    // New constructor accepting userId
    public DashboardFrame(String userId) {
        this.userId = userId;
        // Fetch user full name from DB
        try {
            int uid = Integer.parseInt(userId);
            nepxpress.database.UserDAO userDAO = new nepxpress.database.UserDAO();
            this.userFullName = getUserFullName(uid);
        } catch (Exception e) {
            this.userFullName = "User " + userId;
        }
        setTitle("nepXpress — Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Always maximize on open
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        add(createHeader(),  BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        add(createMainView(),BorderLayout.CENTER);

        selectView("Dashboard"); // default selection
    }

    // Old default constructor for compatibility
    public DashboardFrame() {
        this(null);
    }

    // Helper method to fetch user's full name from DB
    private String getUserFullName(int userId) {
        String fullName = "User";
        java.sql.Connection conn = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        try {
            conn = nepxpress.database.DatabaseConnection.getConnection();
            String sql = "SELECT first_name, surname FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                fullName = rs.getString("first_name") + " " + rs.getString("surname");
            }
        } catch (Exception e) {
            // fallback to User
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception ex) {}
            // do not close conn (managed elsewhere)
        }
        return fullName;
    }

    /* ───────────────── HEADER ───────────────── */
    private JComponent createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(8,0,8,0));
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
        side.setPreferredSize(new Dimension(180, 0));

        // Create a panel for the User label with FlowLayout.LEFT
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        userPanel.setBackground(NAV_BG);
        
        userLbl = new JLabel(userFullName);
        userLbl.setForeground(Color.WHITE);
        userLbl.setFont(new Font("SansSerif", Font.BOLD, 24));
        userLbl.setBorder(new EmptyBorder(16,8,8,0));
        userPanel.add(userLbl);
        side.add(userPanel);

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        navPanel.setBackground(NAV_BG);
        
        NavItem dashboardNav = makeNav("Dashboard", null);
        dashboardNav.setBorder(new EmptyBorder(2, 0, 2, 0));
        navPanel.add(dashboardNav);
        
        NavItem dispatchNav = makeNav("Dispatch", null);
        dispatchNav.setBorder(new EmptyBorder(2, 0, 2, 0));
        navPanel.add(dispatchNav);
        
        // Create Parcels nav item with special handling
        NavItem parcelsNav = makeNav("Parcels", null);
        parcelsNav.setBorder(new EmptyBorder(2, 0, 2, 0));
        parcelsNav.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectView("Parcels");
            }
        });
        navPanel.add(parcelsNav);
        
        NavItem trackNav = makeNav("Track Parcels", null);
        trackNav.setBorder(new EmptyBorder(2, 0, 2, 0));
        navPanel.add(trackNav);
        
        NavItem complaintsNav = makeNav("Complaints", null);
        complaintsNav.setBorder(new EmptyBorder(2, 0, 2, 0));
        navPanel.add(complaintsNav);
        
        NavItem contactNav = makeNav("Contact", null);
        contactNav.setBorder(new EmptyBorder(2, 0, 2, 0));
        navPanel.add(contactNav);
        
        side.add(navPanel);
        side.add(Box.createVerticalGlue());
        
        // Deactivate Account button
        NavItem deactivateBtn = new NavItem("Deactivate Account", null);
        deactivateBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        deactivateBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Custom styled panel
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 12, 24));
                mainPanel.setBackground(Color.WHITE);

                JLabel title = new JLabel("Delete Your Account");
                title.setFont(new Font("Segoe UI", Font.BOLD, 22));
                title.setForeground(new Color(40, 40, 40));
                title.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                mainPanel.add(title);

                JLabel subtitle = new JLabel("We're sorry to see you go.");
                subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                subtitle.setForeground(new Color(120, 120, 120));
                subtitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                mainPanel.add(subtitle);
                mainPanel.add(Box.createVerticalStrut(12));

                // Divider
                JSeparator sep = new JSeparator();
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                mainPanel.add(sep);
                mainPanel.add(Box.createVerticalStrut(8));

                JLabel beforeLabel = new JLabel("Before you go...");
                beforeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
                beforeLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                mainPanel.add(beforeLabel);
                mainPanel.add(Box.createVerticalStrut(4));

                JPanel bullets = new JPanel();
                bullets.setLayout(new BoxLayout(bullets, BoxLayout.Y_AXIS));
                bullets.setBackground(Color.WHITE);
                bullets.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                JLabel b1 = new JLabel("\u2022 If you're sick of getting notifications, you can disable them in settings.");
                JLabel b2 = new JLabel("\u2022 If you want to change your username, you can do that in your profile.");
                JLabel b3 = new JLabel("\u2022 Account deletion is \u003cb\u003efinal\u003c/b\u003e. There will be no way to restore your account.");
                b1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                b2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                b3.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                bullets.add(b1);
                bullets.add(b2);
                bullets.add(b3);
                mainPanel.add(bullets);
                mainPanel.add(Box.createVerticalStrut(10));

                // Username and password fields
                JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 8));
                formPanel.setBackground(Color.WHITE);
                JTextField usernameField = new JTextField();
                JPasswordField passwordField = new JPasswordField();
                JTextField reasonField = new JTextField();
                formPanel.add(new JLabel("Username (Mobile):"));
                formPanel.add(usernameField);
                formPanel.add(new JLabel("Password:"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Reason for deactivation:"));
                formPanel.add(reasonField);
                mainPanel.add(formPanel);
                mainPanel.add(Box.createVerticalStrut(8));

                // Custom buttons
                JButton keepBtn = new JButton("Never mind, keep my account");
                keepBtn.setBackground(new Color(233, 30, 99));
                keepBtn.setForeground(Color.WHITE);
                keepBtn.setFocusPainted(false);
                JButton deactivateBtn = new JButton("Deactivate my account");
                deactivateBtn.setBackground(new Color(120, 120, 120));
                deactivateBtn.setForeground(Color.WHITE);
                deactivateBtn.setFocusPainted(false);

                JPanel btnPanel = new JPanel();
                btnPanel.setBackground(Color.WHITE);
                btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                btnPanel.add(keepBtn);
                btnPanel.add(Box.createHorizontalStrut(12));
                btnPanel.add(deactivateBtn);
                mainPanel.add(btnPanel);

                JDialog dialog = new JDialog(DashboardFrame.this, "Deactivate Account", true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setContentPane(mainPanel);
                dialog.pack();
                dialog.setLocationRelativeTo(DashboardFrame.this);
                dialog.setResizable(false);

                keepBtn.addActionListener(ev -> dialog.dispose());

                deactivateBtn.addActionListener(ev -> {
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    String reason = reasonField.getText().trim();
                    if (username.isEmpty() || password.isEmpty() || reason.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Validate credentials
                    nepxpress.database.UserDAO userDAO = new nepxpress.database.UserDAO();
                    if (!userDAO.validateLogin(username, password)) {
                        JOptionPane.showMessageDialog(dialog, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    dialog.dispose();

                    // Twitter-style blue confirmation dialog
                    JPanel bluePanel = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(new Color(230, 240, 255));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                        }
                    };
                    bluePanel.setLayout(new BoxLayout(bluePanel, BoxLayout.Y_AXIS));
                    bluePanel.setBackground(Color.WHITE);
                    bluePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    // Center content vertically and horizontally
                    bluePanel.add(Box.createVerticalGlue());

                // After dialog.pack(), maximize dialog window (must be set on the JDialog instance)
                SwingUtilities.invokeLater(() -> {
                    Window dialogWindow = SwingUtilities.getWindowAncestor(bluePanel);
                    if (dialogWindow instanceof JDialog) {
                        JDialog dlg = (JDialog) dialogWindow;
                        dlg.setResizable(false);
                        dlg.setUndecorated(true);
                        // Set to full screen bounds
                        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                        Rectangle bounds = env.getMaximumWindowBounds();
                        dlg.setBounds(bounds);
                        dlg.setAlwaysOnTop(true);
                        // Optionally, add a semi-transparent overlay effect
                        dlg.getContentPane().setBackground(new Color(240, 245, 255, 220));
                    }
                });

                JLabel bigTitle = new JLabel("<html><span style='font-size:20px; font-weight:bold; color:#222;'>Is this goodbye?</span></html>");
                bigTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
                bigTitle.setForeground(new Color(30, 60, 110));
                bigTitle.setBorder(BorderFactory.createEmptyBorder(16, 30, 0, 0));
                bigTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                bigTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
                bluePanel.add(bigTitle);
                    bigTitle.setForeground(new Color(30, 60, 110));
                    bigTitle.setBorder(BorderFactory.createEmptyBorder(16, 30, 0, 0));
                    bigTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                    bigTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
                    bluePanel.add(bigTitle);

                    JLabel blueSubtitle = new JLabel("<html><div style='text-align:center; width:100%'><span style='font-size:14px; color:#444;'>Are you sure you don't want to reconsider? Was it something we said? <a href='#' style='color:#1da1f2;'>Tell us.</a></span></div></html>");
                    blueSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    blueSubtitle.setForeground(new Color(90, 110, 140));
                    blueSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
                    bluePanel.add(blueSubtitle);
                    bluePanel.add(Box.createVerticalStrut(8));

                    JPanel blueBox = new JPanel();
                    blueBox.setLayout(new BoxLayout(blueBox, BoxLayout.Y_AXIS));
                    blueBox.setBackground(Color.WHITE);
                    blueBox.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 225, 245)),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                    ));
                    blueBox.setAlignmentX(Component.CENTER_ALIGNMENT);

                    JLabel blueTitle = new JLabel("<html><span style='font-size:16px; color:#1d3557;'><b>Before you deactivate <span style='color:#1da1f2;'>know this:</span></b></span></html>");
                    blueTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    blueTitle.setForeground(new Color(30, 60, 110));
                    blueTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                    blueBox.add(blueTitle);
                    blueBox.add(Box.createVerticalStrut(5));

                    JLabel item1 = new JLabel("<html><ul style='margin:0;padding-left:20px;'><li><b>We will only retain your user data for 30 days</b> and then it will be permanently deleted. You can reactivate your account at any point within 30 days of deactivation by logging back in.</li></ul></html>");
                    JLabel item2 = new JLabel("<html><ul style='margin:0;padding-left:20px;'><li>You don't need to deactivate your account to <span style='color:#1da1f2;'>change your username</span>. You can change it on the <span style='color:#1da1f2;'>settings page</span>. All @replies and followers will remain unchanged.</li></ul></html>");
                    JLabel item3 = new JLabel("<html><ul style='margin:0;padding-left:20px;'><li>We have no control over <span style='color:#1da1f2;'>content indexed by search engines</span> like Google.</li></ul></html>");
                    item1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    item2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    item3.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    blueBox.add(item1);
                    blueBox.add(item2);
                    blueBox.add(item3);
                    bluePanel.add(blueBox);
                    bluePanel.add(Box.createVerticalStrut(8));

                    JPanel btnPanel2 = new JPanel();
                    btnPanel2.setBackground(Color.WHITE);
                    btnPanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
                    JButton blueDeactivate = new JButton("Deactivate");
                    blueDeactivate.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    blueDeactivate.setBackground(new Color(29, 155, 209));
                    blueDeactivate.setForeground(Color.WHITE);
                    blueDeactivate.setFocusPainted(false);
                    blueDeactivate.setPreferredSize(new Dimension(180, 38));
                    blueDeactivate.setBorder(BorderFactory.createEmptyBorder(6, 24, 6, 24));
                    blueDeactivate.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    JButton cancelBtn = new JButton("Cancel");
                    cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    cancelBtn.setBackground(Color.WHITE);
                    cancelBtn.setForeground(new Color(29, 155, 209));
                    cancelBtn.setFocusPainted(false);
                    cancelBtn.setPreferredSize(new Dimension(120, 38));
                    cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(29, 155, 209)));
                    cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnPanel2.add(blueDeactivate);
                    btnPanel2.add(Box.createHorizontalStrut(20));
                    btnPanel2.add(cancelBtn);
                    bluePanel.add(btnPanel2);
                    bluePanel.add(Box.createVerticalGlue());

                    JDialog blueDialog = new JDialog(DashboardFrame.this, "Deactivate Account", true);
                    blueDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    blueDialog.setContentPane(bluePanel);
                    blueDialog.pack();
                    blueDialog.setLocationRelativeTo(DashboardFrame.this);
                    blueDialog.setResizable(false);

                    cancelBtn.addActionListener(ev2 -> blueDialog.dispose());

                    blueDeactivate.addActionListener(ev2 -> {
                        // Switch the content of blueDialog to the final confirmation message
                        JPanel goodbyePanel = new JPanel();
                        goodbyePanel.setLayout(new BoxLayout(goodbyePanel, BoxLayout.Y_AXIS));
                        goodbyePanel.setBackground(Color.WHITE);
                        goodbyePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

                        JLabel doneLabel = new JLabel("Your account has been deactivated");
                        doneLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
                        doneLabel.setForeground(new Color(29, 155, 209));
                        doneLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                        goodbyePanel.add(doneLabel);
                        goodbyePanel.add(Box.createVerticalStrut(16));
                        JLabel infoLabel = new JLabel("You can reactivate your account by logging in again within 20 days.");
                        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                        infoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                        goodbyePanel.add(infoLabel);
                        goodbyePanel.add(Box.createVerticalStrut(20));
                        JButton closeBtn = new JButton("Close");
                        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        closeBtn.setBackground(new Color(29, 155, 209));
                        closeBtn.setForeground(Color.WHITE);
                        closeBtn.setFocusPainted(false);
                        closeBtn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                        closeBtn.addActionListener(ev3 -> {
                            blueDialog.dispose();
                            dispose();
                            new RegisterView().setVisible(true);
                        });
                        goodbyePanel.add(closeBtn);
                        blueDialog.setContentPane(goodbyePanel);
                        blueDialog.pack();
                        blueDialog.setLocationRelativeTo(DashboardFrame.this);
                    });

                    blueDialog.setVisible(true);
                });

                dialog.setVisible(true);
            }
        });
        side.add(deactivateBtn);
        
        NavItem logoutBtn = new NavItem("Logout", null);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    DashboardFrame.this,
                    "Are you sure you want to log out?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose(); // Close dashboard frame
                    new RegisterView().setVisible(true); // Open login page
                }
            }
        });
        side.add(logoutBtn);
        
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
    }

    /* ───────────────── MAIN VIEW ───────────────── */
    private JComponent createMainView() {
        // dashboard (home) panel
        JPanel dashboardPanel = makeDashboardCards();

        // Create dispatch panel
        DispatchPanel dispatchPanel = new DispatchPanel();

        // Create parcels panel by getting content from ParcelsFrame
        ParcelsFrame parcelsFrame = new ParcelsFrame();
        JPanel parcelsPanel = parcelsFrame.getMainPanel();

        // Create track parcels panel
        TrackParcelsPanel trackParcelsPanel = new TrackParcelsPanel();

        // Create complaints panel by getting content from ComplaintsFrame
        ComplaintsFrame complaintsFrame = new ComplaintsFrame();
        JPanel complaintsPanel = complaintsFrame.getMainPanel();

        // Create contacts panel by getting content from ContactsFrame
        ContactsFrame contactsFrame = new ContactsFrame();
        JPanel contactsPanel = contactsFrame.getMainPanel();

        // Add all panels to card layout
        mainView.add(dashboardPanel, "Dashboard");
        mainView.add(dispatchPanel, "Dispatch");
        mainView.add(parcelsPanel, "Parcels");
        mainView.add(trackParcelsPanel, "Track Parcels");
        mainView.add(complaintsPanel, "Complaints");
        mainView.add(contactsPanel, "Contact");

        return mainView;
    }

    private JPanel placeholder(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(title + " view coming soon", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 22));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    /* ───────────────── DASHBOARD CARDS ───────────────── */
    private JPanel makeDashboardCards() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(16,25,16,25));

        JLabel home = new JLabel("Home");
        home.setFont(new Font("SansSerif", Font.BOLD, 24));
        home.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(home);
        
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

        container.add(headerPanel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2,3,32,32));
        grid.setBorder(new EmptyBorder(32,25,32,25));
        grid.setBackground(Color.WHITE);

        grid.add(orderCard("10", "Booked Orders",    "flag"));
        grid.add(orderCard("5",  "Pending Orders",   "pending"));
        grid.add(orderCard("9",  "Delivered",        "delivered"));
        grid.add(orderCard("7",  "Cancelled Orders", "cancel"));
        grid.add(orderCard("0",  "Dispatched Orders","dispatchList"));
        grid.add(orderCard("7",  "Returned",         "returned"));

        container.add(grid, BorderLayout.CENTER);
        return container;
    }

    private JPanel orderCard(String count, String text, String iconKey) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(CARD_BORDER,2));

        JPanel textPan = new JPanel();
        textPan.setLayout(new BoxLayout(textPan, BoxLayout.Y_AXIS));
        textPan.setBackground(Color.WHITE);
        textPan.setBorder(new EmptyBorder(12,16,12,16));
        JLabel c = new JLabel(count);
        c.setFont(COUNT_FONT);
        JLabel t = new JLabel(text);
        t.setFont(LABEL_FONT);
        textPan.add(c);
        textPan.add(t);

        card.add(textPan, BorderLayout.CENTER);

        return card;
    }

    /* ───────────────── NAV ITEM CLASS ───────────────── */
    private class NavItem extends JPanel {
        private final JLabel lbl;
        private final String text;
        NavItem(String text, String iconKey) {
            this.text = text;
            setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
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
                case "flag":           return drawFlag();
                case "pending":        return drawHourglass();
                case "delivered":      return drawTruckCheck();
                case "cancel":         return drawCircleX();
                case "dispatchList":   return drawClipboard();
                case "returned":       return drawEnvelopeArrow();
                default:                return placeholder();
            }
        }

        /* Generic placeholder */
        private static ImageIcon placeholder() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.LIGHT_GRAY); g.fillRect(0,0,32,32);
            g.setColor(Color.DARK_GRAY);  g.drawLine(0,0,32,32); g.drawLine(32,0,0,32);
            g.dispose();
            return new ImageIcon(img);
        }

        /* Below: simple programmatic icons (monochrome) */
        private static ImageIcon drawHouse() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawPolygon(new int[]{4,16,28}, new int[]{16,4,16},3); // roof
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
        private static ImageIcon drawFlag() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawLine(8,6,8,26);
            g.drawPolyline(new int[]{8,24,14,24}, new int[]{6,8,16,24},4);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawHourglass() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawRect(8,6,16,4);
            g.drawRect(8,22,16,4);
            g.drawLine(8,10,24,22);
            g.drawLine(24,10,8,22);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawTruckCheck() {
            ImageIcon base = drawTruck();
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.drawImage(base.getImage(),0,0,null);
            g.setColor(Color.BLACK);
            g.drawLine(19,8,23,12);
            g.drawLine(23,12,28,4);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawCircleX() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawOval(4,4,24,24);
            g.drawLine(8,8,24,24);
            g.drawLine(24,8,8,24);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawClipboard() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawRect(8,6,16,20);
            g.drawRect(12,4,8,4);
            g.drawLine(10,12,22,12);
            g.drawLine(10,16,22,16);
            g.drawLine(10,20,22,20);
            g.dispose();
            return new ImageIcon(img);
        }
        private static ImageIcon drawEnvelopeArrow() {
            BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = prep(img);
            g.drawRect(4,8,24,14);
            g.drawLine(4,8,16,18);
            g.drawLine(28,8,16,18);
            g.drawLine(20,22,28,22);
            g.drawLine(28,22,28,14);
            g.drawLine(28,14,24,18);
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
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}