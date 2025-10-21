package persistence;

import model.Admin;
import util.XMLHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para gestionar la persistencia de administradores
 * Utiliza archivos XML para almacenar los datos
 */
public class AdminDAO {
    private static final String ARCHIVO = "data/admins.xml";
    private List<Admin> admins;

    /**
     * Constructor que inicializa el DAO y carga los datos existentes
     * Crea el directorio data si no existe
     */
    public AdminDAO() {
        // Crear directorio si no existe
        new File("data").mkdirs();
        admins = cargar();
    }

    /**
     * Agrega un nuevo administrador al sistema
     * @param admin Objeto Admin a agregar
     * @throws IllegalArgumentException Si ya existe un administrador con el mismo ID
     */
    public void agregar(Admin admin) {
        if (buscarPorId(admin.getId()) == null) {
            admins.add(admin);
            guardar();
        } else {
            throw new IllegalArgumentException("Ya existe un administrador con el ID: " + admin.getId());
        }
    }

    /**
     * Busca un administrador por su ID
     * @param id ID del administrador a buscar
     * @return Objeto Admin encontrado o null si no existe
     */
    public Admin buscarPorId(String id) {
        return admins.stream()
                .filter(admin -> admin.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todos los administradores del sistema
     * @return Lista de todos los administradores (copia para evitar modificaciones externas)
     */
    public List<Admin> getTodos() {
        return new ArrayList<>(admins); // Retorna copia para evitar modificaciones externas
    }

    /**
     * Elimina un administrador por su ID
     * @param id ID del administrador a eliminar
     * @throws IllegalArgumentException Si no se encuentra un administrador con el ID especificado
     */
    public void eliminar(String id) {
        boolean removido = admins.removeIf(admin -> admin.getId().equalsIgnoreCase(id));
        if (removido) {
            guardar();
        } else {
            throw new IllegalArgumentException("No se encontró un administrador con el ID: " + id);
        }
    }

    /**
     * Actualiza la información de un administrador existente
     * @param nuevo Objeto Admin con la información actualizada
     * @throws IllegalArgumentException Si no se encuentra un administrador con el ID especificado
     */
    public void actualizar(Admin nuevo) {
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getId().equalsIgnoreCase(nuevo.getId())) {
                admins.set(i, nuevo);
                guardar();
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró un administrador con el ID: " + nuevo.getId());
    }

    /**
     * Guarda la lista de administradores en el archivo XML
     */
    private void guardar() {
        try {
            XMLHelper.guardar(ARCHIVO, new AdminList(admins));
        } catch (Exception e) {
            System.err.println("Error al guardar administrador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de administradores desde el archivo XML
     * @return Lista de administradores cargada o lista vacía si hay error
     */
    private List<Admin> cargar() {
        try {
            AdminList lista = XMLHelper.cargar(ARCHIVO, AdminList.class);
            return lista != null && lista.getAdmins() != null ? lista.getAdmins() : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("No se pudo cargar la lista de administradores. Iniciando con lista vacía.");
            return new ArrayList<>();
        }
    }
}