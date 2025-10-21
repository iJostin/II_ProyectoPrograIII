package view;

import model.Medico;
import javax.swing.*;

/**
 * Vista principal para la interfaz de usuario del médico.
 * Proporciona una interfaz con pestañas para las diferentes funcionalidades del médico.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class MedicoView extends JFrame{
    // Componentes de la interfaz gráfica
    private JPanel PanelPrincipalMedico;
    private JTabbedPane TapPanelMedico;
    private JPanel MedicPanel;
    private JPanel MedicFormPanel;
    private JPanel JTableMedicPanel;
    private JLabel IdMedicLabel;
    private JTextField IdMedicTextField;
    private JLabel NameMedicLabel;
    private JTextField NameMedicTextField;
    private JLabel FieldMedicLabel;
    private JTextField FieldTextField;
    private JButton SaveButton;
    private JButton ClearButton;
    private JButton DeleteButton;
    private JTable MedicTabel;
    private JScrollPane JScrollMedicPane;
    private JPanel SearchMedicPanel;
    private JLabel SearchLabel;
    private JTextField SearchTextField;
    private JButton buscarButton;
    private JButton reporteButton;

    // Vistas relacionadas
    private CambiarClaveView cambiarClaveView;
    private RecetaView recetaView;
    private DashboardView dashboardView;
    private Medico medicoActual;
    private HistorialRecetasView historialRecetasView;

    /**
     * Constructor de la vista principal del médico.
     * Inicializa la interfaz gráfica y configura las pestañas de funcionalidad.
     *
     * @param medico El objeto médico que representa al usuario actual
     */
    public MedicoView(Medico medico) {
        this.medicoActual = medico;

        // Configurar el look and feel de Nimbus
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

        // Configuración básica de la ventana
        setTitle("Sistema Hospitalario - Médico 🩺");
        setContentPane(PanelPrincipalMedico);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Inicializar las vistas relacionadas
        cambiarClaveView = new CambiarClaveView(medico.getId());
        recetaView = new RecetaView(medicoActual);
        dashboardView = new DashboardView();
        historialRecetasView = new HistorialRecetasView();

        // Agregar pestañas al panel principal
        TapPanelMedico.addTab("💉 Receta", recetaView.getPanelPrincipalReceta());
        TapPanelMedico.addTab("\uD83D\uDCCB Historial de Recetas", historialRecetasView.getPanelPrincipalHistorialRecetas());
        TapPanelMedico.addTab("🔑 Cambiar Contraseña", cambiarClaveView.getPanelPrincipal());
    }
}