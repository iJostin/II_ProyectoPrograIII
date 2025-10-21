package persistence;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import model.Paciente;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase wrapper para la serialización/deserialización de listas de pacientes en XML
 * Anotada con JAXB para mapeo XML
 */
@XmlRootElement(name = "pacientes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PacienteList {
    @XmlElement(name = "paciente")
    private List<Paciente> pacientes;

    /**
     * Constructor por defecto que inicializa una lista vacía
     */
    public PacienteList() {
        this.pacientes = new ArrayList<>();
    }

    /**
     * Constructor con lista de pacientes
     * @param pacientes Lista de pacientes (puede ser null)
     */
    public PacienteList(List<Paciente> pacientes) {
        this.pacientes = pacientes != null ? pacientes : new ArrayList<>();
    }

    /**
     * Obtiene la lista de pacientes
     * @return Lista de pacientes
     */
    public List<Paciente> getPacientes() {
        return pacientes;
    }

    /**
     * Establece la lista de pacientes
     * @param pacientes Lista de pacientes
     */
    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }
}