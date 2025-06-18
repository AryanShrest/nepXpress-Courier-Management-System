/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nepxpress.view;

/**
 *
 * @author Asus
 */
import javax.swing.*;

import nepxpress.view.Dashboard;

public class NepXpress {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String adminId = JOptionPane.showInputDialog(null, "Enter Admin ID:", "Admin Login", JOptionPane.PLAIN_MESSAGE);
            if (adminId == null) return;
            JPasswordField pf = new JPasswordField();
            int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (okCxl != JOptionPane.OK_OPTION) return;
            String password = new String(pf.getPassword());
            if ("9708096796".equals(adminId.trim()) && "12345678".equals(password)) {
                Dashboard adminDash = new Dashboard();
                adminDash.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid admin ID or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

