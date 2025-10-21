package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

/**
 * Utilidad para manejar conexiones a la base de datos MySQL
 * Implementa el patrón Singleton para reutilizar conexiones
 */
public class DatabaseConnection {

    // Configuración de la base de datos
    private static final String DB_URL = "";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    // Pool de conexiones simple (ThreadLocal para múltiples hilos)
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    /**
     * Obtiene una conexión a la base de datos
     * Si ya existe una conexión activa en el hilo actual, la reutiliza
     * @return Connection objeto de conexión a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = connectionHolder.get();

        // Si no hay conexión o está cerrada, crear una nueva
        if (conn == null || conn.isClosed()) {
            try {
                // Cargar el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Crear nueva conexión
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Configurar la conexión
                conn.setAutoCommit(true); // Auto-commit por defecto

                // Guardar en ThreadLocal
                connectionHolder.set(conn);

                System.out.println("✅ Conexión establecida a la base de datos");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL no encontrado: " + e.getMessage(), e);
            }
        }

        return conn;
    }

    /**
     * Cierra la conexión actual del hilo
     */
    public static void closeConnection() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("🔌 Conexión cerrada");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            } finally {
                connectionHolder.remove();
            }
        }
    }

    /**
     * Verifica si la conexión a la base de datos está disponible
     * @return true si puede conectarse, false en caso contrario
     */
    public static boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Error al probar conexión: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inicia una transacción deshabilitando el auto-commit
     * @throws SQLException si ocurre un error
     */
    public static void beginTransaction() throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
    }

    /**
     * Confirma (commit) una transacción
     * @throws SQLException si ocurre un error
     */
    public static void commitTransaction() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn != null && !conn.getAutoCommit()) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }

    /**
     * Revierte (rollback) una transacción
     * @throws SQLException si ocurre un error
     */
    public static void rollbackTransaction() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                if (!conn.getAutoCommit()) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error al hacer rollback: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene información sobre la conexión actual
     * @return String con información de la conexión
     */
    public static String getConnectionInfo() {
        try {
            Connection conn = getConnection();
            return String.format(
                    "Database: %s | Usuario: %s | AutoCommit: %s | Cerrada: %s",
                    conn.getCatalog(),
                    conn.getMetaData().getUserName(),
                    conn.getAutoCommit(),
                    conn.isClosed()
            );
        } catch (SQLException e) {
            return "Error al obtener información: " + e.getMessage();
        }
    }
}