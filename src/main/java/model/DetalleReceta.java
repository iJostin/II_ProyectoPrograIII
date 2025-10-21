package model;

import jakarta.xml.bind.annotation.*;

/**
 * Representa un detalle de receta médica que contiene información sobre un medicamento prescrito
 * Incluye cantidad, indicaciones y duración del tratamiento
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DetalleReceta {
    @XmlElement
    private Medicamento medicamento;

    @XmlElement
    private int cantidad;

    @XmlElement
    private String indicaciones;

    @XmlElement
    private int dias;

    /**
     * Constructor por defecto
     */
    public DetalleReceta() {}

    /**
     * Constructor con todos los parámetros
     * @param medicamento Medicamento prescrito
     * @param cantidad Cantidad del medicamento
     * @param indicaciones Instrucciones de uso
     * @param dias Duración del tratamiento en días
     */
    public DetalleReceta(Medicamento medicamento, int cantidad, String indicaciones, int dias) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.dias = dias;
    }

    /**
     * Obtiene el medicamento del detalle
     * @return Objeto Medicamento
     */
    public Medicamento getMedicamento() { return medicamento; }

    /**
     * Establece el medicamento del detalle
     * @param medicamento Objeto Medicamento
     */
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    /**
     * Obtiene la cantidad del medicamento
     * @return Cantidad del medicamento
     */
    public int getCantidad() { return cantidad; }

    /**
     * Establece la cantidad del medicamento
     * @param cantidad Cantidad del medicamento
     */
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    /**
     * Obtiene las indicaciones de uso
     * @return Instrucciones de uso del medicamento
     */
    public String getIndicaciones() { return indicaciones; }

    /**
     * Establece las indicaciones de uso
     * @param indicaciones Instrucciones de uso del medicamento
     */
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    /**
     * Obtiene la duración del tratamiento
     * @return Duración en días del tratamiento
     */
    public int getDias() { return dias; }

    /**
     * Establece la duración del tratamiento
     * @param dias Duración en días del tratamiento
     */
    public void setDias(int dias) { this.dias = dias; }
}