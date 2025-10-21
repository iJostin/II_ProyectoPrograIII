package controller;

import model.Admin;
import service.AdminService;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de administradores
 * Actúa como intermediario entre la vista y el servicio de administradores
 */
public class AdminController {
    private static AdminService adminService = new AdminService();

    /**
     * Agrega un nuevo administrador al sistema
     * @param m Objeto Admin a agregar
     */
    public void agregar(Admin m) {
        adminService.agregar(m);
    }

    /**
     * Actualiza la información de un administrador existente
     * @param m Objeto Admin con la información actualizada
     */
    public void actualizar(Admin m) {
        adminService.actualizar(m);
    }

    /**
     * Elimina un administrador del sistema por su ID
     * @param id ID del administrador a eliminar
     */
    public void eliminar(String id) {
        adminService.eliminar(id);
    }

    /**
     * Busca un administrador por su ID
     * @param id ID del administrador a buscar
     * @return Objeto Admin encontrado o null si no existe
     */
    public Admin buscar(String id) {
        return adminService.buscarPorId(id);
    }

    /**
     * Obtiene la lista de todos los administradores del sistema
     * @return Lista de objetos Admin
     */
    public static List<Admin> listar() {
        return adminService.listar();
    }
}