package nepxpress.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.ImageInputStream;
import nepxpress.database.RiderDAO;
import nepxpress.database.UserDAO;

public class RiderSignupView extends JFrame {
    private JPanel contentPanel;
    private JPanel personalInfoPanel;
    private JPanel vehicleInfoPanel;
    
    // Personal Info Components
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField mobileNumberField;
    private JComboBox<String> genderComboBox;
    private JTextField dateOfBirthField;
    private JComboBox<String> cityComboBox;
    private JButton chooseFileButton;
    private JLabel fileNameLabel;
    private JButton nextStepButton;
    
    // Vehicle Info Components
    private JComboBox<String> brandComboBox;
    private JComboBox<String> modelComboBox;
    private JTextField registrationField;
    private JComboBox<String> yearComboBox;
    private JTextField taxTokenField;
    private JTextField fitnessNumberField;
    private JButton submitButton;

    public RiderSignupView() {
        // Set up window properties
        setTitle("Rider Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Create main content panel with card layout
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Create and add both panels
        createPersonalInfoPanel();
        createVehicleInfoPanel();
        
        // Add panels to card layout
        contentPanel.add(personalInfoPanel, "personal");
        contentPanel.add(vehicleInfoPanel, "vehicle");
        
        // Add content panel to frame
        add(contentPanel);
        
        // Pack and center the window
        pack();
        setLocationRelativeTo(null);
        
        // Set a minimum size
        setMinimumSize(new Dimension(400, 550));
        
        // Add validation listeners
        addValidationListeners();
    }
    
    private void createPersonalInfoPanel() {
        personalInfoPanel = new JPanel();
        personalInfoPanel.setBackground(Color.WHITE);
        personalInfoPanel.setLayout(new BoxLayout(personalInfoPanel, BoxLayout.Y_AXIS));
        personalInfoPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        // Personal Information Section
        JPanel sectionPanel = createSectionPanel("01 Personal Information");
        
        // Name Fields Panel (First Name and Last Name)
        JPanel nameFieldsPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        nameFieldsPanel.setOpaque(false);
        
        // First Name
        JPanel firstNamePanel = new JPanel();
        firstNamePanel.setLayout(new BoxLayout(firstNamePanel, BoxLayout.Y_AXIS));
        firstNamePanel.setOpaque(false);
        JLabel firstNameLabel = new JLabel("First Name *");
        firstNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        firstNameField = createStyledTextField("Enter your first name", 140);
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(Box.createVerticalStrut(3));
        firstNamePanel.add(firstNameField);
        
        // Last Name
        JPanel lastNamePanel = new JPanel();
        lastNamePanel.setLayout(new BoxLayout(lastNamePanel, BoxLayout.Y_AXIS));
        lastNamePanel.setOpaque(false);
        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lastNameField = createStyledTextField("Enter your last name", 140);
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(Box.createVerticalStrut(3));
        lastNamePanel.add(lastNameField);
        
        nameFieldsPanel.add(firstNamePanel);
        nameFieldsPanel.add(lastNamePanel);
        
        // Mobile Number
        JPanel mobilePanel = new JPanel();
        mobilePanel.setLayout(new BoxLayout(mobilePanel, BoxLayout.Y_AXIS));
        mobilePanel.setOpaque(false);
        JLabel mobileLabel = new JLabel("Mobile Number *");
        mobileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mobileNumberField = createStyledTextField("977XXXXXXX", 300);
        mobilePanel.add(mobileLabel);
        mobilePanel.add(Box.createVerticalStrut(3));
        mobilePanel.add(mobileNumberField);
        
        // Gender
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new BoxLayout(genderPanel, BoxLayout.Y_AXIS));
        genderPanel.setOpaque(false);
        JLabel genderLabel = new JLabel("Gender *");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        genderComboBox = new JComboBox<>(new String[]{"Select Gender", "Male", "Female", "Other"});
        styleComboBox(genderComboBox, 300);
        genderPanel.add(genderLabel);
        genderPanel.add(Box.createVerticalStrut(3));
        genderPanel.add(genderComboBox);
        
        // Date of Birth
        JPanel dobPanel = new JPanel();
        dobPanel.setLayout(new BoxLayout(dobPanel, BoxLayout.Y_AXIS));
        dobPanel.setOpaque(false);
        JLabel dobLabel = new JLabel("Date Of Birth *");
        dobLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel dateInputPanel = new JPanel(new BorderLayout(5, 0));
        dateInputPanel.setOpaque(false);
        dateOfBirthField = createStyledTextField("mm/dd/yyyy", 270);
        JButton calendarButton = new JButton("📅");
        calendarButton.setBackground(Color.WHITE);
        calendarButton.setPreferredSize(new Dimension(30, 25));
        calendarButton.setFocusPainted(false);
        calendarButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        dateInputPanel.add(dateOfBirthField, BorderLayout.CENTER);
        dateInputPanel.add(calendarButton, BorderLayout.EAST);
        dobPanel.add(dobLabel);
        dobPanel.add(Box.createVerticalStrut(3));
        dobPanel.add(dateInputPanel);
        
        // City
        JPanel cityPanel = new JPanel();
        cityPanel.setLayout(new BoxLayout(cityPanel, BoxLayout.Y_AXIS));
        cityPanel.setOpaque(false);
        JLabel cityLabel = new JLabel("City");
        cityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cityComboBox = new JComboBox<>(new String[]{"Select City", "Kathmandu", "Pokhara", "Lalitpur", "Bhaktapur"});
        styleComboBox(cityComboBox, 300);
        cityPanel.add(cityLabel);
        cityPanel.add(Box.createVerticalStrut(3));
        cityPanel.add(cityComboBox);
        
        // Photo Upload
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setOpaque(false);
        JLabel photoLabel = new JLabel("Upload your photo *");
        photoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        uploadPanel.setOpaque(false);
        chooseFileButton = new JButton("Choose File");
        chooseFileButton.setBackground(Color.WHITE);
        chooseFileButton.setFocusPainted(false);
        chooseFileButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        fileNameLabel = new JLabel("No file chosen");
        fileNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        uploadPanel.add(chooseFileButton);
        uploadPanel.add(Box.createHorizontalStrut(10));
        uploadPanel.add(fileNameLabel);
        
        // Upload instructions
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setOpaque(false);
        instructionsPanel.setBorder(new EmptyBorder(3, 0, 0, 0));
        
        String[] instructions = {
            "*Please upload a clear image of your full face from front",
            "*Full face should be visible",
            "*Image size cannot exceed 1MB"
        };
        
        for (String instruction : instructions) {
            JLabel instructionLabel = new JLabel(instruction);
            instructionLabel.setForeground(new Color(119, 119, 119));
            instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            instructionLabel.setAlignmentX(LEFT_ALIGNMENT);
            instructionsPanel.add(instructionLabel);
        }
        
        photoPanel.add(photoLabel);
        photoPanel.add(Box.createVerticalStrut(3));
        photoPanel.add(uploadPanel);
        photoPanel.add(instructionsPanel);
        
        // Next Step Button
        nextStepButton = new JButton("Next Step →");
        nextStepButton.setBackground(new Color(67, 160, 71));
        nextStepButton.setForeground(Color.WHITE);
        nextStepButton.setFocusPainted(false);
        nextStepButton.setBorderPainted(false);
        nextStepButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nextStepButton.setPreferredSize(new Dimension(150, 32));
        nextStepButton.setMaximumSize(new Dimension(150, 32));
        nextStepButton.setAlignmentX(CENTER_ALIGNMENT);
        
        // Add hover effect
        nextStepButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nextStepButton.setBackground(new Color(56, 142, 60));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                nextStepButton.setBackground(new Color(67, 160, 71));
            }
        });
        
        // Add action listener
        nextStepButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "vehicle");
            setTitle("Vehicle Information - Rider Registration");
        });
        
        // Add components to section panel with specific spacing
        sectionPanel.add(Box.createVerticalStrut(8));
        sectionPanel.add(nameFieldsPanel);
        sectionPanel.add(Box.createVerticalStrut(12));
        sectionPanel.add(mobilePanel);
        sectionPanel.add(Box.createVerticalStrut(12));
        sectionPanel.add(genderPanel);
        sectionPanel.add(Box.createVerticalStrut(12));
        sectionPanel.add(dobPanel);
        sectionPanel.add(Box.createVerticalStrut(12));
        sectionPanel.add(cityPanel);
        sectionPanel.add(Box.createVerticalStrut(12));
        sectionPanel.add(photoPanel);
        sectionPanel.add(Box.createVerticalStrut(15));
        sectionPanel.add(nextStepButton);
        
        // Add to personal info panel
        personalInfoPanel.add(sectionPanel);
        
        // Add file chooser functionality
        addFileChooser();
    }
    
    private void createVehicleInfoPanel() {
        vehicleInfoPanel = new JPanel();
        vehicleInfoPanel.setBackground(Color.WHITE);
        vehicleInfoPanel.setLayout(new BoxLayout(vehicleInfoPanel, BoxLayout.Y_AXIS));
        vehicleInfoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Vehicle Information Section
        JPanel vehicleSection = createSectionPanel("02 Vehicle Information");
        
        // Brand and Model Panel (side by side)
        JPanel brandModelPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        brandModelPanel.setOpaque(false);
        
        // Brand
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);
        JLabel brandLabel = new JLabel("Select Brand *");
        brandComboBox = new JComboBox<>(new String[]{"Select The Brand", "NS", "Ntorq", "Bullet"});
        styleComboBox(brandComboBox, 150);
        brandPanel.add(brandLabel);
        brandPanel.add(Box.createVerticalStrut(4));
        brandPanel.add(brandComboBox);
        
        // Model
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.Y_AXIS));
        modelPanel.setOpaque(false);
        JLabel modelLabel = new JLabel("Select Model *");
        modelComboBox = new JComboBox<>(new String[]{"Select Your Bike Model", "Bajaj Pulsar NS200", "Royal Enfield Bullet 350", "TVS Ntorq 125"});
        styleComboBox(modelComboBox, 150);
        modelPanel.add(modelLabel);
        modelPanel.add(Box.createVerticalStrut(4));
        modelPanel.add(modelComboBox);
        
        brandModelPanel.add(brandPanel);
        brandModelPanel.add(modelPanel);
        
        // Registration Number
        JPanel regPanel = new JPanel();
        regPanel.setLayout(new BoxLayout(regPanel, BoxLayout.Y_AXIS));
        regPanel.setOpaque(false);
        JLabel regLabel = new JLabel("Registration Number *");
        registrationField = createStyledTextField("Digits", 150);
        regPanel.add(regLabel);
        regPanel.add(Box.createVerticalStrut(4));
        regPanel.add(registrationField);
        
        // Year
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.Y_AXIS));
        yearPanel.setOpaque(false);
        JLabel yearLabel = new JLabel("Year *");
        String[] years = new String[24];
        years[0] = "Select Year";
        for (int i = 1; i < 24; i++) {
            years[i] = String.valueOf(2024 - i);
        }
        yearComboBox = new JComboBox<>(years);
        styleComboBox(yearComboBox, 150);
        yearPanel.add(yearLabel);
        yearPanel.add(Box.createVerticalStrut(4));
        yearPanel.add(yearComboBox);
        
        // Tax Token Number
        JPanel taxPanel = new JPanel();
        taxPanel.setLayout(new BoxLayout(taxPanel, BoxLayout.Y_AXIS));
        taxPanel.setOpaque(false);
        JLabel taxLabel = new JLabel("Tax Token Number *");
        taxTokenField = createStyledTextField("Add your Token Number", 150);
        taxPanel.add(taxLabel);
        taxPanel.add(Box.createVerticalStrut(4));
        taxPanel.add(taxTokenField);
        
        // Fitness Number
        JPanel fitnessPanel = new JPanel();
        fitnessPanel.setLayout(new BoxLayout(fitnessPanel, BoxLayout.Y_AXIS));
        fitnessPanel.setOpaque(false);
        JLabel fitnessLabel = new JLabel("Fitness Number");
        fitnessNumberField = createStyledTextField("Add your Fitness Number", 150);
        fitnessPanel.add(fitnessLabel);
        fitnessPanel.add(Box.createVerticalStrut(4));
        fitnessPanel.add(fitnessNumberField);
        
        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(76, 175, 80));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setPreferredSize(new Dimension(150, 35));
        submitButton.setMaximumSize(new Dimension(150, 35));
        submitButton.setAlignmentX(CENTER_ALIGNMENT);
        
        // Add components to vehicle section
        vehicleSection.add(Box.createVerticalStrut(10));
        vehicleSection.add(brandModelPanel);
        vehicleSection.add(Box.createVerticalStrut(15));
        vehicleSection.add(regPanel);
        vehicleSection.add(Box.createVerticalStrut(15));
        vehicleSection.add(yearPanel);
        vehicleSection.add(Box.createVerticalStrut(15));
        vehicleSection.add(taxPanel);
        vehicleSection.add(Box.createVerticalStrut(15));
        vehicleSection.add(fitnessPanel);
        vehicleSection.add(Box.createVerticalStrut(20));
        vehicleSection.add(submitButton);
        
        // Add to vehicle info panel
        vehicleInfoPanel.add(vehicleSection);
    }
    
    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Create title panel with gradient background
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(247, 248, 250), getWidth(), 0, new Color(242, 243, 245));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(51, 51, 51));
        titlePanel.add(titleLabel);
        
        panel.add(titlePanel);
        return panel;
    }
    
    private JTextField createStyledTextField(String placeholder, int width) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 25));
        field.setMaximumSize(new Dimension(2000, 25));
        field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        
        return field;
    }
    
    private void styleComboBox(JComboBox<?> comboBox, int width) {
        comboBox.setPreferredSize(new Dimension(width, 25));
        comboBox.setMaximumSize(new Dimension(2000, 25));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Style the dropdown
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                return this;
            }
        });
    }

    // Add file chooser functionality
    private void addFileChooser() {
        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // Set file filter for images
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    String name = f.getName().toLowerCase();
                    return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
                           name.endsWith(".png") || name.endsWith(".gif");
                }
                public String getDescription() {
                    return "Image files (*.jpg, *.jpeg, *.png, *.gif)";
                }
            });
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                // Validate file size (max 1MB)
                if (selectedFile.length() > 1024 * 1024) {
                    JOptionPane.showMessageDialog(this,
                        "File size must not exceed 1MB.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate image dimensions and format
                try {
                    javax.imageio.ImageInputStream iis = javax.imageio.ImageIO.createImageInputStream(selectedFile);
                    javax.imageio.ImageReader reader = javax.imageio.ImageIO.getImageReaders(iis).next();
                    reader.setInput(iis);
                    
                    // Check image format
                    String format = reader.getFormatName().toLowerCase();
                    if (!format.equals("jpeg") && !format.equals("jpg") && 
                        !format.equals("png") && !format.equals("gif")) {
                        JOptionPane.showMessageDialog(this,
                            "Invalid image format. Please use JPG, PNG or GIF.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Check image dimensions
                    int width = reader.getWidth(0);
                    int height = reader.getHeight(0);
                    if (width < 100 || height < 100) {
                        JOptionPane.showMessageDialog(this,
                            "Image dimensions too small. Minimum size is 100x100 pixels.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // If all validations pass, update the label
                    fileNameLabel.setText(selectedFile.getName());
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error validating image file: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void addValidationListeners() {
        // Add validation to Next Step button
        nextStepButton.addActionListener(e -> {
            if (validatePersonalInfo()) {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "vehicle");
                setTitle("Vehicle Information - Rider Registration");
            }
        });
        
        // Add validation to Submit button
        submitButton.addActionListener(e -> {
            if (validateVehicleInfo()) {
                handleSubmit();
            }
        });
        
        // Add mobile number format validation
        mobileNumberField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (!mobileNumberField.getText().equals("977XXXXXXX")) {
                    validateMobileNumber();
                }
            }
        });
        
        // Add date format validation
        dateOfBirthField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (!dateOfBirthField.getText().equals("mm/dd/yyyy")) {
                    validateDateFormat();
                }
            }
        });
    }
    
    private boolean validatePersonalInfo() {
        StringBuilder errors = new StringBuilder("Please correct the following errors:\n");
        boolean isValid = true;
        
        // Validate First Name
        if (firstNameField.getText().isEmpty() || firstNameField.getText().equals("Enter your first name")) {
            errors.append("- First Name is required\n");
            isValid = false;
        }
        
        // Validate Mobile Number
        if (!validateMobileNumber()) {
            errors.append("- Invalid mobile number format (should be 977XXXXXXX)\n");
            isValid = false;
        }
        
        // Validate Gender
        if (genderComboBox.getSelectedIndex() == 0) {
            errors.append("- Please select your gender\n");
            isValid = false;
        }
        
        // Validate Date of Birth
        if (!validateDateFormat()) {
            errors.append("- Invalid date format (should be mm/dd/yyyy)\n");
            isValid = false;
        }
        
        // Validate Photo Upload
        if (fileNameLabel.getText().equals("No file chosen")) {
            errors.append("- Please upload your photo\n");
            isValid = false;
        }
        
        if (!isValid) {
            JOptionPane.showMessageDialog(this,
                errors.toString(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        return isValid;
    }
    
    private boolean validateVehicleInfo() {
        StringBuilder errors = new StringBuilder("Please correct the following errors:\n");
        boolean isValid = true;
        
        // Validate Brand
        if (brandComboBox.getSelectedIndex() == 0) {
            errors.append("- Please select a brand\n");
            isValid = false;
        }
        
        // Validate Model
        if (modelComboBox.getSelectedIndex() == 0) {
            errors.append("- Please select a model\n");
            isValid = false;
        }
        
        // Validate Registration Number
        if (registrationField.getText().isEmpty() || registrationField.getText().equals("Digits")) {
            errors.append("- Registration number is required\n");
            isValid = false;
        } else if (!registrationField.getText().matches("^[0-9]+$")) {
            errors.append("- Registration number should contain only digits\n");
            isValid = false;
        }
        
        // Validate Year
        if (yearComboBox.getSelectedIndex() == 0) {
            errors.append("- Please select a year\n");
            isValid = false;
        }
        
        // Validate Tax Token Number
        if (taxTokenField.getText().isEmpty() || taxTokenField.getText().equals("Add your Token Number")) {
            errors.append("- Tax token number is required\n");
            isValid = false;
        }
        
        if (!isValid) {
            JOptionPane.showMessageDialog(this,
                errors.toString(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        return isValid;
    }
    
    private boolean validateMobileNumber() {
        String mobile = mobileNumberField.getText();
        if (mobile.equals("977XXXXXXX")) return false;
        
        // Check if the number starts with 977 and has exactly 10 digits
        return mobile.matches("^977\\d{7}$");
    }
    
    private boolean validateDateFormat() {
        String date = dateOfBirthField.getText();
        if (date.equals("mm/dd/yyyy")) return false;
        
        try {
            // Check format
            if (!date.matches("^\\d{2}/\\d{2}/\\d{4}$")) return false;
            
            // Parse the date
            String[] parts = date.split("/");
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            
            // Basic date validation
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;
            
            // More specific month/day validation
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30) return false;
            } else if (month == 2) {
                // Leap year check
                boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                if (day > (isLeapYear ? 29 : 28)) return false;
            }
            
            // Check if date is not in future
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month - 1, day);
            if (cal.getTime().after(new java.util.Date())) return false;
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void handleSubmit() {
        try {
            // Get personal information
            String firstName = firstNameField.getText().trim();
            String surname = lastNameField.getText().trim();
            String mobileNumber = mobileNumberField.getText().trim();
            String gender = (String) genderComboBox.getSelectedItem();
            String dateOfBirth = dateOfBirthField.getText().trim();
            
            // Get vehicle information
            String brand = (String) brandComboBox.getSelectedItem();
            String model = (String) modelComboBox.getSelectedItem();
            String vehicleType = brand + " " + model;
            String registrationNumber = registrationField.getText().trim();
            String licenseNumber = taxTokenField.getText().trim(); // Using tax token as license number
            
            // Create RiderDAO instance
            RiderDAO riderDAO = new RiderDAO();
            
            // Check if registration number already exists
            if (riderDAO.isVehicleRegistrationTaken(registrationNumber)) {
                JOptionPane.showMessageDialog(this,
                    "A vehicle with this registration number already exists.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if license number already exists
            if (riderDAO.isLicenseNumberTaken(licenseNumber)) {
                JOptionPane.showMessageDialog(this,
                    "A rider with this license number already exists.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create UserDAO instance
            UserDAO userDAO = new UserDAO();
            
            // Check if mobile number already exists
            if (userDAO.emailOrMobileExists(mobileNumber)) {
                JOptionPane.showMessageDialog(this,
                    "An account with this mobile number already exists.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // First create the user account
            // Using mobile number as password initially - user can change it later
            boolean userCreated = userDAO.createUser(
                firstName,
                surname,
                mobileNumber,  // Using mobile as email/mobile
                mobileNumber,  // Using mobile as initial password
                dateOfBirth,
                gender,
                "Rider"  // Account type
            );
            
            if (!userCreated) {
                JOptionPane.showMessageDialog(this,
                    "Failed to create user account. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get the newly created user's ID
            int userId = userDAO.getUserIdByMobile(mobileNumber);
            
            if (userId == -1) {
                JOptionPane.showMessageDialog(this,
                    "Error retrieving user account. Please contact support.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create rider record
            boolean success = riderDAO.createRider(userId, vehicleType, licenseNumber, registrationNumber);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Rider registration successful!\n\n" +
                    "Name: " + firstName + " " + surname + "\n" +
                    "Mobile: " + mobileNumber + "\n" +
                    "Vehicle: " + vehicleType + "\n" +
                    "Registration: " + registrationNumber + "\n" +
                    "License: " + licenseNumber + "\n\n" +
                    "Note: Your initial password is your mobile number.\n" +
                    "Please change it after your first login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                // If rider creation fails, we should roll back the user creation
                userDAO.deleteUser(userId);
                JOptionPane.showMessageDialog(this,
                    "Failed to complete rider registration. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "An error occurred during registration: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 