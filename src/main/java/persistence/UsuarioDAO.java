package persistence;

import model.Farmaceuta;
import model.Usuario;
import util.XMLHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para gestionar la persistencia de usuarios genéricos
 * Utiliza archivos XML para almacenar los datos (nota: esta clase parece estar incompleta o específica para farmacéuticos)
 */
public class UsuarioDAO {
    private static final String ARCHIVO = "data/usuarios.xml";
    private List<Usuario> usuarios;

    /**
     * Constructor que inicializa el DAO y carga los datos existentes
     */
    public UsuarioDAO() {
        usuarios = cargar();
    }

    /**
     * Agrega un nuevo usuario al sistema
     * @param u Objeto Usuario a agregar
     */
    public void agregar(Usuario u) {
        usuarios.add(u);
        guardar();
    }

    /**
     * Busca un usuario por su ID
     * @param id ID del usuario a buscar
     * @return Objeto Usuario encontrado o null si no existe
     */
    public Usuario buscarPorId(String id) {
        return usuarios.stream()
                .filter(u -> u.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /**
     * Obtiene todos los usuarios del sistema
     * @return Lista de todos los usuarios
     */
    public List<Usuario> getTodos() {
        return usuarios;
    }

    /**
     * Elimina un usuario por su ID
     * @param id ID del usuario a eliminar
     */
    public void eliminar(String id) {
        usuarios.removeIf(f -> f.getId().equalsIgnoreCase(id));
        guardar();
    }

    /**
     * Actualiza la información de un usuario existente
     * @param nuevo Objeto Usuario con la información actualizada
     */
    public void actualizar(Usuario nuevo) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equalsIgnoreCase(nuevo.getId())) {
                usuarios.set(i, nuevo);
                break;
            }
        }
        guardar();
    }

    /**
     * Guarda la lista de usuarios en el archivo XML
     */
    private void guardar() {
        XMLHelper.guardar(ARCHIVO, usuarios);
    }

    /**
     * Carga la lista de usuarios desde el archivo XML
     * @return Lista de usuarios cargada o lista vacía si hay error
     */
    private List<Usuario> cargar() {
        List<Usuario> lista = XMLHelper.cargar(ARCHIVO, ArrayList.class);
        return lista != null ? lista : new ArrayList<>();
    }
}