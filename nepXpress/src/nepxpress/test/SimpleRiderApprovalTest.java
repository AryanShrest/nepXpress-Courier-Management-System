package nepxpress.test;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import nepxpress.database.RiderDAO;
import nepxpress.database.RiderInfo;

public class SimpleRiderApprovalTest extends JFrame {
    
    public SimpleRiderApprovalTest() {
        setTitle("Simple Rider Approval Test");
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
        
        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        if (pendingRiders.isEmpty()) {
            JLabel noRidersLabel = new JLabel("No pending rider registrations found");
            noRidersLabel.setHorizontalAlignment(JLabel.CENTER);
            noRidersLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            panel.add(noRidersLabel, BorderLayout.CENTER);
        } else {
            // Add each rider as a panel with approve/reject buttons
            for (RiderInfo rider : pendingRiders) {
                JPanel riderPanel = createRiderPanel(rider);
                contentPanel.add(riderPanel);
                contentPanel.add(Box.createVerticalStrut(20));
            }
            
            // Add content panel to scroll pane
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            panel.add(scrollPane, BorderLayout.CENTER);
        }
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshPanel());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(panel);
    }
    
    private JPanel createRiderPanel(RiderInfo rider) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Rider info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        infoPanel.setBackground(Color.WHITE);
        
        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(new JLabel(rider.getFullName()));
        
        infoPanel.add(new JLabel("Contact:"));
        infoPanel.add(new JLabel(rider.getEmailOrMobile()));
        
        infoPanel.add(new JLabel("Vehicle:"));
        infoPanel.add(new JLabel(rider.getVehicleType() + " (" + rider.getVehicleRegistration() + ")"));
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton approveButton = new JButton("Approve");
        approveButton.setBackground(new Color(76, 175, 80));
        approveButton.setForeground(Color.WHITE);
        approveButton.setBorderPainted(false);
        approveButton.setFocusPainted(false);
        approveButton.addActionListener(e -> approveRider(rider.getId(), rider.getEmailOrMobile()));
        
        JButton rejectButton = new JButton("Reject");
        rejectButton.setBackground(new Color(244, 67, 54));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setBorderPainted(false);
        rejectButton.setFocusPainted(false);
        rejectButton.addActionListener(e -> rejectRider(rider.getId(), rider.getEmailOrMobile()));
        
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void refreshPanel() {
        // Just recreate the entire panel
        SwingUtilities.invokeLater(() -> {
            SimpleRiderApprovalTest newFrame = new SimpleRiderApprovalTest();
            newFrame.setVisible(true);
            this.dispose();
        });
    }
    
    private void approveRider(int riderId, String contactInfo) {
        // Update rider status to Active
        RiderDAO riderDAO = new RiderDAO();
        boolean success = riderDAO.updateRiderStatus(riderId, "Active");
        
        if (success) {
            try {
                JOptionPane.showMessageDialog(this,
                    "Rider approved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
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
        boolean success = riderDAO.updateRiderStatus(riderId, "Suspended");
        
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
            SimpleRiderApprovalTest test = new SimpleRiderApprovalTest();
            test.setVisible(true);
        });
    }
} 