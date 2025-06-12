package nepxpress.test;

import nepxpress.util.EmailService;
import java.util.Scanner;

public class EmailTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Email Configuration Test");
        System.out.println("=======================");
        
        // Get test email address
        System.out.print("Enter your email address to receive test code: ");
        String testEmail = scanner.nextLine().trim();
        
        // Generate and send code
        String verificationCode = EmailService.generateVerificationCode();
        System.out.println("\nAttempting to send verification code: " + verificationCode);
        System.out.println("To email: " + testEmail);
        System.out.println("\nSending...");
        
        boolean success = EmailService.sendVerificationCode(testEmail, verificationCode);
        
        if (success) {
            System.out.println("\nSuccess! Please check your email for the verification code.");
            System.out.println("The code sent was: " + verificationCode);
        } else {
            System.out.println("\nFailed to send email. Please check:");
           
        
        scanner.close();
    }
} 