package nepxpress.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class RiderSignupView extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField mobileNumberField;
    private JComboBox<String> genderComboBox;
    private JTextField dateOfBirthField;
    private JComboBox<String> cityComboBox;
    private JComboBox<String> brandComboBox;
    private JComboBox<String> modelComboBox;
    private JButton chooseFileButton;
    private JLabel fileNameLabel;

    public RiderSignupView() {
        setTitle("Rider Registration");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Personal Information Section Title
        JLabel personalTitle = new JLabel("01 Personal Information");
        personalTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel personalTitlePanel = new JPanel();
        personalTitlePanel.setLayout(new BoxLayout(personalTitlePanel, BoxLayout.X_AXIS));
        personalTitlePanel.setBackground(new Color(247, 248, 250));
        personalTitlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        personalTitlePanel.add(Box.createHorizontalStrut(10));
        personalTitlePanel.add(personalTitle);
        personalTitlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(personalTitlePanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Name Fields Panel
        JPanel nameFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        nameFieldsPanel.setBackground(Color.WHITE);
        
        // First Name
        JPanel firstNamePanel = new JPanel();
        firstNamePanel.setLayout(new BoxLayout(firstNamePanel, BoxLayout.Y_AXIS));
        firstNamePanel.setBackground(Color.WHITE);
        JLabel firstNameLabel = new JLabel("First Name *");
        firstNameField = createTextField(170);
        firstNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        firstNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(Box.createVerticalStrut(5));
        firstNamePanel.add(firstNameField);

        // Last Name
        JPanel lastNamePanel = new JPanel();
        lastNamePanel.setLayout(new BoxLayout(lastNamePanel, BoxLayout.Y_AXIS));
        lastNamePanel.setBackground(Color.WHITE);
        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameField = createTextField(170);
        lastNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lastNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(Box.createVerticalStrut(5));
        lastNamePanel.add(lastNameField);

        nameFieldsPanel.add(firstNamePanel);
        nameFieldsPanel.add(lastNamePanel);
        nameFieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(nameFieldsPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Add other form fields
        addFormField(formPanel, "Mobile Number *", mobileNumberField = createTextField(350));
        
        // Gender dropdown
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setPreferredSize(new Dimension(350, 30));
        genderComboBox.setMaximumSize(new Dimension(350, 30));
        genderComboBox.setBackground(Color.WHITE);
        addFormField(formPanel, "Gender *", genderComboBox);

        // Date of Birth
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.setBackground(Color.WHITE);
        dateOfBirthField = new JTextField("mm/dd/yyyy");
        dateOfBirthField.setPreferredSize(new Dimension(320, 30));
        dateOfBirthField.setMaximumSize(new Dimension(320, 30));
        JButton calendarButton = new JButton("📅");
        calendarButton.setPreferredSize(new Dimension(30, 30));
        calendarButton.setMaximumSize(new Dimension(30, 30));
        calendarButton.setBackground(Color.WHITE);
        datePanel.add(dateOfBirthField);
        datePanel.add(calendarButton);
        addFormField(formPanel, "Date Of Birth *", datePanel);

        // City dropdown
        cityComboBox = new JComboBox<>(new String[]{"Select City", "Kathmandu", "Pokhara", "Lalitpur", "Bhaktapur"});
        cityComboBox.setPreferredSize(new Dimension(350, 30));
        cityComboBox.setMaximumSize(new Dimension(350, 30));
        cityComboBox.setBackground(Color.WHITE);
        addFormField(formPanel, "City", cityComboBox);

        formPanel.add(Box.createVerticalStrut(10));

        // Vehicle Information Section Title
        JLabel vehicleTitle = new JLabel("02 Vehicle Information");
        vehicleTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel vehicleTitlePanel = new JPanel();
        vehicleTitlePanel.setLayout(new BoxLayout(vehicleTitlePanel, BoxLayout.X_AXIS));
        vehicleTitlePanel.setBackground(new Color(247, 248, 250));
        vehicleTitlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        vehicleTitlePanel.add(Box.createHorizontalStrut(10));
        vehicleTitlePanel.add(vehicleTitle);
        vehicleTitlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(vehicleTitlePanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Vehicle Fields Panel
        JPanel vehicleFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        vehicleFieldsPanel.setBackground(Color.WHITE);

        // Brand
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(Color.WHITE);
        JLabel brandLabel = new JLabel("Select Brand *");
        brandComboBox = new JComboBox<>(new String[]{"Select The Brand", "NS", "Ntorq", "Bullet"});
        brandComboBox.setPreferredSize(new Dimension(170, 30));
        brandComboBox.setMaximumSize(new Dimension(170, 30));
        brandComboBox.setBackground(Color.WHITE);
        brandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(brandLabel);
        brandPanel.add(Box.createVerticalStrut(5));
        brandPanel.add(brandComboBox);

        // Model
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.Y_AXIS));
        modelPanel.setBackground(Color.WHITE);
        JLabel modelLabel = new JLabel("Select Model *");
        modelComboBox = new JComboBox<>(new String[]{
            "Select Your Bike Model",
            "Bajaj Pulsar NS200",
            "Royal Enfield Bullet 350",
            "TVS Ntorq 125"
        });
        modelComboBox.setPreferredSize(new Dimension(170, 30));
        modelComboBox.setMaximumSize(new Dimension(170, 30));
        modelComboBox.setBackground(Color.WHITE);
        modelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        modelComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        modelPanel.add(modelLabel);
        modelPanel.add(Box.createVerticalStrut(5));
        modelPanel.add(modelComboBox);

        vehicleFieldsPanel.add(brandPanel);
        vehicleFieldsPanel.add(modelPanel);
        vehicleFieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(vehicleFieldsPanel);
        formPanel.add(Box.createVerticalStrut(15));

        // File upload section
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
        filePanel.setBackground(Color.WHITE);
        filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel fileButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fileButtonPanel.setBackground(Color.WHITE);
        chooseFileButton = new JButton("Choose File");
        chooseFileButton.setBackground(Color.WHITE);
        fileNameLabel = new JLabel("No file chosen");
        fileButtonPanel.add(chooseFileButton);
        fileButtonPanel.add(fileNameLabel);
        fileButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // File upload instructions
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setBackground(Color.WHITE);
        instructionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] instructionTexts = {
            "*Please upload a clear image of your full face from front",
            "*Full face should be visible",
            "*Image size cannot exceed 1MB"
        };
        
        for (String text : instructionTexts) {
            JLabel instruction = new JLabel(text);
            instruction.setForeground(Color.GRAY);
            instruction.setFont(new Font("Arial", Font.PLAIN, 11));
            instruction.setAlignmentX(Component.LEFT_ALIGNMENT);
            instructionsPanel.add(instruction);
            instructionsPanel.add(Box.createVerticalStrut(2));
        }

        filePanel.add(fileButtonPanel);
        filePanel.add(Box.createVerticalStrut(5));
        filePanel.add(instructionsPanel);

        // Add file chooser functionality
        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                fileNameLabel.setText(selectedFile.getName());
            }
        });

        // Add all panels to main panel
        mainPanel.add(formPanel);
        mainPanel.add(filePanel);

        // Add to frame
        add(mainPanel);
    }

    private JTextField createTextField(int width) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 30));
        field.setMaximumSize(new Dimension(width, 30));
        return field;
    }

    private void addFormField(JPanel panel, String labelText, JComponent field) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(5));
        fieldPanel.add(field);
        
        panel.add(fieldPanel);
        panel.add(Box.createVerticalStrut(10));
    }
} 