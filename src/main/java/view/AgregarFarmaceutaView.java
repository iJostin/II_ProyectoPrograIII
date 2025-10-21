package view;

import controller.FarmaceutaController;
import model.Farmaceuta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Vista para la gestión de farmacéuticos del sistema hospitalario.
 * Permite agregar, modificar, eliminar y listar farmacéuticos.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class AgregarFarmaceutaView {
    private JPanel PanelPrincipalAgregarFarmaceuta;
    private JPanel PanelAgregarFarmaceuta;
    private JPanel PanelId;
    private JPanel PanelNombre;
    private JPanel PanelClave;
    private JPanel PanelIdLabel;
    private JPanel PanelIdText;
    private JLabel IdLabel;
    private JPanel PanelNombreLabel;
    private JPanel PanelNombreText;
    private JPanel PanelClaveLabel;
    private JPanel PanelClaveText;
    private JTextField IdText;
    private JTextField NombreText;
    private JPasswordField ClavePass;
    private JPanel PanelTablefarmaceutas;
    private JTable FarmaceutasTable;
    private JScrollPane ScrollPanelTable;
    private JPanel PanelBotonesFarmaceuta;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton modificarButton;
    private JButton limpiarButton;

    private FarmaceutaController farmaceutaController;
    private DefaultTableModel tableModel;

    /**
     * Constructor que inicializa la vista de gestión de farmacéuticos.
     * Configura el look and feel, inicializa el controlador y los componentes de la interfaz.
     */
    public AgregarFarmaceutaView() {
        // Inicializar controller
        farmaceutaController = new FarmaceutaController();

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

        // Inicializar componentes
        inicializarTabla();
        configurarEventos();
        cargarDatos();
    }

    /**
     * Inicializa la tabla de farmacéuticos con las columnas apropiadas.
     * Configura la tabla para que no sea editable y permite selección simple.
     */
    private void inicializarTabla() {
        // Crear modelo de tabla
        String[] columnas = {"ID", "Nombre", "Clave"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        FarmaceutasTable.setModel(tableModel);
        FarmaceutasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar evento de clic en la tabla
        FarmaceutasTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }
        });
    }

    /**
     * Configura los eventos de los botones de la interfaz.
     * Asocia las acciones de agregar, modificar, eliminar y limpiar.
     */
    private void configurarEventos() {
        // Evento para agregar farmaceuta
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarFarmaceuta();
            }
        });

        // Evento para modificar farmaceuta
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarFarmaceuta();
            }
        });

        // Evento para eliminar farmaceuta
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarFarmaceuta();
            }
        });

        // Evento para limpiar campos
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
    }

    /**
     * Agrega un nuevo farmacéutico al sistema.
     * Valida los campos, verifica duplicados y utiliza el controlador para persistir los datos.
     */
    private void agregarFarmaceuta() {
        if (validarCampos()) {
            String id = IdText.getText().trim();
            String nombre = NombreText.getText().trim();
            String clave = new String(ClavePass.getPassword());

            // Verificar si el ID ya existe
            if (farmaceutaController.buscar(id) != null) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                        "Ya existe un farmaceuta con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Farmaceuta farmaceuta = new Farmaceuta(id, clave, nombre);

            try {
                farmaceutaController.agregar(farmaceuta);
                JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                        "Farmaceuta agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                        "Error al agregar farmaceuta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica un farmacéutico existente en el sistema.
     * Requiere que se seleccione un farmacéutico de la tabla.
     */
    private void modificarFarmaceuta() {
        int filaSeleccionada = FarmaceutasTable.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                    "Seleccione un farmaceuta de la tabla para modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validarCampos()) {
            String id = IdText.getText().trim();
            String nombre = NombreText.getText().trim();
            String clave = new String(ClavePass.getPassword());

            Farmaceuta farmaceuta = new Farmaceuta(id, clave, nombre);

            try {
                farmaceutaController.actualizar(farmaceuta);
                JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                        "Farmaceuta modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                        "Error al modificar farmaceuta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Elimina un farmacéutico del sistema.
     * Solicita confirmación antes de proceder con la eliminación.
     */
    private void eliminarFarmaceuta() {
        int filaSeleccionada = FarmaceutasTable.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                    "Seleccione un farmaceuta de la tabla para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(filaSeleccionada, 0);
        String nombre = (String) tableModel.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(PanelPrincipalAgregarFarmaceuta,
                "¿Está seguro de eliminar al farmaceuta " + nombre + " (ID: " + id + ")?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                farmaceutaController.eliminar(id);
                JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                        "Farmaceuta eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                        "Error al eliminar farmaceuta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Carga los datos del farmacéutico seleccionado en la tabla al formulario.
     */
    private void seleccionarFila() {
        int filaSeleccionada = FarmaceutasTable.getSelectedRow();

        if (filaSeleccionada != -1) {
            String id = (String) tableModel.getValueAt(filaSeleccionada, 0);
            String nombre = (String) tableModel.getValueAt(filaSeleccionada, 1);
            String clave = (String) tableModel.getValueAt(filaSeleccionada, 2);

            IdText.setText(id);
            NombreText.setText(nombre);
            ClavePass.setText(clave);
        }
    }

    /**
     * Limpia todos los campos del formulario y deselecciona cualquier fila de la tabla.
     */
    private void limpiarCampos() {
        IdText.setText("");
        NombreText.setText("");
        ClavePass.setText("");
        FarmaceutasTable.clearSelection();
    }

    /**
     * Valida que los campos del formulario estén completos y sean válidos.
     *
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        if (IdText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                    "El campo ID es obligatorio", "Error de validación", JOptionPane.ERROR_MESSAGE);
            IdText.requestFocus();
            return false;
        }

        if (NombreText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                    "El campo Nombre es obligatorio", "Error de validación", JOptionPane.ERROR_MESSAGE);
            NombreText.requestFocus();
            return false;
        }

        if (ClavePass.getPassword().length == 0) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                    "El campo Clave es obligatorio", "Error de validación", JOptionPane.ERROR_MESSAGE);
            ClavePass.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Carga todos los farmacéuticos desde la base de datos y los muestra en la tabla.
     */
    private void cargarDatos() {
        // Limpiar tabla
        tableModel.setRowCount(0);

        try {
            List<Farmaceuta> farmaceutas = farmaceutaController.listar();

            for (Farmaceuta farmaceuta : farmaceutas) {
                Object[] fila = {
                        farmaceuta.getId(),
                        farmaceuta.getNombre(),
                        farmaceuta.getClave()
                };
                tableModel.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarFarmaceuta,
                    "Error al cargar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retorna el panel principal de la vista de gestión de farmacéuticos.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipalAgregarFarmaceuta() {
        return PanelPrincipalAgregarFarmaceuta;
    }

    /**
     * Método público para refrescar los datos de la tabla desde fuera de la clase.
     * Útil para actualizar la vista después de operaciones externas.
     */
    public void refrescarDatos() {
        cargarDatos();
    }
}