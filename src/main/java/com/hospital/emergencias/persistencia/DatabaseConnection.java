package com.hospital.emergencias.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase de conexión a la base de datos SQLite para el sistema de emergencias.
 * 
 * <p>Implementa el patrón <b>Singleton</b> para garantizar que exista
 * una única instancia de conexión a la base de datos durante toda la
 * ejecución de la aplicación.</p>
 * 
 * <p>Al obtener la conexión por primera vez, se crea automáticamente
 * la tabla {@code pacientes} si no existe.</p>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 1.0
 */
public class DatabaseConnection {

    // ─────────────────────────────────────────────
    //  Constantes
    // ─────────────────────────────────────────────

    /** Ruta de la base de datos SQLite (se crea en la raíz del proyecto). */
    private static final String URL = "jdbc:sqlite:emergencias.db";

    /** Sentencia DDL para crear la tabla pacientes si no existe. */
    private static final String CREAR_TABLA_PACIENTES =
            "CREATE TABLE IF NOT EXISTS pacientes ("
            + "  id              INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "  nombreCompleto  TEXT    NOT NULL, "
            + "  edad            INTEGER NOT NULL, "
            + "  dpi             TEXT    NOT NULL, "
            + "  sintomas        TEXT    NOT NULL, "
            + "  prioridad       TEXT    NOT NULL CHECK(prioridad IN ('Critical','High','Medium','Low')), "
            + "  horaIngreso     TEXT    NOT NULL, "
            + "  atendido        INTEGER NOT NULL DEFAULT 0"
            + ")";

    // ─────────────────────────────────────────────
    //  Singleton
    // ─────────────────────────────────────────────

    /** Instancia única de la clase (patrón Singleton). */
    private static DatabaseConnection instancia;

    /** Conexión activa a la base de datos. */
    private Connection conexion;

    // ─────────────────────────────────────────────
    //  Constructor privado
    // ─────────────────────────────────────────────

    /**
     * Constructor privado que establece la conexión con SQLite
     * y crea la tabla de pacientes si no existe.
     */
    private DatabaseConnection() {
        try {
            conexion = DriverManager.getConnection(URL);
            System.out.println("[DB] Conexión a SQLite establecida correctamente.");
            crearTabla();
        } catch (SQLException e) {
            System.err.println("[DB] Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  Método Singleton
    // ─────────────────────────────────────────────

    /**
     * Obtiene la instancia única de {@code DatabaseConnection}.
     * 
     * <p>Si la instancia aún no existe o la conexión se cerró,
     * se crea una nueva automáticamente.</p>
     * 
     * @return instancia singleton de {@code DatabaseConnection}
     */
    public static synchronized DatabaseConnection getInstancia() {
        try {
            if (instancia == null || instancia.conexion == null || instancia.conexion.isClosed()) {
                instancia = new DatabaseConnection();
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error al verificar el estado de la conexión: " + e.getMessage());
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    // ─────────────────────────────────────────────
    //  Métodos públicos
    // ─────────────────────────────────────────────

    /**
     * Devuelve la conexión activa a la base de datos.
     * 
     * @return objeto {@link Connection} de JDBC
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos de forma segura.
     * 
     * <p>Debe llamarse al finalizar la aplicación para liberar recursos.</p>
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[DB] Conexión a SQLite cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error al cerrar la conexión: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  Métodos privados
    // ─────────────────────────────────────────────

    /**
     * Crea la tabla {@code pacientes} en la base de datos si no existe.
     * 
     * <p>Se ejecuta automáticamente al establecer la conexión por primera vez.
     * Las columnas corresponden a los atributos del modelo {@code Paciente}:</p>
     * <ul>
     *   <li>{@code id} — INTEGER PRIMARY KEY AUTOINCREMENT</li>
     *   <li>{@code nombreCompleto} — TEXT NOT NULL</li>
     *   <li>{@code edad} — INTEGER NOT NULL</li>
     *   <li>{@code dpi} — TEXT NOT NULL</li>
     *   <li>{@code sintomas} — TEXT NOT NULL</li>
     *   <li>{@code prioridad} — TEXT NOT NULL con CHECK (Critical, High, Medium, Low)</li>
     *   <li>{@code horaIngreso} — TEXT NOT NULL (almacenado como ISO-8601)</li>
     *   <li>{@code atendido} — INTEGER NOT NULL DEFAULT 0 (0 = en espera, 1 = atendido)</li>
     * </ul>
     */
    private void crearTabla() {
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(CREAR_TABLA_PACIENTES);
            System.out.println("[DB] Tabla 'pacientes' verificada/creada exitosamente.");
        } catch (SQLException e) {
            System.err.println("[DB] Error al crear la tabla 'pacientes': " + e.getMessage());
        }
    }
}
