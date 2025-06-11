package com.courier.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.HashMap;
import java.util.Map;

// Import required panel classes
import com.courier.ui.DispatchPanel;
import com.courier.ui.ParcelsFrame;
import com.courier.ui.TrackParcelsPanel;
import com.courier.ui.ComplaintsFrame;
import com.courier.ui.ContactsFrame;
import com.courier.ui.DeactivateAccountFrame;

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
    private static final Font NAV_FONT   = new Font("SansSerif", Font.BOLD, 14);
    private static final Font COUNT_FONT = new Font("SansSerif", Font.BOLD, 22);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);

    /* ─────  FIELDS  ───── */
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     mainView   = new JPanel(cardLayout);
    private final Map<String, NavItem> navButtons = new HashMap<>();

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
        
        JLabel userLbl = new JLabel("User");
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
        
        // Add Deactivate Account button
        NavItem deactivateNav = makeNav("Deactivate Account", null);
        deactivateNav.setBorder(new EmptyBorder(2, 0, 2, 0));
        navPanel.add(deactivateNav);
        
        side.add(navPanel);
        side.add(Box.createVerticalGlue());
        
        NavItem logoutBtn = new NavItem("Logout", null);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
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

        // Create deactivate account panel by getting content from DeactivateAccountFrame
        DeactivateAccountFrame deactivateAccountFrame = new DeactivateAccountFrame();
        JPanel deactivateAccountPanel = deactivateAccountFrame.getMainPanel();

        // Add all panels to card layout
        mainView.add(dashboardPanel, "Dashboard");
        mainView.add(dispatchPanel, "Dispatch");
        mainView.add(parcelsPanel, "Parcels");
        mainView.add(trackParcelsPanel, "Track Parcels");
        mainView.add(complaintsPanel, "Complaints");
        mainView.add(contactsPanel, "Contact");
        mainView.add(deactivateAccountPanel, "Deactivate Account");

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

    /* ─────────────────── MAIN ─────────────────── */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
