package view;

import controller.MedicoController;
import model.Medico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista para la gestión de médicos del sistema hospitalario.
 * Permite agregar, modificar, eliminar y listar médicos con sus especialidades.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class AgregarMedico {
    private JPanel PanelPrincipalAgregarMedico;
    private JPanel PanelAgregarMedicos;
    private JPanel PanelIdLabel;
    private JPanel PanelIdText;
    private JLabel IdLabel;
    private JTextField IdText;
    private JPanel PanelNombreLabel;
    private JPanel PanelNombreText;
    private JLabel NombreLabel;
    private JTextField NombreText;
    private JPanel PanelClaveLabel;
    private JLabel ClaveLabel;
    private JTextField ClaveText;
    private JTextField EspecialidadText;
    private JPanel PanelEspecialidadLabel;
    private JLabel EspecialidadLabel;
    private JPanel PanelClaveText;
    private JPanel PanelEspecialidadText;
    private JPanel PanelTableMedicos;
    private JTable TableMedicos;
    private JScrollPane panelScrollMedicos;
    private JPanel PanelMedicosBottums;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton modificarButton;
    private JButton limpiarButton;

    private MedicoController medicoController;
    private DefaultTableModel tableModel;

    /**
     * Constructor que inicializa la vista de gestión de médicos.
     * Configura el look and feel, inicializa el controlador y los componentes de la interfaz.
     */
    public AgregarMedico() {
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

        medicoController = new MedicoController();
        inicializarTabla();
        configurarEventos();
        cargarDatos();
    }

    /**
     * Inicializa la tabla de médicos con las columnas apropiadas.
     * Configura la tabla para que no sea editable y permite selección simple.
     */
    private void inicializarTabla() {
        String[] columnas = {"ID", "Nombre", "Clave", "Especialidad"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        TableMedicos.setModel(tableModel);
        TableMedicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Agregar listener para selección en tabla
        TableMedicos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = TableMedicos.getSelectedRow();
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
                agregarMedico();
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarMedico();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMedico();
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
     * Agrega un nuevo médico al sistema.
     * Valida los campos y utiliza el controlador para persistir los datos.
     */
    private void agregarMedico() {
        if (validarCampos()) {
            try {
                String id = IdText.getText().trim();
                String nombre = NombreText.getText().trim();
                String clave = ClaveText.getText().trim();
                String especialidad = EspecialidadText.getText().trim();

                Medico nuevoMedico = new Medico(id, clave, nombre, especialidad);
                medicoController.agregar(nuevoMedico);

                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        "Médico agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        "Error al agregar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica un médico existente en el sistema.
     * Requiere que se seleccione un médico de la tabla.
     */
    private void modificarMedico() {
        int selectedRow = TableMedicos.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                    "Seleccione un médico de la tabla para modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validarCampos()) {
            try {
                String id = IdText.getText().trim();
                String nombre = NombreText.getText().trim();
                String clave = ClaveText.getText().trim();
                String especialidad = EspecialidadText.getText().trim();

                Medico medicoModificado = new Medico(id, clave, nombre, especialidad);
                medicoController.actualizar(medicoModificado);

                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        "Médico modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        "Error al modificar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Elimina un médico del sistema.
     * Solicita confirmación antes de proceder con la eliminación.
     */
    private void eliminarMedico() {
        int selectedRow = TableMedicos.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                    "Seleccione un médico de la tabla para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(PanelPrincipalAgregarMedico,
                "¿Está seguro de que desea eliminar el médico con ID: " + id + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                medicoController.eliminar(id);
                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        "Médico eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                        "Error al eliminar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia todos los campos del formulario y deselecciona cualquier fila de la tabla.
     */
    private void limpiarFormulario() {
        IdText.setText("");
        NombreText.setText("");
        ClaveText.setText("");
        EspecialidadText.setText("");
        TableMedicos.clearSelection();
    }

    /**
     * Carga los datos del médico seleccionado en la tabla al formulario.
     *
     * @param row La fila de la tabla que contiene los datos a cargar
     */
    private void cargarDatosEnFormulario(int row) {
        IdText.setText((String) tableModel.getValueAt(row, 0));
        NombreText.setText((String) tableModel.getValueAt(row, 1));
        ClaveText.setText((String) tableModel.getValueAt(row, 2));
        EspecialidadText.setText((String) tableModel.getValueAt(row, 3));
    }

    /**
     * Carga todos los médicos desde la base de datos y los muestra en la tabla.
     */
    private void cargarDatos() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            List<Medico> medicos = medicoController.listar();
            for (Medico medico : medicos) {
                Object[] row = {
                        medico.getId(),
                        medico.getNombre(),
                        medico.getClave(),
                        medico.getEspecialidad()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                    "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida que los campos del formulario estén completos y sean válidos.
     *
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        if (IdText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                    "El campo ID es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            IdText.requestFocus();
            return false;
        }

        if (NombreText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                    "El campo Nombre es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            NombreText.requestFocus();
            return false;
        }

        if (ClaveText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                    "El campo Clave es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            ClaveText.requestFocus();
            return false;
        }

        if (EspecialidadText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalAgregarMedico,
                    "El campo Especialidad es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            EspecialidadText.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Retorna el panel principal de la vista de gestión de médicos.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipalAgregarMedico() {
        return PanelPrincipalAgregarMedico;
    }
}