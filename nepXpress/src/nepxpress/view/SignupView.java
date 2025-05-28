package nepxpress.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;

public class SignupView extends javax.swing.JFrame {
    
    private RoundedPanel mainPanel;
    private JLabel titleLabel, subtitleLabel;
    private RoundedTextField firstNameField, surnameField, emailField;
    private RoundedPasswordField passwordField;
    private JComboBox<String> dayBox, monthBox, yearBox;
    private JRadioButton femaleRadio, maleRadio, customRadio;
    private ButtonGroup genderGroup;
    private JButton signUpButton;
    private JLabel loginLink;
    
    public SignupView() {
        initComponents();
        setSize(900, 600);
        setLocationRelativeTo(null);
        setTitle("Create Account - nepXpress");
    }
    
    private void initComponents() {
        setupCustomScrollBars();
        
        mainPanel = new RoundedPanel(40);
        mainPanel.setBackground(Color.WHITE);
        
        // Title and subtitle
        titleLabel = new JLabel("Create a new account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        subtitleLabel = new JLabel("It's quick and easy.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Name fields
        firstNameField = new RoundedTextField(25);
        firstNameField.setPlaceholder("First name");
        
        surnameField = new RoundedTextField(25);
        surnameField.setPlaceholder("Surname");
        
        // Email field
        emailField = new RoundedTextField(25);
        emailField.setPlaceholder("Mobile number or email address");
        
        // Password field
        passwordField = new RoundedPasswordField(25);
        passwordField.setPlaceholder("New password");
        
        // Date selection combo boxes
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }
        dayBox = new JComboBox<>(days);
        
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        monthBox = new JComboBox<>(months);
        
        String[] years = new String[100];
        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        yearBox = new JComboBox<>(years);
        
        // Gender radio buttons
        femaleRadio = new JRadioButton("Female");
        maleRadio = new JRadioButton("Male");
        customRadio = new JRadioButton("Custom");
        
        genderGroup = new ButtonGroup();
        genderGroup.add(femaleRadio);
        genderGroup.add(maleRadio);
        genderGroup.add(customRadio);
        
        // Sign up button
        signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(0, 164, 0));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        
        // Login link
        loginLink = new JLabel("<html><u>Already have an account?</u></html>");
        loginLink.setForeground(new Color(0, 102, 255));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Layout
        setLayout(new BorderLayout());
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 20, 10);
        mainPanel.add(subtitleLabel, gbc);
        
        // Name fields in one row
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 5);
        mainPanel.add(firstNameField, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(5, 5, 5, 10);
        mainPanel.add(surnameField, gbc);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridy = 4;
        mainPanel.add(passwordField, gbc);
        
        // Date selection
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Date of birth"));
        datePanel.add(dayBox);
        datePanel.add(monthBox);
        datePanel.add(yearBox);
        gbc.gridy = 5;
        mainPanel.add(datePanel, gbc);
        
        // Gender radio buttons
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(new JLabel("Gender"));
        genderPanel.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(customRadio);
        gbc.gridy = 6;
        mainPanel.add(genderPanel, gbc);
        
        // Sign up button
        gbc.gridy = 7;
        gbc.insets = new Insets(20, 10, 5, 10);
        mainPanel.add(signUpButton, gbc);
        
        // Login link
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 10, 10, 10);
        mainPanel.add(loginLink, gbc);
        
        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        
        // Add action listeners
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterView().setVisible(true);
                dispose();
            }
        });
    }
    
    private void setupCustomScrollBars() {
        // Apply custom scrollbar UI to all scrollbars in the application
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.thumb", new Color(200, 200, 200));
        UIManager.put("ScrollBar.track", new Color(240, 240, 240, 100));
        
        // Create a scrollable panel for the main content
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        
        // Add the scrollPane to the frame
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private class ModernScrollBarUI extends BasicScrollBarUI {
        private final int THUMB_SIZE = 6;  // Made thinner

        @Override
        protected Dimension getMinimumThumbSize() {
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                return new Dimension(THUMB_SIZE, THUMB_SIZE * 3);
            } else {
                return new Dimension(THUMB_SIZE * 3, THUMB_SIZE);
            }
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Paint a very subtle track
            g2.setColor(new Color(240, 240, 240, 100));
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                g2.fillRect(trackBounds.x + trackBounds.width/2 - 1, 
                           trackBounds.y, 2, trackBounds.height);
            } else {
                g2.fillRect(trackBounds.x, trackBounds.y + trackBounds.height/2 - 1,
                           trackBounds.width, 2);
            }
            g2.dispose();
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Main thumb color
            Color thumbColor = new Color(200, 200, 200);
            if (isDragging) {
                thumbColor = new Color(170, 170, 170);  // Darker when dragging
            } else if (isThumbRollover()) {
                thumbColor = new Color(185, 185, 185);  // Slightly darker on hover
            }
            
            g2.setColor(thumbColor);
            
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                // Center the thumb horizontally in the track
                int centerX = thumbBounds.x + (thumbBounds.width - THUMB_SIZE) / 2;
                g2.fillRoundRect(centerX, thumbBounds.y, 
                               THUMB_SIZE, thumbBounds.height, 
                               THUMB_SIZE, THUMB_SIZE);
            } else {
                // Center the thumb vertically in the track
                int centerY = thumbBounds.y + (thumbBounds.height - THUMB_SIZE) / 2;
                g2.fillRoundRect(thumbBounds.x, centerY,
                               thumbBounds.width, THUMB_SIZE,
                               THUMB_SIZE, THUMB_SIZE);
            }
            g2.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            scrollbar.repaint();
        }
    }
    
    private class RoundedTextField extends JTextField {
        private String placeholder;
        private int radius;
        
        public RoundedTextField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            setPreferredSize(new Dimension(200, 35));
            setBackground(new Color(240, 240, 240));
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
                g2.setColor(Color.GRAY);
                g2.setFont(getFont());
                g2.drawString(placeholder, 15, (getHeight() + g2.getFontMetrics().getAscent()) / 2 - 2);
                g2.dispose();
            }
        }
    }
    
    private class RoundedPasswordField extends JPasswordField {
        private String placeholder;
        private int radius;
        
        public RoundedPasswordField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            setPreferredSize(new Dimension(200, 35));
            setBackground(new Color(240, 240, 240));
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
            
            if (getPassword().length == 0 && placeholder != null) {
                g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.setFont(getFont());
                g2.drawString(placeholder, 15, (getHeight() + g2.getFontMetrics().getAscent()) / 2 - 2);
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