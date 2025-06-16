package nepxpress.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.sql.*;
import com.cms.database.DatabaseConnection;

public class Parcels extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> entriesCombo;
    private JLabel showingLabel;
    private JButton prevButton, nextButton, currentPageButton;
    private int currentPage = 1;
    private int entriesPerPage = 10;
    
    public Parcels() {
        setLayout(new BorderLayout());
        
        // Header Panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(76, 108, 153));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));
        JLabel titleLabel = new JLabel("nepXpress");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Sub Header
        JPanel subHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subHeaderPanel.setBackground(Color.WHITE);
        JLabel parcelsLabel = new JLabel("Parcels");
        parcelsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        subHeaderPanel.add(parcelsLabel);
        add(subHeaderPanel, BorderLayout.CENTER);
        
        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top Controls Panel
        JPanel controlsPanel = new JPanel(new BorderLayout());
        
        // Entries control
        JPanel entriesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel showLabel = new JLabel("Show");
        entriesCombo = new JComboBox<>(new String[]{"10", "25", "50", "100"});
        JLabel entriesLabel = new JLabel("entries");
        entriesPanel.add(showLabel);
        entriesPanel.add(entriesCombo);
        entriesPanel.add(entriesLabel);
        
        // Search control
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        controlsPanel.add(entriesPanel, BorderLayout.WEST);
        controlsPanel.add(searchPanel, BorderLayout.EAST);
        
        // Table
        String[] columns = {"#", "Reference Number", "Sender Name", "Receiver Name", "Status", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only action column is editable
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(40);
        
        // Custom renderer for the Status column
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(73, 168, 201));
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });
        
        // Custom renderer and editor for the Action column
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor());
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Pagination Panel
        JPanel paginationPanel = new JPanel(new BorderLayout());
        showingLabel = new JLabel("Showing 1 to 2 of 2 entries");
        
        JPanel pageButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prevButton = new JButton("Previous");
        currentPageButton = new JButton("1");
        nextButton = new JButton("Next");
        
        pageButtonsPanel.add(prevButton);
        pageButtonsPanel.add(currentPageButton);
        pageButtonsPanel.add(nextButton);
        
        paginationPanel.add(showingLabel, BorderLayout.WEST);
        paginationPanel.add(pageButtonsPanel, BorderLayout.EAST);
        
        contentPanel.add(controlsPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(paginationPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.SOUTH);
        
        // Add sample data
        addSampleData();
        
        // Add listeners
        setupListeners();
    }
    
    private void addSampleData() {
        Object[] row1 = {1, "505604168988", "John Smith", "Sample", "Collected", null};
        Object[] row2 = {2, "117967400213", "John Smith", "Claire Blake", "Collected", null};
        model.addRow(row1);
        model.addRow(row2);
    }
    
    private void setupListeners() {
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
        });
        
        entriesCombo.addActionListener(e -> updateEntriesPerPage());
    }
    
    private void search() {
        String searchText = searchField.getText().toLowerCase();
        // Implement search functionality
    }
    
    private void updateEntriesPerPage() {
        entriesPerPage = Integer.parseInt((String)entriesCombo.getSelectedItem());
        // Implement pagination update
    }
}

class ButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton viewBtn, editBtn, deleteBtn;
    
    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        viewBtn = createButton(new Color(73, 168, 201));
        editBtn = createButton(new Color(65, 105, 225));
        deleteBtn = createButton(new Color(220, 53, 69));
        
        add(viewBtn);
        add(editBtn);
        add(deleteBtn);
    }
    
    private JButton createButton(Color bgColor) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        return btn;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton viewBtn, editBtn, deleteBtn;
    
    public ButtonEditor() {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        viewBtn = createButton(new Color(73, 168, 201), "View");
        editBtn = createButton(new Color(65, 105, 225), "Edit");
        deleteBtn = createButton(new Color(220, 53, 69), "Delete");
        
        panel.add(viewBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        
        viewBtn.addActionListener(e -> handleView());
        editBtn.addActionListener(e -> handleEdit());
        deleteBtn.addActionListener(e -> handleDelete());
    }
    
    private JButton createButton(Color bgColor, String tooltip) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setToolTipText(tooltip);
        return btn;
    }
    
    private void handleView() {
        // Implement view functionality
    }
    
    private void handleEdit() {
        // Implement edit functionality
    }
    
    private void handleDelete() {
        // Implement delete functionality
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        return null;
    }
} 