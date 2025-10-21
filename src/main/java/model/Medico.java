package model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Representa un médico del sistema, hereda de Usuario
 * Los médicos pueden crear recetas médicas para los pacientes
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Medico extends Usuario {
    @XmlElement
    private String nombre;
    @XmlElement
    private String especialidad;

    /**
     * Constructor por defecto
     */
    public Medico() {}

    /**
     * Constructor con todos los parámetros
     * @param id Identificador único del médico
     * @param clave Contraseña de acceso
     * @param nombre Nombre del médico
     * @param especialidad Especialidad médica
     */
    public Medico(String id, String clave, String nombre, String especialidad) {
        super(id, clave, "MEDICO");
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    /**
     * Obtiene el nombre del médico
     * @return Nombre del médico
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del médico
     * @param nombre Nombre del médico
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la especialidad del médico
     * @return Especialidad médica
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Establece la especialidad del médico
     * @param especialidad Especialidad médica
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Representación en String del objeto Medico
     * @return String con información del médico
     */
    @Override
    public String toString() {
        return "Medico{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                '}';
    }
}