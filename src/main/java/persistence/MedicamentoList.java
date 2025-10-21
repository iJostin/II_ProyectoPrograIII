package persistence;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import model.Medicamento;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase wrapper para la serialización/deserialización de listas de medicamentos en XML
 * Anotada con JAXB para mapeo XML
 */
@XmlRootElement(name = "medicamentos")
@XmlAccessorType(XmlAccessType.FIELD)
public class MedicamentoList {
    @XmlElement(name = "medicamento")
    private List<Medicamento> medicamentos;

    /**
     * Constructor por defecto que inicializa una lista vacía
     */
    public MedicamentoList() {
        this.medicamentos = new ArrayList<>();
    }

    /**
     * Constructor con lista de medicamentos
     * @param medicamentos Lista de medicamentos (puede ser null)
     */
    public MedicamentoList(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos != null ? medicamentos : new ArrayList<>();
    }

    /**
     * Obtiene la lista de medicamentos
     * @return Lista de medicamentos
     */
    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    /**
     * Establece la lista de medicamentos
     * @param medicamentos Lista de medicamentos
     */
    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }
}