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
            System.out.println("1. Email credentials in EmailService.java are set correctly");
            System.out.println("2. Using correct Gmail app password (not regular Gmail password)");
            System.out.println("3. Internet connection is working");
            System.out.println("4. Check console for detailed error messages");
        }
        
        scanner.close();
    }
} 