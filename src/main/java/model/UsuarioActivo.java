package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un usuario actualmente conectado al sistema
 */
public class UsuarioActivo {
    private String usuarioId;
    private String tipo; // ADMIN, MEDICO, FARMACEUTA, PACIENTE
    private String nombre;
    private LocalDateTime fechaLogin;
    private LocalDateTime ultimaActividad;
    private String ipCliente;

    /**
     * Constructor por defecto
     */
    public UsuarioActivo() {}

    /**
     * Constructor con parámetros
     */
    public UsuarioActivo(String usuarioId, String tipo, String nombre,
                         LocalDateTime fechaLogin, String ipCliente) {
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.nombre = nombre;
        this.fechaLogin = fechaLogin;
        this.ultimaActividad = LocalDateTime.now();
        this.ipCliente = ipCliente;
    }

    // Getters y Setters
    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaLogin() {
        return fechaLogin;
    }

    public void setFechaLogin(LocalDateTime fechaLogin) {
        this.fechaLogin = fechaLogin;
    }

    public LocalDateTime getUltimaActividad() {
        return ultimaActividad;
    }

    public void setUltimaActividad(LocalDateTime ultimaActividad) {
        this.ultimaActividad = ultimaActividad;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    /**
     * Obtiene un identificador visual del usuario (tipo + nombre)
     */
    public String getIdentificador() {
        String emoji = switch (tipo) {
            case "ADMIN" -> "👔";
            case "MEDICO" -> "👨‍⚕️";
            case "FARMACEUTA" -> "💊";
            case "PACIENTE" -> "🏥";
            default -> "👤";
        };
        return emoji + " " + nombre + " (" + tipo + ")";
    }

    /**
     * Obtiene el tiempo conectado en formato legible
     */
    public String getTiempoConectado() {
        if (fechaLogin == null) return "N/A";

        LocalDateTime ahora = LocalDateTime.now();
        long minutos = java.time.Duration.between(fechaLogin, ahora).toMinutes();

        if (minutos < 60) {
            return minutos + " min";
        } else {
            long horas = minutos / 60;
            long mins = minutos % 60;
            return horas + "h " + mins + "m";
        }
    }

    @Override
    public String toString() {
        return "UsuarioActivo{" +
                "usuarioId='" + usuarioId + '\'' +
                ", tipo='" + tipo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fechaLogin=" + fechaLogin +
                ", ipCliente='" + ipCliente + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioActivo that = (UsuarioActivo) o;
        return usuarioId != null && usuarioId.equals(that.usuarioId);
    }

    @Override
    public int hashCode() {
        return usuarioId != null ? usuarioId.hashCode() : 0;
    }
}

