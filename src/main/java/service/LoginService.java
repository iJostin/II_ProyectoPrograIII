package service;

import model.Usuario;
import persistence.UsuarioDAO;

/**
 * Servicio para gestionar la autenticación de usuarios
 * Proporciona métodos para login y cambio de contraseña
 */
public class LoginService {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Autentica a un usuario en el sistema
     * @param id ID del usuario que intenta autenticarse
     * @param clave Contraseña del usuario
     * @return Objeto Usuario autenticado o null si las credenciales son incorrectas
     */
    public Usuario login(String id, String clave) {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u != null && u.getClave().equals(clave)) {
            return u;
        }
        return null;
    }

    /**
     * Cambia la contraseña de un usuario
     * @param id ID del usuario que desea cambiar la contraseña
     * @param nuevaClave Nueva contraseña a establecer
     * @return true si el cambio fue exitoso, false si el usuario no existe
     */
    public boolean cambiarClave(String id, String nuevaClave) {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u != null) {
            u.setClave(nuevaClave);
            usuarioDAO.actualizar(u);
            return true;
        }
        return false;
    }
}