package model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Clase abstracta que representa un usuario base del sistema
 * Define las propiedades comunes para todos los tipos de usuarios
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Usuario {
    @XmlElement
    protected String id;
    @XmlElement
    protected String clave;
    @XmlElement
    protected String tipo;

    /**
     * Constructor por defecto
     */
    public Usuario() {}

    /**
     * Constructor con parámetros básicos
     * @param id Identificador único del usuario
     * @param clave Contraseña de acceso
     * @param tipo Tipo de usuario (ADMIN, MEDICO, FARMACEUTA, PACIENTE)
     */
    public Usuario(String id, String clave, String tipo) {
        this.id = id;
        this.clave = clave;
        this.tipo = tipo;
    }

    /**
     * Obtiene el ID del usuario
     * @return Identificador único del usuario
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID del usuario
     * @param id Identificador único del usuario
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene la contraseña del usuario
     * @return Contraseña de acceso
     */
    public String getClave() {
        return clave;
    }

    /**
     * Establece la contraseña del usuario
     * @param clave Contraseña de acceso
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * Obtiene el tipo de usuario
     * @return Tipo de usuario (ADMIN, MEDICO, FARMACEUTA, PACIENTE)
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de usuario
     * @param tipo Tipo de usuario (ADMIN, MEDICO, FARMACEUTA, PACIENTE)
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Representación en String del objeto Usuario
     * @return String con información básica del usuario
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}