package persistence;

import model.Medicamento;
import util.XMLHelper;
import util.DatabaseConnection;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {

    public void agregar(Medicamento medicamento) throws SQLException {
        String sql = "INSERT INTO medicamentos (codigo, nombre, presentacion) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, medicamento.getCodigo());
            pstmt.setString(2, medicamento.getNombre());
            pstmt.setString(3, medicamento.getPresentacion());
            pstmt.executeUpdate();

            System.out.println("✅ Medicamento agregado: " + medicamento.getCodigo());

        } catch (SQLException e) {
            throw new SQLException("No se pudo agregar el medicamento: " + e.getMessage(), e);
        }
    }

    public Medicamento buscarPorId(String codigo) {
        String sql = "SELECT * FROM medicamentos WHERE codigo = ? AND activo = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Medicamento(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("presentacion")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar medicamento: " + e.getMessage());
        }

        return null;
    }

    public List<Medicamento> getTodos() {
        List<Medicamento> medicamentos = new ArrayList<>();
        String sql = "SELECT * FROM medicamentos WHERE activo = TRUE ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                medicamentos.add(new Medicamento(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("presentacion")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar medicamentos: " + e.getMessage());
        }

        return medicamentos;
    }

    public void actualizar(Medicamento medicamento) throws SQLException {
        String sql = "UPDATE medicamentos SET nombre = ?, presentacion = ? WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, medicamento.getNombre());
            pstmt.setString(2, medicamento.getPresentacion());
            pstmt.setString(3, medicamento.getCodigo());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el medicamento: " + medicamento.getCodigo());
            }

            System.out.println("✅ Medicamento actualizado: " + medicamento.getCodigo());

        } catch (SQLException e) {
            throw new SQLException("No se pudo actualizar el medicamento: " + e.getMessage(), e);
        }
    }

    public void eliminar(String codigo) throws SQLException {
        String sql = "UPDATE medicamentos SET activo = FALSE WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el medicamento: " + codigo);
            }

            System.out.println("✅ Medicamento desactivado: " + codigo);

        } catch (SQLException e) {
            throw new SQLException("No se pudo eliminar el medicamento: " + e.getMessage(), e);
        }
    }
}