package com.hospital.emergencias.persistencia;

import com.hospital.emergencias.modelo.NivelPrioridad;
import com.hospital.emergencias.modelo.Paciente;
import com.hospital.emergencias.modelo.RegistroIngreso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para los registros de ingreso de pacientes.
 * 
 * <p>Encapsula todas las operaciones de acceso a datos relacionadas
 * con la tabla {@code pacientes} en la base de datos SQLite.</p>
 * 
 * <p>Utiliza {@link PreparedStatement} en todos los métodos para
 * prevenir inyección SQL y mejorar el rendimiento.</p>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 2.0
 */
public class PacienteDAO {

    // ─────────────────────────────────────────────
    //  Consultas SQL preparadas
    // ─────────────────────────────────────────────

    /** Inserta un nuevo registro de ingreso en la tabla. */
    private static final String SQL_INSERTAR =
            "INSERT INTO pacientes (nombreCompleto, edad, dpi, sintomas, prioridad, horaIngreso, atendido) "
            + "VALUES (?, ?, ?, ?, ?, ?, 0)";

    /** Marca un registro como atendido según su ID. */
    private static final String SQL_MARCAR_ATENDIDO =
            "UPDATE pacientes SET atendido = 1 WHERE id = ?";

    /** Obtiene todos los registros que aún no han sido atendidos. */
    private static final String SQL_EN_ESPERA =
            "SELECT id, nombreCompleto, edad, dpi, sintomas, prioridad, horaIngreso, atendido "
            + "FROM pacientes WHERE atendido = 0 ORDER BY horaIngreso ASC";

    // ─────────────────────────────────────────────
    //  Conexión
    // ─────────────────────────────────────────────

    /** Referencia a la conexión Singleton de la base de datos. */
    private final Connection conexion;

    /**
     * Constructor que obtiene la conexión desde el Singleton {@link DatabaseConnection}.
     */
    public PacienteDAO() {
        this.conexion = DatabaseConnection.getInstancia().getConexion();
    }

    // ─────────────────────────────────────────────
    //  Métodos CRUD
    // ─────────────────────────────────────────────

    /**
     * Registra un nuevo ingreso de paciente en la base de datos.
     * 
     * <p>Inserta los datos del paciente y del registro de ingreso en la tabla.
     * El campo {@code atendido} se establece en 0 (false) automáticamente
     * y el {@code id} es generado por la base de datos (AUTOINCREMENT).</p>
     * 
     * @param registro objeto {@link RegistroIngreso} con los datos a registrar
     * @return {@code true} si el registro fue exitoso, {@code false} en caso contrario
     */
    public boolean registrarPaciente(RegistroIngreso registro) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERTAR)) {

            ps.setString(1, registro.getPaciente().getNombreCompleto());
            ps.setInt(2, registro.getPaciente().getEdad());
            ps.setString(3, registro.getPaciente().getDpi());
            ps.setString(4, registro.getPaciente().getSintomas());
            ps.setString(5, registro.getPrioridad().getEtiqueta());  // Guarda "Critical", "High", etc.
            ps.setString(6, registro.getHoraIngreso().toString());   // ISO-8601

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("[DAO] Paciente registrado exitosamente: "
                        + registro.getPaciente().getNombreCompleto());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error al registrar paciente: " + e.getMessage());
        }

        return false;
    }

    /**
     * Marca un registro de ingreso como atendido en la base de datos.
     * 
     * @param id identificador único del registro a marcar como atendido
     * @return {@code true} si se actualizó correctamente, {@code false} si no se encontró el ID
     */
    public boolean marcarComoAtendido(int id) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_MARCAR_ATENDIDO)) {

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("[DAO] Paciente con ID " + id + " marcado como atendido.");
                return true;
            } else {
                System.out.println("[DAO] No se encontró registro con ID " + id + ".");
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error al marcar paciente como atendido: " + e.getMessage());
        }

        return false;
    }

    /**
     * Obtiene la lista de registros de ingreso que están en espera (no atendidos).
     * 
     * <p>Consulta la tabla {@code pacientes} filtrando por {@code atendido = 0}
     * y ordena los resultados por hora de ingreso ascendente.</p>
     * 
     * @return lista de {@link RegistroIngreso} en espera; lista vacía si no hay ninguno
     */
    public List<RegistroIngreso> obtenerPacientesEnEspera() {
        List<RegistroIngreso> registrosEnEspera = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(SQL_EN_ESPERA);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RegistroIngreso registro = mapearResultSet(rs);
                registrosEnEspera.add(registro);
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error al obtener pacientes en espera: " + e.getMessage());
        }

        return registrosEnEspera;
    }

    // ─────────────────────────────────────────────
    //  Método auxiliar
    // ─────────────────────────────────────────────

    /**
     * Convierte una fila del {@link ResultSet} en un objeto {@link RegistroIngreso}.
     * 
     * <p>Crea primero un {@link Paciente} con los datos personales y luego
     * lo envuelve en un {@link RegistroIngreso} con los datos del ingreso.</p>
     * 
     * @param rs resultado de la consulta posicionado en la fila actual
     * @return objeto {@link RegistroIngreso} con los datos de la fila
     * @throws SQLException si ocurre un error al leer las columnas
     */
    private RegistroIngreso mapearResultSet(ResultSet rs) throws SQLException {
        // Datos personales → Paciente
        String nombreCompleto = rs.getString("nombreCompleto");
        int edad = rs.getInt("edad");
        String dpi = rs.getString("dpi");
        String sintomas = rs.getString("sintomas");

        Paciente paciente = new Paciente(nombreCompleto, edad, dpi, sintomas);

        // Datos del ingreso → RegistroIngreso
        int id = rs.getInt("id");
        NivelPrioridad prioridad = NivelPrioridad.desdeTexto(rs.getString("prioridad"));
        LocalDateTime horaIngreso = LocalDateTime.parse(rs.getString("horaIngreso"));
        boolean atendido = rs.getInt("atendido") == 1;

        return new RegistroIngreso(id, paciente, prioridad, horaIngreso, atendido);
    }
}
