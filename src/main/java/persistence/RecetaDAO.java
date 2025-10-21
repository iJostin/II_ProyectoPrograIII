package persistence;

import model.*;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para gestionar recetas médicas usando MySQL con JDBC
 */
public class RecetaDAO {

    /**
     * Agrega una nueva receta al sistema
     * @param receta Objeto Receta a agregar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void agregar(Receta receta) throws SQLException {
        String sqlReceta = "INSERT INTO recetas (id, paciente_id, medico_id, " +
                "fecha_confeccion, fecha_retiro, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlDetalle = "INSERT INTO detalle_recetas (receta_id, medicamento_codigo, " +
                "cantidad, indicaciones, dias) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Insertar receta
            try (PreparedStatement pstmt = conn.prepareStatement(sqlReceta)) {
                pstmt.setString(1, receta.getId());
                pstmt.setString(2, receta.getPaciente().getId());
                pstmt.setString(3, receta.getMedico().getId());
                pstmt.setDate(4, Date.valueOf(receta.getFechaConfeccion()));
                pstmt.setDate(5, Date.valueOf(receta.getFechaRetiro()));
                pstmt.setString(6, receta.getEstado());
                pstmt.executeUpdate();
            }

            // Insertar detalles de la receta
            if (receta.getDetalles() != null && !receta.getDetalles().isEmpty()) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDetalle)) {
                    for (DetalleReceta detalle : receta.getDetalles()) {
                        pstmt.setString(1, receta.getId());
                        pstmt.setString(2, detalle.getMedicamento().getCodigo());
                        pstmt.setInt(3, detalle.getCantidad());
                        pstmt.setString(4, detalle.getIndicaciones());
                        pstmt.setInt(5, detalle.getDias());
                        pstmt.executeUpdate();
                    }
                }
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Receta agregada: " + receta.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            System.err.println("❌ Error al agregar receta: " + e.getMessage());
            throw new SQLException("No se pudo agregar la receta: " + e.getMessage(), e);
        }
    }

    /**
     * Busca una receta por su ID (incluye detalles)
     * @param id ID de la receta a buscar
     * @return Objeto Receta encontrado o null si no existe
     */
    public Receta buscarPorId(String id) {
        String sqlReceta = "SELECT r.*, " +
                "p.nombre as paciente_nombre, p.fecha_nacimiento, p.telefono, " +
                "m.nombre as medico_nombre, m.especialidad " +
                "FROM recetas r " +
                "INNER JOIN pacientes p ON r.paciente_id = p.id " +
                "INNER JOIN medicos m ON r.medico_id = m.id " +
                "WHERE r.id = ?";

        String sqlDetalles = "SELECT dr.*, med.nombre as medicamento_nombre, " +
                "med.presentacion " +
                "FROM detalle_recetas dr " +
                "INNER JOIN medicamentos med ON dr.medicamento_codigo = med.codigo " +
                "WHERE dr.receta_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {

            Receta receta = null;

            // Buscar receta principal
            try (PreparedStatement pstmt = conn.prepareStatement(sqlReceta)) {
                pstmt.setString(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        receta = mapResultSetToReceta(rs);
                    }
                }
            }

            if (receta == null) {
                return null;
            }

            // Buscar detalles de la receta
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDetalles)) {
                pstmt.setString(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        DetalleReceta detalle = mapResultSetToDetalle(rs);
                        receta.agregarDetalle(detalle);
                    }
                }
            }

            return receta;

        } catch (SQLException e) {
            System.err.println("Error al buscar receta: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene todas las recetas del sistema
     * @return Lista de todas las recetas
     */
    public List<Receta> getTodos() {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT r.*, " +
                "p.nombre as paciente_nombre, p.fecha_nacimiento, p.telefono, " +
                "m.nombre as medico_nombre, m.especialidad " +
                "FROM recetas r " +
                "INNER JOIN pacientes p ON r.paciente_id = p.id " +
                "INNER JOIN medicos m ON r.medico_id = m.id " +
                "ORDER BY r.fecha_confeccion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Receta receta = mapResultSetToReceta(rs);
                // Cargar detalles para cada receta
                cargarDetalles(receta);
                recetas.add(receta);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar recetas: " + e.getMessage());
        }

        return recetas;
    }

    /**
     * Actualiza la información de una receta existente
     * @param receta Objeto Receta con la información actualizada
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void actualizar(Receta receta) throws SQLException {
        String sqlReceta = "UPDATE recetas SET paciente_id = ?, medico_id = ?, " +
                "fecha_confeccion = ?, fecha_retiro = ?, estado = ? " +
                "WHERE id = ?";

        String sqlEliminarDetalles = "DELETE FROM detalle_recetas WHERE receta_id = ?";

        String sqlInsertarDetalle = "INSERT INTO detalle_recetas (receta_id, medicamento_codigo, " +
                "cantidad, indicaciones, dias) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction();

            // Actualizar receta principal
            try (PreparedStatement pstmt = conn.prepareStatement(sqlReceta)) {
                pstmt.setString(1, receta.getPaciente().getId());
                pstmt.setString(2, receta.getMedico().getId());
                pstmt.setDate(3, Date.valueOf(receta.getFechaConfeccion()));
                pstmt.setDate(4, Date.valueOf(receta.getFechaRetiro()));
                pstmt.setString(5, receta.getEstado());
                pstmt.setString(6, receta.getId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No se encontró la receta con ID: " + receta.getId());
                }
            }

            // Eliminar detalles antiguos
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEliminarDetalles)) {
                pstmt.setString(1, receta.getId());
                pstmt.executeUpdate();
            }

            // Insertar nuevos detalles
            if (receta.getDetalles() != null && !receta.getDetalles().isEmpty()) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertarDetalle)) {
                    for (DetalleReceta detalle : receta.getDetalles()) {
                        pstmt.setString(1, receta.getId());
                        pstmt.setString(2, detalle.getMedicamento().getCodigo());
                        pstmt.setInt(3, detalle.getCantidad());
                        pstmt.setString(4, detalle.getIndicaciones());
                        pstmt.setInt(5, detalle.getDias());
                        pstmt.executeUpdate();
                    }
                }
            }

            DatabaseConnection.commitTransaction();
            System.out.println("✅ Receta actualizada: " + receta.getId());

        } catch (SQLException e) {
            DatabaseConnection.rollbackTransaction();
            System.err.println("❌ Error al actualizar receta: " + e.getMessage());
            throw new SQLException("No se pudo actualizar la receta: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una receta del sistema por su ID
     * @param id ID de la receta a eliminar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void eliminar(String id) throws SQLException {
        String sql = "DELETE FROM recetas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la receta con ID: " + id);
            }

            System.out.println("✅ Receta eliminada: " + id);

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar receta: " + e.getMessage());
            throw new SQLException("No se pudo eliminar la receta: " + e.getMessage(), e);
        }
    }

    /**
     * Cambia el estado de una receta
     * @param id ID de la receta
     * @param nuevoEstado Nuevo estado de la receta
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void cambiarEstado(String id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE recetas SET estado = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la receta con ID: " + id);
            }

            System.out.println("✅ Estado de receta actualizado: " + id + " -> " + nuevoEstado);

        } catch (SQLException e) {
            System.err.println("❌ Error al cambiar estado de receta: " + e.getMessage());
            throw new SQLException("No se pudo cambiar el estado: " + e.getMessage(), e);
        }
    }

    /**
     * Busca recetas por paciente
     * @param pacienteId ID del paciente
     * @return Lista de recetas del paciente
     */
    public List<Receta> buscarPorPaciente(String pacienteId) {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT r.*, " +
                "p.nombre as paciente_nombre, p.fecha_nacimiento, p.telefono, " +
                "m.nombre as medico_nombre, m.especialidad " +
                "FROM recetas r " +
                "INNER JOIN pacientes p ON r.paciente_id = p.id " +
                "INNER JOIN medicos m ON r.medico_id = m.id " +
                "WHERE r.paciente_id = ? " +
                "ORDER BY r.fecha_confeccion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pacienteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Receta receta = mapResultSetToReceta(rs);
                    cargarDetalles(receta);
                    recetas.add(receta);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar recetas por paciente: " + e.getMessage());
        }

        return recetas;
    }

    /**
     * Busca recetas por estado
     * @param estado Estado de la receta
     * @return Lista de recetas con ese estado
     */
    public List<Receta> buscarPorEstado(String estado) {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT r.*, " +
                "p.nombre as paciente_nombre, p.fecha_nacimiento, p.telefono, " +
                "m.nombre as medico_nombre, m.especialidad " +
                "FROM recetas r " +
                "INNER JOIN pacientes p ON r.paciente_id = p.id " +
                "INNER JOIN medicos m ON r.medico_id = m.id " +
                "WHERE r.estado = ? " +
                "ORDER BY r.fecha_confeccion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estado);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Receta receta = mapResultSetToReceta(rs);
                    cargarDetalles(receta);
                    recetas.add(receta);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar recetas por estado: " + e.getMessage());
        }

        return recetas;
    }

    /**
     * Carga los detalles de una receta desde la base de datos
     * @param receta Receta a la que se le cargarán los detalles
     */
    private void cargarDetalles(Receta receta) {
        String sql = "SELECT dr.*, med.nombre as medicamento_nombre, med.presentacion " +
                "FROM detalle_recetas dr " +
                "INNER JOIN medicamentos med ON dr.medicamento_codigo = med.codigo " +
                "WHERE dr.receta_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, receta.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DetalleReceta detalle = mapResultSetToDetalle(rs);
                    receta.agregarDetalle(detalle);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar detalles de receta: " + e.getMessage());
        }
    }

    /**
     * Mapea un ResultSet a un objeto Receta
     * @param rs ResultSet con los datos de la receta
     * @return Objeto Receta creado
     * @throws SQLException si ocurre un error al leer el ResultSet
     */
    private Receta mapResultSetToReceta(ResultSet rs) throws SQLException {
        // Crear paciente
        Paciente paciente = new Paciente();
        paciente.setId(rs.getString("paciente_id"));
        paciente.setNombre(rs.getString("paciente_nombre"));
        paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        paciente.setTelefono(rs.getString("telefono"));

        // Crear médico
        Medico medico = new Medico();
        medico.setId(rs.getString("medico_id"));
        medico.setNombre(rs.getString("medico_nombre"));
        medico.setEspecialidad(rs.getString("especialidad"));

        // Crear receta
        Receta receta = new Receta();
        receta.setId(rs.getString("id"));
        receta.setPaciente(paciente);
        receta.setMedico(medico);
        receta.setFechaConfeccion(rs.getDate("fecha_confeccion").toLocalDate());
        receta.setFechaRetiro(rs.getDate("fecha_retiro").toLocalDate());
        receta.setEstado(rs.getString("estado"));

        return receta;
    }

    /**
     * Mapea un ResultSet a un objeto DetalleReceta
     * @param rs ResultSet con los datos del detalle
     * @return Objeto DetalleReceta creado
     * @throws SQLException si ocurre un error al leer el ResultSet
     */
    private DetalleReceta mapResultSetToDetalle(ResultSet rs) throws SQLException {
        // Crear medicamento
        Medicamento medicamento = new Medicamento();
        medicamento.setCodigo(rs.getString("medicamento_codigo"));
        medicamento.setNombre(rs.getString("medicamento_nombre"));
        medicamento.setPresentacion(rs.getString("presentacion"));

        // Crear detalle
        DetalleReceta detalle = new DetalleReceta();
        detalle.setMedicamento(medicamento);
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setIndicaciones(rs.getString("indicaciones"));
        detalle.setDias(rs.getInt("dias"));

        return detalle;
    }
}