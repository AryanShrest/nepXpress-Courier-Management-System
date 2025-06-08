package nepxpress.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrackParcelsPanel extends JPanel {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField mobileField;
    private JTextField emailField;
    private JTextField courierTypeField;
    private JTextField referenceField;
    private JButton trackButton;

    public TrackParcelsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setupUI();
    }

    private void setupUI() {
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(16,25,16,25));

        JLabel headerLabel = new JLabel("Track Parcels");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(headerLabel);
        
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

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 25, 30, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels column
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = createLabel("Enter Name:");
        formPanel.add(nameLabel, gbc);

        gbc.gridy++;
        JLabel addressLabel = createLabel("Enter Address:");
        formPanel.add(addressLabel, gbc);

        gbc.gridy++;
        JLabel mobileLabel = createLabel("Enter Mobile Number:");
        formPanel.add(mobileLabel, gbc);

        gbc.gridy++;
        JLabel emailLabel = createLabel("Enter email:");
        formPanel.add(emailLabel, gbc);

        gbc.gridy++;
        JLabel typeLabel = createLabel("Enter Courier Type:");
        formPanel.add(typeLabel, gbc);

        gbc.gridy++;
        JLabel refLabel = createLabel("Enter Reference Number:");
        formPanel.add(refLabel, gbc);

        // Fields column
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = createTextField();
        formPanel.add(nameField, gbc);

        gbc.gridy++;
        addressField = createTextField();
        formPanel.add(addressField, gbc);

        gbc.gridy++;
        mobileField = createTextField();
        formPanel.add(mobileField, gbc);

        gbc.gridy++;
        emailField = createTextField();
        formPanel.add(emailField, gbc);

        gbc.gridy++;
        courierTypeField = createTextField();
        formPanel.add(courierTypeField, gbc);

        gbc.gridy++;
        referenceField = createTextField();
        formPanel.add(referenceField, gbc);

        // Track Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(30, 10, 15, 10);
        
        trackButton = new JButton("TRACK");
        trackButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        trackButton.setForeground(Color.WHITE);
        trackButton.setBackground(new Color(89, 171, 32));
        trackButton.setPreferredSize(new Dimension(120, 40));
        trackButton.setBorderPainted(false);
        trackButton.setFocusPainted(false);
        trackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trackButton.addActionListener(e -> handleTrackButtonClick());
        formPanel.add(trackButton, gbc);

        // Add form to a scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void handleTrackButtonClick() {
        // TODO: Implement tracking logic
        String reference = referenceField.getText().trim();
        if (reference.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a reference number",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Add your tracking logic here
    }
} 