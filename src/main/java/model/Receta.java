package model;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una receta médica en el sistema
 * Contiene información sobre el paciente, médico, fechas, estado y detalles de medicamentos
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Receta {
    @XmlElement
    private String id;

    @XmlElement
    private Paciente paciente;

    @XmlElement
    private Medico medico;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement
    private LocalDate fechaConfeccion;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement
    private LocalDate fechaRetiro;

    @XmlElement
    private String estado; // "CONFECCIONADA", "PROCESO", "LISTA", "ENTREGADA"

    @XmlElement(name = "detalle")
    private List<DetalleReceta> detalles = new ArrayList<>();

    /**
     * Constructor por defecto
     */
    public Receta() {}

    /**
     * Constructor con parámetros básicos
     * @param id Identificador único de la receta
     * @param paciente Paciente destinatario de la receta
     * @param medico Médico que emite la receta
     * @param fechaConfeccion Fecha de creación de la receta
     * @param fechaRetiro Fecha límite para retirar los medicamentos
     * @param estado Estado actual de la receta
     */
    public Receta(String id, Paciente paciente, Medico medico, LocalDate fechaConfeccion, LocalDate fechaRetiro, String estado) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
        this.detalles = new ArrayList<>();
    }

    /**
     * Obtiene el ID de la receta
     * @return Identificador único de la receta
     */
    public String getId() { return id; }

    /**
     * Establece el ID de la receta
     * @param id Identificador único de la receta
     */
    public void setId(String id) { this.id = id; }

    /**
     * Obtiene el paciente de la receta
     * @return Objeto Paciente
     */
    public Paciente getPaciente() { return paciente; }

    /**
     * Establece el paciente de la receta
     * @param paciente Objeto Paciente
     */
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    /**
     * Obtiene el médico de la receta
     * @return Objeto Medico
     */
    public Medico getMedico() { return medico; }

    /**
     * Establece el médico de la receta
     * @param medico Objeto Medico
     */
    public void setMedico(Medico medico) { this.medico = medico; }

    /**
     * Obtiene la fecha de confección
     * @return Fecha de creación de la receta
     */
    public LocalDate getFechaConfeccion() { return fechaConfeccion; }

    /**
     * Establece la fecha de confección
     * @param fechaConfeccion Fecha de creación de la receta
     */
    public void setFechaConfeccion(LocalDate fechaConfeccion) { this.fechaConfeccion = fechaConfeccion; }

    /**
     * Obtiene la fecha de retiro
     * @return Fecha límite para retirar medicamentos
     */
    public LocalDate getFechaRetiro() { return fechaRetiro; }

    /**
     * Establece la fecha de retiro
     * @param fechaRetiro Fecha límite para retirar medicamentos
     */
    public void setFechaRetiro(LocalDate fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    /**
     * Obtiene el estado de la receta
     * @return Estado actual (CONFECCIONADA, PROCESO, LISTA, ENTREGADA)
     */
    public String getEstado() { return estado; }

    /**
     * Establece el estado de la receta
     * @param estado Estado actual (CONFECCIONADA, PROCESO, LISTA, ENTREGADA)
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Obtiene la lista de detalles de la receta
     * @return Lista de objetos DetalleReceta
     */
    public List<DetalleReceta> getDetalles() { return detalles; }

    /**
     * Establece la lista de detalles de la receta
     * @param detalles Lista de objetos DetalleReceta
     */
    public void setDetalles(List<DetalleReceta> detalles) { this.detalles = detalles; }

    /**
     * Agrega un detalle a la receta
     * @param d Objeto DetalleReceta a agregar
     */
    public void agregarDetalle(DetalleReceta d) {
        detalles.add(d);
    }

    /**
     * Elimina un detalle de la receta
     * @param d Objeto DetalleReceta a eliminar
     */
    public void eliminarDetalle(DetalleReceta d) {
        detalles.remove(d);
    }
}