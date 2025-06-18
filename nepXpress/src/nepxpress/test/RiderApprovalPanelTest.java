package nepxpress.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultCellEditor;
import java.awt.*;
import java.util.List;
import nepxpress.database.RiderDAO;
import nepxpress.database.RiderInfo;

public class RiderApprovalPanelTest extends JFrame {
    
    public RiderApprovalPanelTest() {
        setTitle("Rider Approval Panel Test");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add title
        JLabel titleLabel = new JLabel("Rider Registration Approvals");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Fetch pending rider registrations
        RiderDAO riderDAO = new RiderDAO();
        List<RiderInfo> pendingRiders = riderDAO.getPendingRiders();
        
        // Create table model
        String[] columns = {"ID", "Name", "Contact", "Vehicle Type", "License", "Reg. Number", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only action column is editable
            }
        };
        
        // Add pending riders to table
        for (RiderInfo rider : pendingRiders) {
            model.addRow(new Object[] {
                rider.getId(),
                rider.getFullName(),
                rider.getEmailOrMobile(),
                rider.getVehicleType(),
                rider.getLicenseNumber(),
                rider.getVehicleRegistration(),
                "Actions"
            });
        }
        
        // Create table
        JTable riderTable = new JTable(model);
        riderTable.setRowHeight(40);
        riderTable.getTableHeader().setReorderingAllowed(false);
        riderTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        riderTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        riderTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        riderTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        riderTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        riderTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        riderTable.getColumnModel().getColumn(6).setPreferredWidth(200);
        
        // Add action buttons to table
        riderTable.getColumnModel().getColumn(6).setCellRenderer(new RiderActionButtonRenderer());
        riderTable.getColumnModel().getColumn(6).setCellEditor(new RiderActionButtonEditor(new JCheckBox()));
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(riderTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add no riders message if needed
        if (pendingRiders.isEmpty()) {
            JLabel noRidersLabel = new JLabel("No pending rider registrations found");
            noRidersLabel.setHorizontalAlignment(JLabel.CENTER);
            noRidersLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            panel.add(noRidersLabel, BorderLayout.CENTER);
        }
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshPanel());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(panel);
    }
    
    private void refreshPanel() {
        // Just recreate the entire panel
        SwingUtilities.invokeLater(() -> {
            RiderApprovalPanelTest newFrame = new RiderApprovalPanelTest();
            newFrame.setVisible(true);
            this.dispose();
        });
    }
    
    // Button renderer for rider approval actions
    class RiderActionButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton approveButton;
        private JButton rejectButton;
        
        public RiderActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            approveButton = new JButton("Approve");
            approveButton.setBackground(new Color(76, 175, 80));
            approveButton.setForeground(Color.WHITE);
            approveButton.setBorderPainted(false);
            approveButton.setFocusPainted(false);
            
            rejectButton = new JButton("Reject");
            rejectButton.setBackground(new Color(244, 67, 54));
            rejectButton.setForeground(Color.WHITE);
            rejectButton.setBorderPainted(false);
            rejectButton.setFocusPainted(false);
            
            add(approveButton);
            add(rejectButton);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // Button editor for rider approval actions
    class RiderActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton approveButton;
        private JButton rejectButton;
        private int riderId;
        private String riderEmail;
        
        public RiderActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            approveButton = new JButton("Approve");
            approveButton.setBackground(new Color(76, 175, 80));
            approveButton.setForeground(Color.WHITE);
            approveButton.setBorderPainted(false);
            approveButton.setFocusPainted(false);
            approveButton.addActionListener(e -> {
                approveRider(riderId, riderEmail);
                fireEditingStopped();
            });
            
            rejectButton = new JButton("Reject");
            rejectButton.setBackground(new Color(244, 67, 54));
            rejectButton.setForeground(Color.WHITE);
            rejectButton.setBorderPainted(false);
            rejectButton.setFocusPainted(false);
            rejectButton.addActionListener(e -> {
                rejectRider(riderId, riderEmail);
                fireEditingStopped();
            });
            
            panel.add(approveButton);
            panel.add(rejectButton);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            riderId = (int) table.getValueAt(row, 0);
            riderEmail = (String) table.getValueAt(row, 2);
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
    
    private void approveRider(int riderId, String contactInfo) {
        // Update rider status to Active
        RiderDAO riderDAO = new RiderDAO();
        boolean success = riderDAO.updateRiderStatus(riderId, "Active");
        
        if (success) {
            try {
                if (contactInfo.contains("@")) {
                    JOptionPane.showMessageDialog(this,
                        "Rider approved successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                refreshPanel();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Rider approved but failed to refresh: " + e.getMessage(),
                    "Partial Success",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to approve rider. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejectRider(int riderId, String contactInfo) {
        // Update rider status to Rejected
        RiderDAO riderDAO = new RiderDAO();
        boolean success = riderDAO.updateRiderStatus(riderId, "Rejected");
        
        if (success) {
            try {
                JOptionPane.showMessageDialog(this,
                    "Rider rejected successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                refreshPanel();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Rider rejected but failed to refresh: " + e.getMessage(),
                    "Partial Success",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to reject rider. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RiderApprovalPanelTest test = new RiderApprovalPanelTest();
            test.setVisible(true);
        });
    }
} 