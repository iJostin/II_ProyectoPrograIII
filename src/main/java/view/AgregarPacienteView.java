package view;

import controller.PacienteController;
import model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista para la gestión de pacientes del sistema hospitalario.
 * Permite agregar, modificar, eliminar y listar pacientes con información personal completa.
 * Incluye manejo de fechas de nacimiento mediante ComboBoxes.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class AgregarPacienteView extends JFrame {
    private JPanel PanelPrincipalPacientes;
    private JPanel PanelAgregarPaciente;
    private JPanel PanelIdLabel;
    private JLabel IdLabel;
    private JPanel PanelIdText;
    private JTextField IdText;
    private JPanel PanelNombreLabel;
    private JLabel NombreLabel;
    private JPanel PanelNombreText;
    private JTextField NombreText;
    private JPanel PanelFechaNacimiento;
    private JPanel PanelDia;
    private JPanel PanelMes;
    private JPanel PanelAnno;
    private JPanel PanelDiaLabel;
    private JPanel PanelDiaCombox;
    private JLabel DiaLabel;
    private JPanel TituloPanel;
    private JLabel TituloLabel;
    private JComboBox<String> DiaCombox;
    private JPanel PanelMesLabel;
    private JPanel PanelMesCombox;
    private JComboBox<String> MesCombox;
    private JPanel PanelAnnoLabel;
    private JLabel AnnoLabel;
    private JComboBox<String> AnnoCombox;
    private JPanel PanelAnnoCombox;
    private JPanel NumeroTelefonoPanel;
    private JPanel PanelNumeroLabel;
    private JPanel PanelNumeroText;
    private JLabel NumeroLabel;
    private JTextField NumeroText;
    private JPanel PanelButtoms;
    private JPanel PanelTablePacientes;
    private JTable PacientesTable;
    private JScrollPane JScrollPacientes;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton modificarButton;
    private JButton limpiarButton;

    private PacienteController pacienteController;
    private DefaultTableModel tableModel;

    /**
     * Constructor que inicializa la vista de gestión de pacientes.
     * Configura el look and feel, inicializa el controlador y los componentes de la interfaz.
     */
    public AgregarPacienteView() {
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
        pacienteController = new PacienteController();
        inicializarComboBoxes();
        inicializarTabla();
        configurarEventos();
        cargarDatos();
    }

    /**
     * Inicializa los ComboBoxes para la selección de fecha de nacimiento.
     * Configura días (1-31), meses (1-12) y años (desde 1900 hasta el año actual).
     */
    private void inicializarComboBoxes() {
        // Inicializar ComboBox de días
        for (int i = 1; i <= 31; i++) {
            DiaCombox.addItem(String.format("%02d", i));
        }

        // Inicializar ComboBox de meses
        String[] meses = {"01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"};
        for (String mes : meses) {
            MesCombox.addItem(mes);
        }

        // Inicializar ComboBox de años (desde 1900 hasta año actual)
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear; year >= 1900; year--) {
            AnnoCombox.addItem(String.valueOf(year));
        }
    }

    /**
     * Inicializa la tabla de pacientes con las columnas apropiadas.
     * Configura la tabla para que no sea editable y permite selección simple.
     */
    private void inicializarTabla() {
        String[] columnas = {"ID", "Nombre", "Fecha de nacimiento", "Telefono"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        PacientesTable.setModel(tableModel);
        PacientesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Agregar listener para selección en tabla
        PacientesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = PacientesTable.getSelectedRow();
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
                agregarPaciente();
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarPaciente();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPaciente();
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
     * Agrega un nuevo paciente al sistema.
     * Valida los campos y utiliza el controlador para persistir los datos.
     */
    private void agregarPaciente() {
        if (validarCampos()) {
            try {
                String id = IdText.getText().trim();
                String nombre = NombreText.getText().trim();
                LocalDate fecha = obtenerFechaSeleccionada();
                String telefono = NumeroText.getText().trim();

                Paciente nuevoPaciente = new Paciente(id, nombre, fecha, telefono);
                pacienteController.agregar(nuevoPaciente);

                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        "Paciente agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        "Error al agregar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Modifica un paciente existente en el sistema.
     * Requiere que se seleccione un paciente de la tabla.
     */
    private void modificarPaciente() {
        int selectedRow = PacientesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                    "Seleccione un paciente de la tabla para modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validarCampos()) {
            try {
                String id = IdText.getText().trim();
                String nombre = NombreText.getText().trim();
                LocalDate fecha = obtenerFechaSeleccionada();
                String telefono = NumeroText.getText().trim();

                Paciente pacienteModificado = new Paciente(id, nombre, fecha, telefono);
                pacienteController.actualizar(pacienteModificado);

                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        "Paciente modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        "Error al modificar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Elimina un paciente del sistema.
     * Solicita confirmación antes de proceder con la eliminación.
     */
    private void eliminarPaciente() {
        int selectedRow = PacientesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                    "Seleccione un paciente de la tabla para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(PanelPrincipalPacientes,
                "¿Está seguro de que desea eliminar el paciente con ID: " + id + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pacienteController.eliminar(id);
                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        "Paciente eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                        "Error al eliminar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia todos los campos del formulario y deselecciona cualquier fila de la tabla.
     */
    private void limpiarFormulario() {
        IdText.setText("");
        NombreText.setText("");
        NumeroText.setText("");
        DiaCombox.setSelectedIndex(0);
        MesCombox.setSelectedIndex(0);
        AnnoCombox.setSelectedIndex(0);
        PacientesTable.clearSelection();
    }

    /**
     * Carga los datos del paciente seleccionado en la tabla al formulario.
     * Incluye el parsing de la fecha para configurar los ComboBoxes correspondientes.
     *
     * @param row La fila de la tabla que contiene los datos a cargar
     */
    private void cargarDatosEnFormulario(int row) {
        IdText.setText((String) tableModel.getValueAt(row, 0));
        NombreText.setText((String) tableModel.getValueAt(row, 1));

        // Parsear la fecha y configurar los ComboBox
        String fechaStr = (String) tableModel.getValueAt(row, 2);
        if (fechaStr != null && !fechaStr.isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(fechaStr);
                DiaCombox.setSelectedItem(String.format("%02d", fecha.getDayOfMonth()));
                MesCombox.setSelectedItem(String.format("%02d", fecha.getMonthValue()));
                AnnoCombox.setSelectedItem(String.valueOf(fecha.getYear()));
            } catch (Exception e) {
                System.err.println("Error al parsear fecha: " + e.getMessage());
            }
        }

        NumeroText.setText((String) tableModel.getValueAt(row, 3));
    }

    /**
     * Carga todos los pacientes desde la base de datos y los muestra en la tabla.
     */
    private void cargarDatos() {
        tableModel.setRowCount(0); // Limpiar tabla
        try {
            List<Paciente> pacientes = pacienteController.listar();
            for (Paciente paciente : pacientes) {
                Object[] row = {
                        paciente.getId(),
                        paciente.getNombre(),
                        paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento().toString() : "",
                        paciente.getTelefono()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                    "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene la fecha seleccionada en los ComboBoxes y la convierte a LocalDate.
     *
     * @return LocalDate representando la fecha seleccionada
     * @throws IllegalArgumentException si la fecha seleccionada no es válida
     */
    private LocalDate obtenerFechaSeleccionada() {
        try {
            int dia = Integer.parseInt((String) DiaCombox.getSelectedItem());
            int mes = Integer.parseInt((String) MesCombox.getSelectedItem());
            int anno = Integer.parseInt((String) AnnoCombox.getSelectedItem());

            return LocalDate.of(anno, mes, dia);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha seleccionada no válida");
        }
    }

    /**
     * Valida que los campos del formulario estén completos y sean válidos.
     * Incluye validación de campos obligatorios y validación de fecha.
     *
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        if (IdText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                    "El campo ID es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            IdText.requestFocus();
            return false;
        }

        if (NombreText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                    "El campo Nombre es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            NombreText.requestFocus();
            return false;
        }

        if (NumeroText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                    "El campo Teléfono es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            NumeroText.requestFocus();
            return false;
        }

        // Validar que la fecha sea válida
        try {
            obtenerFechaSeleccionada();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PanelPrincipalPacientes,
                    "Seleccione una fecha de nacimiento válida", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Retorna el panel principal de la vista de gestión de pacientes.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipalAgregarPaciente() {
        return PanelPrincipalPacientes;
    }
}