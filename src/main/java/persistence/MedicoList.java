package persistence;

import model.Medico;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase wrapper para la serialización/deserialización de listas de médicos en XML
 * Anotada con JAXB para mapeo XML
 */
@XmlRootElement(name = "medicos")
@XmlAccessorType(XmlAccessType.FIELD)
public class MedicoList {
    @XmlElement(name = "medico")
    private List<Medico> medicos;

    /**
     * Constructor por defecto que inicializa una lista vacía
     */
    public MedicoList() {
        this.medicos = new ArrayList<>();
    }

    /**
     * Constructor con lista de médicos
     * @param medicos Lista de médicos (puede ser null)
     */
    public MedicoList(List<Medico> medicos) {
        this.medicos = medicos != null ? medicos : new ArrayList<>();
    }

    /**
     * Obtiene la lista de médicos
     * @return Lista de médicos
     */
    public List<Medico> getMedicos() {
        return medicos;
    }

    /**
     * Establece la lista de médicos
     * @param medicos Lista de médicos
     */
    public void setMedicos(List<Medico> medicos) {
        this.medicos = medicos;
    }
}