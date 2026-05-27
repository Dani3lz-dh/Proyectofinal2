package com.hospital.emergencias.controlador;

import com.hospital.emergencias.modelo.ColaPrioridad;
import com.hospital.emergencias.modelo.NivelPrioridad;
import com.hospital.emergencias.modelo.Paciente;
import com.hospital.emergencias.modelo.RegistroIngreso;
import com.hospital.emergencias.persistencia.PacienteDAO;

import java.util.List;

/**
 * Controlador principal de la lógica de negocio del sistema de emergencias.
 * 
 * <p>Actúa como puente entre la capa de persistencia ({@link PacienteDAO})
 * y la lógica del modelo ({@link ColaPrioridad}). Se encarga de:</p>
 * <ul>
 *   <li>Registrar pacientes en la BD y encolarlos</li>
 *   <li>Atender al siguiente paciente más urgente</li>
 *   <li>Cargar el estado previo al iniciar la aplicación</li>
 * </ul>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 2.0
 */
public class GestorEmergencias {

    // ─────────────────────────────────────────────
    //  Atributos
    // ─────────────────────────────────────────────

    /** Cola de prioridad del modelo (lógica de triaje). */
    private final ColaPrioridad colaPrioridad;

    /** DAO para las operaciones de persistencia en la base de datos. */
    private final PacienteDAO pacienteDAO;

    // ─────────────────────────────────────────────
    //  Constructor
    // ─────────────────────────────────────────────

    /**
     * Crea el gestor de emergencias.
     * 
     * <p>Al iniciar, carga automáticamente los pacientes en espera
     * desde la base de datos para restaurar el estado previo.</p>
     */
    public GestorEmergencias() {
        this.colaPrioridad = new ColaPrioridad();
        this.pacienteDAO = new PacienteDAO();
        cargarPacientesEnEspera();
    }

    // ─────────────────────────────────────────────
    //  Métodos principales
    // ─────────────────────────────────────────────

    /**
     * Registra y encola un nuevo paciente en el sistema de emergencias.
     * 
     * <p>Crea un {@link RegistroIngreso} con los datos del paciente y la prioridad,
     * lo persiste en la base de datos y lo agrega a la cola de prioridad.</p>
     * 
     * @param paciente  datos personales del paciente
     * @param prioridad nivel de prioridad asignado en triaje
     * @return el {@link RegistroIngreso} creado, o {@code null} si falló el registro
     */
    public RegistroIngreso encolarPaciente(Paciente paciente, NivelPrioridad prioridad) {
        RegistroIngreso registro = new RegistroIngreso(paciente, prioridad);

        boolean registrado = pacienteDAO.registrarPaciente(registro);

        if (registrado) {
            colaPrioridad.encolar(registro);
            System.out.println("[GESTOR] Paciente encolado: " + paciente.getNombreCompleto()
                    + " | Prioridad: " + prioridad.getEtiqueta());
            return registro;
        }

        System.err.println("[GESTOR] No se pudo encolar al paciente: "
                + paciente.getNombreCompleto());
        return null;
    }

    /**
     * Desencola y atiende al siguiente paciente con mayor prioridad.
     * 
     * <p>Extrae el registro más urgente de la cola, lo marca como atendido
     * en la base de datos y actualiza su estado en el objeto.</p>
     * 
     * @return el {@link RegistroIngreso} atendido, o {@code null} si la cola está vacía
     */
    public RegistroIngreso atenderSiguiente() {
        RegistroIngreso registro = colaPrioridad.desencolar();

        if (registro == null) {
            System.out.println("[GESTOR] No hay pacientes en espera.");
            return null;
        }

        // Marcar como atendido en la base de datos
        pacienteDAO.marcarComoAtendido(registro.getId());
        registro.setAtendido(true);

        System.out.println("[GESTOR] Paciente atendido: " + registro.getNombreCompleto()
                + " | Prioridad: " + registro.getPrioridadTexto());

        return registro;
    }

    // ─────────────────────────────────────────────
    //  Métodos de consulta
    // ─────────────────────────────────────────────

    /**
     * Obtiene la lista ordenada de registros en espera para la tabla.
     * 
     * @return lista de {@link RegistroIngreso} ordenados por prioridad y hora
     */
    public List<RegistroIngreso> obtenerRegistrosOrdenados() {
        return colaPrioridad.obtenerTodosOrdenados();
    }

    /**
     * Obtiene el siguiente registro a atender sin sacarlo de la cola.
     * 
     * @return el {@link RegistroIngreso} más urgente, o {@code null} si está vacía
     */
    public RegistroIngreso verSiguiente() {
        return colaPrioridad.verSiguiente();
    }

    /**
     * Devuelve la cantidad de pacientes en espera.
     * @return número de pacientes en la cola
     */
    public int cantidadEnEspera() {
        return colaPrioridad.cantidadEnEspera();
    }

    /**
     * Verifica si la cola está vacía.
     * @return {@code true} si no hay pacientes en espera
     */
    public boolean colaVacia() {
        return colaPrioridad.estaVacia();
    }

    // ─────────────────────────────────────────────
    //  Métodos privados
    // ─────────────────────────────────────────────

    /**
     * Carga los pacientes en espera desde la BD a la cola de prioridad.
     * Se ejecuta al iniciar para restaurar el estado previo.
     */
    private void cargarPacientesEnEspera() {
        List<RegistroIngreso> enEspera = pacienteDAO.obtenerPacientesEnEspera();

        for (RegistroIngreso registro : enEspera) {
            colaPrioridad.encolar(registro);
        }

        System.out.println("[GESTOR] Se cargaron " + enEspera.size()
                + " paciente(s) en espera desde la base de datos.");
    }
}
