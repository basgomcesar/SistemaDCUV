package sistemadcuv.controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import sistemadcuv.modelo.dao.ArchivoDAO;
import sistemadcuv.modelo.dao.CambioDAO;
import sistemadcuv.modelo.dao.TipoArtefactoDAO;
import sistemadcuv.modelo.pojo.Archivo;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.modelo.pojo.TipoArtefacto;
import sistemadcuv.observador.ObservadorCambios;
import sistemadcuv.utils.Utilidades;


public class FXMLRegistroDeCambioController implements Initializable {

    @FXML
    private Button btnCrear;
    @FXML
    private TextField tfNombreCambio;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private TextField tfRazon;
    @FXML
    private TextField tfImpacto;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lNombreProyecto;
    @FXML
    private Label lbDesarrollador;
    @FXML
    private TextField tfEsfuerzo;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFin;
    @FXML
    private TableView<Archivo> tvArchivo;
    @FXML
    private TableColumn colNombreArchivo;
    @FXML
    private Button bAgregar;
    @FXML
    private Button btnEliminar;
    private Desarrollador desarrolladorSesion;
    @FXML
    private ComboBox<TipoArtefacto> cbTipoCambio;
    private ObservableList<TipoArtefacto> tiposDeCambios;
    private Archivo documentoCambio;
    private ObservableList<Archivo> archivos;
    private ObservadorCambios observadorCambios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tiposDeCambios = FXCollections.observableArrayList();
        archivos = FXCollections.observableArrayList();
        configurarCampoEsfuerzo();
        cargarInformacionTiposArtefactos();
        configurarDatePicker();
        configurarTablaArchivos();
    }    
    private void configurarTablaArchivos(){
        colNombreArchivo.setCellValueFactory(new PropertyValueFactory("nombreArchivo"));
    }
    @FXML
    private void clicCrear(ActionEvent event) {
        if(!esCamposVacios()){
            guardarCambio();
        }else{
            Utilidades.mostrarAletarSimple("Campos vacios",
                    "Por favor asegurese de llenar "
                            + "los campos correspondientes", 
                    Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean cerrarVentana = Utilidades.mostrarDialogoConfirmacion("Cancelar proceso", 
                    "¿Desea cancelar el registro? tenga en cuenta que se perderá la información");                            
            if(cerrarVentana){
                cerrarVentana();
            }
    }


    @FXML
    private void clicEliminar(ActionEvent event) {
        Archivo archivoSeleccionado = tvArchivo.getSelectionModel().getSelectedItem();
        if (archivoSeleccionado != null) {
            archivos.remove(archivoSeleccionado);
            tvArchivo.setItems(archivos);
            Utilidades.mostrarAletarSimple("Eliminación exitosa", "Archivo eliminado correctamente", Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarAletarSimple("Error al eliminar", "Selecciona un archivo de la tabla para eliminar", Alert.AlertType.WARNING);
        }   
    }

    void inicializarFormulario(Desarrollador desarrolladorSesion,ObservadorCambios observador) {
        this.desarrolladorSesion = desarrolladorSesion;
        this.observadorCambios = observador;
        lNombreProyecto.setText(desarrolladorSesion.getNombreProyecto());
        lbDesarrollador.setText(desarrolladorSesion.getNombreCompleto());
        
    }
    private boolean esCamposVacios(){
        return tfNombreCambio.getText().trim().isEmpty() ||
                tfDescripcion.getText().trim().isEmpty() ||
                tfEsfuerzo.getText().trim().isEmpty() ||
                dpInicio.getValue() == null ||
                dpFin.getValue() == null ||
                tfRazon.getText().trim().isEmpty() ||
                tfImpacto.getText().trim().isEmpty() ||
                cbTipoCambio.getSelectionModel().getSelectedItem() ==null;
    }

    private void configurarCampoEsfuerzo() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    } else {
                        return null;
                    }
                });
        tfEsfuerzo.setTextFormatter(textFormatter);
    }

    private void cargarInformacionTiposArtefactos() {
        HashMap<String, Object> resultado = TipoArtefactoDAO.
                obtenerTiposDeArtefactos();
        if(!(boolean) resultado.get("error")){
            ArrayList<TipoArtefacto> tiposCambio = (ArrayList<TipoArtefacto>) resultado.
                    get("tiposArtefactos");
            tiposDeCambios.addAll(tiposCambio);
            cbTipoCambio.setItems(tiposDeCambios);
        }else{
            Utilidades.mostrarAletarSimple("Error",
                    "Error al cargar los tipos de cambios", 
                    Alert.AlertType.ERROR);
        }
    }

    private void habilitarFechaFin() {
        this.dpFin.setDisable(false);
        this.dpFin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(dpInicio.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Estilo para las fechas deshabilitadas
                }
            }
        });
    }
    @FXML
    private void btnAgregarArchivo(ActionEvent event) {
        try {
            FileChooser dialogoSeleccion = new FileChooser();
            dialogoSeleccion.setTitle("Selecciona el archivo");
            String etiquetaTipoDato = "Archivo PDF(*.pdf)";
            String extensionArchivo = "*.PDF";
            FileChooser.ExtensionFilter filtroSeleccion =
                    new FileChooser.ExtensionFilter(etiquetaTipoDato, extensionArchivo);
            dialogoSeleccion.getExtensionFilters().add(filtroSeleccion);
            Stage escenarioBase = (Stage) btnCancelar.getScene().getWindow();
            List<File> archivosSeleccionados = dialogoSeleccion.showOpenMultipleDialog(escenarioBase);

            if (archivosSeleccionados != null) {
                for (File archivo : archivosSeleccionados) {
                    documentoCambio = new Archivo();
                    documentoCambio.setArchivo(Files.readAllBytes(archivo.toPath()));
                    documentoCambio.setNombreArchivo(archivo.getName());
                    archivos.add(documentoCambio);
                }
                tvArchivo.setItems(archivos);
            }
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("ERROR AL CARGAR", "Ha ocurrido un error"
                    + " al cargar los archivos", Alert.AlertType.WARNING);
        }
    }
    private void configurarDatePicker() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("es", "ES"));
        dpInicio.setConverter(new LocalDateStringConverter(formatter,null));
        dpFin.setConverter(new LocalDateStringConverter(formatter,null));
        this.dpInicio.setEditable(false);
        this.dpFin.setDisable(true);
    }

    @FXML
    private void clicDpInicio(ActionEvent event) {
        habilitarFechaFin();
        if(dpFin.getValue()!=null){
            if(dpFin.getValue().isBefore(dpInicio.getValue()))
                    dpFin.setValue(dpInicio.getValue());
            this.dpFin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date.isBefore(dpInicio.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                });
        }
    }

    private void guardarCambio() {
        Cambio nuevoCambio = new Cambio();
        nuevoCambio.setNombre(tfNombreCambio.getText());
        nuevoCambio.setDescripcion(tfDescripcion.getText());
        nuevoCambio.setEsfuerzo(Integer.parseInt(tfEsfuerzo.getText()));
        nuevoCambio.setFechaInicio(dpInicio.getValue().toString());
        nuevoCambio.setFechaFin(dpFin.getValue().toString());
        nuevoCambio.setImpacto(tfImpacto.getText());
        nuevoCambio.setRazonCambio(tfRazon.getText());
        nuevoCambio.setIdDesarrollador(this.desarrolladorSesion.getIdDesarrollador());
        nuevoCambio.setIdTipoCambio(cbTipoCambio.getSelectionModel().
                getSelectedItem().
                getIdArtefacto());
        HashMap<String, Object> respuesta = CambioDAO.registrarCambioBajoImpacto(nuevoCambio);
        if(!(boolean)respuesta.get("error")){
            if (archivos != null && !archivos.isEmpty()){
                for (Archivo archivo : archivos) {
                    archivo.setIdCambio((int) respuesta.get("idCambio"));
                    registrarArchivo(archivo);
                }  
            }
            this.observadorCambios.operacionExitosa("Operacion exitosa", 
            nuevoCambio.getNombre() );
            Utilidades.mostrarAletarSimple("Registro exitoso", 
                    "El cambio se ha registrado ", 
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAletarSimple("Error", 
                    "Error al intentar guardar el cambio", 
                    Alert.AlertType.ERROR);
        }
    } 

    private void registrarArchivo(Archivo archivo) {
        HashMap<String, Object> respuesta = ArchivoDAO.registrarArchivoDeCambio(archivo);
            if( (boolean) respuesta.get("error")){
                Utilidades.mostrarAletarSimple("Error en el registro", 
                        (String) respuesta.get("mensaje"), Alert.AlertType.WARNING);   
            }
    }

    private void cerrarVentana() {
        Stage escenario = (Stage) btnCancelar.getScene().getWindow();
        escenario.close();
    }
    
}
