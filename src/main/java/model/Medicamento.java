package model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Representa un medicamento en el sistema
 * Contiene información básica del medicamento como código, nombre y presentación
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Medicamento {
    @XmlElement
    private String codigo;
    @XmlElement
    private String nombre;
    @XmlElement
    private String presentacion; // ej: "Acetaminofén 500mg"

    /**
     * Constructor por defecto
     */
    public Medicamento() {}

    /**
     * Constructor con todos los parámetros
     * @param codigo Código único del medicamento
     * @param nombre Nombre comercial del medicamento
     * @param presentacion Presentación y dosificación
     */
    public Medicamento(String codigo, String nombre, String presentacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
    }

    /**
     * Obtiene el código del medicamento
     * @return Código único del medicamento
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Obtiene el nombre del medicamento
     * @return Nombre comercial del medicamento
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la presentación del medicamento
     * @return Presentación y dosificación
     */
    public String getPresentacion() {
        return presentacion;
    }

    /**
     * Establece el código del medicamento
     * @param codigo Código único del medicamento
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Establece el nombre del medicamento
     * @param nombre Nombre comercial del medicamento
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la presentación del medicamento
     * @param presentacion Presentación y dosificación
     */
    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    /**
     * Representación en String del objeto Medicamento
     * @return String con información del medicamento
     */
    @Override
    public String toString() {
        return "Medicamento{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", presentacion='" + presentacion + '\'' +
                '}';
    }
}