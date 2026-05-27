package com.hospital.emergencias.controlador;

import com.hospital.emergencias.modelo.NivelPrioridad;
import com.hospital.emergencias.modelo.Paciente;
import com.hospital.emergencias.modelo.RegistroIngreso;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Controlador de eventos de la interfaz JavaFX.
 * 
 * <p>Maneja la comunicación entre la Vista ({@code VistaEmergencias})
 * y la lógica de negocio ({@link GestorEmergencias}). Se encarga de:</p>
 * <ul>
 *   <li>Capturar y validar los datos del formulario</li>
 *   <li>Delegar las operaciones al {@link GestorEmergencias}</li>
 *   <li>Actualizar la interfaz gráfica con los resultados</li>
 * </ul>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 1.0
 */
public class EmergenciasController {

    // ─────────────────────────────────────────────
    //  Atributos
    // ─────────────────────────────────────────────

    /** Gestor de lógica de negocio. */
    private final GestorEmergencias gestor;

    // Referencias a los controles de la vista
    private final TextField txtNombre;
    private final TextField txtEdad;
    private final TextField txtDpi;
    private final TextField txtSintomas;
    private final ComboBox<String> cmbPrioridad;
    private final TableView<RegistroIngreso> tablaPacientes;
    private final ObservableList<RegistroIngreso> listaPacientes;
    private final Label lblEstado;

    // ─────────────────────────────────────────────
    //  Constructor
    // ─────────────────────────────────────────────

    /**
     * Crea el controlador con las referencias a los controles de la vista.
     * 
     * @param gestor         gestor de lógica de negocio
     * @param txtNombre      campo de texto del nombre
     * @param txtEdad        campo de texto de la edad
     * @param txtDpi         campo de texto del DPI
     * @param txtSintomas    campo de texto de los síntomas
     * @param cmbPrioridad   combo box de prioridad
     * @param tablaPacientes tabla de pacientes en espera
     * @param listaPacientes lista observable que alimenta la tabla
     * @param lblEstado      etiqueta de estado
     */
    public EmergenciasController(GestorEmergencias gestor,
                                  TextField txtNombre, TextField txtEdad,
                                  TextField txtDpi, TextField txtSintomas,
                                  ComboBox<String> cmbPrioridad,
                                  TableView<RegistroIngreso> tablaPacientes,
                                  ObservableList<RegistroIngreso> listaPacientes,
                                  Label lblEstado) {
        this.gestor = gestor;
        this.txtNombre = txtNombre;
        this.txtEdad = txtEdad;
        this.txtDpi = txtDpi;
        this.txtSintomas = txtSintomas;
        this.cmbPrioridad = cmbPrioridad;
        this.tablaPacientes = tablaPacientes;
        this.listaPacientes = listaPacientes;
        this.lblEstado = lblEstado;
    }

    // ─────────────────────────────────────────────
    //  Manejo de eventos
    // ─────────────────────────────────────────────

    /**
     * Maneja el evento del botón "Registrar Paciente".
     * 
     * <p>Valida los campos del formulario, crea un {@link Paciente} y un
     * {@link RegistroIngreso}, los registra a través del {@link GestorEmergencias}
     * y actualiza la tabla.</p>
     */
    public void onRegistrarPaciente() {
        // Validar campos vacíos
        if (txtNombre.getText().trim().isEmpty()
                || txtEdad.getText().trim().isEmpty()
                || txtDpi.getText().trim().isEmpty()
                || txtSintomas.getText().trim().isEmpty()) {
            lblEstado.setText("Error: Todos los campos son obligatorios.");
            return;
        }

        // Validar edad numérica
        int edad;
        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
        } catch (NumberFormatException e) {
            lblEstado.setText("Error: La edad debe ser un número válido.");
            return;
        }

        try {
            // Crear Paciente (datos personales)
            Paciente paciente = new Paciente(
                    txtNombre.getText().trim(),
                    edad,
                    txtDpi.getText().trim(),
                    txtSintomas.getText().trim()
            );

            // Convertir la prioridad del ComboBox al enum
            NivelPrioridad prioridad = NivelPrioridad.desdeTexto(cmbPrioridad.getValue());

            // Registrar y encolar
            RegistroIngreso registro = gestor.encolarPaciente(paciente, prioridad);

            if (registro != null) {
                lblEstado.setText("Paciente registrado: " + paciente.getNombreCompleto()
                        + " | Prioridad: " + prioridad.getEtiqueta());
                limpiarFormulario();
                actualizarTabla();
            } else {
                lblEstado.setText("Error al registrar el paciente.");
            }

        } catch (IllegalArgumentException e) {
            lblEstado.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón "Atender Siguiente".
     * 
     * <p>Desencola al paciente más urgente, lo marca como atendido
     * y actualiza la tabla e información de estado.</p>
     */
    public void onAtenderSiguiente() {
        RegistroIngreso atendido = gestor.atenderSiguiente();

        if (atendido != null) {
            lblEstado.setText("Atendido: " + atendido.getNombreCompleto()
                    + " | Prioridad: " + atendido.getPrioridadTexto());
            actualizarTabla();
        } else {
            lblEstado.setText("No hay pacientes en espera.");
        }
    }

    /**
     * Actualiza la tabla con los datos actuales de la cola de prioridad.
     * Se llama después de cada operación de registro o atención.
     */
    public void actualizarTabla() {
        listaPacientes.clear();
        List<RegistroIngreso> ordenados = gestor.obtenerRegistrosOrdenados();
        listaPacientes.addAll(ordenados);
    }

    // ─────────────────────────────────────────────
    //  Métodos privados
    // ─────────────────────────────────────────────

    /**
     * Limpia todos los campos del formulario y restaura valores por defecto.
     */
    private void limpiarFormulario() {
        txtNombre.clear();
        txtEdad.clear();
        txtDpi.clear();
        txtSintomas.clear();
        cmbPrioridad.setValue("Medium");
    }
}
