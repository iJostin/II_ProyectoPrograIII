package persistence;

import model.*;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar sesiones activas de usuarios (login/logout)
 */
public class SesionDAO {

    /**
     * Registra el login de un usuario
     * @param usuarioId ID del usuario que hace login
     * @param ipCliente IP desde donde se conecta
     * @throws SQLException si ocurre un error
     */
    public void registrarLogin(String usuarioId, String ipCliente) throws SQLException {
        String sql = "CALL sp_registrar_login(?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, usuarioId);
            cstmt.setString(2, ipCliente);
            cstmt.execute();

            System.out.println("✅ Login registrado: " + usuarioId + " desde " + ipCliente);

        } catch (SQLException e) {
            System.err.println("Error al registrar login: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Registra el logout de un usuario
     * @param usuarioId ID del usuario que hace logout
     * @throws SQLException si ocurre un error
     */
    public void registrarLogout(String usuarioId) throws SQLException {
        String sql = "CALL sp_registrar_logout(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, usuarioId);
            cstmt.execute();

            System.out.println("✅ Logout registrado: " + usuarioId);

        } catch (SQLException e) {
            System.err.println("Error al registrar logout: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene la lista de usuarios actualmente conectados
     * @return Lista de usuarios activos con su información básica
     */
    public List<UsuarioActivo> obtenerUsuariosActivos() {
        List<UsuarioActivo> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM v_usuarios_activos ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UsuarioActivo usuario = new UsuarioActivo();
                usuario.setUsuarioId(rs.getString("usuario_id"));
                usuario.setTipo(rs.getString("tipo"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setFechaLogin(rs.getTimestamp("fecha_login").toLocalDateTime());
                usuario.setUltimaActividad(rs.getTimestamp("ultima_actividad").toLocalDateTime());
                usuario.setIpCliente(rs.getString("ip_cliente"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios activos: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Verifica si un usuario está actualmente conectado
     * @param usuarioId ID del usuario a verificar
     * @return true si está conectado, false en caso contrario
     */
    public boolean estaConectado(String usuarioId) {
        String sql = "SELECT COUNT(*) FROM sesiones_activas WHERE usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuarioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
        }

        return false;
    }

    /**
     * Actualiza la última actividad de un usuario conectado
     * @param usuarioId ID del usuario
     */
    public void actualizarActividad(String usuarioId) {
        String sql = "UPDATE sesiones_activas SET ultima_actividad = CURRENT_TIMESTAMP " +
                "WHERE usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuarioId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar actividad: " + e.getMessage());
        }
    }

    /**
     * Limpia sesiones inactivas (más de X minutos sin actividad)
     * @param minutosInactividad Minutos de inactividad para considerar sesión muerta
     */
    public void limpiarSesionesInactivas(int minutosInactividad) {
        String sql = "DELETE FROM sesiones_activas " +
                "WHERE ultima_actividad < DATE_SUB(NOW(), INTERVAL ? MINUTE)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, minutosInactividad);
            int eliminadas = pstmt.executeUpdate();

            if (eliminadas > 0) {
                System.out.println("🧹 Sesiones inactivas limpiadas: " + eliminadas);
            }

        } catch (SQLException e) {
            System.err.println("Error al limpiar sesiones: " + e.getMessage());
        }
    }
}
