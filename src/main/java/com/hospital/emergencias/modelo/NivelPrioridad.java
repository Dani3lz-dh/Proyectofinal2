package com.hospital.emergencias.modelo;

/**
 * Enum que representa los niveles de prioridad del sistema de triaje hospitalario.
 * 
 * <p>Cada nivel tiene un valor numérico interno para ordenamiento en la cola
 * de prioridad (menor valor = mayor urgencia) y una etiqueta legible
 * para mostrar en la interfaz gráfica.</p>
 * 
 * <p>Orden de atención: CRITICAL → HIGH → MEDIUM → LOW</p>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 1.0
 */
public enum NivelPrioridad {

    /** Prioridad crítica — atención inmediata. */
    CRITICAL(1, "Critical"),

    /** Prioridad alta — atención urgente. */
    HIGH(2, "High"),

    /** Prioridad media — atención moderada. */
    MEDIUM(3, "Medium"),

    /** Prioridad baja — atención no urgente. */
    LOW(4, "Low");

    // ─────────────────────────────────────────────
    //  Atributos
    // ─────────────────────────────────────────────

    /** Valor numérico para comparación (menor = más urgente). */
    private final int valor;

    /** Etiqueta legible para la interfaz gráfica. */
    private final String etiqueta;

    // ─────────────────────────────────────────────
    //  Constructor
    // ─────────────────────────────────────────────

    /**
     * Constructor del enum.
     * 
     * @param valor    valor numérico de la prioridad
     * @param etiqueta texto legible para mostrar en la UI
     */
    NivelPrioridad(int valor, String etiqueta) {
        this.valor = valor;
        this.etiqueta = etiqueta;
    }

    // ─────────────────────────────────────────────
    //  Getters
    // ─────────────────────────────────────────────

    /**
     * Obtiene el valor numérico de la prioridad.
     * 
     * @return valor numérico (1 = más urgente, 4 = menos urgente)
     */
    public int getValor() {
        return valor;
    }

    /**
     * Obtiene la etiqueta legible de la prioridad.
     * 
     * @return etiqueta para la interfaz (ej: "Critical", "High")
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    // ─────────────────────────────────────────────
    //  Métodos utilitarios
    // ─────────────────────────────────────────────

    /**
     * Convierte un texto a su correspondiente {@code NivelPrioridad}.
     * 
     * <p>Busca por la etiqueta del enum (ej: "Critical", "High").
     * No es sensible a mayúsculas/minúsculas.</p>
     * 
     * @param texto texto a convertir
     * @return el {@code NivelPrioridad} correspondiente
     * @throws IllegalArgumentException si el texto no coincide con ninguna prioridad
     */
    public static NivelPrioridad desdeTexto(String texto) {
        for (NivelPrioridad nivel : values()) {
            if (nivel.etiqueta.equalsIgnoreCase(texto)) {
                return nivel;
            }
        }
        throw new IllegalArgumentException(
                "Prioridad no válida: '" + texto + "'. "
                + "Valores permitidos: Critical, High, Medium, Low");
    }

    /**
     * Representación en texto del nivel de prioridad.
     * 
     * @return la etiqueta legible
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
