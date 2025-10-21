package view;

import controller.RecetaController;
import model.Receta;
import service.RecetaService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista para el historial de recetas del sistema hospitalario.
 * Permite buscar y filtrar recetas por fecha de confección y por paciente.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class HistorialRecetasView extends JFrame {
    private JPanel PanelPrincipalHistorialRecetas;
    private JPanel PanelBuscarEnHistorial;
    private JPanel PanelBuscarPorFecha;
    private JPanel PanelBuscarPorPaciente;
    private JPanel PanelPorFechaLabel;
    private JLabel BuscarPorFechaLabel;
    private JPanel PanelBuscarPorFechaJCombox;
    private JPanel PanelDiaLabel;
    private JLabel DiaLabel;
    private JPanel PanelMesLabel;
    private JLabel MesLabel;
    private JPanel PanelAnnoLabel;
    private JLabel AnnoLabel;
    private JPanel PanelDiaJCombox;
    private JPanel PanelMesJCombox;
    private JPanel PanelAnnoJCombox;
    private JComboBox<String> DiaBox;
    private JComboBox<String> MesBox;
    private JComboBox<String> AnnoBox;
    private JPanel PanelPacienteLabel;
    private JLabel PacienteLabel;
    private JPanel PanelPacienteText;
    private JTextField PacienteText;
    private JPanel PanelRecetasTable;
    private JTable TableRecetas;
    private JScrollPane ScrollRecetasTable;

    private RecetaService recetaService;
    private DefaultTableModel tableModel;
    private List<Receta> todasLasRecetas; // Cache de todas las recetas

    // Columnas de la tabla
    private final String[] columnas = {
            "ID Receta",
            "ID Paciente",
            "Nombre Paciente",
            "Médico",
            "Estado",
            "Fecha Confección"
    };

    /**
     * Constructor que inicializa la vista del historial de recetas.
     * Configura la interfaz gráfica y carga los datos iniciales.
     */
    public HistorialRecetasView() {
        recetaService = new RecetaService();
        initComponents();
        setupTable();
        cargarTodasLasRecetas();
        setupEventListeners();
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void initComponents() {
        setTitle("Historial de Recetas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Panel principal
        PanelPrincipalHistorialRecetas = new JPanel(new BorderLayout(10, 10));
        PanelPrincipalHistorialRecetas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de búsqueda
        PanelBuscarEnHistorial = new JPanel(new GridLayout(2, 1, 5, 5));
        PanelBuscarEnHistorial.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda"));

        setupPanelBuscarPorFecha();
        setupPanelBuscarPorPaciente();

        PanelBuscarEnHistorial.add(PanelBuscarPorFecha);
        PanelBuscarEnHistorial.add(PanelBuscarPorPaciente);

        // Panel de tabla
        setupPanelTabla();

        // Agregar componentes al panel principal
        PanelPrincipalHistorialRecetas.add(PanelBuscarEnHistorial, BorderLayout.NORTH);
        PanelPrincipalHistorialRecetas.add(PanelRecetasTable, BorderLayout.CENTER);

        add(PanelPrincipalHistorialRecetas);
    }

    /**
     * Configura el panel de búsqueda por fecha con ComboBoxes para día, mes y año.
     */
    private void setupPanelBuscarPorFecha() {
        PanelBuscarPorFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        PanelBuscarPorFecha.setBorder(BorderFactory.createTitledBorder("Buscar por Fecha de Confección"));

        BuscarPorFechaLabel = new JLabel("Fecha:");

        // ComboBoxes para fecha
        DiaLabel = new JLabel("Día:");
        DiaBox = new JComboBox<>();
        DiaBox.addItem("--");
        for (int i = 1; i <= 31; i++) {
            DiaBox.addItem(String.format("%02d", i));
        }

        MesLabel = new JLabel("Mes:");
        MesBox = new JComboBox<>();
        MesBox.addItem("--");
        String[] meses = {"01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"};
        for (String mes : meses) {
            MesBox.addItem(mes);
        }

        AnnoLabel = new JLabel("Año:");
        AnnoBox = new JComboBox<>();
        AnnoBox.addItem("----");
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 10; i--) {
            AnnoBox.addItem(String.valueOf(i));
        }

        PanelBuscarPorFecha.add(BuscarPorFechaLabel);
        PanelBuscarPorFecha.add(DiaLabel);
        PanelBuscarPorFecha.add(DiaBox);
        PanelBuscarPorFecha.add(MesLabel);
        PanelBuscarPorFecha.add(MesBox);
        PanelBuscarPorFecha.add(AnnoLabel);
        PanelBuscarPorFecha.add(AnnoBox);
    }

    /**
     * Configura el panel de búsqueda por paciente con campo de texto.
     */
    private void setupPanelBuscarPorPaciente() {
        PanelBuscarPorPaciente = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        PanelBuscarPorPaciente.setBorder(BorderFactory.createTitledBorder("Buscar por Paciente"));

        PacienteLabel = new JLabel("ID or Nombre del Paciente:");
        PacienteText = new JTextField(20);
        PacienteText.setToolTipText("Escriba para filtrar automáticamente...");

        PanelBuscarPorPaciente.add(PacienteLabel);
        PanelBuscarPorPaciente.add(PacienteText);
    }

    /**
     * Configura el panel que contiene la tabla de recetas.
     */
    private void setupPanelTabla() {
        PanelRecetasTable = new JPanel(new BorderLayout());
        PanelRecetasTable.setBorder(BorderFactory.createTitledBorder("Historial de Recetas"));

        TableRecetas = new JTable();
        TableRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableRecetas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        ScrollRecetasTable = new JScrollPane(TableRecetas);
        ScrollRecetasTable.setPreferredSize(new Dimension(850, 300));

        PanelRecetasTable.add(ScrollRecetasTable, BorderLayout.CENTER);
    }

    /**
     * Configura el modelo de la tabla con las columnas apropiadas.
     */
    private void setupTable() {
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };
        TableRecetas.setModel(tableModel);

        // Configurar anchos de columnas
        TableRecetas.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID Receta
        TableRecetas.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID Paciente
        TableRecetas.getColumnModel().getColumn(2).setPreferredWidth(150); // Nombre Paciente
        TableRecetas.getColumnModel().getColumn(3).setPreferredWidth(150); // Médico
        TableRecetas.getColumnModel().getColumn(4).setPreferredWidth(100); // Estado
        TableRecetas.getColumnModel().getColumn(5).setPreferredWidth(120); // Fecha
    }

    /**
     * Configura los eventos de los componentes de la interfaz.
     * Incluye eventos para filtros de fecha y búsqueda por paciente.
     */
    private void setupEventListeners() {
        // Listener para cambios en los ComboBoxes de fecha
        ActionListener fechaListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltros();
            }
        };

        DiaBox.addActionListener(fechaListener);
        MesBox.addActionListener(fechaListener);
        AnnoBox.addActionListener(fechaListener);

        // Listener para cambios en tiempo real en el campo de texto
        PacienteText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltros();
            }
        });
    }

    /**
     * Aplica los filtros seleccionados a la lista de recetas y actualiza la tabla.
     */
    private void aplicarFiltros() {
        if (todasLasRecetas == null) {
            return;
        }

        List<Receta> recetasFiltradas = todasLasRecetas;

        // Aplicar filtro por fecha si está completo
        LocalDate fechaFiltro = obtenerFechaSeleccionada();
        if (fechaFiltro != null) {
            recetasFiltradas = recetasFiltradas.stream()
                    .filter(r -> r.getFechaConfeccion() != null &&
                            r.getFechaConfeccion().equals(fechaFiltro))
                    .collect(Collectors.toList());
        }

        // Aplicar filtro por paciente si hay texto
        String textoPaciente = PacienteText.getText().trim();
        if (!textoPaciente.isEmpty()) {
            final String textoBusqueda = textoPaciente.toLowerCase();
            recetasFiltradas = recetasFiltradas.stream()
                    .filter(r -> r.getPaciente() != null && (
                            (r.getPaciente().getId() != null &&
                                    r.getPaciente().getId().toLowerCase().contains(textoBusqueda)) ||
                                    (r.getPaciente().getNombre() != null &&
                                            r.getPaciente().getNombre().toLowerCase().contains(textoBusqueda))
                    ))
                    .collect(Collectors.toList());
        }

        actualizarTabla(recetasFiltradas);
    }

    /**
     * Obtiene la fecha seleccionada en los ComboBoxes.
     *
     * @return LocalDate representando la fecha seleccionada, o null si no está completa
     */
    private LocalDate obtenerFechaSeleccionada() {
        try {
            String dia = (String) DiaBox.getSelectedItem();
            String mes = (String) MesBox.getSelectedItem();
            String anno = (String) AnnoBox.getSelectedItem();

            // Si cualquier campo no está seleccionado, no hay filtro por fecha
            if ("--".equals(dia) || "--".equals(mes) || "----".equals(anno)) {
                return null;
            }

            return LocalDate.of(
                    Integer.parseInt(anno),
                    Integer.parseInt(mes),
                    Integer.parseInt(dia)
            );

        } catch (Exception e) {
            // Si hay error en la fecha, no aplicar filtro por fecha
            return null;
        }
    }

    /**
     * Carga todas las recetas desde el servicio y las almacena en cache.
     */
    private void cargarTodasLasRecetas() {
        try {
            todasLasRecetas = recetaService.listarTodas();
            actualizarTabla(todasLasRecetas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            todasLasRecetas = List.of(); // Lista vacía en caso de error
        }
    }

    /**
     * Actualiza la tabla con la lista de recetas proporcionada.
     *
     * @param recetas Lista de recetas a mostrar en la tabla
     */
    private void actualizarTabla(List<Receta> recetas) {
        // Limpiar tabla
        tableModel.setRowCount(0);

        // Agregar recetas a la tabla
        for (Receta receta : recetas) {
            Object[] fila = new Object[6];

            fila[0] = receta.getId() != null ? receta.getId() : "";

            if (receta.getPaciente() != null) {
                fila[1] = receta.getPaciente().getId() != null ? receta.getPaciente().getId() : "";
                fila[2] = receta.getPaciente().getNombre() != null ? receta.getPaciente().getNombre() : "";
            } else {
                fila[1] = "";
                fila[2] = "";
            }

            if (receta.getMedico() != null) {
                fila[3] = receta.getMedico().getNombre() != null ? receta.getMedico().getNombre() : "";
            } else {
                fila[3] = "";
            }

            fila[4] = receta.getEstado() != null ? receta.getEstado() : "";
            fila[5] = receta.getFechaConfeccion() != null ? receta.getFechaConfeccion().toString() : "";

            tableModel.addRow(fila);
        }

        // Actualizar el título del panel con el número de resultados
        String titulo = "Historial de Recetas";
        if (recetas.size() != todasLasRecetas.size()) {
            titulo += " (" + recetas.size() + " de " + todasLasRecetas.size() + " recetas)";
        } else {
            titulo += " (" + recetas.size() + " recetas)";
        }

        PanelRecetasTable.setBorder(BorderFactory.createTitledBorder(titulo));
        PanelRecetasTable.repaint();
    }

    /**
     * Método público para refrescar los datos desde fuera de la clase.
     * Útil para actualizar la vista después de operaciones externas.
     */
    public void refrescarDatos() {
        cargarTodasLasRecetas();
    }

    /**
     * Limpia todos los filtros aplicados y muestra todas las recetas.
     */
    public void limpiarFiltros() {
        // Limpiar ComboBoxes de fecha sin disparar eventos
        DiaBox.removeActionListener(DiaBox.getActionListeners()[0]);
        MesBox.removeActionListener(MesBox.getActionListeners()[0]);
        AnnoBox.removeActionListener(AnnoBox.getActionListeners()[0]);

        DiaBox.setSelectedIndex(0);
        MesBox.setSelectedIndex(0);
        AnnoBox.setSelectedIndex(0);

        // Restaurar listeners
        setupEventListeners();

        // Limpiar campo de paciente
        PacienteText.setText("");

        // Mostrar todas las recetas
        if (todasLasRecetas != null) {
            actualizarTabla(todasLasRecetas);
        }
    }

    /**
     * Retorna el panel principal de la vista de historial de recetas.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipalHistorialRecetas(){
        return PanelPrincipalHistorialRecetas;
    }
}