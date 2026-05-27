package com.hospital.emergencias.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que representa un registro de ingreso a emergencias.
 * 
 * <p>Encapsula la información de la admisión de un {@link Paciente}:
 * su nivel de prioridad, hora de ingreso y estado de atención.
 * Separa los datos personales del paciente de los datos del evento
 * de ingreso hospitalario.</p>
 * 
 * <p>Un mismo paciente podría tener múltiples registros de ingreso
 * en diferentes momentos.</p>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 1.0
 */
public class RegistroIngreso {

    // ─────────────────────────────────────────────
    //  Generador de ID
    // ─────────────────────────────────────────────

    /** Generador atómico de IDs para registros de ingreso. */
    private static final AtomicInteger GENERADOR_ID = new AtomicInteger(1);

    // ─────────────────────────────────────────────
    //  Atributos
    // ─────────────────────────────────────────────

    /** Identificador único del registro de ingreso. */
    private int id;

    /** Paciente asociado a este ingreso. */
    private Paciente paciente;

    /** Nivel de prioridad asignado en triaje. */
    private NivelPrioridad prioridad;

    /** Fecha y hora en que el paciente ingresó a emergencias. */
    private LocalDateTime horaIngreso;

    /** Indica si el paciente ya fue atendido o sigue en espera. */
    private boolean atendido;

    // ─────────────────────────────────────────────
    //  Constructores
    // ─────────────────────────────────────────────

    /**
     * Constructor principal. Genera ID automáticamente, registra la hora actual
     * y establece {@code atendido = false}.
     * 
     * @param paciente  paciente que ingresa a emergencias
     * @param prioridad nivel de prioridad asignado en triaje
     */
    public RegistroIngreso(Paciente paciente, NivelPrioridad prioridad) {
        this.id = GENERADOR_ID.getAndIncrement();
        setPaciente(paciente);
        setPrioridad(prioridad);
        this.horaIngreso = LocalDateTime.now();
        this.atendido = false;
    }

    /**
     * Constructor completo para reconstruir un registro desde la base de datos.
     * 
     * @param id          identificador del registro
     * @param paciente    paciente asociado
     * @param prioridad   nivel de prioridad
     * @param horaIngreso hora de ingreso original
     * @param atendido    estado de atención
     */
    public RegistroIngreso(int id, Paciente paciente, NivelPrioridad prioridad,
                           LocalDateTime horaIngreso, boolean atendido) {
        this.id = id;
        setPaciente(paciente);
        setPrioridad(prioridad);
        setHoraIngreso(horaIngreso);
        this.atendido = atendido;
    }

    // ─────────────────────────────────────────────
    //  Getters
    // ─────────────────────────────────────────────

    /**
     * Obtiene el ID del registro de ingreso.
     * @return identificador único
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el paciente asociado a este ingreso.
     * @return objeto {@link Paciente}
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Obtiene el nivel de prioridad del ingreso.
     * @return {@link NivelPrioridad} asignada
     */
    public NivelPrioridad getPrioridad() {
        return prioridad;
    }

    /**
     * Obtiene la etiqueta de la prioridad como texto (para la TableView).
     * @return texto de la prioridad (ej: "Critical")
     */
    public String getPrioridadTexto() {
        return prioridad.getEtiqueta();
    }

    /**
     * Obtiene la hora de ingreso.
     * @return fecha y hora de ingreso
     */
    public LocalDateTime getHoraIngreso() {
        return horaIngreso;
    }

    /**
     * Indica si el paciente ya fue atendido.
     * @return {@code true} si fue atendido, {@code false} si está en espera
     */
    public boolean isAtendido() {
        return atendido;
    }

    // Getters delegados al Paciente (conveniencia para la TableView)

    /** @return nombre completo del paciente */
    public String getNombreCompleto() {
        return paciente.getNombreCompleto();
    }

    /** @return edad del paciente */
    public int getEdad() {
        return paciente.getEdad();
    }

    /** @return DPI del paciente */
    public String getDpi() {
        return paciente.getDpi();
    }

    /** @return síntomas del paciente */
    public String getSintomas() {
        return paciente.getSintomas();
    }

    // ─────────────────────────────────────────────
    //  Setters
    // ─────────────────────────────────────────────

    /**
     * Establece el ID (usado al reconstruir desde la BD).
     * @param id identificador del registro
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el paciente asociado.
     * @param paciente objeto Paciente (no puede ser nulo)
     * @throws IllegalArgumentException si el paciente es nulo
     */
    public void setPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo.");
        }
        this.paciente = paciente;
    }

    /**
     * Establece el nivel de prioridad.
     * @param prioridad nivel de prioridad (no puede ser nulo)
     * @throws IllegalArgumentException si la prioridad es nula
     */
    public void setPrioridad(NivelPrioridad prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula.");
        }
        this.prioridad = prioridad;
    }

    /**
     * Establece la hora de ingreso.
     * @param horaIngreso fecha y hora (no puede ser nula)
     * @throws IllegalArgumentException si la hora es nula
     */
    public void setHoraIngreso(LocalDateTime horaIngreso) {
        if (horaIngreso == null) {
            throw new IllegalArgumentException("La hora de ingreso no puede ser nula.");
        }
        this.horaIngreso = horaIngreso;
    }

    /**
     * Establece el estado de atención.
     * @param atendido {@code true} si fue atendido, {@code false} si está en espera
     */
    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }

    // ─────────────────────────────────────────────
    //  toString
    // ─────────────────────────────────────────────

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "RegistroIngreso {"
                + "\n  ID        = " + id
                + "\n  Paciente  = " + paciente.getNombreCompleto()
                + "\n  Prioridad = " + prioridad.getEtiqueta()
                + "\n  Ingreso   = " + horaIngreso.format(fmt)
                + "\n  Estado    = " + (atendido ? "Atendido" : "En espera")
                + "\n}";
    }
}
