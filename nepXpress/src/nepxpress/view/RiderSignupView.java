package nepxpress.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
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

    // Add file chooser functionality
    private JLabel imagePreviewLabel;

    public RiderSignupView() {
        initComponents();
        setupUI();
        
        // Set window size to accommodate smaller preview panel
        this.setResizable(false);
        this.setSize(900, 700);  // Adjusted width for smaller preview
        
        // Center on screen
        this.setLocationRelativeTo(null);
        
        // Prevent maximizing
        this.setMaximizedBounds(new Rectangle(900, 700));
        this.setExtendedState(JFrame.NORMAL);
    }
    
    private void initComponents() {
        // Set up window properties
        setTitle("Rider Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
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
        
        // Add validation listeners
        addValidationListeners();
    }
    
    private void setupUI() {
        // Main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 0));  // Reduced gap between form and preview
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Left side panel for form
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        // Header panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Rider Registration");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));  // Slightly smaller font
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Add separator lines
        JSeparator line1 = new JSeparator();
        line1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line1.setForeground(new Color(200, 200, 200));
        headerPanel.add(Box.createVerticalStrut(6));  // Reduced spacing
        headerPanel.add(line1);
        
        JSeparator line2 = new JSeparator();
        line2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line2.setForeground(new Color(200, 200, 200));
        headerPanel.add(Box.createVerticalStrut(2));
        headerPanel.add(line2);

        leftPanel.add(headerPanel, BorderLayout.NORTH);

        // Create preview panel (smaller size)
        JPanel previewPanel = new JPanel(new BorderLayout(5, 5));
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        previewPanel.setPreferredSize(new Dimension(250, 300));  // Smaller size

        // Preview label
        JLabel previewLabel = new JLabel("Photo Preview");
        previewLabel.setHorizontalAlignment(JLabel.CENTER);
        previewLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        previewPanel.add(previewLabel, BorderLayout.NORTH);

        // Image preview area
        JLabel imagePreview = new JLabel("No image selected");
        imagePreview.setHorizontalAlignment(JLabel.CENTER);
        imagePreview.setVerticalAlignment(JLabel.CENTER);
        imagePreview.setPreferredSize(new Dimension(230, 250));  // Slightly smaller than panel
        previewPanel.add(imagePreview, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Personal Information Section
        JLabel personalInfoLabel = new JLabel("Personal Information");
        personalInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        personalInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(personalInfoLabel);
        formPanel.add(Box.createVerticalStrut(10));

        // Photo Upload Section
        JPanel photoSection = new JPanel(new BorderLayout(10, 0));
        photoSection.setBackground(Color.WHITE);
        photoSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        photoSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel photoLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        photoLabelPanel.setBackground(Color.WHITE);
        JLabel photoLabel = new JLabel("Upload your photo *");
        photoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        photoLabelPanel.add(photoLabel);

        // Photo upload controls
        JPanel uploadControls = new JPanel();
        uploadControls.setLayout(new BoxLayout(uploadControls, BoxLayout.Y_AXIS));
        uploadControls.setBackground(Color.WHITE);

        // Choose file button and label
        JPanel fileChooserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fileChooserPanel.setBackground(Color.WHITE);
        JButton chooseFileBtn = new JButton("Choose File");
        chooseFileBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JLabel fileNameLabel = new JLabel("No file chosen");
        fileNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        fileNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        fileChooserPanel.add(chooseFileBtn);
        fileChooserPanel.add(fileNameLabel);
        uploadControls.add(fileChooserPanel);

        // Photo guidelines
        String[] guidelines = {
            "*Please upload a clear image of your full face from front",
            "*Full face should be visible",
            "*Image size cannot exceed 1MB"
        };
        
        for (String guideline : guidelines) {
            JLabel guideLabel = new JLabel(guideline);
            guideLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
            guideLabel.setForeground(Color.GRAY);
            guideLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            uploadControls.add(guideLabel);
        }

        photoSection.add(photoLabelPanel, BorderLayout.NORTH);
        photoSection.add(uploadControls, BorderLayout.CENTER);
        formPanel.add(photoSection);
        formPanel.add(Box.createVerticalStrut(15));

        // Add file chooser functionality with preview
        chooseFileBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    String name = f.getName().toLowerCase();
                    return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
                           name.endsWith(".png");
                }
                public String getDescription() {
                    return "Image files (*.jpg, *.jpeg, *.png)";
                }
            });

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // Check file size (1MB = 1048576 bytes)
                if (selectedFile.length() > 1048576) {
                    JOptionPane.showMessageDialog(this,
                        "File size exceeds 1MB limit. Please choose a smaller file.",
                        "File Size Error",
                        JOptionPane.ERROR_MESSAGE);
                    fileNameLabel.setText("No file chosen");
                    imagePreview.setIcon(null);
                    imagePreview.setText("No image selected");
                } else {
                    fileNameLabel.setText(selectedFile.getName());
                    try {
                        // Load and scale the image
                        ImageIcon imageIcon = new ImageIcon(selectedFile.getPath());
                        Image image = imageIcon.getImage();
                        
                        // Calculate scaling to fit preview panel while maintaining aspect ratio
                        int previewWidth = 230;  // Slightly smaller than panel
                        int previewHeight = 250;
                        
                        double scale = Math.min(
                            (double) previewWidth / image.getWidth(null),
                            (double) previewHeight / image.getHeight(null)
                        );
                        
                        int scaledWidth = (int) (image.getWidth(null) * scale);
                        int scaledHeight = (int) (image.getHeight(null) * scale);
                        
                        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                        imagePreview.setIcon(new ImageIcon(scaledImage));
                        imagePreview.setText("");  // Clear the "No image selected" text
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                            "Error loading image: " + ex.getMessage(),
                            "Image Error",
                            JOptionPane.ERROR_MESSAGE);
                        imagePreview.setIcon(null);
                        imagePreview.setText("Error loading image");
                    }
                }
            }
        });

        // Rest of the personal information fields
        addFormField(formPanel, "First Name:", createStyledTextField(), 8);
        addFormField(formPanel, "Last Name:", createStyledTextField(), 8);
        addFormField(formPanel, "Email:", createStyledTextField(), 8);
        addFormField(formPanel, "Mobile:", createStyledTextField(), 8);
        addFormField(formPanel, "Date of Birth:", createStyledTextField(), 8);
        addFormField(formPanel, "Gender:", createGenderComboBox(), 8);
        addFormField(formPanel, "Address:", createStyledTextField(), 8);
        addFormField(formPanel, "City:", createStyledTextField(), 8);

        // Vehicle Information Section
        formPanel.add(Box.createVerticalStrut(15));
        JLabel vehicleInfoLabel = new JLabel("Vehicle Information");
        vehicleInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        vehicleInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(vehicleInfoLabel);
        formPanel.add(Box.createVerticalStrut(10));

        // Vehicle Information fields
        addFormField(formPanel, "Vehicle Company:", createVehicleCompanyField(), 8);
        addFormField(formPanel, "License Number:", createLicenseField(), 8);
        addFormField(formPanel, "Vehicle Number:", createVehicleNumberField(), 8);
        addFormField(formPanel, "Experience (Years):", createExperienceField(), 8);
        addFormField(formPanel, "Area of Operation:", createAreaField(), 8);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(242, 140, 72));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.setMaximumSize(new Dimension(120, 35));  // Slightly smaller button
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add some spacing before the button
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(submitButton);

        // Add scroll support for the form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add left panel and preview panel to main panel
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(previewPanel, BorderLayout.EAST);
        
        setContentPane(mainPanel);
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
        // Add image preview label next to fileNameLabel
        if (imagePreviewLabel == null) {
            imagePreviewLabel = new JLabel();
            imagePreviewLabel.setPreferredSize(new Dimension(60, 60));
            imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            imagePreviewLabel.setOpaque(true);
            imagePreviewLabel.setBackground(Color.WHITE);
            // Add to uploadPanel after fileNameLabel
            fileNameLabel.getParent().add(imagePreviewLabel);
        }
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
                fileNameLabel.setText(selectedFile.getName());
                try {
                    Image img = ImageIO.read(selectedFile);
                    if (img != null) {
                        Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        imagePreviewLabel.setIcon(new ImageIcon(scaledImg));
                    } else {
                        imagePreviewLabel.setIcon(null);
                    }
                } catch (Exception ex) {
                    imagePreviewLabel.setIcon(null);
                    JOptionPane.showMessageDialog(this,
                        "Could not load image: " + ex.getMessage(),
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
                // Get the user ID from the database using mobile number
                UserDAO userDAO = new UserDAO();
                String mobileNumber = mobileNumberField.getText();
                int userId = userDAO.getUserIdByEmailOrMobile(mobileNumber);
                
                if (userId == -1) {
                    JOptionPane.showMessageDialog(this,
                        "Error: Could not find user account. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create rider entry
                RiderDAO riderDAO = new RiderDAO();
                
                // Check if license number is already taken
                String licenseNumber = taxTokenField.getText();
                if (riderDAO.isLicenseNumberTaken(licenseNumber)) {
                    JOptionPane.showMessageDialog(this,
                        "This license number is already registered.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check if vehicle registration is already taken
                String vehicleRegistration = registrationField.getText();
                if (riderDAO.isVehicleRegistrationTaken(vehicleRegistration)) {
                    JOptionPane.showMessageDialog(this,
                        "This vehicle registration number is already registered.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Get vehicle type (brand + model)
                String vehicleType = brandComboBox.getSelectedItem() + " " + modelComboBox.getSelectedItem();
                
                // Create rider account
                boolean success = riderDAO.createRider(userId, vehicleType, licenseNumber, vehicleRegistration);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Rider account created successfully!\nYour account will be activated after verification.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error creating rider account. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
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

    private void addFormField(JPanel panel, String label, JComponent field, int spacing) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.X_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));  // Control field height

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fieldLabel.setPreferredSize(new Dimension(150, 25));  // Fixed label width

        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createHorizontalStrut(10));
        fieldPanel.add(field);

        panel.add(fieldPanel);
        panel.add(Box.createVerticalStrut(spacing));  // Reduced vertical spacing
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)  // Reduced padding
        ));
        return field;
    }

    // Helper methods for creating specific fields
    private JTextField createVehicleCompanyField() {
        return createStyledTextField();
    }

    private JTextField createLicenseField() {
        return createStyledTextField();
    }

    private JTextField createVehicleNumberField() {
        return createStyledTextField();
    }

    private JTextField createExperienceField() {
        return createStyledTextField();
    }

    private JTextField createAreaField() {
        return createStyledTextField();
    }

    private JComboBox<String> createGenderComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Select Gender", "Male", "Female", "Other"});
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        return comboBox;
    }
} 