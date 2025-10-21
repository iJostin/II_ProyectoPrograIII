package view;

import model.Receta;
import model.DetalleReceta;
import service.RecetaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

/**
 * Vista del dashboard del sistema hospitalario.
 * Proporciona visualizaciones gráficas de datos de medicamentos y recetas.
 * Incluye gráficos de líneas para medicamentos y gráfico de pastel para estados de recetas.
 *
 * @author 402590406-Jostin Campos Cortés, 604720874-Bryan Fernández Arguedas, 801370342-Lesber Huerta Cornejo
 * @version 1.0
 */
public class DashboardView {
    // Componentes del form - estos son generados automáticamente por IntelliJ Form Designer
    private JPanel PanelPrincipalDashboard;
    private JPanel PanelDatos;
    private JPanel PanelGraficoMedicamentos;
    private JPanel PanelGraficoRecetas;
    private JPanel PanelDesdeLabel;
    private JLabel DesdeLabel;
    private JPanel PanelHastaLabel;
    private JLabel HastaLabel;
    private JPanel PanelDesdeAnno;
    private JPanel PanelHastaAnno;
    private JPanel PanelDesdeMes;
    private JPanel PanelHastaMes;
    private JComboBox<String> DesdeAnno;
    private JComboBox<String> HastaAnno;
    private JComboBox<String> DesdeMes;
    private JComboBox<String> HastaMes;
    private JPanel PanelMedicamentosLabel;
    private JLabel MedicamentosLabel;
    private JPanel PanelMedicamentosJCombox;
    private JComboBox<String> MedicamentosCombox;
    private JPanel PanelBotones;
    private JButton checkButton;
    private JButton limpiarButttom;
    private JPanel PanelTable;
    private JTable MedicamentosTable;
    private JScrollPane ScrollMedicamentos;

    // Variables para la lógica de negocio
    private RecetaService recetaService;
    private DefaultTableModel tableModel;
    private List<Receta> todasLasRecetas;
    private Set<String> medicamentosSeleccionados;

    // Datos para los gráficos
    private Map<String, Map<String, Integer>> datosMedicamentosPorMes;
    private Map<String, Integer> datosEstadosRecetas;

    /**
     * Constructor que inicializa el dashboard del sistema.
     * Configura la interfaz gráfica y carga los datos iniciales.
     */
    public DashboardView() {
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

        recetaService = new RecetaService();
        medicamentosSeleccionados = new HashSet<>();

        // Los componentes ya están inicializados por el form designer
        setupTable();
        setupEventListeners();
        setupCustomPanels();
        cargarDatosIniciales();
    }

    /**
     * Configura la tabla de medicamentos con el modelo de datos apropiado.
     */
    private void setupTable() {
        // Configurar el modelo de la tabla
        String[] columnas = {"Medicamento", "2025-8", "2025-9", "2025-10"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        MedicamentosTable.setModel(tableModel);
        MedicamentosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Configura los eventos de los botones de la interfaz.
     */
    private void setupEventListeners() {
        // Configurar el botón de check para agregar medicamentos
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarMedicamentoSeleccionado();
            }
        });

        // Configurar el botón de limpiar
        limpiarButttom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFiltros();
            }
        });
    }

    /**
     * Configura los paneles personalizados para los gráficos.
     */
    private void setupCustomPanels() {
        // Configurar el panel de gráfico de medicamentos
        if (PanelGraficoMedicamentos != null) {
            PanelGraficoMedicamentos.setBorder(BorderFactory.createTitledBorder("Medicamentos"));
            PanelGraficoMedicamentos.removeAll();

            JPanel graficoPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    dibujarGraficoLineas(g);
                }
            };
            graficoPanel.setBackground(Color.WHITE);
            PanelGraficoMedicamentos.setLayout(new BorderLayout());
            PanelGraficoMedicamentos.add(graficoPanel, BorderLayout.CENTER);
        }

        // Configurar el panel de gráfico de recetas
        if (PanelGraficoRecetas != null) {
            PanelGraficoRecetas.setBorder(BorderFactory.createTitledBorder("Recetas"));
            PanelGraficoRecetas.removeAll();

            JPanel graficoPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    dibujarGraficoPastel(g);
                }
            };
            graficoPanel.setBackground(Color.WHITE);
            PanelGraficoRecetas.setLayout(new BorderLayout());
            PanelGraficoRecetas.add(graficoPanel, BorderLayout.CENTER);
        }

        // Configurar el panel de datos con borde
        if (PanelDatos != null) {
            PanelDatos.setBorder(BorderFactory.createTitledBorder("Datos"));
        }
    }

    /**
     * Carga los datos iniciales para el dashboard.
     * Inicializa comboboxes, carga recetas y medicamentos.
     */
    private void cargarDatosIniciales() {
        try {
            // Inicializar los ComboBox de años
            inicializarComboAnno(DesdeAnno);
            inicializarComboAnno(HastaAnno);

            // Inicializar los ComboBox de meses
            inicializarComboMes(DesdeMes);
            inicializarComboMes(HastaMes);

            // Cargar datos de recetas
            todasLasRecetas = recetaService.listarTodas();

            // Cargar medicamentos en el combo
            cargarMedicamentosEnCombo();

            // Calcular datos iniciales
            calcularDatos();
            actualizarTabla();
            repintarGraficos();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(PanelPrincipalDashboard,
                    "Error al cargar datos iniciales: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inicializa un combobox con años (desde el año actual hasta 5 años atrás).
     *
     * @param combo El combobox a inicializar
     */
    private void inicializarComboAnno(JComboBox<String> combo) {
        combo.removeAllItems();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 5; i--) {
            combo.addItem(String.valueOf(i));
        }
    }

    /**
     * Inicializa un combobox con los meses del año.
     *
     * @param combo El combobox a inicializar
     */
    private void inicializarComboMes(JComboBox<String> combo) {
        combo.removeAllItems();
        String[] meses = {
                "1-Enero", "2-Febrero", "3-Marzo", "4-Abril", "5-Mayo", "6-Junio",
                "7-Julio", "8-Agosto", "9-Septiembre", "10-Octubre", "11-Noviembre", "12-Diciembre"
        };
        for (String mes : meses) {
            combo.addItem(mes);
        }
    }

    /**
     * Carga los medicamentos disponibles en el combobox a partir de las recetas.
     */
    private void cargarMedicamentosEnCombo() {
        MedicamentosCombox.removeAllItems();
        MedicamentosCombox.addItem("-- Seleccionar Medicamento --");

        Set<String> medicamentos = new HashSet<>();
        for (Receta receta : todasLasRecetas) {
            if (receta.getDetalles() != null) {
                for (DetalleReceta detalle : receta.getDetalles()) {
                    if (detalle.getMedicamento() != null &&
                            detalle.getMedicamento().getNombre() != null) {
                        medicamentos.add(detalle.getMedicamento().getNombre());
                    }
                }
            }
        }

        medicamentos.stream().sorted().forEach(MedicamentosCombox::addItem);
    }

    /**
     * Agrega un medicamento seleccionado a la lista de medicamentos para mostrar en gráficos.
     */
    private void agregarMedicamentoSeleccionado() {
        if (MedicamentosCombox.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(PanelPrincipalDashboard,
                    "Por favor seleccione un medicamento",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String medicamentoSeleccionado = (String) MedicamentosCombox.getSelectedItem();

        if (medicamentosSeleccionados.contains(medicamentoSeleccionado)) {
            JOptionPane.showMessageDialog(PanelPrincipalDashboard,
                    "El medicamento ya está agregado a la tabla",
                    "Medicamento duplicado",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        medicamentosSeleccionados.add(medicamentoSeleccionado);
        calcularDatos();
        actualizarTabla();
        repintarGraficos();

        // Reset del combo
        MedicamentosCombox.setSelectedIndex(0);

        JOptionPane.showMessageDialog(PanelPrincipalDashboard,
                "Medicamento agregado: " + medicamentoSeleccionado,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Limpia todos los filtros y selecciones del dashboard.
     */
    private void limpiarFiltros() {
        // Limpiar selecciones de combos
        if (DesdeAnno.getItemCount() > 0) DesdeAnno.setSelectedIndex(0);
        if (HastaAnno.getItemCount() > 0) HastaAnno.setSelectedIndex(0);
        if (DesdeMes.getItemCount() > 0) DesdeMes.setSelectedIndex(0);
        if (HastaMes.getItemCount() > 0) HastaMes.setSelectedIndex(0);
        if (MedicamentosCombox.getItemCount() > 0) MedicamentosCombox.setSelectedIndex(0);

        // Limpiar medicamentos seleccionados
        medicamentosSeleccionados.clear();

        calcularDatos();
        actualizarTabla();
        repintarGraficos();

        JOptionPane.showMessageDialog(PanelPrincipalDashboard,
                "Filtros y selecciones limpiados",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Calcula los datos para los gráficos a partir de las recetas.
     */
    private void calcularDatos() {
        datosMedicamentosPorMes = new HashMap<>();
        datosEstadosRecetas = new HashMap<>();

        // Calcular datos de medicamentos por mes
        for (Receta receta : todasLasRecetas) {
            if (receta.getFechaConfeccion() != null && receta.getDetalles() != null) {
                String mesAnno = receta.getFechaConfeccion().getYear() + "-" +
                        receta.getFechaConfeccion().getMonthValue();

                for (DetalleReceta detalle : receta.getDetalles()) {
                    if (detalle.getMedicamento() != null &&
                            detalle.getMedicamento().getNombre() != null) {
                        String medicamento = detalle.getMedicamento().getNombre();

                        // Solo incluir medicamentos seleccionados o todos si no hay selección
                        if (medicamentosSeleccionados.isEmpty() ||
                                medicamentosSeleccionados.contains(medicamento)) {
                            datosMedicamentosPorMes.computeIfAbsent(medicamento, k -> new HashMap<>())
                                    .merge(mesAnno, detalle.getCantidad(), Integer::sum);
                        }
                    }
                }
            }
        }

        // Calcular datos de estados de recetas
        datosEstadosRecetas.clear();
        for (Receta receta : todasLasRecetas) {
            if (receta.getEstado() != null) {
                datosEstadosRecetas.merge(receta.getEstado(), 1, Integer::sum);
            }
        }
    }

    /**
     * Actualiza la tabla con los datos calculados de medicamentos.
     */
    private void actualizarTabla() {
        tableModel.setRowCount(0);

        // Solo mostrar medicamentos seleccionados
        for (String medicamento : medicamentosSeleccionados) {
            if (datosMedicamentosPorMes.containsKey(medicamento)) {
                Object[] fila = new Object[4];
                fila[0] = medicamento;

                Map<String, Integer> datosMes = datosMedicamentosPorMes.get(medicamento);
                fila[1] = datosMes.getOrDefault("2025-8", 0);
                fila[2] = datosMes.getOrDefault("2025-9", 0);
                fila[3] = datosMes.getOrDefault("2025-10", 0);

                tableModel.addRow(fila);
            }
        }
    }

    /**
     * Fuerza el repintado de los gráficos.
     */
    private void repintarGraficos() {
        if (PanelGraficoMedicamentos != null) {
            PanelGraficoMedicamentos.repaint();
        }
        if (PanelGraficoRecetas != null) {
            PanelGraficoRecetas.repaint();
        }
    }

    /**
     * Dibuja el gráfico de líneas para mostrar la evolución de medicamentos por mes.
     *
     * @param g El contexto gráfico para dibujar
     */
    private void dibujarGraficoLineas(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = g2d.getClipBounds().width;
        int height = g2d.getClipBounds().height;

        if (medicamentosSeleccionados.isEmpty() || datosMedicamentosPorMes.isEmpty()) {
            g2d.setColor(Color.GRAY);
            g2d.drawString("Seleccione medicamentos con el botón ✓", width/2 - 120, height/2);
            return;
        }

        // Márgenes
        int margenX = 50;
        int margenY = 50;
        int areaGraficoWidth = width - 2 * margenX;
        int areaGraficoHeight = height - 2 * margenY - 50;

        // Dibujar ejes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(margenX, height - margenY - 30, margenX + areaGraficoWidth, height - margenY - 30); // X
        g2d.drawLine(margenX, margenY, margenX, height - margenY - 30); // Y

        // Obtener meses ordenados
        Set<String> todosLosMeses = new TreeSet<>();
        for (Map<String, Integer> datos : datosMedicamentosPorMes.values()) {
            todosLosMeses.addAll(datos.keySet());
        }
        String[] meses = todosLosMeses.toArray(new String[0]);

        if (meses.length == 0) return;

        // Calcular valor máximo para la escala Y
        int maxVal = 1;
        for (String medicamento : medicamentosSeleccionados) {
            if (datosMedicamentosPorMes.containsKey(medicamento)) {
                Map<String, Integer> datos = datosMedicamentosPorMes.get(medicamento);
                int max = datos.values().stream().mapToInt(Integer::intValue).max().orElse(1);
                maxVal = Math.max(maxVal, max);
            }
        }

        // Dibujar escala Y
        g2d.setColor(Color.GRAY);
        for (int i = 0; i <= 5; i++) {
            int valor = (maxVal * i) / 5;
            int y = height - margenY - 30 - (i * areaGraficoHeight / 5);
            g2d.drawString(String.valueOf(valor), 5, y + 5);
            g2d.drawLine(margenX - 5, y, margenX, y);
        }

        // Dibujar etiquetas de meses en X
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < meses.length; i++) {
            int x = margenX + (i * areaGraficoWidth / Math.max(1, meses.length - 1));
            String mesCorto = meses[i].replace("2025-", "");
            g2d.drawString(mesCorto, x - 10, height - margenY - 10);
        }

        // Dibujar líneas para cada medicamento seleccionado
        Color[] colores = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN};
        int colorIndex = 0;

        for (String medicamento : medicamentosSeleccionados) {
            if (datosMedicamentosPorMes.containsKey(medicamento)) {
                Color color = colores[colorIndex % colores.length];
                Map<String, Integer> datosMedicamento = datosMedicamentosPorMes.get(medicamento);

                // Crear array de datos para este medicamento
                int[] datos = new int[meses.length];
                for (int i = 0; i < meses.length; i++) {
                    datos[i] = datosMedicamento.getOrDefault(meses[i], 0);
                }

                // Dibujar línea
                dibujarLinea(g2d, datos, color, margenX, margenY, areaGraficoWidth, areaGraficoHeight, maxVal);
                colorIndex++;
            }
        }

        // Dibujar leyenda
        int leyendaY = height - 25;
        int leyendaX = margenX;
        colorIndex = 0;
        for (String medicamento : medicamentosSeleccionados) {
            if (datosMedicamentosPorMes.containsKey(medicamento)) {
                Color color = colores[colorIndex % colores.length];
                g2d.setColor(color);
                g2d.fillRect(leyendaX, leyendaY - 10, 15, 10);
                g2d.setColor(Color.BLACK);
                String nombreCorto = medicamento.length() > 10 ? medicamento.substring(0, 10) + "..." : medicamento;
                g2d.drawString(nombreCorto, leyendaX + 20, leyendaY);
                leyendaX += nombreCorto.length() * 8 + 40;
                colorIndex++;
            }
        }
    }

    /**
     * Dibuja una línea individual en el gráfico de líneas.
     *
     * @param g2d El contexto gráfico 2D
     * @param datos Los datos a graficar
     * @param color El color de la línea
     * @param margenX Margen en el eje X
     * @param margenY Margen en el eje Y
     * @param width Ancho del área de dibujo
     * @param height Alto del área de dibujo
     * @param maxVal Valor máximo para la escala
     */
    private void dibujarLinea(Graphics2D g2d, int[] datos, Color color, int margenX, int margenY,
                              int width, int height, int maxVal) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));

        if (datos.length < 2) return;

        for (int i = 0; i < datos.length - 1; i++) {
            int x1 = margenX + (i * width / Math.max(1, datos.length - 1));
            int y1 = margenY + height - (datos[i] * height / maxVal);
            int x2 = margenX + ((i + 1) * width / Math.max(1, datos.length - 1));
            int y2 = margenY + height - (datos[i + 1] * height / maxVal);

            g2d.drawLine(x1, y1, x2, y2);

            // Dibujar puntos
            g2d.fillOval(x1 - 3, y1 - 3, 6, 6);
            if (i == datos.length - 2) {
                g2d.fillOval(x2 - 3, y2 - 3, 6, 6);
            }
        }
    }

    /**
     * Dibuja el gráfico de pastel para mostrar la distribución de estados de recetas.
     *
     * @param g El contexto gráfico para dibujar
     */
    private void dibujarGraficoPastel(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = g2d.getClipBounds().width;
        int height = g2d.getClipBounds().height;

        if (datosEstadosRecetas == null || datosEstadosRecetas.isEmpty()) {
            g2d.setColor(Color.GRAY);
            g2d.drawString("No hay datos para mostrar", width/2 - 70, height/2);
            return;
        }

        // Obtener estados ordenados
        String[] estados = datosEstadosRecetas.keySet().toArray(new String[0]);
        Color[] colores = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.MAGENTA};

        int total = datosEstadosRecetas.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) {
            g2d.setColor(Color.GRAY);
            g2d.drawString("No hay recetas para mostrar", width/2 - 70, height/2);
            return;
        }

        // Dibujar gráfico de pastel
        int diameter = Math.min(width - 100, height - 120);
        int x = (width - diameter) / 2;
        int y = 20;

        double startAngle = 0;
        for (int i = 0; i < estados.length; i++) {
            double angle = (datosEstadosRecetas.get(estados[i]) * 360.0) / total;
            g2d.setColor(colores[i % colores.length]);
            g2d.fillArc(x, y, diameter, diameter, (int) startAngle, (int) angle);
            g2d.setColor(Color.BLACK);
            g2d.drawArc(x, y, diameter, diameter, (int) startAngle, (int) angle);
            startAngle += angle;
        }

        // Dibujar etiquetas de valores
        int labelY = y + diameter + 20;
        for (int i = 0; i < estados.length && i < 4; i++) {
            int valor = datosEstadosRecetas.get(estados[i]);
            int porcentaje = (valor * 100) / total;
            String texto = estados[i] + " = " + valor + " (" + porcentaje + "%)";

            g2d.setColor(colores[i % colores.length]);
            g2d.fillOval(10, labelY + (i * 15) - 5, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawString(texto, 25, labelY + (i * 15) + 5);
        }
    }

    /**
     * Retorna el panel principal del dashboard.
     *
     * @return JPanel que contiene todos los componentes de la interfaz
     */
    public JPanel getPanelPrincipalDashboard() {
        return PanelPrincipalDashboard;
    }
}