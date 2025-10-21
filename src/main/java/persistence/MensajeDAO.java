package persistence;

import model.Mensaje;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar mensajes entre usuarios
 */
public class MensajeDAO {

    /**
     * Envía un mensaje de un usuario a otro
     * @param remitenteId ID del usuario que envía
     * @param destinatarioId ID del usuario que recibe
     * @param mensaje Contenido del mensaje
     * @return ID del mensaje creado
     * @throws SQLException si ocurre un error
     */
    public int enviarMensaje(String remitenteId, String destinatarioId, String mensaje)
            throws SQLException {
        String sql = "CALL sp_enviar_mensaje(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, remitenteId);
            cstmt.setString(2, destinatarioId);
            cstmt.setString(3, mensaje);
            cstmt.registerOutParameter(4, Types.INTEGER);

            cstmt.execute();
            int mensajeId = cstmt.getInt(4);

            System.out.println("✅ Mensaje enviado: " + remitenteId + " -> " + destinatarioId);

            return mensajeId;

        } catch (SQLException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene los mensajes recibidos por un usuario
     * @param destinatarioId ID del usuario
     * @param soloNoLeidos Si es true, solo mensajes no leídos
     * @return Lista de mensajes
     */
    public List<Mensaje> obtenerMensajesRecibidos(String destinatarioId, boolean soloNoLeidos) {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = "SELECT m.*, " +
                "ur.tipo as remitente_tipo, " +
                "COALESCE(ar.nombre, mr.nombre, fr.nombre, pr.nombre) as remitente_nombre " +
                "FROM mensajes m " +
                "INNER JOIN usuarios ur ON m.remitente_id = ur.id " +
                "LEFT JOIN admins ar ON ur.id = ar.id " +
                "LEFT JOIN medicos mr ON ur.id = mr.id " +
                "LEFT JOIN farmaceutas fr ON ur.id = fr.id " +
                "LEFT JOIN pacientes pr ON ur.id = pr.id " +
                "WHERE m.destinatario_id = ? ";

        if (soloNoLeidos) {
            sql += "AND m.leido = FALSE ";
        }

        sql += "ORDER BY m.fecha_envio DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, destinatarioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setId(rs.getInt("id"));
                    mensaje.setRemitenteId(rs.getString("remitente_id"));
                    mensaje.setRemitenteNombre(rs.getString("remitente_nombre"));
                    mensaje.setRemitenteTipo(rs.getString("remitente_tipo"));
                    mensaje.setDestinatarioId(rs.getString("destinatario_id"));
                    mensaje.setMensaje(rs.getString("mensaje"));
                    mensaje.setFechaEnvio(rs.getTimestamp("fecha_envio").toLocalDateTime());
                    mensaje.setLeido(rs.getBoolean("leido"));

                    Timestamp fechaLectura = rs.getTimestamp("fecha_lectura");
                    if (fechaLectura != null) {
                        mensaje.setFechaLectura(fechaLectura.toLocalDateTime());
                    }

                    mensajes.add(mensaje);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener mensajes: " + e.getMessage());
        }

        return mensajes;
    }

    /**
     * Marca un mensaje como leído
     * @param mensajeId ID del mensaje
     * @throws SQLException si ocurre un error
     */
    public void marcarComoLeido(int mensajeId) throws SQLException {
        String sql = "CALL sp_marcar_mensaje_leido(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, mensajeId);
            cstmt.execute();

            System.out.println("✅ Mensaje marcado como leído: " + mensajeId);

        } catch (SQLException e) {
            System.err.println("Error al marcar mensaje: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Cuenta los mensajes no leídos de un usuario
     * @param destinatarioId ID del usuario
     * @return Cantidad de mensajes no leídos
     */
    public int contarMensajesNoLeidos(String destinatarioId) {
        String sql = "SELECT COUNT(*) FROM mensajes " +
                "WHERE destinatario_id = ? AND leido = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, destinatarioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al contar mensajes: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Elimina un mensaje
     * @param mensajeId ID del mensaje a eliminar
     * @throws SQLException si ocurre un error
     */
    public void eliminarMensaje(int mensajeId) throws SQLException {
        String sql = "DELETE FROM mensajes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, mensajeId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el mensaje con ID: " + mensajeId);
            }

            System.out.println("✅ Mensaje eliminado: " + mensajeId);

        } catch (SQLException e) {
            System.err.println("Error al eliminar mensaje: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene la conversación entre dos usuarios
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @param limite Cantidad máxima de mensajes a retornar
     * @return Lista de mensajes ordenados por fecha
     */
    public List<Mensaje> obtenerConversacion(String usuario1Id, String usuario2Id, int limite) {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = "SELECT m.*, " +
                "ur.tipo as remitente_tipo, " +
                "COALESCE(ar.nombre, mr.nombre, fr.nombre, pr.nombre) as remitente_nombre " +
                "FROM mensajes m " +
                "INNER JOIN usuarios ur ON m.remitente_id = ur.id " +
                "LEFT JOIN admins ar ON ur.id = ar.id " +
                "LEFT JOIN medicos mr ON ur.id = mr.id " +
                "LEFT JOIN farmaceutas fr ON ur.id = fr.id " +
                "LEFT JOIN pacientes pr ON ur.id = pr.id " +
                "WHERE (m.remitente_id = ? AND m.destinatario_id = ?) " +
                "   OR (m.remitente_id = ? AND m.destinatario_id = ?) " +
                "ORDER BY m.fecha_envio DESC " +
                "LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario1Id);
            pstmt.setString(2, usuario2Id);
            pstmt.setString(3, usuario2Id);
            pstmt.setString(4, usuario1Id);
            pstmt.setInt(5, limite);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setId(rs.getInt("id"));
                    mensaje.setRemitenteId(rs.getString("remitente_id"));
                    mensaje.setRemitenteNombre(rs.getString("remitente_nombre"));
                    mensaje.setRemitenteTipo(rs.getString("remitente_tipo"));
                    mensaje.setDestinatarioId(rs.getString("destinatario_id"));
                    mensaje.setMensaje(rs.getString("mensaje"));
                    mensaje.setFechaEnvio(rs.getTimestamp("fecha_envio").toLocalDateTime());
                    mensaje.setLeido(rs.getBoolean("leido"));
                    mensajes.add(mensaje);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener conversación: " + e.getMessage());
        }

        return mensajes;
    }
}