package nepxpress.view;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordView extends javax.swing.JFrame {

    private class RoundedPanel extends javax.swing.JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
            setBorder(null);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
    }

    private javax.swing.JButton resetButton;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JLabel backToLoginLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel whitePanel;
    private javax.swing.JLabel logoLabel;

    public ForgotPasswordView() {
        initComponents();
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        whitePanel = new RoundedPanel(40);
        titleLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        messageLabel = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        backToLoginLabel = new javax.swing.JLabel();
        logoLabel = new javax.swing.JLabel();

        // Setup main panel
        mainPanel.setBackground(new java.awt.Color(172, 200, 229));

        // Setup white panel
        whitePanel.setBackground(new java.awt.Color(255, 255, 255));

        // Setup title
        titleLabel.setFont(new java.awt.Font("Segoe UI", 0, 24));
        titleLabel.setText("Forgot Password");

        // Setup email field
        emailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
        emailLabel.setText("Email Address");
        
        emailField.setFont(new java.awt.Font("Segoe UI", 0, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        emailField.setBackground(new Color(240, 240, 240));

        // Setup message
        messageLabel.setFont(new java.awt.Font("Segoe UI", 0, 12));
        messageLabel.setText("Once you have submitted the form, you will receive an email");
        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        JLabel messageLabel2 = new JLabel("if the given email address exist in our system.");
        messageLabel2.setFont(new java.awt.Font("Segoe UI", 0, 12));
        messageLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Setup reset button
        resetButton.setBackground(new java.awt.Color(220, 20, 60));
        resetButton.setFont(new java.awt.Font("Segoe UI", 0, 14));
        resetButton.setForeground(new java.awt.Color(255, 255, 255));
        resetButton.setText("Reset");
        resetButton.setBorderPainted(false);
        resetButton.setFocusPainted(false);

        // Setup back to login link
        backToLoginLabel.setFont(new java.awt.Font("Segoe UI", 0, 12));
        backToLoginLabel.setText("<html><u>Back to Login</u></html>");
        backToLoginLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backToLoginLabel.setForeground(new java.awt.Color(76, 115, 13));
        backToLoginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
            }
        });

        // Setup logo
        logoLabel.setFont(new java.awt.Font("Segoe UI", 0, 24));
        logoLabel.setForeground(new java.awt.Color(255, 255, 255));
        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nepxpress/view/Screenshot 2025-05-27 184033.png")));
        logoLabel.setText("nepXpress");
        logoLabel.setText("nepXpress");
        logoLabel.setText("Delivery");

        // Layout for white panel
        GroupLayout whitePanelLayout = new GroupLayout(whitePanel);
        whitePanel.setLayout(whitePanelLayout);
        whitePanelLayout.setHorizontalGroup(
            whitePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(whitePanelLayout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(whitePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(titleLabel)
                    .addComponent(emailField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                    .addComponent(messageLabel)
                    .addComponent(messageLabel2)
                    .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                    .addComponent(backToLoginLabel))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        whitePanelLayout.setVerticalGroup(
            whitePanelLayout.createSequentialGroup()
            .addGap(30)
            .addComponent(titleLabel)
            .addGap(30)
            .addComponent(emailField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
            .addGap(20)
            .addComponent(messageLabel)
            .addGap(5)
            .addComponent(messageLabel2)
            .addGap(30)
            .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
            .addGap(20)
            .addComponent(backToLoginLabel)
            .addContainerGap(30, Short.MAX_VALUE)
        );

        // Layout for main panel
        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(300, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(logoLabel)
                    .addComponent(whitePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(300, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createSequentialGroup()
            .addGap(100)
            .addComponent(logoLabel)
            .addGap(30)
            .addComponent(whitePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(100, Short.MAX_VALUE)
        );

        // Window layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ForgotPasswordView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new ForgotPasswordView().setVisible(true);
        });
    }
} 