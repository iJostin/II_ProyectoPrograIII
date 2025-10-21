package view;

import controller.CambiarClaveController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista para el cambio de contraseña de usuarios del sistema.
 * Permite a los usuarios cambiar su contraseña actual por una nueva.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class CambiarClaveView {
    private JPanel PanelPrincipal;
    private JPanel PanelCambioContrasena;
    private JPanel PanelUsuarioContrasena;
    private JPanel UsuarioLabelPanel;
    private JLabel UsuarioLabel;
    private JPanel UsuarioTextPanel;
    private JTextField TextFieldUsuario;
    private JPanel ContrasenaActualPanel;
    private JPanel ContrasenaActualLabelPanel;
    private JLabel ContrasenaActualLabel;
    private JPanel ContrasenaActualPassPanel;
    private JPasswordField ContrasenaActualPass;
    private JPanel ContrasenaNuevaPanel;
    private JPanel ContrasenaNuevaPanelText1;
    private JLabel ContrasenaNuevaText1;
    private JPanel ContrasenaNuevaPassPanel1;
    private JPasswordField ContrasenaNuevaPass1;
    private JPanel ConfirmaContrasenaTextPanel;
    private JPanel ConfirmaContrasenaPassPanel;
    private JLabel ConfirmaLabel;
    private JPasswordField ConfirmaContrasenaPass;
    private JPanel PanelConfirmarButtom;
    private JButton ConfimarButtom;
    private JPanel PanelIcono;
    private JLabel IconoUsuarioContrasena;

    private CambiarClaveController cambiarClaveController;
    private String usuarioActual; // Para almacenar el usuario actual si se pasa desde otra vista

    /**
     * Constructor por defecto que inicializa la vista sin un usuario específico.
     */
    public CambiarClaveView() {
        this(null);
    }

    /**
     * Constructor que inicializa la vista de cambio de contraseña con un usuario específico.
     *
     * @param usuarioActual El ID del usuario que desea cambiar su contraseña
     */
    public CambiarClaveView(String usuarioActual) {
        this.usuarioActual = usuarioActual;

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
        cambiarClaveController = new CambiarClaveController();

        // Configurar la vista
        configurarVista();

        // Configurar eventos
        configurarEventos();
    }

    /**
     * Configura los aspectos visuales de la vista.
     * Pre-llena el campo de usuario si se proporciona uno y configura tooltips.
     */
    private void configurarVista() {
        // Si tenemos un usuario actual, pre-llenamos el campo y lo deshabilitamos
        if (usuarioActual != null && !usuarioActual.trim().isEmpty()) {
            TextFieldUsuario.setText(usuarioActual);
            TextFieldUsuario.setEditable(false);
            // Enfocar el campo de contraseña actual
            ContrasenaActualPass.requestFocus();
        }

        // Configurar tooltips para ayudar al usuario
        TextFieldUsuario.setToolTipText("Ingrese su ID de usuario");
        ContrasenaActualPass.setToolTipText("Ingrese su contraseña actual");
        ContrasenaNuevaPass1.setToolTipText("Ingrese su nueva contraseña (mínimo 4 caracteres)");
        ConfirmaContrasenaPass.setToolTipText("Confirme su nueva contraseña");
    }

    /**
     * Configura los eventos de los componentes de la interfaz.
     * Incluye eventos de botones y navegación con tecla Enter.
     */
    private void configurarEventos() {
        // Evento del botón confirmar
        ConfimarButtom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarContrasena();
            }
        });

        // Eventos para navegación con Enter entre campos
        TextFieldUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContrasenaActualPass.requestFocus();
            }
        });

        ContrasenaActualPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContrasenaNuevaPass1.requestFocus();
            }
        });

        ContrasenaNuevaPass1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfirmaContrasenaPass.requestFocus();
            }
        });

        ConfirmaContrasenaPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarContrasena();
            }
        });
    }

    /**
     * Procesa el cambio de contraseña validando los campos y llamando al controlador.
     */
    private void cambiarContrasena() {
        String usuario = TextFieldUsuario.getText().trim();
        String claveActual = new String(ContrasenaActualPass.getPassword());
        String claveNueva = new String(ContrasenaNuevaPass1.getPassword());
        String confirmarClave = new String(ConfirmaContrasenaPass.getPassword());

        // Buscar la ventana padre para mostrar mensajes
        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(PanelPrincipal);

        // Llamar al controlador para cambiar la contraseña
        boolean exitoso = cambiarClaveController.cambiarClave(usuario, claveActual, claveNueva, confirmarClave, ventanaPadre);

        if (exitoso) {
            // Limpiar todos los campos después del éxito
            limpiarCampos();
        } else {
            // Limpiar solo los campos de contraseña en caso de error
            limpiarCamposContrasena();
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarCampos() {
        if (usuarioActual == null) { // Solo limpiar si no hay usuario fijo
            TextFieldUsuario.setText("");
        }
        ContrasenaActualPass.setText("");
        ContrasenaNuevaPass1.setText("");
        ConfirmaContrasenaPass.setText("");

        // Enfocar el primer campo disponible
        if (usuarioActual == null) {
            TextFieldUsuario.requestFocus();
        } else {
            ContrasenaActualPass.requestFocus();
        }
    }

    /**
     * Limpia solo los campos relacionados con las contraseñas.
     */
    private void limpiarCamposContrasena() {
        ContrasenaActualPass.setText("");
        ContrasenaNuevaPass1.setText("");
        ConfirmaContrasenaPass.setText("");
        ContrasenaActualPass.requestFocus();
    }

    /**
     * Establece el usuario actual desde otra vista.
     *
     * @param usuario El ID del usuario a establecer
     */
    public void setUsuarioActual(String usuario) {
        this.usuarioActual = usuario;
        if (usuario != null && !usuario.trim().isEmpty()) {
            TextFieldUsuario.setText(usuario);
            TextFieldUsuario.setEditable(false);
        }
    }

    /**
     * Habilita o deshabilita la edición del campo de usuario.
     *
     * @param editable true para habilitar edición, false para deshabilitar
     */
    public void setUsuarioEditable(boolean editable) {
        TextFieldUsuario.setEditable(editable);
    }

    /**
     * Retorna el panel principal de la vista de cambio de contraseña.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipal() {
        return PanelPrincipal;
    }
}