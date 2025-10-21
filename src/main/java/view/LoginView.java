package view;

import controller.LoginController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista de login del sistema hospitalario.
 * Permite a los usuarios autenticarse en el sistema con su ID y contraseña.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class LoginView extends JFrame {
    private JPanel PanelPrincipal;
    private JPanel PanelDatos;
    private JPanel UsuarioPanel;
    private JPanel ContrasenaPanel;
    private JPanel UsuarioLabelPanel;
    private JPanel UsuarioTextFieldPanel;
    private JLabel UsuarioLabel;
    private JTextField UsuarioTextField;
    private JPanel ContrasenaLabelPanel;
    private JPanel ContrasenaTextFieldPanel;
    private JLabel ContrasenaLabel;
    private JPasswordField ContrasenaTextField; // Cambié a JPasswordField para mayor seguridad
    private JPanel PanelIcono;
    private JLabel IconoLabel;
    private JButton entrarButton;

    private LoginController loginController;

    /**
     * Constructor que inicializa la vista de login.
     * Configura la interfaz gráfica y el controlador de autenticación.
     */
    public LoginView() {
        // Configurar Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicializar el controlador
        loginController = new LoginController();

        // Configurar la ventana
        setTitle("Sistema Hospitalario - Login");
        setContentPane(PanelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configurar eventos
        configurarEventos();
    }

    /**
     * Configura los eventos de los componentes de la interfaz.
     * Incluye eventos de botones y navegación con tecla Enter.
     */
    private void configurarEventos() {
        // Evento del botón entrar
        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        // Permitir login con Enter en los campos de texto
        UsuarioTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContrasenaTextField.requestFocus();
            }
        });

        ContrasenaTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }

    /**
     * Realiza el proceso de autenticación del usuario.
     * Valida los campos y llama al controlador para verificar las credenciales.
     */
    private void realizarLogin() {
        String usuario = UsuarioTextField.getText().trim();
        String contrasena = new String(ContrasenaTextField.getPassword());

        // Validar que los campos no estén vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Intentar hacer login
        boolean loginExitoso = loginController.login(usuario, contrasena, this);

        // Si el login es exitoso, cerrar esta ventana
        if (loginExitoso) {
            this.dispose();
        } else {
            // Limpiar campos en caso de error
            ContrasenaTextField.setText("");
            UsuarioTextField.requestFocus();
        }
    }

    /**
     * Retorna el panel principal de la vista de login.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipal() {
        return PanelPrincipal;
    }
}