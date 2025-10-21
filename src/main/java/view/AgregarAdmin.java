package view;

import controller.AdminController;
import model.Admin;
import model.Medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista para la gestión de administradores del sistema.
 * Permite agregar, modificar, eliminar y listar administradores.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class AgregarAdmin {
    private JPanel PanelPrincipalAgregarAdmin;
    private JPanel PanelAgregarAdmin;
    private JPanel PanelAdminID;
    private JPanel PanelAdminIDLabel;
    private JPanel PanelAdminIDText;
    private JPanel PanelAdminClave;
    private JPanel PanelAdminClaveLabel;
    private JPanel PanelAdminClaveText;
    private JPanel PanelTableAdmin;
    private JTable TableAdmin;
    private JScrollPane ScrollPanelAdmin;
    private JTextField IDText;
    private JTextField ClaveText;
    private JLabel ClaveLabel;
    private JLabel IDLabel;
    private JPanel PanelAdminBotones;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton modificarButton;
    private JButton limpiarButton;

    private AdminController adminController;
    private DefaultTableModel tableModel;

    /**
     * Constructor que inicializa la vista de gestión de administradores.
     * Configura el look and feel, inicializa el controlador y los componentes de la interfaz.
     */
    public AgregarAdmin() {
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

        adminController = new AdminController();
        inicializarTabla();
        configurarEventos();
        cargarDatos();
    }

    /**
     * Inicializa la tabla de administradores con las columnas apropiadas.
     * Configura la tabla para que no sea editable y permite selección simple.
     */
    private void inicializarTabla() {
        String[] columnas = {"ID", "Clave"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        TableAdmin.setModel(tableModel);
        TableAdmin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Agregar listener para selección en tabla
        TableAdmin.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = TableAdmin.getSelectedRow();
                if (selectedRow >= 0) {
                    cargarDatosEnFormulario(selectedRow);
                }
            }
        });
    }

    /**
     * Configura los eventos de los botones de la interfaz.
     * Asocia las acciones de agregar, modificar, eliminar y limpiar.
     */
    private void configurarEventos() {
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarAdmin();
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarAdmin();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarAdmin();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
    }

    /**
     * Agrega un nuevo administrador al sistema.
     * Valida los campos y utiliza el controlador para persistir los datos.
     */
    private void agregarAdmin() {
        if (validarCampos()) {
            try {
                String id = IDText.getText().trim();
                String clave = ClaveText.getText().trim();

                Admin nuevoAdmin = new Admin(id, clave);
                adminController.agregar(nuevoAdmin);

                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        "Administrador agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        "Error al agregar administrador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica un administrador existente en el sistema.
     * Requiere que se seleccione un administrador de la tabla.
     */
    private void modificarAdmin() {
        int selectedRow = TableAdmin.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                    "Seleccione un administrador de la tabla para modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validarCampos()) {
            try {
                String id = IDText.getText().trim();
                String clave = ClaveText.getText().trim();

                Admin adminModificado = new Admin(id, clave);
                adminController.actualizar(adminModificado);

                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        "Administrador modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        "Error al modificar administrador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Elimina un administrador del sistema.
     * Solicita confirmación antes de proceder con la eliminación.
     */
    private void eliminarAdmin() {
        int selectedRow = TableAdmin.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                    "Seleccione un administrador de la tabla para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(PanelPrincipalAgregarAdmin,
                "¿Está seguro de que desea eliminar el administrador con ID: " + id + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                adminController.eliminar(id);
                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        "Administrador eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                        "Error al eliminar administrador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia los campos del formulario y deselecciona cualquier fila de la tabla.
     */
    private void limpiarFormulario() {
        IDText.setText("");
        ClaveText.setText("");
        TableAdmin.clearSelection();
    }

    /**
     * Carga los datos del administrador seleccionado en el formulario.
     *
     * @param row La fila de la tabla que contiene los datos a cargar
     */
    private void cargarDatosEnFormulario(int row) {
        IDText.setText((String) tableModel.getValueAt(row, 0));
        ClaveText.setText((String) tableModel.getValueAt(row, 1));
    }

    /**
     * Carga todos los administradores desde la base de datos y los muestra en la tabla.
     */
    private void cargarDatos() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            List<Admin> admins = AdminController.listar();
            for (Admin admin : admins) {
                Object[] row = {
                        admin.getId(),
                        admin.getClave(),
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                    "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida que los campos del formulario estén completos y sean válidos.
     *
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        if (IDText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                    "El campo ID es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            IDText.requestFocus();
            return false;
        }

        if (ClaveText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarAdmin,
                    "El campo Clave es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            ClaveText.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Retorna el panel principal de la vista de gestión de administradores.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipalAgregarAdmin() {
        return PanelPrincipalAgregarAdmin;
    }
}