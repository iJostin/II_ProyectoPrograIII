package persistence;

import model.Admin;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase wrapper para la serialización/deserialización de listas de administradores en XML
 * Anotada con JAXB para mapeo XML
 */
@XmlRootElement(name = "administrador")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminList {
    @XmlElement(name = "administrador")
    private List<Admin> admins;

    /**
     * Constructor por defecto que inicializa una lista vacía
     */
    public AdminList() {
        this.admins = new ArrayList<>();
    }

    /**
     * Constructor con lista de administradores
     * @param admins Lista de administradores (puede ser null)
     */
    public AdminList(List<Admin> admins) {
        this.admins = admins != null ? admins : new ArrayList<>();
    }

    /**
     * Obtiene la lista de administradores
     * @return Lista de administradores
     */
    public List<Admin> getAdmins() {
        return admins;
    }

    /**
     * Establece la lista de administradores
     * @param admins Lista de administradores
     */
    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }
}