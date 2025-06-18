package com.courier.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.*;
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

    /* ─────  THEME  ───── */
    private static final Color HEADER_BG    = new Color(0x436C8E);
    private static final Color NAV_BG       = new Color(0x10273B);
    private static final Color NAV_SELECTED = new Color(0x4A78A4);
    private static final Color NAV_TEXT     = new Color(0xE7EFF7);
    private static final Color CARD_BORDER  = NAV_SELECTED;

    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 26);
    private static final Font NAV_FONT   = new Font("SansSerif", Font.BOLD, 16);
    private static final Font COUNT_FONT = new Font("SansSerif", Font.BOLD, 22);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);

    /* ─────  FIELDS  ───── */
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     mainView   = new JPanel(cardLayout);
    private final Map<String, NavItem> navButtons = new HashMap<>();
    private Connection conn;

    /* ─────  CONSTRUCTOR  ───── */
    public DashboardFrame() {
        setTitle("nepXpress — Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(),  BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        add(createMainView(),BorderLayout.CENTER);

        selectView("Dashboard"); // default selection
        loadDashboardData();
    }

    private void loadDashboardData() {
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                // Update dashboard cards with real data
                updateOrderCounts();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading dashboard data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private void updateOrderCounts() throws SQLException {
        // Get counts for each status
        String query = "SELECT status, COUNT(*) as count FROM parcels GROUP BY status";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            Map<String, Integer> counts = new HashMap<>();
            while (rs.next()) {
                counts.put(rs.getString("status"), rs.getInt("count"));
            }

            // Update the dashboard cards
            updateCard("Booked Orders", counts.getOrDefault("pending", 0));
            updateCard("Pending Orders", counts.getOrDefault("in_transit", 0));
            updateCard("Delivered", counts.getOrDefault("delivered", 0));
            updateCard("Cancelled Orders", counts.getOrDefault("cancelled", 0));
        }
    }

    private void updateCard(String cardName, int count) {
        // Find the card and update its count
        for (Component comp : mainView.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component card : panel.getComponents()) {
                    if (card instanceof JPanel) {
                        JPanel cardPanel = (JPanel) card;
                        for (Component c : cardPanel.getComponents()) {
                            if (c instanceof JPanel) {
                                JPanel textPanel = (JPanel) c;
                                for (Component label : textPanel.getComponents()) {
                                    if (label instanceof JLabel) {
                                        JLabel lbl = (JLabel) label;
                                        if (lbl.getText().equals(cardName)) {
                                            // Update the count label
                                            JLabel countLabel = (JLabel) textPanel.getComponent(0);
                                            countLabel.setText(String.valueOf(count));
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* ───────────────── HEADER ───────────────── */
    private JComponent createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(8,0,8,0));
        
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
        // switch cards
        cardLayout.show(mainView, label);
        // update nav highlight
        navButtons.values().forEach(n -> n.setSelected(false));
        navButtons.get(label).setSelected(true);
    }

    /* ───────────────── MAIN VIEW ───────────────── */
    private JComponent createMainView() {
        // dashboard (home) panel
        JPanel dashboardPanel = makeDashboardCards();

        // placeholder content panels for other views
        mainView.add(dashboardPanel, "Dashboard");
        mainView.add(new DispatchFrame(), "Dispatch");
        mainView.add(new ParcelsFrame(), "Parcels");
        mainView.add(new TrackParcelsFrame(), "Track Parcels");
        mainView.add(new ComplaintsFrame(), "Complaints");
        mainView.add(new ContactsFrame(), "Contact");
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

        JLabel home = new JLabel("Home");
        home.setFont(new Font("SansSerif", Font.BOLD, 24));
        home.setBorder(new EmptyBorder(16,25,8,0));
        container.add(home, BorderLayout.NORTH);
        container.add(new JSeparator(), BorderLayout.AFTER_LAST_LINE);

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

        JLabel icon = new JLabel(IconFactory.get(iconKey));
        icon.setBorder(new EmptyBorder(0,0,0,16));

        card.add(textPan, BorderLayout.CENTER);
        card.add(icon,     BorderLayout.EAST);
        return card;
    }

    /* ───────────────── NAV ITEM CLASS ───────────────── */
    private class NavItem extends JPanel {
        private final JLabel lbl;
        private final String text;
        NavItem(String text, String iconKey) {
            this.text = text;
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            new DashboardFrame().setVisible(true);
        });
    }
}
