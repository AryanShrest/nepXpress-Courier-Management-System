package nepxpress.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import nepxpress.database.UserDAO;
import nepxpress.database.UserSessionDAO;

public class SignupView extends javax.swing.JFrame {
    
    private RoundedPanel mainPanel;
    private JLabel titleLabel;
    private RoundedTextField firstNameField, surnameField, emailField;
    private RoundedPasswordField passwordField;
    private JComboBox<String> dayBox, monthBox, yearBox;
    private JRadioButton femaleRadio, maleRadio, customRadio;
    private JRadioButton userRadio, riderRadio;
    private ButtonGroup genderGroup;
    private ButtonGroup userTypeGroup;
    private JButton signUpButton;
    private JLabel loginLink;
    private JLabel loginText;  // New label for "Already have an account?"
    
    public SignupView() {
        initComponents();
        setSize(900, 600);
        setLocationRelativeTo(null);
        setTitle("Create Account - nepXpress");
    }
    
    // Add getter for main panel
    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    private void initComponents() {
        setupCustomScrollBars();
        
        mainPanel = new RoundedPanel(40);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(370, 300));  // Reduced size to match login
        
        // Title
        titleLabel = new JLabel("Create a new account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Name fields with equal width
        firstNameField = new RoundedTextField(25);
        firstNameField.setPlaceholder("First name");
        firstNameField.setPreferredSize(new Dimension(140, 35));  // Set specific width
        
        surnameField = new RoundedTextField(25);
        surnameField.setPlaceholder("Surname");
        surnameField.setPreferredSize(new Dimension(140, 35));  // Same width as first name
        
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

        // User type radio buttons
        userRadio = new JRadioButton("Regular User");
        riderRadio = new JRadioButton("Rider");
        userRadio.setSelected(true);
        
        userTypeGroup = new ButtonGroup();
        userTypeGroup.add(userRadio);
        userTypeGroup.add(riderRadio);
        
        // Add action listener to rider radio button
        riderRadio.addActionListener(e -> {
            if (riderRadio.isSelected()) {
                // Create and show rider signup window
                RiderSignupView riderView = new RiderSignupView();
                riderView.setVisible(true);
            }
        });
        
        // Sign up button
        signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(0, 164, 0));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setPreferredSize(new Dimension(293, 35));  // Set specific width to match other fields
        
        // Add click listener to signup button
        signUpButton.addActionListener(e -> {
            if (validateSignupForm()) {
                handleSignup();
            }
        });
        
        // Login text and link in one panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setOpaque(false);  // Make panel transparent
        
        loginText = new JLabel("Already have an account? ");
        loginText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginText.setForeground(new Color(100, 100, 100));
        
        loginLink = new JLabel("Login");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginLink.setForeground(new Color(0, 102, 255));
        
        // Create a single clickable panel that contains both labels
        JPanel clickablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        clickablePanel.setOpaque(false);
        clickablePanel.add(loginText);
        clickablePanel.add(loginLink);
        clickablePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add mouse listener to the entire clickable panel
        clickablePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Container parent = mainPanel;
                while (parent != null) {
                    if (parent.getParent() instanceof RegisterView) {
                        RegisterView registerView = (RegisterView) parent.getParent();
                        registerView.switchToLogin();
                        return;
                    }
                    parent = parent.getParent();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Login</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Login");
            }
        });
        
        loginPanel.add(clickablePanel);
        
        // Layout
        setLayout(new BorderLayout());
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 15, 10);  // Reduced from 20px to 15px top/bottom
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(titleLabel, gbc);
        
        // Name fields in one row with equal width
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 10, 0));  // 1 row, 2 columns, 10px gap
        namePanel.setOpaque(false);
        namePanel.add(firstNameField);
        namePanel.add(surnameField);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(3, 10, 3, 10);  // Reduced from 5px to 3px top/bottom
        mainPanel.add(namePanel, gbc);
        
        // Email field
        gbc.gridy = 2;
        gbc.insets = new Insets(3, 10, 3, 10);  // Reduced from 5px to 3px top/bottom
        mainPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridy = 3;
        mainPanel.add(passwordField, gbc);
        
        // Date selection
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Date of birth"));
        datePanel.add(dayBox);
        datePanel.add(monthBox);
        datePanel.add(yearBox);
        gbc.gridy = 4;
        mainPanel.add(datePanel, gbc);
        
        // Gender radio buttons
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(new JLabel("Gender"));
        genderPanel.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(customRadio);
        gbc.gridy = 5;
        mainPanel.add(genderPanel, gbc);

        // User type radio buttons
        JPanel userTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userTypePanel.add(new JLabel("Account Type"));
        userTypePanel.add(userRadio);
        userTypePanel.add(riderRadio);
        gbc.gridy = 6;
        gbc.insets = new Insets(3, 10, 3, 10);  // Reduced from 5px to 3px top/bottom
        mainPanel.add(userTypePanel, gbc);
        
        // Sign up button
        gbc.gridy = 7;
        gbc.insets = new Insets(8, 10, 0, 10);  // Reduced from 10px to 8px top
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(signUpButton, gbc);
        
        // Login panel with text and link
        gbc.gridy = 8;
        gbc.insets = new Insets(-8, 10, 12, 10);  // Reduced from -5px to -8px top and 15px to 12px bottom
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginPanel, gbc);
        
        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
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
            
            // Enable text editing
            setEditable(true);
            setEnabled(true);
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
    
    private boolean validateSignupForm() {
        StringBuilder errors = new StringBuilder();
        boolean isValid = true;
        
        // Validate first name
        if (firstNameField.getText().trim().isEmpty()) {
            errors.append("- First name is required\n");
            isValid = false;
        }
        
        // Validate surname
        if (surnameField.getText().trim().isEmpty()) {
            errors.append("- Surname is required\n");
            isValid = false;
        }
        
        // Validate email/mobile
        String emailOrMobile = emailField.getText().trim();
        if (emailOrMobile.isEmpty()) {
            errors.append("- Email or mobile number is required\n");
            isValid = false;
        } else if (emailOrMobile.contains("@")) {
            // Validate email format
            if (!emailOrMobile.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.append("- Invalid email format\n");
                isValid = false;
            }
        } else {
            // Validate mobile number format (assuming Nepal number format)
            if (!emailOrMobile.matches("^(977)?[9][6-9]\\d{8}$")) {
                errors.append("- Invalid mobile number format\n");
                isValid = false;
            }
        }
        
        // Validate password
        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            errors.append("- Password is required\n");
            isValid = false;
        } else if (password.length() < 8) {
            errors.append("- Password must be at least 8 characters long\n");
            isValid = false;
        }
        
        // Validate date of birth
        if (dayBox.getSelectedIndex() == -1 || monthBox.getSelectedIndex() == -1 || yearBox.getSelectedIndex() == -1) {
            errors.append("- Please select a valid date of birth\n");
            isValid = false;
        }
        
        // Validate gender selection
        if (!femaleRadio.isSelected() && !maleRadio.isSelected() && !customRadio.isSelected()) {
            errors.append("- Please select your gender\n");
            isValid = false;
        }
        
        // Validate user type selection
        if (!userRadio.isSelected() && !riderRadio.isSelected()) {
            errors.append("- Please select an account type\n");
            isValid = false;
        }
        
        if (!isValid) {
            JOptionPane.showMessageDialog(this,
                "Please correct the following errors:\n" + errors.toString(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        return isValid;
    }
    
    private void handleSignup() {
        try {
            // Gather user information
            String firstName = firstNameField.getText().trim();
            String surname = surnameField.getText().trim();
            String emailOrMobile = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            // Build date of birth
            String dob = String.format("%s-%s-%s",
                yearBox.getSelectedItem(),
                String.format("%02d", monthBox.getSelectedIndex() + 1),
                String.format("%02d", Integer.parseInt(dayBox.getSelectedItem().toString()))
            );
            
            // Get gender
            String gender = femaleRadio.isSelected() ? "Female" :
                          maleRadio.isSelected() ? "Male" : "Custom";
            
            // Get account type
            String accountType = userRadio.isSelected() ? "User" : "Rider";
            
            // Create UserDAO instance
            UserDAO userDAO = new UserDAO();
            
            // Check if email/mobile already exists
            if (userDAO.emailOrMobileExists(emailOrMobile)) {
                JOptionPane.showMessageDialog(this,
                    "An account with this email/mobile already exists.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create user in database
            boolean success = userDAO.createUser(firstName, surname, emailOrMobile, 
                                               password, dob, gender, accountType);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Account created successfully!\n\n" +
                    "Name: " + firstName + " " + surname + "\n" +
                    "Email/Mobile: " + emailOrMobile + "\n" +
                    "Date of Birth: " + dob + "\n" +
                    "Gender: " + gender + "\n" +
                    "Account Type: " + accountType + "\n\n" +
                    "Please login with your credentials.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // If rider account, keep the window open as RiderSignupView will handle the rest
                if (accountType.equals("User")) {
                    // Return to login screen
                    Container parent = mainPanel;
                    while (parent != null) {
                        if (parent.getParent() instanceof RegisterView) {
                            RegisterView registerView = (RegisterView) parent.getParent();
                            registerView.switchToLogin();
                            return;
                        }
                        parent = parent.getParent();
                    }
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to create account. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "An error occurred during signup: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 