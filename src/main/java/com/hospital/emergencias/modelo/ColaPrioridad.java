package com.hospital.emergencias.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Clase que encapsula la cola de prioridad del sistema de triaje.
 * 
 * <p>Utiliza una {@link PriorityQueue} de Java con un {@link Comparator}
 * personalizado que ordena los {@link RegistroIngreso} según:</p>
 * <ol>
 *   <li><b>Prioridad médica</b>: Critical → High → Medium → Low
 *       (usando el valor numérico de {@link NivelPrioridad})</li>
 *   <li><b>Hora de ingreso</b> (desempate): el que llegó primero se atiende primero</li>
 * </ol>
 * 
 * <p>Esta clase pertenece a la capa de <b>Modelo</b> del patrón MVC,
 * ya que contiene la lógica interna de la estructura de datos.</p>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 1.0
 */
public class ColaPrioridad {

    // ─────────────────────────────────────────────
    //  Comparador personalizado
    // ─────────────────────────────────────────────

    /**
     * Comparador que define el orden de atención de los registros.
     * 
     * <p><b>Regla 1:</b> Comparar por valor numérico de prioridad (menor = más urgente).
     * <br><b>Regla 2:</b> Si tienen la misma prioridad, desempatar por hora de ingreso
     * (la más antigua primero).</p>
     */
    private static final Comparator<RegistroIngreso> COMPARADOR_TRIAJE = (r1, r2) -> {
        // Regla 1: comparar por prioridad (menor valor = más urgente)
        int cmpPrioridad = Integer.compare(
                r1.getPrioridad().getValor(),
                r2.getPrioridad().getValor()
        );

        if (cmpPrioridad != 0) {
            return cmpPrioridad;
        }

        // Regla 2: misma prioridad → desempate por hora de ingreso (más antigua primero)
        return r1.getHoraIngreso().compareTo(r2.getHoraIngreso());
    };

    // ─────────────────────────────────────────────
    //  Atributo
    // ─────────────────────────────────────────────

    /** Cola de prioridad interna. */
    private final PriorityQueue<RegistroIngreso> cola;

    // ─────────────────────────────────────────────
    //  Constructor
    // ─────────────────────────────────────────────

    /**
     * Crea una cola de prioridad vacía con el comparador de triaje.
     */
    public ColaPrioridad() {
        this.cola = new PriorityQueue<>(COMPARADOR_TRIAJE);
    }

    // ─────────────────────────────────────────────
    //  Métodos principales
    // ─────────────────────────────────────────────

    /**
     * Agrega un registro de ingreso a la cola.
     * 
     * @param registro el {@link RegistroIngreso} a encolar
     */
    public void encolar(RegistroIngreso registro) {
        cola.offer(registro);
    }

    /**
     * Extrae y devuelve el registro con mayor prioridad (el más urgente).
     * 
     * @return el {@link RegistroIngreso} más urgente, o {@code null} si la cola está vacía
     */
    public RegistroIngreso desencolar() {
        return cola.poll();
    }

    /**
     * Consulta el registro con mayor prioridad sin sacarlo de la cola.
     * 
     * @return el {@link RegistroIngreso} más urgente, o {@code null} si la cola está vacía
     */
    public RegistroIngreso verSiguiente() {
        return cola.peek();
    }

    // ─────────────────────────────────────────────
    //  Métodos de consulta
    // ─────────────────────────────────────────────

    /**
     * Devuelve una lista ordenada de todos los registros en espera.
     * 
     * <p>La lista se ordena usando el mismo comparador de triaje.
     * Útil para mostrar los pacientes en la tabla de la interfaz gráfica.</p>
     * 
     * @return lista ordenada de {@link RegistroIngreso} en espera
     */
    public List<RegistroIngreso> obtenerTodosOrdenados() {
        List<RegistroIngreso> lista = new ArrayList<>(cola);
        lista.sort(COMPARADOR_TRIAJE);
        return lista;
    }

    /**
     * Devuelve la cantidad de registros en la cola.
     * 
     * @return número de pacientes en espera
     */
    public int cantidadEnEspera() {
        return cola.size();
    }

    /**
     * Verifica si la cola está vacía.
     * 
     * @return {@code true} si no hay registros en espera
     */
    public boolean estaVacia() {
        return cola.isEmpty();
    }
}
