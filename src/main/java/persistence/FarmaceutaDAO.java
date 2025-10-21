package persistence;

import model.Farmaceuta;
import util.XMLHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para gestionar la persistencia de farmacéuticos
 * Utiliza archivos XML para almacenar los datos
 */
public class FarmaceutaDAO {
    private static final String ARCHIVO = "data/farmaceutas.xml";
    private List<Farmaceuta> farmaceutas;

    /**
     * Constructor que inicializa el DAO y carga los datos existentes
     * Crea el directorio data si no existe
     */
    public FarmaceutaDAO() {
        // Crear directorio si no existe
        new File("data").mkdirs();
        farmaceutas = cargar();
    }

    /**
     * Agrega un nuevo farmacéutico al sistema
     * @param farmaceuta Objeto Farmaceuta a agregar
     * @throws IllegalArgumentException Si ya existe un farmacéutico con el mismo ID
     */
    public void agregar(Farmaceuta farmaceuta) {
        if (buscarPorId(farmaceuta.getId()) == null) {
            farmaceutas.add(farmaceuta);
            guardar();
        } else {
            throw new IllegalArgumentException("Ya existe un farmaceuta con el ID: " + farmaceuta.getId());
        }
    }

    /**
     * Busca un farmacéutico por su ID
     * @param id ID del farmacéutico a buscar
     * @return Objeto Farmaceuta encontrado o null si no existe
     */
    public Farmaceuta buscarPorId(String id) {
        return farmaceutas.stream()
                .filter(farmaceuta -> farmaceuta.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todos los farmacéuticos del sistema
     * @return Lista de todos los farmacéuticos (copia para evitar modificaciones externas)
     */
    public List<Farmaceuta> getTodos() {
        return new ArrayList<>(farmaceutas); // Retorna copia para evitar modificaciones externas
    }

    /**
     * Elimina un farmacéutico por su ID
     * @param id ID del farmacéutico a eliminar
     * @throws IllegalArgumentException Si no se encuentra un farmacéutico con el ID especificado
     */
    public void eliminar(String id) {
        boolean removido = farmaceutas.removeIf(farmaceuta -> farmaceuta.getId().equalsIgnoreCase(id));
        if (removido) {
            guardar();
        } else {
            throw new IllegalArgumentException("No se encontró un farmaceuta con el ID: " + id);
        }
    }

    /**
     * Actualiza la información de un farmacéutico existente
     * @param nuevo Objeto Farmaceuta con la información actualizada
     * @throws IllegalArgumentException Si no se encuentra un farmacéutico con el ID especificado
     */
    public void actualizar(Farmaceuta nuevo) {
        for (int i = 0; i < farmaceutas.size(); i++) {
            if (farmaceutas.get(i).getId().equalsIgnoreCase(nuevo.getId())) {
                farmaceutas.set(i, nuevo);
                guardar();
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró un farmaceuta con el ID: " + nuevo.getId());
    }

    /**
     * Guarda la lista de farmacéuticos en el archivo XML
     */
    private void guardar() {
        try {
            XMLHelper.guardar(ARCHIVO, new FarmaceutaList(farmaceutas));
        } catch (Exception e) {
            System.err.println("Error al guardar farmaceutas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de farmacéuticos desde el archivo XML
     * @return Lista de farmacéuticos cargada o lista vacía si hay error
     */
    private List<Farmaceuta> cargar() {
        try {
            FarmaceutaList lista = XMLHelper.cargar(ARCHIVO, FarmaceutaList.class);
            return lista != null && lista.getFarmaceutas() != null ? lista.getFarmaceutas() : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("No se pudo cargar la lista de médicos. Iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }
}