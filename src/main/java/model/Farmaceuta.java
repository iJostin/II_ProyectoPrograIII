package model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Representa un farmacéutico del sistema, hereda de Usuario
 * Los farmacéuticos se encargan de gestionar la dispensación de medicamentos
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Farmaceuta extends Usuario {
    @XmlElement
    private String nombre;
    @XmlElement
    private String especialidad;

    /**
     * Constructor por defecto
     */
    public Farmaceuta() {}

    /**
     * Constructor con parámetros básicos
     * @param id Identificador único del farmacéutico
     * @param clave Contraseña de acceso
     * @param nombre Nombre del farmacéutico
     */
    public Farmaceuta(String id, String clave, String nombre) {
        super(id, clave, "FARMACEUTA");
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre del farmacéutico
     * @return Nombre del farmacéutico
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del farmacéutico
     * @param nombre Nombre del farmacéutico
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Representación en String del objeto Farmaceuta
     * @return String con información del farmacéutico
     */
    @Override
    public String toString() {
        return "Farmaceuta{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}