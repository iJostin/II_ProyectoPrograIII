package persistence;

import model.Admin;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar los Administradores del sistema
 */
public class AdminDAO {

    // ===== AGREGAR =====
    public void agregar(Admin admin) throws SQLException {
        String sqlUsuario = "INSERT INTO usuarios (id, clave, tipo) VALUES (?, ?, 'ADMIN')";
        String sqlAdmin = "INSERT INTO admins (id, nombre) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Insertar en usuarios
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, admin.getId());
                pstmt.setString(2, admin.getClave());
                pstmt.executeUpdate();
            }

            // Insertar en admins
            try (PreparedStatement pstmt = conn.prepareStatement(sqlAdmin)) {
                pstmt.setString(1, admin.getId());
                pstmt.setString(2, admin.getNombre());
                pstmt.executeUpdate();
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Admin agregado: " + admin.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            throw new SQLException("No se pudo agregar el admin: " + e.getMessage(), e);
        }
    }

    // ===== BUSCAR POR ID =====
    public Admin buscarPorId(String id) {
        String sql = "SELECT u.id, u.clave, a.nombre " +
                "FROM usuarios u INNER JOIN admins a ON u.id = a.id " +
                "WHERE u.id = ? AND u.tipo = 'ADMIN'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Admin a = new Admin();
                    a.setId(rs.getString("id"));
                    a.setClave(rs.getString("clave"));
                    a.setNombre(rs.getString("nombre"));
                    a.setTipo("ADMIN");
                    return a;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar admin: " + e.getMessage());
        }

        return null;
    }

    // ===== LISTAR TODOS =====
    public List<Admin> getTodos() {
        List<Admin> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.clave, a.nombre " +
                "FROM usuarios u INNER JOIN admins a ON u.id = a.id " +
                "WHERE u.tipo = 'ADMIN' AND u.activo = TRUE " +
                "ORDER BY a.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Admin a = new Admin();
                a.setId(rs.getString("id"));
                a.setClave(rs.getString("clave"));
                a.setNombre(rs.getString("nombre"));
                a.setTipo("ADMIN");
                lista.add(a);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar admins: " + e.getMessage());
        }

        return lista;
    }

    // ===== ACTUALIZAR =====
    public void actualizar(Admin admin) throws SQLException {
        String sqlUsuario = "UPDATE usuarios SET clave = ? WHERE id = ?";
        String sqlAdmin = "UPDATE admins SET nombre = ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Actualizar usuarios
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, admin.getClave());
                pstmt.setString(2, admin.getId());
                pstmt.executeUpdate();
            }

            // Actualizar admin
            try (PreparedStatement pstmt = conn.prepareStatement(sqlAdmin)) {
                pstmt.setString(1, admin.getNombre());
                pstmt.setString(2, admin.getId());
                int rows = pstmt.executeUpdate();

                if (rows == 0) throw new SQLException("No se encontró el admin con ID: " + admin.getId());
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Admin actualizado: " + admin.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            throw new SQLException("No se pudo actualizar el admin: " + e.getMessage(), e);
        }
    }

    // ===== ELIMINAR =====
    public void eliminar(String id) throws SQLException {
        String sql = "UPDATE usuarios SET activo = FALSE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();

            if (rows == 0) throw new SQLException("No se encontró el admin con ID: " + id);

            System.out.println("✅ Admin desactivado: " + id);

        } catch (SQLException e) {
            throw new SQLException("No se pudo eliminar el admin: " + e.getMessage(), e);
        }
    }

    // ===== BUSCAR POR NOMBRE =====
    public List<Admin> buscarPorNombre(String nombre) {
        List<Admin> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.clave, a.nombre " +
                "FROM usuarios u INNER JOIN admins a ON u.id = a.id " +
                "WHERE u.tipo = 'ADMIN' AND u.activo = TRUE AND a.nombre LIKE ? " +
                "ORDER BY a.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Admin a = new Admin();
                    a.setId(rs.getString("id"));
                    a.setClave(rs.getString("clave"));
                    a.setNombre(rs.getString("nombre"));
                    a.setTipo("ADMIN");
                    lista.add(a);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar admins por nombre: " + e.getMessage());
        }

        return lista;
    }
}
