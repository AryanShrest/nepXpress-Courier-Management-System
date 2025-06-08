package nepxpress.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import java.awt.Container;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Cursor;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import nepxpress.database.UserDAO;
import nepxpress.database.UserSessionDAO;
import javax.swing.SwingUtilities;
import javax.mail.MessagingException;
import nepxpress.util.EmailUtil;

/**
 *
 * @author Asus
 */
public class RegisterView extends javax.swing.JFrame {

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

    private class RoundedBorder extends javax.swing.border.AbstractBorder {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getBackground());
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }
    }

    private class RoundedPasswordField extends javax.swing.JPasswordField {
        private int radius;

        public RoundedPasswordField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class PlaceholderTextField extends javax.swing.JTextField {
        private String placeholder;
        private int radius;

        public PlaceholderTextField(String placeholder, int radius) {
            super();
            this.placeholder = placeholder;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            
            super.paintComponent(g);

            if (getText().isEmpty() && placeholder != null) {
                g2 = (java.awt.Graphics2D) g.create();
                g2.setColor(new Color(150, 150, 150));
                g2.setFont(getFont());
                java.awt.FontMetrics metrics = g2.getFontMetrics();
                int x = 15;
                int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }

    private class RoundedTextField extends javax.swing.JTextField {
        private int radius;
        public RoundedTextField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Store the original login panel layout
    private GroupLayout originalLayout;
    private Component[] loginComponents;

    /**
     * Creates new form RegisterView
     */
    public RegisterView() {
        initComponents();
        // setSize(900, 600); // Comment this out
        // setLocationRelativeTo(null); // Comment this out

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
            }
        });
        originalLayout = (GroupLayout) jPanel2.getLayout();
        loginComponents = jPanel2.getComponents();
        
        // Set white background for panel2
        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        // Style password fields with rounded corners
        javax.swing.border.Border emptyBorder = javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15);
        
        jPhoneField.setBorder(emptyBorder);
        jPhoneField.setBackground(new java.awt.Color(240, 240, 240));
        
        jPasswordField2.setBorder(emptyBorder);
        jPasswordField2.setBackground(new java.awt.Color(240, 240, 240));

        // Add underline to forgot password text and make it clickable
        jLabel3.setText("<html><u>Forgot password?</u></html>");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchToForgotPassword();
            }
        });
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new RoundedPanel(40);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPhoneField = new RoundedTextField(25);
        jPasswordField2 = new RoundedPasswordField(25);
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jLabel4 = new javax.swing.JLabel();
        toggleBgPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Setup main panel
        jPanel1.setBackground(new java.awt.Color(172, 200, 229));
        
        // Setup white panel
        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        
        // Setup components
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel1.setText("Phone Number :");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel2.setText("Password :");

        jPhoneField.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPhoneField.setPreferredSize(new java.awt.Dimension(293, 28));
        jPhoneField.setBackground(new java.awt.Color(240, 240, 240));
        jPhoneField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));

        jButton1.setBackground(new java.awt.Color(157, 205, 90));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        // Setup toggle background panel
        toggleBgPanel.setBackground(new java.awt.Color(0, 0, 0));
        toggleBgPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        toggleBgPanel.setPreferredSize(new java.awt.Dimension(120, 20));

        // Setup login toggle button
        jToggleButton1.setText("Login");
        jToggleButton1.setFont(new java.awt.Font("Segoe UI", 0, 11));
        jToggleButton1.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setBackground(new java.awt.Color(51, 122, 183));
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.setContentAreaFilled(true);
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.setPreferredSize(new java.awt.Dimension(65, 24));
        jToggleButton1.setMinimumSize(new java.awt.Dimension(65, 24));
        jToggleButton1.setMaximumSize(new java.awt.Dimension(65, 24));
        jToggleButton1.setMargin(new java.awt.Insets(0, 2, 0, 2));
        jToggleButton1.setSelected(true);

        // Setup signup toggle button
        jToggleButton2.setText("Signup");
        jToggleButton2.setFont(new java.awt.Font("Segoe UI", 0, 11));
        jToggleButton2.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton2.setBackground(new java.awt.Color(0, 0, 0));
        jToggleButton2.setBorderPainted(false);
        jToggleButton2.setContentAreaFilled(true);
        jToggleButton2.setFocusPainted(false);
        jToggleButton2.setPreferredSize(new java.awt.Dimension(65, 24));
        jToggleButton2.setMinimumSize(new java.awt.Dimension(65, 24));
        jToggleButton2.setMaximumSize(new java.awt.Dimension(65, 24));
        jToggleButton2.setMargin(new java.awt.Insets(0, 2, 0, 2));

        // Add toggle button action listeners
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1.setBackground(new java.awt.Color(51, 122, 183));
                jToggleButton2.setBackground(new java.awt.Color(0, 0, 0));
                jToggleButton1.setSelected(true);
                jToggleButton2.setSelected(false);
            }
        });

        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2.setBackground(new java.awt.Color(51, 122, 183));
                jToggleButton1.setBackground(new java.awt.Color(0, 0, 0));
                jToggleButton2.setSelected(true);
                jToggleButton1.setSelected(false);
                // Switch to signup panel in the same window
                switchToSignup();
            }
        });

        // Setup logo
        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nepxpress/view/Screenshot 2025-05-27 184033.png")));
        jLabel4.setText("nepXpress");

        // Add toggle buttons to background panel with zero gaps
        javax.swing.GroupLayout toggleBgPanelLayout = new javax.swing.GroupLayout(toggleBgPanel);
        toggleBgPanel.setLayout(toggleBgPanelLayout);
        toggleBgPanelLayout.setHorizontalGroup(
            toggleBgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addGroup(toggleBgPanelLayout.createSequentialGroup()
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        toggleBgPanelLayout.setVerticalGroup(
            toggleBgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toggleBgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        // Setup white panel layout
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jPhoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .addComponent(jPasswordField2))
                .addContainerGap(37, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(14, 14, 14)
                .addComponent(jPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(37, 37, 37))
        );

        // Setup main panel layout
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel1Layout.createSequentialGroup()
                .addContainerGap(300, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(300, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(30, 30, 30)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
        );

        // Setup window layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jPasswordField2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String phoneNumber = jPhoneField.getText().trim();
        String password = new String(jPasswordField2.getPassword()).trim();
        
        // Validate input
        if (phoneNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both phone number and password.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate phone number format
        if (!phoneNumber.matches("^(977)?[9][6-9]\\d{8}$")) {
            JOptionPane.showMessageDialog(this,
                "Invalid phone number format. Please use format: 977XXXXXXX",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Create UserDAO instance
            UserDAO userDAO = new UserDAO();
            
            // Validate login
            if (userDAO.validateLogin(phoneNumber, password)) {
                int userId = userDAO.getUserIdByMobile(phoneNumber);
                if (userId == -1) {
                    JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(this,
                    "Login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                // TODO: Navigate to main application window
                UserSessionDAO sessionDAO = new UserSessionDAO();
                String sessionToken = sessionDAO.createSession(
                    userId,
                    "127.0.0.1", // or get local IP if needed
                    System.getProperty("os.name") // or any other info you want
                );
                // Store the session token (e.g., in a cookie or local storage)
                // You can then use this token to validate the user's session on subsequent requests
                if (sessionDAO.validateSession(sessionToken)) {
                    // User is logged in
                    // Open the user dashboard
                    SwingUtilities.invokeLater(() -> {
                        new DashboardFrame().setVisible(true);
                    });
                    dispose();
                } else {
                    // Session is invalid or expired
                    // Redirect to login page
                    switchToLogin();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid phone number or password.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "An error occurred during login: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean mockAuthenticate(String username, String password) {
        // TODO: Replace with actual database authentication
        // This is just a mock implementation for testing
        return username.equals("admin") && password.equals("admin123");
    }

    private void switchToForgotPassword() {
        System.out.println("Switching to forgot password view...");
        // Store the current size of white panel
        Dimension whiteBoxSize = jPanel2.getSize();
        
        // Create forgot password components
        JPanel forgotPanel = new RoundedPanel(40);
        forgotPanel.setBackground(Color.WHITE);
        forgotPanel.setLayout(new GroupLayout(forgotPanel));
        
        // Title
        JLabel titleLabel = new JLabel("Forgot Password");
        titleLabel.setFont(new Font("Segoe UI", 0, 24));
        
        // Email field
        PlaceholderTextField emailField = new PlaceholderTextField("Email Address", 25);
        emailField.setFont(new Font("Segoe UI", 0, 14));
        emailField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        emailField.setBackground(new Color(240, 240, 240));
        
        // Message
        JLabel messageLabel = new JLabel("Once you have submitted the form, you will receive an email");
        messageLabel.setFont(new Font("Segoe UI", 0, 12));
        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        JLabel messageLabel2 = new JLabel("if the given email address exist in our system.");
        messageLabel2.setFont(new Font("Segoe UI", 0, 12));
        messageLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(206, 255, 137));
        resetButton.setFont(new Font("Segoe UI", 0, 14));
        resetButton.setForeground(Color.BLACK);
        resetButton.setBorderPainted(false);
        resetButton.setFocusPainted(false);

        // Back to login link
        JLabel backToLoginLabel = new JLabel("<html><u>Back to Login</u></html>");
        backToLoginLabel.setFont(new Font("Segoe UI", 0, 12));
        backToLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLoginLabel.setForeground(new Color(76, 115, 13));
        backToLoginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchToLogin();
            }
        });

        // Add action listener to reset button
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println("Reset button clicked!");
                String email = emailField.getText().trim();
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterView.this,
                        "Please enter your email address.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    System.out.println("Attempting to send verification code to: " + email);
                    String verificationCode = EmailUtil.sendVerificationCode(email);
                    System.out.println("Verification code sent successfully!");
                    switchToVerificationView(email, verificationCode);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    System.out.println("Failed to send email: " + e.getMessage());
                    JOptionPane.showMessageDialog(RegisterView.this,
                        "Failed to send verification code: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Unexpected error: " + e.getMessage());
                    JOptionPane.showMessageDialog(RegisterView.this,
                        "An unexpected error occurred: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Layout
        GroupLayout layout = (GroupLayout) forgotPanel.getLayout();
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(titleLabel)
                    .addComponent(emailField, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
                    .addComponent(messageLabel)
                    .addComponent(messageLabel2)
                    .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
                    .addComponent(backToLoginLabel))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        
        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addGap(30)
            .addComponent(titleLabel)
            .addGap(30)
            .addComponent(emailField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
            .addGap(20)
            .addComponent(messageLabel)
            .addGap(5)
            .addComponent(messageLabel2)
            .addGap(30)
            .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
            .addGap(20)
            .addComponent(backToLoginLabel)
            .addContainerGap(30, Short.MAX_VALUE)
        );

        // Remove all components from white panel
        jPanel2.removeAll();
        
        // Add forgot password panel
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(forgotPanel, BorderLayout.CENTER);
        
        // Ensure the white panel maintains its size
        jPanel2.setPreferredSize(whiteBoxSize);
        jPanel2.setSize(whiteBoxSize);
        
        // Refresh the panel
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    public void switchToLogin() {
        // Set toggle button states
        jToggleButton1.setBackground(new java.awt.Color(51, 122, 183));
        jToggleButton2.setBackground(new java.awt.Color(0, 0, 0));
        jToggleButton1.setSelected(true);
        jToggleButton2.setSelected(false);
        
        // Remove all components
        jPanel2.removeAll();
        
        // Restore original login components
        jPanel2.setLayout(originalLayout);
        
        // Add toggle buttons
        jPanel2.add(jToggleButton1);
        jPanel2.add(jToggleButton2);
        
        // Add other login components
        jPanel2.add(jLabel1);  // Username label
        jPanel2.add(jPhoneField);  // Username field
        jPanel2.add(jLabel2);  // Password label
        jPanel2.add(jPasswordField2);  // Password field
        jPanel2.add(jLabel3);  // Forgot password link
        jPanel2.add(jButton1);  // Login button
        
        // Update the layout
        javax.swing.GroupLayout jPanel2Layout = (javax.swing.GroupLayout) jPanel2.getLayout();
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel2Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addGap(10, 10, 10)
                .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addGap(25, 25, 25)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        
        // Refresh the panel
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void switchToVerificationView(String email, String verificationCode) {
        System.out.println("Switching to verification view...");
        // Store the current size of white panel
        Dimension whiteBoxSize = jPanel2.getSize();
        
        // Create verification panel
        JPanel verificationPanel = new RoundedPanel(40);
        verificationPanel.setBackground(Color.WHITE);
        verificationPanel.setLayout(new GroupLayout(verificationPanel));
        
        // Title
        JLabel titleLabel = new JLabel("Verify Your Email");
        titleLabel.setFont(new Font("Segoe UI", 0, 24));
        
        // Code field
        PlaceholderTextField codeField = new PlaceholderTextField("Enter Verification Code", 25);
        codeField.setFont(new Font("Segoe UI", 0, 14));
        codeField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        codeField.setBackground(new Color(240, 240, 240));
        
        // Message
        JLabel messageLabel = new JLabel("Please enter the verification code sent to your email:");
        messageLabel.setFont(new Font("Segoe UI", 0, 12));
        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        // Verify button
        JButton verifyButton = new JButton("Verify");
        verifyButton.setBackground(new Color(206, 255, 137));
        verifyButton.setFont(new Font("Segoe UI", 0, 14));
        verifyButton.setForeground(Color.BLACK);
        verifyButton.setBorderPainted(false);
        verifyButton.setFocusPainted(false);
        
        // Add action listener to verify button
        verifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String enteredCode = codeField.getText().trim();
                if (enteredCode.equals(verificationCode)) {
                    // Code matches - proceed to reset password
                    switchToResetPasswordView(email);
                } else {
                    JOptionPane.showMessageDialog(RegisterView.this,
                        "Invalid verification code. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Back button
        JLabel backButton = new JLabel("<html><u>Back</u></html>");
        backButton.setFont(new Font("Segoe UI", 0, 12));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setForeground(new Color(76, 115, 13));
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchToForgotPassword();
            }
        });
        
        // Layout
        GroupLayout layout = (GroupLayout) verificationPanel.getLayout();
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(titleLabel)
                    .addComponent(messageLabel)
                    .addComponent(emailLabel)
                    .addComponent(codeField, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
                    .addComponent(verifyButton, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
                    .addComponent(backButton))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        
        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addGap(30)
            .addComponent(titleLabel)
            .addGap(30)
            .addComponent(messageLabel)
            .addGap(5)
            .addComponent(emailLabel)
            .addGap(20)
            .addComponent(codeField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
            .addGap(30)
            .addComponent(verifyButton, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
            .addGap(20)
            .addComponent(backButton)
            .addContainerGap(30, Short.MAX_VALUE)
        );
        
        // Remove all components from white panel
        jPanel2.removeAll();
        
        // Add verification panel
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(verificationPanel, BorderLayout.CENTER);
        
        // Ensure the white panel maintains its size
        jPanel2.setPreferredSize(whiteBoxSize);
        jPanel2.setSize(whiteBoxSize);
        
        // Refresh the panel
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void switchToResetPasswordView(String email) {
        System.out.println("Switching to reset password view...");
        // Store the current size of white panel
        Dimension whiteBoxSize = jPanel2.getSize();
        
        // Create reset password panel
        JPanel resetPanel = new RoundedPanel(40);
        resetPanel.setBackground(Color.WHITE);
        resetPanel.setLayout(new GroupLayout(resetPanel));
        
        // Title
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font("Segoe UI", 0, 24));
        
        // Password fields
        RoundedPasswordField newPasswordField = new RoundedPasswordField(25);
        newPasswordField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        newPasswordField.setBackground(new Color(240, 240, 240));
        
        RoundedPasswordField confirmPasswordField = new RoundedPasswordField(25);
        confirmPasswordField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        confirmPasswordField.setBackground(new Color(240, 240, 240));
        
        // Labels
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(new Font("Segoe UI", 0, 14));
        
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", 0, 14));
        
        // Submit button
        JButton submitButton = new JButton("Change Password");
        submitButton.setBackground(new Color(206, 255, 137));
        submitButton.setFont(new Font("Segoe UI", 0, 14));
        submitButton.setForeground(Color.BLACK);
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        
        // Add action listener to submit button
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                
                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterView.this,
                        "Please fill in all fields.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterView.this,
                        "Passwords do not match.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // TODO: Update password in database
                // For now, just show success message and return to login
                JOptionPane.showMessageDialog(RegisterView.this,
                    "Password has been reset successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                switchToLogin();
            }
        });
        
        // Layout
        GroupLayout layout = (GroupLayout) resetPanel.getLayout();
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, GroupLayout.Alignment.CENTER)
                    .addComponent(newPasswordLabel)
                    .addComponent(newPasswordField, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmPasswordLabel)
                    .addComponent(confirmPasswordField, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitButton, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        
        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addGap(30)
            .addComponent(titleLabel)
            .addGap(30)
            .addComponent(newPasswordLabel)
            .addGap(5)
            .addComponent(newPasswordField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
            .addGap(20)
            .addComponent(confirmPasswordLabel)
            .addGap(5)
            .addComponent(confirmPasswordField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
            .addGap(30)
            .addComponent(submitButton, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(30, Short.MAX_VALUE)
        );
        
        // Remove all components from white panel
        jPanel2.removeAll();
        
        // Add reset password panel
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(resetPanel, BorderLayout.CENTER);
        
        // Ensure the white panel maintains its size
        jPanel2.setPreferredSize(whiteBoxSize);
        jPanel2.setSize(whiteBoxSize);
        
        // Refresh the panel
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    // Add this new method to switch to signup panel
    private void switchToSignup() {
        // Store the current size of white panel
        Dimension whiteBoxSize = jPanel2.getSize();
        
        // Create signup panel
        SignupView signupView = new SignupView();
        JPanel signupContent = signupView.getMainPanel();
        
        // Remove all components from white panel
        jPanel2.removeAll();
        
        // Add signup panel
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(signupContent, BorderLayout.CENTER);
        
        // Ensure the white panel maintains its size
        jPanel2.setPreferredSize(whiteBoxSize);
        jPanel2.setSize(whiteBoxSize);
        
        // Refresh the panel
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RegisterView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegisterView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegisterView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RegisterView view = new RegisterView();
                view.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JPanel toggleBgPanel;
    private javax.swing.JTextField jPhoneField;
    // End of variables declaration//GEN-END:variables
}