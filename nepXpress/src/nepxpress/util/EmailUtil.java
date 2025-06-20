package nepxpress.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {
    // Gmail credentials
    private static final String FROM_EMAIL = "aryanshrestha0708@gmail.com"; // Your Gmail address
    private static final String EMAIL_PASSWORD = "uehdnzexsgkxqyar"; // Your 16-character app password
    
    /**
     * Sends a general email with the given subject and message
     * @param toEmail Recipient email address
     * @param subject Email subject
     * @param message Email message body (plain text)
     * @throws MessagingException If there's an error sending the email
     */
    public static void sendEmail(String toEmail, String subject, String message) throws MessagingException {
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        // Create session with authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
        
        try {
            // Create message
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(FROM_EMAIL));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            emailMessage.setSubject(subject);
            
            // Create HTML content
            String htmlContent = String.format(
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #333;'>%s</h2>" +
                "<p>%s</p>" +
                "<p>Best regards,<br>The nepXpress Team</p>" +
                "</div>",
                subject,
                message.replace("\n", "<br>")
            );
            
            // Set content type and actual content
            emailMessage.setContent(htmlContent, "text/html; charset=utf-8");
            
            // Send message
            Transport.send(emailMessage);
            
        } catch (MessagingException e) {
            throw new MessagingException("Failed to send email: " + e.getMessage());
        }
    }
    
    public static String sendVerificationCode(String toEmail) throws MessagingException {
        // Generate a random 6-digit verification code
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        // Create session with authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
        
        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("nepXpress Password Reset Verification Code");
            
            // Create HTML content
            String htmlContent = String.format(
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #333;'>Password Reset Verification</h2>" +
                "<p>You have requested to reset your password for your nepXpress account. " +
                "Please use the following verification code to proceed:</p>" +
                "<div style='background-color: #f5f5f5; padding: 15px; text-align: center; " +
                "font-size: 24px; letter-spacing: 5px; margin: 20px 0;'>" +
                "<strong>%s</strong>" +
                "</div>" +
                "<p>If you did not request this password reset, please ignore this email.</p>" +
                "<p>Best regards,<br>The nepXpress Team</p>" +
                "</div>",
                verificationCode
            );
            
            // Set content type and actual content
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            // Send message
            Transport.send(message);
            
            return verificationCode;
            
        } catch (MessagingException e) {
            throw new MessagingException("Failed to send verification code: " + e.getMessage());
        }
    }
} 