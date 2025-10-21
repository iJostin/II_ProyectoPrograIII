package persistence;

import model.Medicamento;
import util.XMLHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para gestionar la persistencia de medicamentos
 * Utiliza archivos XML para almacenar los datos
 */
public class MedicamentoDAO {
    private static final String ARCHIVO = "data/medicamentos.xml";
    private List<Medicamento> medicamentos;

    /**
     * Constructor que inicializa el DAO y carga los datos existentes
     * Crea el directorio data si no existe
     */
    public MedicamentoDAO() {
        // Crear directorio si no existe
        new File("data").mkdirs();
        medicamentos = cargar();
    }

    /**
     * Agrega un nuevo medicamento al sistema
     * @param medicamento Objeto Medicamento a agregar
     * @throws IllegalArgumentException Si ya existe un medicamento con el mismo código
     */
    public void agregar(Medicamento medicamento) {
        if (buscarPorId(medicamento.getCodigo()) == null) {
            medicamentos.add(medicamento);
            guardar();
        } else {
            throw new IllegalArgumentException("Ya existe un medicamento con el código: " + medicamento.getCodigo());
        }
    }

    /**
     * Busca un medicamento por su código
     * @param codigo Código del medicamento a buscar
     * @return Objeto Medicamento encontrado o null si no existe
     */
    public Medicamento buscarPorId(String codigo) {
        return medicamentos.stream()
                .filter(medicamento -> medicamento.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todos los medicamentos del sistema
     * @return Lista de todos los medicamentos (copia para evitar modificaciones externas)
     */
    public List<Medicamento> getTodos() {
        return new ArrayList<>(medicamentos); // Retorna copia para evitar modificaciones externas
    }

    /**
     * Elimina un medicamento por su código
     * @param codigo Código del medicamento a eliminar
     * @throws IllegalArgumentException Si no se encuentra un medicamento con el código especificado
     */
    public void eliminar(String codigo) {
        boolean removido = medicamentos.removeIf(medicamento -> medicamento.getCodigo().equalsIgnoreCase(codigo));
        if (removido) {
            guardar();
        } else {
            throw new IllegalArgumentException("No se encontró un medicamento con el código: " + codigo);
        }
    }

    /**
     * Actualiza la información de un medicamento existente
     * @param nuevo Objeto Medicamento con la información actualizada
     * @throws IllegalArgumentException Si no se encuentra un medicamento con el código especificado
     */
    public void actualizar(Medicamento nuevo) {
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equalsIgnoreCase(nuevo.getCodigo())) {
                medicamentos.set(i, nuevo);
                guardar();
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró un medicamento con el código: " + nuevo.getCodigo());
    }

    /**
     * Guarda la lista de medicamentos en el archivo XML
     */
    private void guardar() {
        try {
            XMLHelper.guardar(ARCHIVO, new MedicamentoList(medicamentos));
        } catch (Exception e) {
            System.err.println("Error al guardar medicamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de medicamentos desde el archivo XML
     * @return Lista de medicamentos cargada o lista vacía si hay error
     */
    private List<Medicamento> cargar() {
        try {
            MedicamentoList lista = XMLHelper.cargar(ARCHIVO, MedicamentoList.class);
            return lista != null && lista.getMedicamentos() != null ? lista.getMedicamentos() : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("No se pudo cargar la lista de medicamentos. Iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }
}