package persistence;

import model.Paciente;
import util.XMLHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para gestionar la persistencia de pacientes
 * Utiliza archivos XML para almacenar los datos
 */
public class PacienteDAO {
    private static final String ARCHIVO = "data/pacientes.xml";
    private List<Paciente> pacientes;

    /**
     * Constructor que inicializa el DAO y carga los datos existentes
     * Crea el directorio data si no existe
     */
    public PacienteDAO() {
        // Crear directorio si no existe
        new File("data").mkdirs();
        pacientes = cargar();
    }

    /**
     * Agrega un nuevo paciente al sistema
     * @param paciente Objeto Paciente a agregar
     * @throws IllegalArgumentException Si ya existe un paciente con el mismo ID
     */
    public void agregar(Paciente paciente) {
        if (buscarPorId(paciente.getId()) == null) {
            pacientes.add(paciente);
            guardar();
        } else {
            throw new IllegalArgumentException("Ya existe un paciente con el ID: " + paciente.getId());
        }
    }

    /**
     * Busca un paciente por su ID
     * @param id ID del paciente a buscar
     * @return Objeto Paciente encontrado o null si no existe
     */
    public Paciente buscarPorId(String id) {
        return pacientes.stream()
                .filter(paciente -> paciente.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todos los pacientes del sistema
     * @return Lista de todos los pacientes (copia para evitar modificaciones externas)
     */
    public List<Paciente> getTodos() {
        return new ArrayList<>(pacientes); // Retorna copia para evitar modificaciones externas
    }

    /**
     * Elimina un paciente por su ID
     * @param id ID del paciente a eliminar
     * @throws IllegalArgumentException Si no se encuentra un paciente con el ID especificado
     */
    public void eliminar(String id) {
        boolean removido = pacientes.removeIf(paciente -> paciente.getId().equalsIgnoreCase(id));
        if (removido) {
            guardar();
        } else {
            throw new IllegalArgumentException("No se encontró un paciente con el ID: " + id);
        }
    }

    /**
     * Actualiza la información de un paciente existente
     * @param nuevo Objeto Paciente con la información actualizada
     * @throws IllegalArgumentException Si no se encuentra un paciente con el ID especificado
     */
    public void actualizar(Paciente nuevo) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId().equalsIgnoreCase(nuevo.getId())) {
                pacientes.set(i, nuevo);
                guardar();
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró un paciente con el ID: " + nuevo.getId());
    }

    /**
     * Guarda la lista de pacientes en el archivo XML
     */
    private void guardar() {
        try {
            XMLHelper.guardar(ARCHIVO, new PacienteList(pacientes));
        } catch (Exception e) {
            System.err.println("Error al guardar pacientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de pacientes desde el archivo XML
     * @return Lista de pacientes cargada o lista vacía si hay error
     */
    private List<Paciente> cargar() {
        try {
            PacienteList lista = XMLHelper.cargar(ARCHIVO, PacienteList.class);
            return lista != null && lista.getPacientes() != null ? lista.getPacientes() : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("No se pudo cargar la lista de pacientes. Iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }
}