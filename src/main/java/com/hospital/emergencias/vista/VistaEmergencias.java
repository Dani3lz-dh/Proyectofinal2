package com.hospital.emergencias.vista;

import com.hospital.emergencias.controlador.EmergenciasController;
import com.hospital.emergencias.controlador.GestorEmergencias;
import com.hospital.emergencias.modelo.RegistroIngreso;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Vista principal del sistema de emergencias hospitalarias en JavaFX.
 * 
 * <p>Se encarga exclusivamente de construir la interfaz gráfica.
 * El manejo de eventos y la lógica se delegan al {@link EmergenciasController}.</p>
 * 
 * @author Sistema de Emergencias Hospitalarias
 * @version 2.0
 */
public class VistaEmergencias extends Application {

    /** Controlador de eventos de la interfaz. */
    private EmergenciasController controller;

    /** Lista observable que alimenta la tabla. */
    private ObservableList<RegistroIngreso> listaPacientes;

    // Campos del formulario
    private TextField txtNombre;
    private TextField txtEdad;
    private TextField txtDpi;
    private TextField txtSintomas;
    private ComboBox<String> cmbPrioridad;
    private TableView<RegistroIngreso> tablaPacientes;
    private Label lblEstado;

    @Override
    public void start(Stage primaryStage) {
        listaPacientes = FXCollections.observableArrayList();

        // ── Layout principal ──
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Título
        Label lblTitulo = new Label("Sistema de Emergencias Hospitalarias");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Formulario de registro
        GridPane formulario = crearFormulario();

        // Tabla de pacientes en espera
        tablaPacientes = crearTabla();

        // Etiqueta de estado
        lblEstado = new Label("Sistema listo.");
        lblEstado.setStyle("-fx-font-style: italic;");

        // ── Inicializar el controlador con las referencias a los controles ──
        GestorEmergencias gestor = new GestorEmergencias();
        controller = new EmergenciasController(
                gestor, txtNombre, txtEdad, txtDpi, txtSintomas,
                cmbPrioridad, tablaPacientes, listaPacientes, lblEstado
        );

        // Botones de acción (conectados al controlador)
        HBox botones = crearBotones();

        // Ensamblar todo
        root.getChildren().addAll(lblTitulo, formulario, botones,
                new Label("Pacientes en espera:"), tablaPacientes, lblEstado);

        // Cargar pacientes existentes en la tabla
        controller.actualizarTabla();

        // ── Escena y ventana ──
        Scene scene = new Scene(root, 700, 550);
        primaryStage.setTitle("Emergencias Hospitalarias");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Crea el formulario de registro de pacientes.
     */
    private GridPane crearFormulario() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        // Fila 0: Nombre y Edad
        grid.add(new Label("Nombre completo:"), 0, 0);
        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del paciente");
        grid.add(txtNombre, 1, 0);

        grid.add(new Label("Edad:"), 2, 0);
        txtEdad = new TextField();
        txtEdad.setPromptText("Edad");
        txtEdad.setPrefWidth(60);
        grid.add(txtEdad, 3, 0);

        // Fila 1: DPI y Prioridad
        grid.add(new Label("DPI:"), 0, 1);
        txtDpi = new TextField();
        txtDpi.setPromptText("Documento de identificación");
        grid.add(txtDpi, 1, 1);

        grid.add(new Label("Prioridad:"), 2, 1);
        cmbPrioridad = new ComboBox<>();
        cmbPrioridad.getItems().addAll("Critical", "High", "Medium", "Low");
        cmbPrioridad.setValue("Medium");
        grid.add(cmbPrioridad, 3, 1);

        // Fila 2: Síntomas
        grid.add(new Label("Síntomas:"), 0, 2);
        txtSintomas = new TextField();
        txtSintomas.setPromptText("Descripción de síntomas");
        grid.add(txtSintomas, 1, 2, 3, 1);

        return grid;
    }

    /**
     * Crea los botones de acción conectados al controlador.
     */
    private HBox crearBotones() {
        Button btnRegistrar = new Button("Registrar Paciente");
        btnRegistrar.setOnAction(e -> controller.onRegistrarPaciente());

        Button btnAtender = new Button("Atender Siguiente");
        btnAtender.setOnAction(e -> controller.onAtenderSiguiente());

        HBox hbox = new HBox(10, btnRegistrar, btnAtender);
        hbox.setPadding(new Insets(5, 0, 5, 0));
        return hbox;
    }

    /**
     * Crea la tabla con las columnas correspondientes a los datos del registro.
     */
    @SuppressWarnings("unchecked")
    private TableView<RegistroIngreso> crearTabla() {
        TableView<RegistroIngreso> tabla = new TableView<>();

        TableColumn<RegistroIngreso, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(40);

        TableColumn<RegistroIngreso, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colNombre.setPrefWidth(150);

        TableColumn<RegistroIngreso, Integer> colEdad = new TableColumn<>("Edad");
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colEdad.setPrefWidth(50);

        TableColumn<RegistroIngreso, String> colDpi = new TableColumn<>("DPI");
        colDpi.setCellValueFactory(new PropertyValueFactory<>("dpi"));
        colDpi.setPrefWidth(100);

        TableColumn<RegistroIngreso, String> colSintomas = new TableColumn<>("Síntomas");
        colSintomas.setCellValueFactory(new PropertyValueFactory<>("sintomas"));
        colSintomas.setPrefWidth(150);

        TableColumn<RegistroIngreso, String> colPrioridad = new TableColumn<>("Prioridad");
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridadTexto"));
        colPrioridad.setPrefWidth(80);

        TableColumn<RegistroIngreso, LocalDateTime> colHora = new TableColumn<>("Hora Ingreso");
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaIngreso"));
        colHora.setPrefWidth(120);
        // Formatear la fecha en la celda
        colHora.setCellFactory(column -> new TableCell<RegistroIngreso, LocalDateTime>() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(fmt));
            }
        });

        tabla.getColumns().addAll(colId, colNombre, colEdad, colDpi,
                colSintomas, colPrioridad, colHora);
        tabla.setItems(listaPacientes);
        tabla.setPlaceholder(new Label("No hay pacientes en espera."));

        return tabla;
    }
}
