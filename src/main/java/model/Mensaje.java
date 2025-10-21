package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje {
    private int id;
    private String remitenteId;
    private String remitenteNombre;
    private String remitenteTipo;
    private String destinatarioId;
    private String destinatarioNombre;
    private String destinatarioTipo;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean leido;
    private LocalDateTime fechaLectura;

    /**
     * Constructor por defecto
     */
    public Mensaje() {}

    /**
     * Constructor para crear un nuevo mensaje
     */
    public Mensaje(String remitenteId, String destinatarioId, String mensaje) {
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemitenteId() {
        return remitenteId;
    }

    public void setRemitenteId(String remitenteId) {
        this.remitenteId = remitenteId;
    }

    public String getRemitenteNombre() {
        return remitenteNombre;
    }

    public void setRemitenteNombre(String remitenteNombre) {
        this.remitenteNombre = remitenteNombre;
    }

    public String getRemitenteTipo() {
        return remitenteTipo;
    }

    public void setRemitenteTipo(String remitenteTipo) {
        this.remitenteTipo = remitenteTipo;
    }

    public String getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(String destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getDestinatarioNombre() {
        return destinatarioNombre;
    }

    public void setDestinatarioNombre(String destinatarioNombre) {
        this.destinatarioNombre = destinatarioNombre;
    }

    public String getDestinatarioTipo() {
        return destinatarioTipo;
    }

    public void setDestinatarioTipo(String destinatarioTipo) {
        this.destinatarioTipo = destinatarioTipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public LocalDateTime getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(LocalDateTime fechaLectura) {
        this.fechaLectura = fechaLectura;
    }

    /**
     * Obtiene el identificador del remitente con emoji
     */
    public String getRemitenteIdentificador() {
        String emoji = switch (remitenteTipo != null ? remitenteTipo : "") {
            case "ADMIN" -> "👔";
            case "MEDICO" -> "👨‍⚕️";
            case "FARMACEUTA" -> "💊";
            case "PACIENTE" -> "🏥";
            default -> "👤";
        };
        return emoji + " " + (remitenteNombre != null ? remitenteNombre : remitenteId);
    }

    /**
     * Obtiene el identificador del destinatario con emoji
     */
    public String getDestinatarioIdentificador() {
        String emoji = switch (destinatarioTipo != null ? destinatarioTipo : "") {
            case "ADMIN" -> "👔";
            case "MEDICO" -> "👨‍⚕️";
            case "FARMACEUTA" -> "💊";
            case "PACIENTE" -> "🏥";
            default -> "👤";
        };
        return emoji + " " + (destinatarioNombre != null ? destinatarioNombre : destinatarioId);
    }

    /**
     * Obtiene la fecha de envío en formato legible
     */
    public String getFechaEnvioFormateada() {
        if (fechaEnvio == null) return "N/A";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaEnvio.format(formatter);
    }

    /**
     * Obtiene el estado del mensaje (leído/no leído)
     */
    public String getEstado() {
        if (leido) {
            if (fechaLectura != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                return "✓✓ Leído " + fechaLectura.format(formatter);
            }
            return "✓✓ Leído";
        }
        return "✓ Enviado";
    }

    /**
     * Preview del mensaje (primeros 50 caracteres)
     */
    public String getPreview() {
        if (mensaje == null) return "";
        if (mensaje.length() <= 50) return mensaje;
        return mensaje.substring(0, 50) + "...";
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "id=" + id +
                ", de='" + remitenteNombre + '\'' +
                ", para='" + destinatarioNombre + '\'' +
                ", mensaje='" + getPreview() + '\'' +
                ", leido=" + leido +
                '}';
    }
}