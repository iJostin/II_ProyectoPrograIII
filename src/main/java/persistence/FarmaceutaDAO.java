package persistence;

import model.Farmaceuta;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar los Farmaceutas del sistema
 */
public class FarmaceutaDAO {

    // ===== AGREGAR =====
    public void agregar(Farmaceuta farmaceuta) throws SQLException {
        String sqlUsuario = "INSERT INTO usuarios (id, clave, tipo) VALUES (?, ?, 'FARMACEUTA')";
        String sqlFarmaceuta = "INSERT INTO farmaceutas (id, nombre) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Insertar en usuarios
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, farmaceuta.getId());
                pstmt.setString(2, farmaceuta.getClave());
                pstmt.executeUpdate();
            }

            // Insertar en farmaceutas
            try (PreparedStatement pstmt = conn.prepareStatement(sqlFarmaceuta)) {
                pstmt.setString(1, farmaceuta.getId());
                pstmt.setString(2, farmaceuta.getNombre());
                pstmt.executeUpdate();
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Farmaceuta agregado: " + farmaceuta.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            throw new SQLException("No se pudo agregar el farmaceuta: " + e.getMessage(), e);
        }
    }

    // ===== BUSCAR POR ID =====
    public Farmaceuta buscarPorId(String id) {
        String sql = "SELECT u.id, u.clave, f.nombre " +
                "FROM usuarios u INNER JOIN farmaceutas f ON u.id = f.id " +
                "WHERE u.id = ? AND u.tipo = 'FARMACEUTA'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Farmaceuta f = new Farmaceuta();
                    f.setId(rs.getString("id"));
                    f.setClave(rs.getString("clave"));
                    f.setNombre(rs.getString("nombre"));
                    f.setTipo("FARMACEUTA");
                    return f;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar farmaceuta: " + e.getMessage());
        }

        return null;
    }

    // ===== LISTAR TODOS =====
    public List<Farmaceuta> getTodos() {
        List<Farmaceuta> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.clave, f.nombre " +
                "FROM usuarios u INNER JOIN farmaceutas f ON u.id = f.id " +
                "WHERE u.tipo = 'FARMACEUTA' AND u.activo = TRUE " +
                "ORDER BY f.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Farmaceuta f = new Farmaceuta();
                f.setId(rs.getString("id"));
                f.setClave(rs.getString("clave"));
                f.setNombre(rs.getString("nombre"));
                f.setTipo("FARMACEUTA");
                lista.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar farmaceutas: " + e.getMessage());
        }

        return lista;
    }

    // ===== ACTUALIZAR =====
    public void actualizar(Farmaceuta farmaceuta) throws SQLException {
        String sqlUsuario = "UPDATE usuarios SET clave = ? WHERE id = ?";
        String sqlFarmaceuta = "UPDATE farmaceutas SET nombre = ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Actualizar usuarios
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, farmaceuta.getClave());
                pstmt.setString(2, farmaceuta.getId());
                pstmt.executeUpdate();
            }

            // Actualizar farmaceuta
            try (PreparedStatement pstmt = conn.prepareStatement(sqlFarmaceuta)) {
                pstmt.setString(1, farmaceuta.getNombre());
                pstmt.setString(2, farmaceuta.getId());
                int rows = pstmt.executeUpdate();

                if (rows == 0) throw new SQLException("No se encontró el farmaceuta con ID: " + farmaceuta.getId());
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Farmaceuta actualizado: " + farmaceuta.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            throw new SQLException("No se pudo actualizar el farmaceuta: " + e.getMessage(), e);
        }
    }

    // ===== ELIMINAR =====
    public void eliminar(String id) throws SQLException {
        String sql = "UPDATE usuarios SET activo = FALSE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();

            if (rows == 0) throw new SQLException("No se encontró el farmaceuta con ID: " + id);

            System.out.println("✅ Farmaceuta desactivado: " + id);

        } catch (SQLException e) {
            throw new SQLException("No se pudo eliminar el farmaceuta: " + e.getMessage(), e);
        }
    }

    // ===== BUSCAR POR NOMBRE =====
    public List<Farmaceuta> buscarPorNombre(String nombre) {
        List<Farmaceuta> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.clave, f.nombre " +
                "FROM usuarios u INNER JOIN farmaceutas f ON u.id = f.id " +
                "WHERE u.tipo = 'FARMACEUTA' AND u.activo = TRUE AND f.nombre LIKE ? " +
                "ORDER BY f.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Farmaceuta f = new Farmaceuta();
                    f.setId(rs.getString("id"));
                    f.setClave(rs.getString("clave"));
                    f.setNombre(rs.getString("nombre"));
                    f.setTipo("FARMACEUTA");
                    lista.add(f);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar farmaceutas por nombre: " + e.getMessage());
        }

        return lista;
    }
}
