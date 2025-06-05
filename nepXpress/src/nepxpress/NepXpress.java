/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nepxpress;

import nepxpress.view.RegisterView;
import nepxpress.database.DatabaseTest;

/**
 *
 * @author Asus
 */
public class NepXpress {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Test database connection first */
        DatabaseTest.testConnection();
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            RegisterView view = new RegisterView();
            view.setVisible(true);
        });
    }
    
}
