package nepxpress.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {
    private static final String FROM_EMAIL = "aryanshrestha0708@gmail.com"; // Your Gmail address
    private static final String EMAIL_PASSWORD = "cfjy qkim jfbf nbqm"; // Your 16-character app password
    
    public static String sendVerificationCode(String toEmail) throws MessagingException {
        // Generate a random 6-digit verification code
        String verificationCode = generateVerificationCode();
        
        // Email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        // Create session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
        
        // Create message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Password Reset Verification Code");
        
        // Email body
        String emailBody = String.format(
            "Dear User,\n\n" +
            "You have requested to reset your password. Please use the following verification code:\n\n" +
            "%s\n\n" +
            "This code will expire in 10 minutes.\n\n" +
            "If you did not request this password reset, please ignore this email.\n\n" +
            "Best regards,\n" +
            "NepXpress Team",
            verificationCode
        );
        
        message.setText(emailBody);
        
        // Send message
        Transport.send(message);
        
        return verificationCode;
    }
    
    private static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 