package model;

/**
 * Representa un administrador del sistema, hereda de Usuario
 * Los administradores tienen permisos para gestionar todo el sistema
 */
public class Admin extends Usuario {
    private String nombre;

    /**
     * Constructor por defecto
     */
    public Admin() {}

    /**
     * Constructor con parámetros básicos
     * @param id Identificador único del administrador
     * @param clave Contraseña de acceso
     */
    public Admin(String id, String clave) {
        super(id, clave, "Administrador");
    }

    /**
     * Obtiene el nombre del administrador
     * @return Nombre del administrador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del administrador
     * @param nombre Nombre del administrador
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}