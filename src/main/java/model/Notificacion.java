package model;

import java.time.LocalDateTime;

public class Notificacion {

    public enum TipoNotificacion {
        LOGIN,           // Usuario se conectó
        LOGOUT,          // Usuario se desconectó
        MENSAJE_NUEVO,   // Nuevo mensaje recibido
        RECETA_NUEVA,    // Nueva receta creada
        RECETA_ACTUALIZADA, // Receta actualizada
        SISTEMA          // Mensaje del sistema
    }

    private TipoNotificacion tipo;
    private String usuarioId;
    private String mensaje;
    private Object datos; // Datos adicionales según el tipo
    private LocalDateTime timestamp;

    /**
     * Constructor por defecto
     */
    public Notificacion() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor con tipo y mensaje
     */
    public Notificacion(TipoNotificacion tipo, String mensaje) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor completo
     */
    public Notificacion(TipoNotificacion tipo, String usuarioId, String mensaje, Object datos) {
        this.tipo = tipo;
        this.usuarioId = usuarioId;
        this.mensaje = mensaje;
        this.datos = datos;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getDatos() {
        return datos;
    }

    public void setDatos(Object datos) {
        this.datos = datos;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "tipo=" + tipo +
                ", usuarioId='" + usuarioId + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
