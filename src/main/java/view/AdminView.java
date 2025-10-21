package view;

import model.Admin;
import javax.swing.*;

/**
 * Vista principal para la interfaz de administrador del sistema hospitalario.
 * Proporciona acceso a todas las funcionalidades administrativas a través de una interfaz de pestañas.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class AdminView extends JFrame {
    private JPanel PanelPrincipalAdmin;
    private JTabbedPane TapPanelAdmin;

    private CambiarClaveView cambiarClaveView;
    private DashboardView dashboardView;
    private AgregarPacienteView agregarPacienteView;
    private AgregarMedico agregarMedico;
    private AgregarMedicamentosView agregarMedicamentosView;
    private AgregarFarmaceutaView agregarFarmaceutaView;
    private AgregarAdmin agregarAdmin;
    private Admin adminActual;
    private DashboardView  dashboardViewActual;

    /**
     * Constructor por defecto que inicializa la vista sin un administrador específico.
     */
    public AdminView() {
        this(null);
    }

    /**
     * Constructor que inicializa la vista de administrador con un usuario específico.
     * Configura la interfaz gráfica y todas las pestañas de funcionalidad.
     *
     * @param admin El objeto Admin que representa al administrador actualmente logueado
     */
    public AdminView(Admin admin) {
        this.adminActual = admin;
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

        setTitle("Sistema Hospitalario - Administrador 💼");
        setContentPane(PanelPrincipalAdmin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Si tenemos un admin actual, pasar su ID a la vista de cambiar clave
        if (admin != null) {
            cambiarClaveView = new CambiarClaveView(admin.getId());
        } else {
            cambiarClaveView = new CambiarClaveView();
        }

        agregarPacienteView = new AgregarPacienteView();
        agregarMedico = new AgregarMedico();
        agregarMedicamentosView = new AgregarMedicamentosView();
        agregarFarmaceutaView = new AgregarFarmaceutaView();
        agregarAdmin = new AgregarAdmin();
        dashboardView = new DashboardView();

        TapPanelAdmin.addTab("👨‍⚕️ Agregar Médicos", agregarMedico.getPanelPrincipalAgregarMedico());
        TapPanelAdmin.addTab("💊 Agregar Farmaceutas", agregarFarmaceutaView.getPanelPrincipalAgregarFarmaceuta());
        TapPanelAdmin.addTab("🏥 Agregar Pacientes", agregarPacienteView.getPanelPrincipalAgregarPaciente());
        TapPanelAdmin.addTab("💉 Agregar Medicamentos", agregarMedicamentosView.getPanelPrincipalMedicamentos());
        TapPanelAdmin.addTab("🔧 Agregar Administrador", agregarAdmin.getPanelPrincipalAgregarAdmin());
        TapPanelAdmin.addTab("📊 Dashboard", dashboardView.getPanelPrincipalDashboard());
        TapPanelAdmin.addTab("🔑 Cambiar Clave", cambiarClaveView.getPanelPrincipal());
    }

    /**
     * Retorna el panel principal de la vista de administrador.
     *
     * @return JPanel que contiene todos los componentes de la interfaz de administrador
     */
    public JPanel getPanelPrincipalAdmin(){
        return PanelPrincipalAdmin;
    }
}