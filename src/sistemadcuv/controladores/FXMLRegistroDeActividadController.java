package sistemadcuv.controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import sistemadcuv.modelo.dao.ActividadDAO;
import sistemadcuv.modelo.dao.ArchivoDAO;
import sistemadcuv.modelo.dao.CambioDAO;
import sistemadcuv.modelo.pojo.Actividad;
import sistemadcuv.modelo.pojo.Archivo;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.observador.ObservadorActividades;
import sistemadcuv.observador.ObservadorParticipantes;
import sistemadcuv.utils.Utilidades;

public class FXMLRegistroDeActividadController implements Initializable,ObservadorParticipantes {

    @FXML
    private TableView<Archivo> tvArchivo;
    @FXML
    private TableColumn colNombreArchivo;
    @FXML
    private Label lNombreProyecto;
    private ResponsableDeProyecto responsableSesion;
    @FXML
    private TextField tfNombreActividad;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFin;
    @FXML
    private Button btnDesarrollador;
    private Desarrollador desarrolladorSeleccionado;
    private Archivo documentoActividad;
    private ObservableList<Archivo> archivos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        archivos = FXCollections.observableArrayList();
        configurarTablaArchivos();
        configurarDatePicker();
    }    

    @FXML
    private void btnCrear(ActionEvent event) {
        if(!esCamposVacios()){
            guardarActividad();
        }else{
            Utilidades.mostrarAletarSimple("Campos vacios", 
                    "Por favor asegurese de llenar los campos "
                            + "correspondientes ", 
                    Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        boolean cerrarVentana = Utilidades.mostrarDialogoConfirmacion("Cancelar proceso", 
                    "¿Desea cancelar el registro? tenga en cuenta que se perderá la información");                            
            if(cerrarVentana){
                cerrarVentana();
            }
    }
    private void configurarTablaArchivos(){
        colNombreArchivo.setCellValueFactory(new PropertyValueFactory("nombreArchivo"));
    }
    @FXML
    private void btnAgregarArchivo(ActionEvent event) {
        try {
            FileChooser dialogoSeleccion = new FileChooser();
            dialogoSeleccion.setTitle("Selecciona el archivo");
            String etiquetaTipoDato = "Archivo PDF(*.pdf)";
            String extensionArchivo = "*.PDF";
            FileChooser.ExtensionFilter filtroSeleccion =
                    new FileChooser.ExtensionFilter(etiquetaTipoDato, 
                            extensionArchivo);
            dialogoSeleccion.getExtensionFilters().add(filtroSeleccion);
            Stage escenarioBase = (Stage) dpFin.getScene().getWindow();
            List<File> archivosSeleccionados = dialogoSeleccion.
                    showOpenMultipleDialog(escenarioBase);

            if (archivosSeleccionados != null) {
                for (File archivo : archivosSeleccionados) {
                    documentoActividad = new Archivo();
                    documentoActividad.setArchivo(Files.readAllBytes(archivo.toPath()));
                    documentoActividad.setNombreArchivo(archivo.getName());
                    archivos.add(documentoActividad);
                }
                tvArchivo.setItems(archivos);
            }
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("ERROR AL CARGAR", "Ha ocurrido un error"
                    + " al cargar los archivos", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Archivo archivoSeleccionado = tvArchivo.getSelectionModel().getSelectedItem();
        if (archivoSeleccionado != null) {
            archivos.remove(archivoSeleccionado);
            tvArchivo.setItems(archivos);
            Utilidades.mostrarAletarSimple("Eliminación exitosa", 
                    "Archivo eliminado correctamente", 
                    Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarAletarSimple("Error al eliminar", 
                    "Selecciona un archivo de la tabla para eliminar", 
                    Alert.AlertType.WARNING);
        }   
    }

    @FXML
    private void btnAsignarDesarrollador(ActionEvent event) {
        try{
            FXMLLoader loader = Utilidades.cargarVista(
                    "vistas/FXMLListaDeParticipantesDelProyecto.fxml");
            Parent vista = loader.load();
            Scene escena = new Scene(vista);
            FXMLListaDeParticipantesDelProyectoController controller = loader.getController();
            controller.inicializarFormulario(responsableSesion,this);      
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Registrar actividad");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    void inicializarFormulario(ResponsableDeProyecto responsableSesion, ObservadorActividades observador) {
        this.responsableSesion = responsableSesion;
        cargarInformacion();
    }

    private void cargarInformacion() {
        lNombreProyecto.setText(responsableSesion.getNombreProyecto());
    }
    
    private boolean esCamposVacios(){
        return tfNombreActividad.getText().trim().isEmpty() ||
                tfDescripcion.getText().trim().isEmpty() ||
                dpInicio.getValue() == null ||
                dpFin.getValue() == null ;
    }

    @Override
    public void desarrolladorSeleccion(Desarrollador desarrolladorSeleccionado) {
        this.desarrolladorSeleccionado = desarrolladorSeleccionado;
        this.btnDesarrollador.setText(desarrolladorSeleccionado.getNombreCompleto());
    }

    private void guardarActividad() {
        Actividad nuevaActividad = new Actividad();
        nuevaActividad.setTitulo(tfNombreActividad.getText());
        nuevaActividad.setFechaInicio(dpInicio.getValue().toString());
        nuevaActividad.setFechaFin(dpFin.getValue().toString());
        nuevaActividad.setDescripcion(tfDescripcion.getText());
        HashMap<String, Object> respuesta;
        if(desarrolladorSeleccionado != null){
            nuevaActividad.setIdDesarrollador(desarrolladorSeleccionado.
                    getIdDesarrollador());
            respuesta = ActividadDAO.registrarActividadPendiente(nuevaActividad);
        }else
            respuesta = ActividadDAO.registrarActividadSinAsignar(nuevaActividad);
        if(!(boolean)respuesta.get("error")){
            if (archivos != null && !archivos.isEmpty()){
                for (Archivo archivo : archivos) {
                    archivo.setIdActividad((int) respuesta.get("idActividad"));
                    registrarArchivo(archivo);
                }  
            }
            Utilidades.mostrarAletarSimple("Actividad registrada", 
                    (String) respuesta.get("mensaje"),
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAletarSimple("Error al guardar actividad", 
                    (String) respuesta.get("mensaje"), 
                    Alert.AlertType.ERROR);
        }
    }

    private void registrarArchivo(Archivo archivo) {
        HashMap<String, Object> respuesta = ArchivoDAO.registrarArchivoDeActividad(archivo);
            if( (boolean) respuesta.get("error")){
                Utilidades.mostrarAletarSimple(
                        "Error en el registro", 
                        (String) respuesta.get("mensaje"), 
                        Alert.AlertType.WARNING);   
            }
    }
    private void configurarDatePicker() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", 
                new Locale("es", "ES"));
        dpInicio.setConverter(new LocalDateStringConverter(formatter,null));
        dpFin.setConverter(new LocalDateStringConverter(formatter,null));
        this.dpInicio.setEditable(false);
        this.dpFin.setEditable(false);
        this.dpInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); 
                }
            }
        });
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
    private void habilitarFechaFin() {
        this.dpFin.setDisable(false);
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

    private void cerrarVentana() {
        Stage escenario = (Stage) dpInicio.getScene().getWindow();
        escenario.close();
    }
}
