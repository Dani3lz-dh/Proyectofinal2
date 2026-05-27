package com.hospital.emergencias.modelo;

/**
 * Clase modelo que representa los datos personales de un Paciente.
 * 
 * <p>Aplica los principios de encapsulación: todos los atributos son privados
 * y se acceden únicamente a través de getters y setters con validación.</p>
 * 
 * <p>Esta clase contiene exclusivamente la información personal del paciente.
 * Los datos del ingreso a emergencias (prioridad, hora, estado) se manejan
 * en la clase {@link RegistroIngreso}.</p>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 2.0
 */
public class Paciente {

    // ─────────────────────────────────────────────
    //  Atributos (encapsulados como privados)
    // ─────────────────────────────────────────────

    /** Nombre completo del paciente. */
    private String nombreCompleto;

    /** Edad del paciente en años. */
    private int edad;

    /** Documento Personal de Identificación (DPI) del paciente. */
    private String dpi;

    /** Descripción de los síntomas que presenta el paciente al ingresar. */
    private String sintomas;

    // ─────────────────────────────────────────────
    //  Constructores
    // ─────────────────────────────────────────────

    /**
     * Constructor completo.
     *
     * @param nombreCompleto nombre completo del paciente
     * @param edad           edad del paciente (debe ser mayor a 0)
     * @param dpi            documento personal de identificación
     * @param sintomas       descripción de los síntomas
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public Paciente(String nombreCompleto, int edad, String dpi, String sintomas) {
        setNombreCompleto(nombreCompleto);
        setEdad(edad);
        setDpi(dpi);
        setSintomas(sintomas);
    }

    // ─────────────────────────────────────────────
    //  Getters
    // ─────────────────────────────────────────────

    /**
     * Obtiene el nombre completo del paciente.
     * @return nombre completo
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Obtiene la edad del paciente.
     * @return edad en años
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Obtiene el DPI del paciente.
     * @return documento personal de identificación
     */
    public String getDpi() {
        return dpi;
    }

    /**
     * Obtiene la descripción de los síntomas del paciente.
     * @return síntomas reportados
     */
    public String getSintomas() {
        return sintomas;
    }

    // ─────────────────────────────────────────────
    //  Setters (con validación)
    // ─────────────────────────────────────────────

    /**
     * Establece el nombre completo del paciente.
     * @param nombreCompleto nombre completo (no puede ser nulo ni vacío)
     * @throws IllegalArgumentException si el nombre es nulo o está vacío
     */
    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo no puede ser nulo ni vacío.");
        }
        this.nombreCompleto = nombreCompleto.trim();
    }

    /**
     * Establece la edad del paciente.
     * @param edad edad en años (debe ser mayor a 0 y menor o igual a 150)
     * @throws IllegalArgumentException si la edad está fuera del rango válido
     */
    public void setEdad(int edad) {
        if (edad <= 0 || edad > 150) {
            throw new IllegalArgumentException(
                    "La edad debe ser un valor entre 1 y 150. Valor recibido: " + edad);
        }
        this.edad = edad;
    }

    /**
     * Establece el DPI del paciente.
     * @param dpi documento personal de identificación (no puede ser nulo ni vacío)
     * @throws IllegalArgumentException si el DPI es nulo o está vacío
     */
    public void setDpi(String dpi) {
        if (dpi == null || dpi.trim().isEmpty()) {
            throw new IllegalArgumentException("El DPI no puede ser nulo ni vacío.");
        }
        this.dpi = dpi.trim();
    }

    /**
     * Establece la descripción de los síntomas del paciente.
     * @param sintomas descripción de síntomas (no puede ser nulo ni vacío)
     * @throws IllegalArgumentException si los síntomas son nulos o están vacíos
     */
    public void setSintomas(String sintomas) {
        if (sintomas == null || sintomas.trim().isEmpty()) {
            throw new IllegalArgumentException("Los síntomas no pueden ser nulos ni vacíos.");
        }
        this.sintomas = sintomas.trim();
    }

    // ─────────────────────────────────────────────
    //  toString
    // ─────────────────────────────────────────────

    /**
     * Representación en texto del paciente.
     * @return cadena con la información personal del paciente
     */
    @Override
    public String toString() {
        return "Paciente {"
                + " Nombre=" + nombreCompleto
                + ", Edad=" + edad
                + ", DPI=" + dpi
                + ", Síntomas=" + sintomas
                + " }";
    }
}
