package view;

import controller.RecetaController;
import model.Receta;
import model.Paciente;
import persistence.RecetaDAO;
import service.PacienteService;
import service.RecetaService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista para el despacho de recetas del sistema hospitalario.
 * Permite buscar recetas por paciente y cambiar su estado (PROCESO, LISTA, ENTREGADA).
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class DespachoView {
    private JPanel PanelPrincipalDespacho;
    private JPanel PanelBusquedaPaciente;
    private JPanel PanelRecetaTable;
    private JPanel PanelBotones;
    private JPanel PanelNombreIDLabel;
    private JLabel NombreIDLabel;
    private JPanel PanelNombreIDText;
    private JTextField IDNombreText;
    private JTable RecetasTable;
    private JScrollPane ScrollTable;
    private JButton limpiarButton;

    private DefaultTableModel tableModel;
    private RecetaService recetaService;
    private PacienteService pacienteService;
    private RecetaController recetaController;

    /**
     * Constructor que inicializa la vista de despacho de recetas.
     * Configura la interfaz gráfica y los servicios necesarios.
     */
    public DespachoView() {
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

        // Inicializar servicios
        recetaService = new RecetaService();
        pacienteService = new PacienteService();
        recetaController = new RecetaController();

        initializeComponents();
        setupEventListeners();
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     * Configura la tabla y establece propiedades visuales.
     */
    private void initializeComponents() {
        // Configurar el modelo de la tabla
        String[] columnas = {"ID Receta", "Paciente", "Médico", "Fecha Emisión", "Fecha Retiro", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        if (RecetasTable != null) {
            RecetasTable.setModel(tableModel);
            RecetasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Configurar el ancho de las columnas
            RecetasTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
            RecetasTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Paciente
            RecetasTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Médico
            RecetasTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha Emisión
            RecetasTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Fecha Retiro
            RecetasTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Estado
        }

        // Configurar placeholder del texto
        if (IDNombreText != null) {
            IDNombreText.setToolTipText("Ingrese ID o nombre del paciente");
        }

        if (NombreIDLabel != null) {
            NombreIDLabel.setText("Buscar Paciente (ID/Nombre):");
        }

        if (limpiarButton != null) {
            limpiarButton.setText("Limpiar");
        }
    }

    /**
     * Configura los eventos de los componentes de la interfaz.
     * Incluye eventos de búsqueda, doble clic y limpieza.
     */
    private void setupEventListeners() {
        // Listener para búsqueda en tiempo real
        if (IDNombreText != null) {
            IDNombreText.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    buscarYMostrarRecetas();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    buscarYMostrarRecetas();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    buscarYMostrarRecetas();
                }
            });
        }

        // Listener para doble clic en la tabla
        if (RecetasTable != null) {
            RecetasTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = RecetasTable.getSelectedRow();
                        if (selectedRow >= 0) {
                            String recetaId = (String) tableModel.getValueAt(selectedRow, 0);
                            mostrarDialogoEstado(recetaId);
                        }
                    }
                }
            });
        }

        // Listener para botón limpiar
        if (limpiarButton != null) {
            limpiarButton.addActionListener(e -> limpiarBusqueda());
        }
    }

    /**
     * Realiza la búsqueda de recetas según el texto ingresado y actualiza la tabla.
     */
    private void buscarYMostrarRecetas() {
        String textoBusqueda = IDNombreText.getText().trim();

        // Limpiar tabla si no hay texto de búsqueda
        if (textoBusqueda.isEmpty()) {
            tableModel.setRowCount(0);
            return;
        }

        try {
            // Buscar pacientes que coincidan con el texto de búsqueda
            List<Paciente> pacientesEncontrados = buscarPacientes(textoBusqueda);

            if (pacientesEncontrados.isEmpty()) {
                tableModel.setRowCount(0);
                return;
            }

            // Obtener todas las recetas
            List<Receta> todasRecetas = recetaService.listarTodas();

            // Filtrar recetas que pertenezcan a los pacientes encontrados
            List<Receta> recetasFiltradas = todasRecetas.stream()
                    .filter(receta -> {
                        if (receta.getPaciente() == null) return false;

                        return pacientesEncontrados.stream()
                                .anyMatch(paciente ->
                                        paciente.getId().equals(receta.getPaciente().getId())
                                );
                    })
                    .collect(Collectors.toList());

            // Actualizar tabla
            actualizarTabla(recetasFiltradas);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(PanelPrincipalDespacho,
                    "Error al buscar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Busca pacientes que coincidan con el texto de búsqueda (por ID o nombre).
     *
     * @param textoBusqueda El texto a buscar
     * @return Lista de pacientes encontrados
     */
    private List<Paciente> buscarPacientes(String textoBusqueda) {
        List<Paciente> pacientesEncontrados = new ArrayList<>();
        List<Paciente> todosPacientes = pacienteService.listar();

        for (Paciente paciente : todosPacientes) {
            // Buscar por ID del paciente
            if (paciente.getId() != null &&
                    paciente.getId().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                pacientesEncontrados.add(paciente);
                continue;
            }

            // Buscar por nombre del paciente
            if (paciente.getNombre() != null &&
                    paciente.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                pacientesEncontrados.add(paciente);
            }
        }

        return pacientesEncontrados;
    }

    /**
     * Actualiza la tabla con las recetas proporcionadas.
     *
     * @param recetas Lista de recetas a mostrar en la tabla
     */
    private void actualizarTabla(List<Receta> recetas) {
        // Limpiar tabla
        tableModel.setRowCount(0);

        // Formato para fechas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Agregar recetas a la tabla
        for (Receta receta : recetas) {
            Object[] fila = new Object[6];

            fila[0] = receta.getId();
            fila[1] = receta.getPaciente() != null ? receta.getPaciente().getNombre() : "N/A";
            fila[2] = receta.getMedico() != null ? receta.getMedico().getNombre() : "N/A";
            fila[3] = receta.getFechaConfeccion() != null ? receta.getFechaConfeccion().format(formatter) : "N/A";
            fila[4] = receta.getFechaRetiro() != null ? receta.getFechaRetiro().format(formatter) : "N/A";
            fila[5] = receta.getEstado() != null ? receta.getEstado() : "N/A";

            tableModel.addRow(fila);
        }
    }

    /**
     * Muestra un diálogo para cambiar el estado de una receta específica.
     *
     * @param recetaId El ID de la receta a modificar
     */
    private void mostrarDialogoEstado(String recetaId) {
        // Crear opciones del combobox
        String[] estados = {"PROCESO", "LISTA", "ENTREGADA"};

        // Buscar la receta actual para mostrar su estado
        Receta receta = recetaService.buscarPorId(recetaId);
        String estadoActual = receta != null ? receta.getEstado() : "CONFECCIONADA";

        // Crear panel personalizado para el diálogo
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelInfo = new JLabel("<html><b>Receta ID:</b> " + recetaId +
                "<br><b>Estado actual:</b> " + estadoActual + "</html>");
        JLabel labelPregunta = new JLabel("Seleccione el nuevo estado:");

        JComboBox<String> comboEstados = new JComboBox<>(estados);
        comboEstados.setPreferredSize(new Dimension(200, 25));

        panel.add(labelInfo, BorderLayout.NORTH);
        panel.add(labelPregunta, BorderLayout.CENTER);
        panel.add(comboEstados, BorderLayout.SOUTH);

        // Mostrar diálogo
        int result = JOptionPane.showConfirmDialog(
                PanelPrincipalDespacho,
                panel,
                "Cambiar Estado de Receta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String nuevoEstado = (String) comboEstados.getSelectedItem();
            cambiarEstadoReceta(recetaId, nuevoEstado);
        }
    }

    /**
     * Cambia el estado de una receta específica.
     *
     * @param recetaId El ID de la receta a modificar
     * @param nuevoEstado El nuevo estado a asignar
     */
    private void cambiarEstadoReceta(String recetaId, String nuevoEstado) {
        try {
            // Si el estado es ENTREGADA, actualizar también la fecha de retiro
            if ("ENTREGADA".equalsIgnoreCase(nuevoEstado)) {
                actualizarRecetaEntregada(recetaId, nuevoEstado);
            } else {
                // Solo cambiar estado para otros casos
                recetaService.cambiarEstado(recetaId, nuevoEstado);
            }

            // Crear un nuevo servicio para obtener datos frescos
            RecetaService servicioFresco = new RecetaService();

            // Actualizar la fila específica en la tabla
            actualizarFilaEnTabla(recetaId, nuevoEstado, servicioFresco);

            // Mostrar mensaje de confirmación
            String mensaje = "Estado de la receta " + recetaId + " cambiado a: " + nuevoEstado;
            if ("ENTREGADA".equalsIgnoreCase(nuevoEstado)) {
                mensaje += "\nFecha de retiro actualizada a: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

            JOptionPane.showMessageDialog(
                    PanelPrincipalDespacho,
                    mensaje,
                    "Estado Actualizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    PanelPrincipalDespacho,
                    "Error al cambiar estado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    /**
     * Actualiza una receta como entregada, estableciendo la fecha de retiro actual.
     *
     * @param recetaId El ID de la receta a actualizar
     * @param nuevoEstado El nuevo estado (debe ser "ENTREGADA")
     */
    private void actualizarRecetaEntregada(String recetaId, String nuevoEstado) {
        try {
            // Buscar la receta
            Receta receta = recetaService.buscarPorId(recetaId);
            if (receta != null) {
                // Cambiar el estado
                receta.setEstado(nuevoEstado);

                // Actualizar la fecha de retiro a la fecha actual
                receta.setFechaRetiro(LocalDate.now());

                // Guardar los cambios usando el DAO directamente para asegurar que se guarden ambos cambios
                RecetaDAO dao = new RecetaDAO();
                dao.actualizar(receta);

                System.out.println("Receta " + recetaId + " marcada como ENTREGADA con fecha: " + LocalDate.now());
            } else {
                throw new IllegalStateException("No se encontró la receta con ID: " + recetaId);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar receta como entregada: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Actualiza una fila específica en la tabla con datos frescos.
     *
     * @param recetaId El ID de la receta a actualizar
     * @param nuevoEstado El nuevo estado de la receta
     * @param servicioFresco Servicio para obtener datos actualizados
     */
    private void actualizarFilaEnTabla(String recetaId, String nuevoEstado, RecetaService servicioFresco) {
        // Buscar la fila que corresponde al ID de receta
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String idEnTabla = (String) tableModel.getValueAt(i, 0);
            if (recetaId.equals(idEnTabla)) {
                // Actualizar solo la columna del estado
                tableModel.setValueAt(nuevoEstado, i, 5);

                // Opcional: Actualizar toda la fila con datos frescos
                try {
                    Receta recetaActualizada = servicioFresco.buscarPorId(recetaId);
                    if (recetaActualizada != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        tableModel.setValueAt(recetaActualizada.getId(), i, 0);
                        tableModel.setValueAt(
                                recetaActualizada.getPaciente() != null ?
                                        recetaActualizada.getPaciente().getNombre() : "N/A", i, 1
                        );
                        tableModel.setValueAt(
                                recetaActualizada.getMedico() != null ?
                                        recetaActualizada.getMedico().getNombre() : "N/A", i, 2
                        );
                        tableModel.setValueAt(
                                recetaActualizada.getFechaConfeccion() != null ?
                                        recetaActualizada.getFechaConfeccion().format(formatter) : "N/A", i, 3
                        );
                        tableModel.setValueAt(
                                recetaActualizada.getFechaRetiro() != null ?
                                        recetaActualizada.getFechaRetiro().format(formatter) : "N/A", i, 4
                        );
                        tableModel.setValueAt(recetaActualizada.getEstado(), i, 5);
                    }
                } catch (Exception e) {
                    // Si hay error actualizando los datos completos, al menos el estado ya está actualizado
                    System.err.println("Error al actualizar datos completos de la fila: " + e.getMessage());
                }

                // Repintar la tabla para asegurar que se vea el cambio
                tableModel.fireTableRowsUpdated(i, i);
                break;
            }
        }
    }

    /**
     * Limpia el campo de búsqueda y la tabla.
     */
    private void limpiarBusqueda() {
        if (IDNombreText != null) {
            IDNombreText.setText("");
        }
        // La tabla se limpiará automáticamente por el listener del documento
    }

    /**
     * Muestra la ventana de despacho de recetas.
     */
    public void mostrar() {
        JFrame frame = new JFrame("Despacho de Recetas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(PanelPrincipalDespacho);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Getters

    /**
     * Retorna el panel principal de la vista de despacho.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipalDespacho() {
        return PanelPrincipalDespacho;
    }

    /**
     * Retorna el campo de texto para búsqueda por ID o nombre.
     *
     * @return JTextField del campo de búsqueda
     */
    public JTextField getIDNombreText() {
        return IDNombreText;
    }

    /**
     * Retorna la tabla de recetas.
     *
     * @return JTable que muestra las recetas
     */
    public JTable getRecetasTable() {
        return RecetasTable;
    }

    /**
     * Retorna el botón de limpiar.
     *
     * @return JButton para limpiar la búsqueda
     */
    public JButton getLimpiarButton() {
        return limpiarButton;
    }
}