package view;

import controller.MedicamentoController;
import model.Medicamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista para la gestión de medicamentos del sistema hospitalario.
 * Permite agregar, modificar, eliminar y listar medicamentos con sus propiedades.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class AgregarMedicamentosView extends JFrame {
    private JPanel PanelPrincipalMedicamentos;
    private JPanel PanelAgregarMedicamentos;
    private JPanel PanelCodigoLabel;
    private JPanel PanelCodigoText;
    private JPanel PanelNombreLabel;
    private JPanel PanelNombreText;
    private JPanel PanelPresentacionLabel;
    private JPanel PanelPresentacionText;
    private JLabel CodigoLabel;
    private JLabel NombreLabel;
    private JLabel PresentacionLabel;
    private JTextField CodigoText;
    private JTextField NombreText;
    private JTextField PresentacionText;
    private JPanel PanelTableMedicamentos;
    private JTable TableMedicamentos;
    private JPanel PanelBotonesMedicamentos;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton modificarButton;
    private JButton limpiarButton;
    private JScrollPane ScrollPanelMedicamentos;

    private MedicamentoController medicamentoController;
    private DefaultTableModel tableModel;

    /**
     * Constructor que inicializa la vista de gestión de medicamentos.
     * Configura el look and feel, inicializa el controlador y los componentes de la interfaz.
     */
    public AgregarMedicamentosView() {
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

        medicamentoController = new MedicamentoController();
        inicializarTabla();
        configurarEventos();
        cargarDatos();
    }

    /**
     * Inicializa la tabla de medicamentos con las columnas apropiadas.
     * Configura la tabla para que no sea editable y permite selección simple.
     */
    private void inicializarTabla() {
        String[] columnas = {"Código", "Nombre", "Presentación"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        TableMedicamentos.setModel(tableModel);
        TableMedicamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Agregar listener para selección en tabla
        TableMedicamentos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = TableMedicamentos.getSelectedRow();
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
                agregarMedicamento();
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarMedicamento();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMedicamento();
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
     * Agrega un nuevo medicamento al sistema.
     * Valida los campos, verifica duplicados y utiliza el controlador para persistir los datos.
     */
    private void agregarMedicamento() {
        if (validarCampos()) {
            try {
                String codigo = CodigoText.getText().trim();
                String nombre = NombreText.getText().trim();
                String presentacion = PresentacionText.getText().trim();

                // Verificar si ya existe un medicamento con ese código
                Medicamento existente = medicamentoController.buscar(codigo);
                if (existente != null) {
                    JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                            "Ya existe un medicamento con el código: " + codigo,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Medicamento nuevoMedicamento = new Medicamento(codigo, nombre, presentacion);
                medicamentoController.agregar(nuevoMedicamento);

                JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                        "Medicamento agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                        "Error al agregar medicamento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica un medicamento existente en el sistema.
     * Requiere que se seleccione un medicamento de la tabla.
     */
    private void modificarMedicamento() {
        int selectedRow = TableMedicamentos.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                    "Seleccione un medicamento de la tabla para modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validarCampos()) {
            try {
                String codigo = CodigoText.getText().trim();
                String nombre = NombreText.getText().trim();
                String presentacion = PresentacionText.getText().trim();

                Medicamento medicamentoModificado = new Medicamento(codigo, nombre, presentacion);
                medicamentoController.actualizar(medicamentoModificado);

                JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                        "Medicamento modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                        "Error al modificar medicamento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Elimina un medicamento del sistema.
     * Solicita confirmación antes de proceder con la eliminación.
     */
    private void eliminarMedicamento() {
        int selectedRow = TableMedicamentos.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                    "Seleccione un medicamento de la tabla para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        String nombre = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(PanelPrincipalMedicamentos,
                "¿Está seguro de que desea eliminar el medicamento:\n" +
                        "Código: " + codigo + "\n" +
                        "Nombre: " + nombre + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                medicamentoController.eliminar(codigo);
                JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                        "Medicamento eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                        "Error al eliminar medicamento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia todos los campos del formulario y deselecciona cualquier fila de la tabla.
     */
    private void limpiarFormulario() {
        CodigoText.setText("");
        NombreText.setText("");
        PresentacionText.setText("");
        TableMedicamentos.clearSelection();
    }

    /**
     * Carga los datos del medicamento seleccionado en la tabla al formulario.
     *
     * @param row La fila de la tabla que contiene los datos a cargar
     */
    private void cargarDatosEnFormulario(int row) {
        CodigoText.setText((String) tableModel.getValueAt(row, 0));
        NombreText.setText((String) tableModel.getValueAt(row, 1));
        PresentacionText.setText((String) tableModel.getValueAt(row, 2));
    }

    /**
     * Carga todos los medicamentos desde la base de datos y los muestra en la tabla.
     */
    private void cargarDatos() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            List<Medicamento> medicamentos = medicamentoController.listar();
            for (Medicamento medicamento : medicamentos) {
                Object[] row = {
                        medicamento.getCodigo(),
                        medicamento.getNombre(),
                        medicamento.getPresentacion()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                    "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida que los campos del formulario estén completos y sean válidos.
     * Incluye validación de campos obligatorios y formato del código.
     *
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        if (CodigoText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                    "El campo Código es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            CodigoText.requestFocus();
            return false;
        }

        if (NombreText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                    "El campo Nombre es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            NombreText.requestFocus();
            return false;
        }

        if (PresentacionText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                    "El campo Presentación es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            PresentacionText.requestFocus();
            return false;
        }

        // Validación adicional: verificar que el código solo contenga caracteres alfanuméricos
        String codigo = CodigoText.getText().trim();
        if (!codigo.matches("[a-zA-Z0-9]+")) {
            JOptionPane.showMessageDialog(PanelPrincipalMedicamentos,
                    "El código solo puede contener letras y números", "Validación", JOptionPane.WARNING_MESSAGE);
            CodigoText.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Retorna el panel principal de la vista de gestión de medicamentos.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipalMedicamentos() {
        return PanelPrincipalMedicamentos;
    }
}