package model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import util.LocalDateAdapter;

import java.time.LocalDate;

/**
 * Representa un paciente del sistema, hereda de Usuario
 * Los pacientes son los destinatarios de las recetas médicas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Paciente extends Usuario {
    @XmlElement
    private String nombre;
    @XmlElement
    private String telefono;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement
    private LocalDate fechaNacimiento;

    /**
     * Constructor por defecto
     */
    public Paciente() {
        super();
    }

    /**
     * Constructor con todos los parámetros
     * @param id Identificador único del paciente
     * @param nombre Nombre completo del paciente
     * @param fechaNacimiento Fecha de nacimiento del paciente
     * @param telefono Número de teléfono de contacto
     */
    public Paciente(String id, String nombre, LocalDate fechaNacimiento, String telefono) {
        super(id, null, "PACIENTE"); // Llamar al constructor de Usuario con tipo PACIENTE
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    /**
     * Obtiene el nombre del paciente
     * @return Nombre completo del paciente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del paciente
     * @param nombre Nombre completo del paciente
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el teléfono del paciente
     * @return Número de teléfono de contacto
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del paciente
     * @param telefono Número de teléfono de contacto
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la fecha de nacimiento del paciente
     * @return Fecha de nacimiento
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento del paciente
     * @param fechaNacimiento Fecha de nacimiento
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Representación en String del objeto Paciente
     * @return String con información del paciente
     */
    @Override
    public String toString() {
        return "Paciente{" +
                "id='" + getId() + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fecha de cumpleaños='" + fechaNacimiento +
                ", telefono " + telefono + '\'' +
                '}';
    }
}