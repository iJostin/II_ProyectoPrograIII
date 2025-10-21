package persistence;

import model.Medico;
import util.XMLHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para gestionar la persistencia de médicos
 * Utiliza archivos XML para almacenar los datos
 */
public class MedicoDAO {
    private static final String ARCHIVO = "data/medicos.xml";
    private List<Medico> medicos;

    /**
     * Constructor que inicializa el DAO и carga los datos existentes
     * Crea el directorio data si no existe
     */
    public MedicoDAO() {
        // Crear directorio si no existe
        new File("data").mkdirs();
        medicos = cargar();
    }

    /**
     * Agrega un nuevo médico al sistema
     * @param medico Objeto Medico a agregar
     * @throws IllegalArgumentException Si ya existe un médico con el mismo ID
     */
    public void agregar(Medico medico) {
        if (buscarPorId(medico.getId()) == null) {
            medicos.add(medico);
            guardar();
        } else {
            throw new IllegalArgumentException("Ya existe un médico con el ID: " + medico.getId());
        }
    }

    /**
     * Busca un médico por su ID
     * @param id ID del médico a buscar
     * @return Objeto Medico encontrado o null si no existe
     */
    public Medico buscarPorId(String id) {
        return medicos.stream()
                .filter(medico -> medico.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todos los médicos del sistema
     * @return Lista de todos los médicos (copia para evitar modificaciones externas)
     */
    public List<Medico> getTodos() {
        return new ArrayList<>(medicos); // Retorna copia para evitar modificaciones externas
    }

    /**
     * Elimina un médico por su ID
     * @param id ID del médico a eliminar
     * @throws IllegalArgumentException Si no se encuentra un médico con el ID especificado
     */
    public void eliminar(String id) {
        boolean removido = medicos.removeIf(medico -> medico.getId().equalsIgnoreCase(id));
        if (removido) {
            guardar();
        } else {
            throw new IllegalArgumentException("No se encontró un médico con el ID: " + id);
        }
    }

    /**
     * Actualiza la información de un médico existente
     * @param nuevo Objeto Medico con la información actualizada
     * @throws IllegalArgumentException Si no se encuentra un médico con el ID especificado
     */
    public void actualizar(Medico nuevo) {
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getId().equalsIgnoreCase(nuevo.getId())) {
                medicos.set(i, nuevo);
                guardar();
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró un médico con el ID: " + nuevo.getId());
    }

    /**
     * Guarda la lista de médicos en el archivo XML
     */
    private void guardar() {
        try {
            XMLHelper.guardar(ARCHIVO, new MedicoList(medicos));
        } catch (Exception e) {
            System.err.println("Error al guardar médicos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de médicos desde el archivo XML
     * @return Lista de médicos cargada o lista vacía si hay error
     */
    private List<Medico> cargar() {
        try {
            MedicoList lista = XMLHelper.cargar(ARCHIVO, MedicoList.class);
            return lista != null && lista.getMedicos() != null ? lista.getMedicos() : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("No se pudo cargar la lista de médicos. Iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }
}