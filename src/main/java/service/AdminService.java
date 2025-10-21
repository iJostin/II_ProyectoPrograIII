package service;

import model.Admin;
import persistence.AdminDAO;
import java.util.List;

/**
 * Servicio para gestionar las operaciones de administradores
 * Actúa como intermediario entre los controladores y el DAO de administradores
 * Proporciona métodos para CRUD de administradores
 */
public class AdminService {
    private AdminDAO adminDAO = new AdminDAO();

    /**
     * Agrega un nuevo administrador al sistema
     * @param f Objeto Admin a agregar
     */
    public void agregar(Admin f) {
        adminDAO.agregar(f);
    }

    /**
     * Actualiza la información de un administrador existente
     * @param f Objeto Admin con la información actualizada
     */
    public void actualizar(Admin f) {
        adminDAO.actualizar(f);
    }

    /**
     * Elimina un administrador del sistema por su ID
     * @param id ID del administrador a eliminar
     */
    public void eliminar(String id) {
        adminDAO.eliminar(id);
    }

    /**
     * Busca un administrador por su ID
     * @param id ID del administrador a buscar
     * @return Objeto Admin encontrado o null si no existe
     */
    public Admin buscarPorId(String id) {
        return adminDAO.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todos los administradores del sistema
     * @return Lista de objetos Admin
     */
    public List<Admin> listar() {
        return adminDAO.getTodos();
    }
}