
import view.*;

import javax.swing.*;

/**
 * Clase principal para probar la aplicación de gestión de médicos
 */


import view.LoginView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configurar el Look and Feel antes de crear cualquier componente
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ejecutar en el Event Dispatch Thread para thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crear y mostrar la ventana de login
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            }
        });
    }
}
