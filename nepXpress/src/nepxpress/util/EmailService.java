package nepxpress.util;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    
    // TODO: Replace these values with your Gmail credentials
    private static final String FROM_EMAIL = "your-email@gmail.com"; // Your Gmail address
    private static final String EMAIL_PASSWORD = "your-app-password"; // Your Gmail app password
    
    // Instructions to get Gmail app password:
    // 1. Go to Google Account settings (https://myaccount.google.com)
    // 2. Enable 2-Step Verification if not enabled
    // 3. Go to Security → App passwords
    // 4. Select "Mail" and your device
    // 5. Copy the 16-character password
    
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generates 6-digit code
        return String.valueOf(code);
    }
    
    public static boolean sendVerificationCode(String toEmail, String verificationCode) {
        // Validate email configuration
        if (FROM_EMAIL.equals("your-email@gmail.com") || EMAIL_PASSWORD.equals("your-app-password")) {
            LOGGER.severe("ERROR: Email credentials not configured!");
            LOGGER.severe("Please update FROM_EMAIL and EMAIL_PASSWORD in EmailService.java");
            LOGGER.severe("You need to:");
            LOGGER.severe("1. Replace 'your-email@gmail.com' with your actual Gmail address");
            LOGGER.severe("2. Replace 'your-app-password' with your Gmail app password");
            LOGGER.severe("3. Make sure you're using an App Password, not your regular Gmail password");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        try {
            LOGGER.info("Starting email verification process...");
            LOGGER.info("Sending verification code to: " + toEmail);
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
                }
            });
            
            // Enable debug mode
            session.setDebug(true);
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("nepXpress Password Reset Code");
            
            String emailContent = String.format(
                "<html><body style='font-family: Arial, sans-serif;'>" +
                "<div style='background-color: #f5f5f5; padding: 20px; border-radius: 5px;'>" +
                "<h2 style='color: #333;'>Password Reset Verification</h2>" +
                "<p>Your verification code is:</p>" +
                "<h1 style='color: #4CAF50; font-size: 32px; letter-spacing: 5px;'>%s</h1>" +
                "<p style='color: #666;'>This code will expire in 10 minutes.</p>" +
                "<p style='color: #999; font-size: 12px;'>If you did not request this, please ignore this email.</p>" +
                "</div></body></html>",
                verificationCode
            );
            
            message.setContent(emailContent, "text/html; charset=utf-8");
            
            LOGGER.info("Connecting to Gmail SMTP server...");
            Transport.send(message);
            LOGGER.info("Email sent successfully!");
            return true;
            
        } catch (AuthenticationFailedException e) {
            LOGGER.severe("Authentication failed! Please check:");
            LOGGER.severe("1. Email address is correct");
            LOGGER.severe("2. App password is correct (16 characters)");
            LOGGER.severe("3. 2-Step Verification is enabled");
            LOGGER.severe("Error details: " + e.getMessage());
            return false;
        } catch (MessagingException e) {
            LOGGER.severe("Failed to send email!");
            LOGGER.severe("Error details: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            LOGGER.severe("Unexpected error occurred!");
            LOGGER.severe("Error details: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 