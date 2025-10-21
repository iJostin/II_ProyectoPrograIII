package persistence;

import model.Farmaceuta;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase wrapper para la serialización/deserialización de listas de farmacéuticos en XML
 * Anotada con JAXB para mapeo XML
 */
@XmlRootElement(name = "farmaceutas")
@XmlAccessorType(XmlAccessType.FIELD)
public class FarmaceutaList {
    @XmlElement(name = "farmaceuta")
    private List<Farmaceuta> farmaceutas;

    /**
     * Constructor por defecto que inicializa una lista vacía
     */
    public FarmaceutaList() {
        this.farmaceutas = new ArrayList<>();
    }

    /**
     * Constructor con lista de farmacéuticos
     * @param farmaceutas Lista de farmacéuticos (puede ser null)
     */
    public FarmaceutaList(List<Farmaceuta> farmaceutas) {
        this.farmaceutas = farmaceutas != null ? farmaceutas : new ArrayList<>();
    }

    /**
     * Obtiene la lista de farmacéuticos
     * @return Lista de farmacéuticos
     */
    public List<Farmaceuta> getFarmaceutas() {
        return farmaceutas;
    }

    /**
     * Establece la lista de farmacéuticos
     * @param farmaceutas Lista de farmacéuticos
     */
    public void setFarmaceutas(List<Farmaceuta> farmaceutas) {
        this.farmaceutas = farmaceutas != null ? farmaceutas : new ArrayList<>();
    }
}