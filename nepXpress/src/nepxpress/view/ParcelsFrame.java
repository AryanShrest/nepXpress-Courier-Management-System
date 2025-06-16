package nepxpress.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

public class ParcelsFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> entriesComboBox;
    private JButton prevButton, nextButton;
    private JLabel pageInfoLabel;

    public ParcelsFrame() {
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
        headerPanel.setBorder(new EmptyBorder(16,25,16,25));

        JLabel parcelsTitle = new JLabel("Parcels");
        parcelsTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        parcelsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(parcelsTitle);
        
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

        // Controls Panel (Show entries and Search)
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(Color.WHITE);
        controlsPanel.setBorder(new EmptyBorder(20, 25, 10, 25));

        // Show entries combo box
        JPanel showEntriesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        showEntriesPanel.setBackground(Color.WHITE);
        JLabel showLabel = new JLabel("Show");
        entriesComboBox = new JComboBox<>(new String[]{"10", "25", "50", "100"});
        JLabel entriesLabel = new JLabel("entries");
        showEntriesPanel.add(showLabel);
        showEntriesPanel.add(entriesComboBox);
        showEntriesPanel.add(entriesLabel);

        // Search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        controlsPanel.add(showEntriesPanel, BorderLayout.WEST);
        controlsPanel.add(searchPanel, BorderLayout.EAST);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(0, 25, 20, 25));

        // Create table
        String[] columns = {"#", "Reference Number", "Sender Name", "Receiver Name", "Status", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only Action column is editable
            }
        };

        table = new JTable(model);
        table.setRowHeight(50);
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(245, 245, 245));

        // Style the table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // #
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Reference Number
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Sender Name
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Receiver Name
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(5).setPreferredWidth(250); // Action - Increased width from 200 to 250

        // Add sample data
        model.addRow(new Object[]{1, "505604168988", "John Smith", "Sample", "Collected", ""});
        model.addRow(new Object[]{2, "117967400213", "John Smith", "Claire Blake", "Collected", ""});

        // Style the status column
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusColumnRenderer());

        // Set custom renderer and editor for Action column with multiple buttons
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonsRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ActionButtonsEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Pagination Panel
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(Color.WHITE);
        paginationPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        pageInfoLabel = new JLabel("Showing 1 to 2 of 2 entries");
        prevButton = createPaginationButton("Previous");
        nextButton = createPaginationButton("Next");
        JLabel currentPage = new JLabel("1");
        currentPage.setBackground(new Color(65, 105, 225));
        currentPage.setForeground(Color.WHITE);
        currentPage.setOpaque(true);
        currentPage.setBorder(new EmptyBorder(5, 10, 5, 10));

        paginationPanel.add(pageInfoLabel);
        paginationPanel.add(Box.createHorizontalStrut(20));
        paginationPanel.add(prevButton);
        paginationPanel.add(currentPage);
        paginationPanel.add(nextButton);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(paginationPanel, BorderLayout.SOUTH);

        mainPanel.add(controlsPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JButton createPaginationButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Custom renderer for the Status column
    private class StatusColumnRenderer extends JLabel implements TableCellRenderer {
        public StatusColumnRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("SansSerif", Font.PLAIN, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            if ("Collected".equals(value)) {
                setBackground(new Color(70, 192, 222));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            }
            return this;
        }
    }

    // Custom renderer for the Action column buttons
    private class ActionButtonsRenderer extends JPanel implements TableCellRenderer {
        private JButton viewBtn, editBtn, deleteBtn;

        public ActionButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));  // Increased horizontal gap from 3 to 5
            setBackground(Color.WHITE);

            viewBtn = createActionButton("View", new Color(70, 192, 222));    // Light blue
            editBtn = createActionButton("Update", new Color(65, 105, 225));  // Royal blue
            deleteBtn = createActionButton("Delete", new Color(220, 53, 69)); // Red

            add(viewBtn);
            add(editBtn);
            add(deleteBtn);
        }

        private JButton createActionButton(String text, Color bgColor) {
            JButton btn = new JButton(text);
            btn.setBackground(bgColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(75, 30));  // Increased width from 65 to 75
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom editor for the Action column buttons
    private class ActionButtonsEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton viewBtn, editBtn, deleteBtn;
        private String referenceNumber;

        public ActionButtonsEditor() {
            super(new JTextField());
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));  // Increased horizontal gap from 3 to 5
            panel.setBackground(Color.WHITE);

            viewBtn = createActionButton("View", new Color(70, 192, 222), e -> handleView());
            editBtn = createActionButton("Update", new Color(65, 105, 225), e -> handleEdit());
            deleteBtn = createActionButton("Delete", new Color(220, 53, 69), e -> handleDelete());

            panel.add(viewBtn);
            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        private JButton createActionButton(String text, Color bgColor, ActionListener listener) {
            JButton btn = new JButton(text);
            btn.setBackground(bgColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(75, 30));  // Increased width from 65 to 75
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(listener);
            return btn;
        }

        private void handleView() {
            JOptionPane.showMessageDialog(null, "Viewing parcel: " + referenceNumber);
            fireEditingStopped();
        }

        private void handleEdit() {
            JOptionPane.showMessageDialog(null, "Editing parcel: " + referenceNumber);
            fireEditingStopped();
        }

        private void handleDelete() {
            int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete parcel: " + referenceNumber + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Handle deletion
            }
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            referenceNumber = table.getValueAt(row, 1).toString();
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    public JPanel getMainPanel() {
        return (JPanel) getContentPane();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ParcelsFrame().setVisible(true);
        });
    }
} 