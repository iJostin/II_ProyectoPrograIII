package view;

import model.*;
import controller.MedicamentoController;
import controller.RecetaController;
import controller.PacienteController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Vista para la creación y gestión de recetas médicas.
 * Permite buscar pacientes, agregar medicamentos y crear recetas completas.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class RecetaView {
    // Componentes de la interfaz gráfica
    private JPanel PanelPrincipalReceta;
    private JPanel BuscarPacientePanel;
    private JPanel BuscarPacienteLabelPanel;
    private JLabel BuscarPacienteLabel;
    private JPanel BuscarPacienteTextPanel;
    private JTextField BuscarPacienteText;
    private JPanel AgregarPacienteButtomPanel;
    private JButton AgregarPacienteButtom;
    private JPanel BuscarPacienteTablePanel;
    private JTable PacientesTable;
    private JScrollPane JScrollPacientes;
    private JPanel AgregarMedicamentoPanel;
    private JPanel MedicamentoLabelPanel;
    private JLabel MedicamentoLabel;
    private JPanel MedicamentoTextPanel;
    private JTextField MedicamentoText;
    private JPanel AgregarMedicamentoButtomPanel;
    private JButton AgregarMedicamentoButtom;
    private JPanel MedicamentoTablePanel;
    private JTable MedicamentoTable;
    private JScrollPane JScrollMedicamentos;
    private JPanel RecetaPanel;
    private JTable RecetaTable;
    private JScrollPane JScrollReceta;
    private JPanel PanelButtoms;
    private JButton crearRecetaButton;
    private JButton eliminarPacienteButton;
    private JButton eliminarMedicamentoButton;
    private JPanel PanelTituloReceta;
    private JPanel PanelTituloRecetaLabel;
    private JLabel RecetaLabel;
    private JButton instruccionesButton;
    private JButton duracionDelTratamientoButton;
    private JButton cantidadButton;

    // Datos de la aplicación
    private Medico medicoActual;
    private Paciente pacienteSeleccionado;
    private DefaultTableModel modeloPacientes;
    private DefaultTableModel modeloMedicamentos;
    private DefaultTableModel modeloReceta;
    private Timer timerBusquedaPacientes;
    private Timer timerBusquedaMedicamentos;

    /**
     * Constructor de la vista de recetas.
     * Inicializa la interfaz y configura los listeners para la búsqueda incremental.
     *
     * @param medico El objeto médico que está creando la receta
     */
    public RecetaView(Medico medico) {
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

        inicializarTablas();
        configurarBusquedaIncremental();
        configurarListeners();
    }

    /**
     * Inicializa los modelos de tabla para pacientes, medicamentos y recetas.
     * Configura las columnas y hace que las celdas no sean editables directamente.
     */
    private void inicializarTablas() {
        String[] columnasPacientes = {"ID", "Nombre", "Teléfono", "Fecha Nacimiento"};
        modeloPacientes = new DefaultTableModel(columnasPacientes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        PacientesTable.setModel(modeloPacientes);

        String[] columnasMedicamentos = {"Código", "Nombre", "Presentación"};
        modeloMedicamentos = new DefaultTableModel(columnasMedicamentos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        MedicamentoTable.setModel(modeloMedicamentos);

        String[] columnasReceta = {"Código", "Nombre", "Presentación", "Cantidad", "Instrucciones", "Días"};
        modeloReceta = new DefaultTableModel(columnasReceta, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        RecetaTable.setModel(modeloReceta);
    }

    /**
     * Configura la búsqueda incremental para pacientes y medicamentos.
     * Utiliza timers para evitar búsquedas excesivas durante la escritura.
     */
    private void configurarBusquedaIncremental() {
        timerBusquedaPacientes = new Timer(300, e -> buscarPacientesIncremental());
        timerBusquedaPacientes.setRepeats(false);

        timerBusquedaMedicamentos = new Timer(300, e -> buscarMedicamentosIncremental());
        timerBusquedaMedicamentos.setRepeats(false);

        // Listener para búsqueda de pacientes
        BuscarPacienteText.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timerBusquedaPacientes.restart(); }
            public void removeUpdate(DocumentEvent e) { timerBusquedaPacientes.restart(); }
            public void changedUpdate(DocumentEvent e) { timerBusquedaPacientes.restart(); }
        });

        // Listener para búsqueda de medicamentos
        MedicamentoText.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timerBusquedaMedicamentos.restart(); }
            public void removeUpdate(DocumentEvent e) { timerBusquedaMedicamentos.restart(); }
            public void changedUpdate(DocumentEvent e) { timerBusquedaMedicamentos.restart(); }
        });
    }

    /**
     * Realiza la búsqueda incremental de pacientes basada en el texto ingresado.
     * Filtra pacientes por ID o nombre que coincidan con el criterio de búsqueda.
     */
    private void buscarPacientesIncremental() {
        String criterio = BuscarPacienteText.getText().trim();
        modeloPacientes.setRowCount(0);
        if (criterio.isEmpty()) return;

        List<Paciente> todos = new PacienteController().listar();
        for (Paciente p : todos) {
            if (p.getId().toLowerCase().contains(criterio.toLowerCase()) ||
                    p.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                modeloPacientes.addRow(new Object[]{p.getId(), p.getNombre(), p.getTelefono(), p.getFechaNacimiento()});
            }
        }
    }

    /**
     * Realiza la búsqueda incremental de medicamentos basada en el texto ingresado.
     * Filtra medicamentos por código o nombre que coincidan con el criterio de búsqueda.
     */
    private void buscarMedicamentosIncremental() {
        String criterio = MedicamentoText.getText().trim();
        modeloMedicamentos.setRowCount(0);
        if (criterio.isEmpty()) return;

        List<Medicamento> todos = new MedicamentoController().listar();
        for (Medicamento m : todos) {
            if (m.getCodigo().toLowerCase().contains(criterio.toLowerCase()) ||
                    m.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                modeloMedicamentos.addRow(new Object[]{m.getCodigo(), m.getNombre(), m.getPresentacion()});
            }
        }
    }

    /**
     * Configura los listeners para las interacciones del usuario.
     * Incluye selección de pacientes, medicamentos y acciones de botones.
     */
    private void configurarListeners() {
        // Listener para selección de pacientes
        PacientesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && PacientesTable.getSelectedRow() != -1) seleccionarPaciente();
        });
        PacientesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) seleccionarPaciente();
            }
        });

        // Listener para selección de medicamentos
        MedicamentoTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && MedicamentoTable.getSelectedRow() != -1) seleccionarMedicamento();
        });
        MedicamentoTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) seleccionarMedicamento();
            }
        });

        // Listeners para botones de acciones
        cantidadButton.addActionListener(e -> editarCantidad());
        instruccionesButton.addActionListener(e -> editarInstrucciones());
        duracionDelTratamientoButton.addActionListener(e -> editarDuracion());
        eliminarMedicamentoButton.addActionListener(e -> eliminarMedicamento());
        crearRecetaButton.addActionListener(e -> crearReceta());
    }

    /**
     * Selecciona un paciente de la tabla de resultados de búsqueda.
     * Actualiza el paciente seleccionado y limpia la búsqueda.
     */
    private void seleccionarPaciente() {
        int fila = PacientesTable.getSelectedRow();
        if (fila != -1) {
            String id = modeloPacientes.getValueAt(fila, 0).toString();
            pacienteSeleccionado = new PacienteController().buscar(id);
            if (pacienteSeleccionado != null) {
                JOptionPane.showMessageDialog(PanelPrincipalReceta,
                        "Paciente seleccionado: " + pacienteSeleccionado.getNombre());
                BuscarPacienteText.setText("");
                modeloPacientes.setRowCount(0);
            }
        }
    }

    /**
     * Selecciona un medicamento de la tabla de resultados de búsqueda.
     * Agrega el medicamento seleccionado a la receta en construcción.
     */
    private void seleccionarMedicamento() {
        int fila = MedicamentoTable.getSelectedRow();
        if (fila != -1) {
            String codigo = modeloMedicamentos.getValueAt(fila, 0).toString();
            Medicamento m = MedicamentoController.buscar(codigo);
            if (m != null) {
                agregarMedicamentoAReceta(m);
                MedicamentoText.setText("");
                modeloMedicamentos.setRowCount(0);
            }
        }
    }

    /**
     * Agrega un medicamento a la receta en construcción.
     * Verifica que el medicamento no esté ya agregado a la receta.
     *
     * @param m El medicamento a agregar a la receta
     */
    private void agregarMedicamentoAReceta(Medicamento m) {
        for (int i = 0; i < modeloReceta.getRowCount(); i++) {
            if (modeloReceta.getValueAt(i, 0).equals(m.getCodigo())) {
                JOptionPane.showMessageDialog(PanelPrincipalReceta, "Este medicamento ya está en la receta");
                return;
            }
        }
        modeloReceta.addRow(new Object[]{m.getCodigo(), m.getNombre(), m.getPresentacion(), 1, "Tomar según indicaciones", 7});
    }

    /**
     * Permite editar la cantidad de un medicamento en la receta.
     * Solicita al usuario ingresar una nueva cantidad válida.
     */
    private void editarCantidad() {
        int fila = RecetaTable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Seleccione un medicamento de la receta");
            return;
        }
        String input = JOptionPane.showInputDialog("Ingrese la cantidad:");
        try {
            int cantidad = Integer.parseInt(input);
            if (cantidad > 0) modeloReceta.setValueAt(cantidad, fila, 3);
            else JOptionPane.showMessageDialog(PanelPrincipalReceta, "La cantidad debe ser mayor a 0");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Ingrese un número válido");
        }
    }

    /**
     * Permite editar las instrucciones de uso de un medicamento en la receta.
     * Solicita al usuario ingresar nuevas instrucciones.
     */
    private void editarInstrucciones() {
        int fila = RecetaTable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Seleccione un medicamento de la receta");
            return;
        }
        String actuales = modeloReceta.getValueAt(fila, 4).toString();
        String nuevas = JOptionPane.showInputDialog("Instrucciones de uso:", actuales);
        if (nuevas != null && !nuevas.trim().isEmpty()) {
            modeloReceta.setValueAt(nuevas.trim(), fila, 4);
        }
    }

    /**
     * Permite editar la duración del tratamiento de un medicamento en la receta.
     * Solicita al usuario ingresar una nueva duración en días.
     */
    private void editarDuracion() {
        int fila = RecetaTable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Seleccione un medicamento de la receta");
            return;
        }
        String input = JOptionPane.showInputDialog("Duración del tratamiento en días:");
        try {
            int dias = Integer.parseInt(input);
            if (dias > 0) modeloReceta.setValueAt(dias, fila, 5);
            else JOptionPane.showMessageDialog(PanelPrincipalReceta, "Los días deben ser mayor a 0");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Ingrese un número válido");
        }
    }

    /**
     * Elimina un medicamento seleccionado de la receta en construcción.
     * Solicita confirmación antes de eliminar el medicamento.
     */
    private void eliminarMedicamento() {
        int fila = RecetaTable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Seleccione un medicamento para eliminar");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(PanelPrincipalReceta,
                "¿Está seguro de eliminar este medicamento de la receta?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) modeloReceta.removeRow(fila);
    }

    /**
     * Crea una receta médica completa con todos los datos ingresados.
     * Valida los datos, solicita fecha de retiro y guarda la receta en el sistema.
     */
    private void crearReceta() {
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Debe seleccionar un paciente");
            return;
        }
        if (modeloReceta.getRowCount() == 0) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "La receta debe tener al menos un medicamento");
            return;
        }

        // Pedir fecha de retiro
        JSpinner fechaSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(fechaSpinner, "yyyy-MM-dd");
        fechaSpinner.setEditor(editor);
        fechaSpinner.setValue(new Date());

        int res = JOptionPane.showConfirmDialog(PanelPrincipalReceta, fechaSpinner,
                "Seleccionar Fecha de Retiro", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        Date fecha = (Date) fechaSpinner.getValue();
        LocalDate fechaRetiro = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (fechaRetiro.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta,
                    "La fecha de retiro no puede ser anterior a la fecha actual");
            return;
        }

        // Validar campos completos
        for (int i = 0; i < modeloReceta.getRowCount(); i++) {
            try {
                int cantidad = Integer.parseInt(modeloReceta.getValueAt(i, 3).toString());
                int dias = Integer.parseInt(modeloReceta.getValueAt(i, 5).toString());
                String instrucciones = modeloReceta.getValueAt(i, 4).toString();

                if (cantidad <= 0 || dias <= 0) {
                    throw new NumberFormatException("Valores deben ser mayores a 0");
                }
                if (instrucciones == null || instrucciones.trim().isEmpty()) {
                    throw new IllegalArgumentException("Las instrucciones no pueden estar vacías");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelPrincipalReceta,
                        "Complete correctamente los campos para: " + modeloReceta.getValueAt(i, 1) +
                                "\nError: " + ex.getMessage());
                return;
            }
        }

        // Generar ID único y verificar que no sea null
        String idReceta = UUID.randomUUID().toString();
        if (idReceta == null || idReceta.trim().isEmpty()) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Error al generar ID de receta");
            return;
        }

        // Verificar que el médico actual no sea null
        if (medicoActual == null) {
            JOptionPane.showMessageDialog(PanelPrincipalReceta, "Error: No hay médico asignado");
            return;
        }

        try {
            // Crear la receta
            System.out.println("Creando receta con ID: " + idReceta);
            System.out.println("Paciente: " + pacienteSeleccionado.getNombre());
            System.out.println("Médico: " + medicoActual.getNombre());

            Receta receta = RecetaController.crearReceta(idReceta, pacienteSeleccionado, medicoActual, fechaRetiro);

            if (receta == null) {
                throw new RuntimeException("La receta creada es null");
            }

            // Agregar detalles
            for (int i = 0; i < modeloReceta.getRowCount(); i++) {
                String codigoMed = modeloReceta.getValueAt(i, 0).toString();
                String nombreMed = modeloReceta.getValueAt(i, 1).toString();
                String presentacionMed = modeloReceta.getValueAt(i, 2).toString();

                if (codigoMed == null || nombreMed == null || presentacionMed == null) {
                    throw new IllegalArgumentException("Datos de medicamento incompletos en fila " + (i+1));
                }

                Medicamento m = new Medicamento(codigoMed.trim(), nombreMed.trim(), presentacionMed.trim());
                int cantidad = Integer.parseInt(modeloReceta.getValueAt(i, 3).toString());
                String instrucciones = modeloReceta.getValueAt(i, 4).toString().trim();
                int dias = Integer.parseInt(modeloReceta.getValueAt(i, 5).toString());

                DetalleReceta detalle = new DetalleReceta(m, cantidad, instrucciones, dias);

                System.out.println("Agregando detalle: " + nombreMed + " (cantidad: " + cantidad + ")");
                RecetaController.agregarDetalle(receta.getId(), detalle);
            }

            JOptionPane.showMessageDialog(PanelPrincipalReceta,
                    "✅ Receta creada exitosamente\n" +
                            "ID: " + idReceta + "\n" +
                            "Paciente: " + pacienteSeleccionado.getNombre() + "\n" +
                            "Fecha de retiro: " + fechaRetiro + "\n" +
                            "Medicamentos: " + modeloReceta.getRowCount());

            limpiarFormulario();

        } catch (Exception ex) {
            System.err.println("Error detallado al crear receta:");
            ex.printStackTrace();

            JOptionPane.showMessageDialog(PanelPrincipalReceta,
                    "Error al crear la receta:\n" +
                            ex.getMessage() + "\n\n" +
                            "Revise la consola para más detalles.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia el formulario de receta, reiniciando todos los campos y selecciones.
     */
    private void limpiarFormulario() {
        BuscarPacienteText.setText("");
        MedicamentoText.setText("");
        modeloPacientes.setRowCount(0);
        modeloMedicamentos.setRowCount(0);
        modeloReceta.setRowCount(0);
        pacienteSeleccionado = null;
    }

    /**
     * Retorna el panel principal de la vista de recetas.
     *
     * @return El panel principal de la vista
     */
    public JPanel getPanelPrincipalReceta() {
        return PanelPrincipalReceta;
    }
}