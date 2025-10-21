package view;

import model.Farmaceuta;
import javax.swing.*;

/**
 * Vista principal para la interfaz de farmacéutico del sistema hospitalario.
 * Proporciona acceso a las funcionalidades específicas de farmacéutico a través de una interfaz de pestañas.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class FarmaceutaView extends JFrame {
    private JPanel PanelPrincipalFarmaceuta;
    private JTabbedPane TapPanelFarmaceuta;

    private CambiarClaveView cambiarClaveView;
    private Farmaceuta farmaceutaActual;
    private HistorialRecetasView historialRecetasView;
    private DespachoView despachoView;

    /**
     * Constructor por defecto que inicializa la vista sin un farmacéutico específico.
     */
    public FarmaceutaView() {
        this(null);
    }

    /**
     * Constructor que inicializa la vista de farmacéutico con un usuario específico.
     * Configura la interfaz gráfica y todas las pestañas de funcionalidad.
     *
     * @param farmaceuta El objeto Farmaceuta que representa al farmacéutico actualmente logueado
     */
    public FarmaceutaView(Farmaceuta farmaceuta) {
        this.farmaceutaActual = farmaceuta;
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
        setTitle("Sistema Hospitalario - Farmaceuta 💊");
        setContentPane(PanelPrincipalFarmaceuta);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Si tenemos un farmaceuta actual, pasar su ID a la vista de cambiar clave
        if (farmaceuta != null) {
            cambiarClaveView = new CambiarClaveView(farmaceuta.getId());
        } else {
            cambiarClaveView = new CambiarClaveView();
        }

        historialRecetasView = new HistorialRecetasView();
        despachoView = new DespachoView();

        TapPanelFarmaceuta.addTab("Despacho de recetas",  despachoView.getPanelPrincipalDespacho());
        TapPanelFarmaceuta.addTab("\uD83D\uDCCB Historial de Recetas", historialRecetasView.getPanelPrincipalHistorialRecetas());
        TapPanelFarmaceuta.addTab("🔑 Cambiar Contraseña", cambiarClaveView.getPanelPrincipal());
    }
}