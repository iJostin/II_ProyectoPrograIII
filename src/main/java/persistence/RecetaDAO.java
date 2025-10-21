package persistence;

import model.Receta;
import util.XMLHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para gestionar la persistencia de recetas médicas
 * Utiliza archivos XML para almacenar los datos con validaciones robustas
 */
public class RecetaDAO {
    private static final String ARCHIVO = "data/recetas.xml";
    private List<Receta> recetas;

    /**
     * Constructor que inicializa el DAO y carga los datos existentes
     */
    public RecetaDAO() {
        recetas = cargar();
    }

    /**
     * Agrega una nueva receta al sistema
     * @param r Objeto Receta a agregar
     * @throws IllegalArgumentException Si la receta o su ID son null o vacíos
     */
    public void agregar(Receta r) {
        // Validar que la receta no sea null y tenga ID
        if (r == null || r.getId() == null || r.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("La receta y su ID no pueden ser null o vacíos");
        }

        recetas.add(r);
        guardar();
    }

    /**
     * Busca una receta por su ID
     * @param id ID de la receta a buscar
     * @return Objeto Receta encontrado o null si no existe
     */
    public Receta buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return recetas.stream()
                .filter(r -> r != null && r.getId() != null && r.getId().equalsIgnoreCase(id.trim()))
                .findFirst().orElse(null);
    }

    /**
     * Obtiene todas las recetas del sistema
     * @return Lista de todas las recetas (copia para evitar modificaciones externas)
     */
    public List<Receta> getTodos() {
        return new ArrayList<>(recetas); // Retornar una copia para evitar modificaciones externas
    }

    /**
     * Elimina una receta por su ID
     * @param id ID de la receta a eliminar
     */
    public void eliminar(String id) {
        if (id != null && !id.trim().isEmpty()) {
            recetas.removeIf(r -> r != null && r.getId() != null && r.getId().equalsIgnoreCase(id.trim()));
            guardar();
        }
    }

    /**
     * Actualiza la información de una receta existente
     * Si no se encuentra, agrega como nueva receta
     * @param nuevo Objeto Receta con la información actualizada
     * @throws IllegalArgumentException Si la receta o su ID son null o vacíos
     */
    public void actualizar(Receta nuevo) {
        if (nuevo == null || nuevo.getId() == null || nuevo.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("La receta y su ID no pueden ser null o vacíos");
        }

        for (int i = 0; i < recetas.size(); i++) {
            Receta actual = recetas.get(i);
            if (actual != null && actual.getId() != null &&
                    actual.getId().equalsIgnoreCase(nuevo.getId())) {
                recetas.set(i, nuevo);
                guardar();
                return;
            }
        }

        // Si no se encontró, agregar como nueva receta
        recetas.add(nuevo);
        guardar();
    }

    /**
     * Guarda la lista de recetas en el archivo XML con validación
     */
    public void guardar() {
        try {
            RecetaListWrapper wrapper = new RecetaListWrapper();
            // Filtrar recetas válidas antes de guardar
            List<Receta> recetasValidas = new ArrayList<>();
            for (Receta r : recetas) {
                if (r != null && r.getId() != null && !r.getId().trim().isEmpty()) {
                    recetasValidas.add(r);
                }
            }
            wrapper.setRecetas(recetasValidas);
            XMLHelper.guardar(ARCHIVO, wrapper);
            System.out.println("Recetas guardadas exitosamente. Total: " + recetasValidas.size());
        } catch (Exception e) {
            System.err.println("Error al guardar recetas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de recetas desde el archivo XML con validación
     * @return Lista de recetas cargada o lista vacía si hay error
     */
    private List<Receta> cargar() {
        try {
            RecetaListWrapper wrapper = XMLHelper.cargar(ARCHIVO, RecetaListWrapper.class);

            if (wrapper == null) {
                System.out.println("Archivo XML vacío o no existe. Creando lista nueva.");
                return new ArrayList<>();
            }

            List<Receta> recetasCargadas = wrapper.getRecetas();
            if (recetasCargadas == null) {
                System.out.println("Lista de recetas null en el XML. Creando lista nueva.");
                return new ArrayList<>();
            }

            // Validar y limpiar recetas inválidas
            List<Receta> recetasValidas = new ArrayList<>();
            for (Receta r : recetasCargadas) {
                if (r != null && r.getId() != null && !r.getId().trim().isEmpty()) {
                    recetasValidas.add(r);
                } else {
                    System.out.println("Receta inválida encontrada y omitida: " +
                            (r == null ? "null" : "ID vacío/null"));
                }
            }

            System.out.println("Recetas cargadas exitosamente. Total: " + recetasValidas.size());
            return recetasValidas;

        } catch (Exception e) {
            System.err.println("Error al cargar recetas del XML: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Creando lista nueva debido al error.");
            return new ArrayList<>();
        }
    }
}