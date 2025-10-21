package persistence;

import model.Paciente;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PacienteDAO {

    // ===== AGREGAR =====
    public void agregar(Paciente paciente) throws SQLException {
        String sqlUsuario = "INSERT INTO usuarios (id, clave, tipo) VALUES (?, ?, 'PACIENTE')";
        String sqlPaciente = "INSERT INTO pacientes (id, nombre, fecha_nacimiento, telefono) " +
                "VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Insertar en usuarios
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, paciente.getId());
                pstmt.setString(2, paciente.getClave());
                pstmt.executeUpdate();
            }

            // Insertar en pacientes
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPaciente)) {
                pstmt.setString(1, paciente.getId());
                pstmt.setString(2, paciente.getNombre());
                pstmt.setDate(3, Date.valueOf(paciente.getFechaNacimiento()));
                pstmt.setString(4, paciente.getTelefono());
                pstmt.executeUpdate();
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Paciente agregado: " + paciente.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            throw new SQLException("No se pudo agregar el paciente: " + e.getMessage(), e);
        }
    }

    // ===== BUSCAR POR ID =====
    public Paciente buscarPorId(String id) {
        String sql = "SELECT u.id, u.clave, p.nombre, p.fecha_nacimiento, p.telefono " +
                "FROM usuarios u " +
                "INNER JOIN pacientes p ON u.id = p.id " +
                "WHERE u.id = ? AND u.tipo = 'PACIENTE'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Paciente paciente = new Paciente();
                    paciente.setId(rs.getString("id"));
                    paciente.setClave(rs.getString("clave"));
                    paciente.setNombre(rs.getString("nombre"));
                    paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                    paciente.setTelefono(rs.getString("telefono"));
                    paciente.setTipo("PACIENTE");
                    return paciente;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar paciente: " + e.getMessage());
        }

        return null;
    }

    // ===== LISTAR TODOS =====
    public List<Paciente> getTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT u.id, u.clave, p.nombre, p.fecha_nacimiento, p.telefono " +
                "FROM usuarios u " +
                "INNER JOIN pacientes p ON u.id = p.id " +
                "WHERE u.tipo = 'PACIENTE' AND u.activo = TRUE " +
                "ORDER BY p.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getString("id"));
                paciente.setClave(rs.getString("clave"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                paciente.setTelefono(rs.getString("telefono"));
                paciente.setTipo("PACIENTE");
                pacientes.add(paciente);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
        }

        return pacientes;
    }

    // ===== ACTUALIZAR =====
    public void actualizar(Paciente paciente) throws SQLException {
        String sqlUsuario = "UPDATE usuarios SET clave = ? WHERE id = ?";
        String sqlPaciente = "UPDATE pacientes SET nombre = ?, fecha_nacimiento = ?, " +
                "telefono = ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Actualizar usuarios
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, paciente.getClave());
                pstmt.setString(2, paciente.getId());
                pstmt.executeUpdate();
            }

            // Actualizar pacientes
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPaciente)) {
                pstmt.setString(1, paciente.getNombre());
                pstmt.setDate(2, Date.valueOf(paciente.getFechaNacimiento()));
                pstmt.setString(3, paciente.getTelefono());
                pstmt.setString(4, paciente.getId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No se encontró el paciente con ID: " + paciente.getId());
                }
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Paciente actualizado: " + paciente.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            throw new SQLException("No se pudo actualizar el paciente: " + e.getMessage(), e);
        }
    }

    // ===== ELIMINAR =====
    public void eliminar(String id) throws SQLException {
        // Soft delete: solo desactivar
        String sql = "UPDATE usuarios SET activo = FALSE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el paciente con ID: " + id);
            }

            System.out.println("✅ Paciente desactivado: " + id);

        } catch (SQLException e) {
            throw new SQLException("No se pudo eliminar el paciente: " + e.getMessage(), e);
        }
    }

    // ===== BÚSQUEDA POR NOMBRE =====
    public List<Paciente> buscarPorNombre(String nombre) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT u.id, u.clave, p.nombre, p.fecha_nacimiento, p.telefono " +
                "FROM usuarios u " +
                "INNER JOIN pacientes p ON u.id = p.id " +
                "WHERE u.tipo = 'PACIENTE' AND u.activo = TRUE " +
                "AND p.nombre LIKE ? " +
                "ORDER BY p.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Paciente paciente = new Paciente();
                    paciente.setId(rs.getString("id"));
                    paciente.setClave(rs.getString("clave"));
                    paciente.setNombre(rs.getString("nombre"));
                    paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                    paciente.setTelefono(rs.getString("telefono"));
                    paciente.setTipo("PACIENTE");
                    pacientes.add(paciente);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar pacientes por nombre: " + e.getMessage());
        }

        return pacientes;
    }
}