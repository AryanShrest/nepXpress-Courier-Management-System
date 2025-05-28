package nepxpress.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ForgotPasswordView extends javax.swing.JFrame {
    
    private RoundedPanel mainPanel;
    private JLabel titleLabel;
    private RoundedTextField usernameField, phoneField;
    private JButton verifyButton;
    
    public ForgotPasswordView() {
        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(null);
        setTitle("Forgot Password - nepXpress");
    }
    
    private void initComponents() {
        // Main panel with light blue background
        mainPanel = new RoundedPanel(40);
        mainPanel.setBackground(new Color(188, 212, 230));
        
        // Title
        titleLabel = new JLabel("Forgot Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Username field
        usernameField = new RoundedTextField(25);
        usernameField.setPlaceholder("Username");
        
        // Phone field
        phoneField = new RoundedTextField(25);
        phoneField.setPlaceholder("Verify Phone Number");
        
        // Verify button
        verifyButton = new JButton("Verify Phone Number");
        verifyButton.setBackground(new Color(157, 205, 90));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setFocusPainted(false);
        verifyButton.setBorderPainted(false);
        verifyButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Layout
        setLayout(new BorderLayout());
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add components with proper spacing
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 40, 30, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 40, 15, 40);
        mainPanel.add(usernameField, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 40, 25, 40);
        mainPanel.add(phoneField, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 40, 30, 40);
        mainPanel.add(verifyButton, gbc);
        
        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        
        // Add window listener for smooth fade effect
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                fadeIn();
            }
        });
    }
    
    private void fadeIn() {
        final float[] opacity = {0f};
        final Timer timer = new Timer(50, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity[0] += 0.1f;
                if (opacity[0] > 1f) {
                    opacity[0] = 1f;
                    timer.stop();
                }
                setOpacity(opacity[0]);
            }
        });
        setOpacity(0f);
        timer.start();
    }
    
    private class RoundedTextField extends JTextField {
        private String placeholder;
        private int radius;
        
        public RoundedTextField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            setPreferredSize(new Dimension(200, 40));
            setBackground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            
            super.paintComponent(g);
            
            if (getText().isEmpty() && placeholder != null) {
                g2 = (Graphics2D) g.create();
                g2.setColor(new Color(150, 150, 150));
                g2.setFont(getFont());
                FontMetrics metrics = g2.getFontMetrics();
                int x = 15;
                int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }
    
    private class RoundedPanel extends JPanel {
        private int radius;
        
        public RoundedPanel(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
    }
} 