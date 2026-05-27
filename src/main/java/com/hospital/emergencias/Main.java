package com.hospital.emergencias;

import com.hospital.emergencias.vista.VistaEmergencias;
import javafx.application.Application;

/**
 * Clase principal de entrada al sistema de emergencias hospitalarias.
 * 
 * <p>Lanza la interfaz gráfica JavaFX a través de {@link VistaEmergencias}.
 * Se utiliza una clase separada para evitar problemas de módulos de JavaFX.</p>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 1.0
 */
public class Main {

    /**
     * Punto de entrada de la aplicación.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        Application.launch(VistaEmergencias.class, args);
    }
}
