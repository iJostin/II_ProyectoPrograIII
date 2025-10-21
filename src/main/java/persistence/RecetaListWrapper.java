package persistence;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import model.Receta;

import java.util.List;

/**
 * Clase wrapper para la serialización/deserialización de listas de recetas en XML
 * Anotada con JAXB para mapeo XML
 */
@XmlRootElement(name = "recetas")
public class RecetaListWrapper {

    private List<Receta> recetas;

    /**
     * Obtiene la lista de recetas
     * @return Lista de recetas
     */
    @XmlElement(name = "receta")
    public List<Receta> getRecetas() {
        return recetas;
    }

    /**
     * Establece la lista de recetas
     * @param recetas Lista de recetas
     */
    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
}