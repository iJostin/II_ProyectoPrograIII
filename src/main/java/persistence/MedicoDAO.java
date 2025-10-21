package persistence;

import model.Medico;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para gestionar médicos usando MySQL con JDBC
 */
public class MedicoDAO {

    /**
     * Agrega un nuevo médico al sistema
     * @param medico Objeto Medico a agregar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void agregar(Medico medico) throws SQLException {
        String sqlUsuario = "INSERT INTO usuarios (id, clave, tipo, activo) VALUES (?, ?, 'MEDICO', TRUE)";
        String sqlMedico = "INSERT INTO medicos (id, nombre, especialidad) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Insertar en tabla usuarios
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, medico.getId());
                pstmt.setString(2, medico.getClave());
                pstmt.executeUpdate();
            }

            // Insertar en tabla medicos
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMedico)) {
                pstmt.setString(1, medico.getId());
                pstmt.setString(2, medico.getNombre());
                pstmt.setString(3, medico.getEspecialidad());
                pstmt.executeUpdate();
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Médico agregado: " + medico.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            System.err.println("❌ Error al agregar médico: " + e.getMessage());
            throw new SQLException("No se pudo agregar el médico: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un médico del sistema por su ID
     * @param id ID del médico a eliminar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void eliminar(String id) throws SQLException {
        String sqlVerificar = "SELECT COUNT(*) FROM recetas WHERE medico_id = ?";
        String sqlDesactivar = "UPDATE usuarios SET activo = FALSE WHERE id = ?";
        String sqlEliminar = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Verificar si tiene recetas
            boolean tieneRecetas = false;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlVerificar)) {
                pstmt.setString(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        tieneRecetas = true;
                    }
                }
            }

            if (tieneRecetas) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDesactivar)) {
                    pstmt.setString(1, id);
                    pstmt.executeUpdate();
                }
                System.out.println("⚠️ Médico desactivado (tiene recetas): " + id);
                return;
            }

            // Si no tiene recetas, eliminar completamente
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEliminar)) {
                pstmt.setString(1, id);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No se encontró el médico con ID: " + id);
                }
            }

            System.out.println("✅ Médico eliminado: " + id);

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar médico: " + e.getMessage());
            throw new SQLException("No se pudo eliminar el médico: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea un ResultSet a un objeto Medico
     * @param rs ResultSet con los datos del médico
     * @return Objeto Medico creado
     * @throws SQLException si ocurre un error al leer el ResultSet
     */
    private Medico mapResultSetToMedico(ResultSet rs) throws SQLException {
        Medico medico = new Medico();
        medico.setId(rs.getString("id"));
        medico.setClave(rs.getString("clave"));
        medico.setNombre(rs.getString("nombre"));
        medico.setEspecialidad(rs.getString("especialidad"));
        medico.setTipo("MEDICO");
        return medico;
    }

    /**
     * Busca médicos por especialidad
     * @param especialidad Especialidad a buscar
     * @return Lista de médicos con esa especialidad
     */
    public List<Medico> buscarPorEspecialidad(String especialidad) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT u.id, u.clave, m.nombre, m.especialidad " +
                "FROM usuarios u " +
                "INNER JOIN medicos m ON u.id = m.id " +
                "WHERE u.tipo = 'MEDICO' AND u.activo = TRUE " +
                "AND m.especialidad LIKE ? " +
                "ORDER BY m.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + especialidad + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapResultSetToMedico(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar médicos por especialidad: " + e.getMessage());
        }

        return medicos;
    }

    /**
     * Obtiene el conteo total de médicos activos
     * @return Número de médicos activos
     */
    public int contarMedicos() {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE tipo = 'MEDICO' AND activo = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar médicos: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Busca un médico por su ID
     * @param id ID del médico a buscar
     * @return Objeto Medico encontrado o null si no existe
     */
    public Medico buscarPorId(String id) {
        String sql = "SELECT u.id, u.clave, m.nombre, m.especialidad " +
                "FROM usuarios u " +
                "INNER JOIN medicos m ON u.id = m.id " +
                "WHERE u.id = ? AND u.tipo = 'MEDICO'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedico(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar médico: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obtiene todos los médicos del sistema
     * @return Lista de todos los médicos
     */
    public List<Medico> getTodos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT u.id, u.clave, m.nombre, m.especialidad " +
                "FROM usuarios u " +
                "INNER JOIN medicos m ON u.id = m.id " +
                "WHERE u.tipo = 'MEDICO' AND u.activo = TRUE " +
                "ORDER BY m.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                medicos.add(mapResultSetToMedico(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar médicos: " + e.getMessage());
        }

        return medicos;
    }

    /**
     * Actualiza la información de un médico existente
     * @param medico Objeto Medico con la información actualizada
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void actualizar(Medico medico) throws SQLException {
        String sqlUsuario = "UPDATE usuarios SET clave = ? WHERE id = ?";
        String sqlMedico = "UPDATE medicos SET nombre = ?, especialidad = ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Actualizar tabla usuarios
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, medico.getClave());
                pstmt.setString(2, medico.getId());
                pstmt.executeUpdate();
            }

            // Actualizar tabla medicos
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMedico)) {
                pstmt.setString(1, medico.getNombre());
                pstmt.setString(2, medico.getEspecialidad());
                pstmt.setString(3, medico.getId());
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected == 0) {
                    throw new SQLException("No se encontró el médico con ID: " + medico.getId());
                }
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Médico actualizado: " + medico.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            System.err.println("❌ Error al actualizar médico: " + e.getMessage());
            throw new SQLException("No se pudo actualizar el médico: " + e.getMessage(), e);
        }
    }
}
