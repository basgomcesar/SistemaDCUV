package sistemadcuv.controladores;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.ArchivoDAO;
import sistemadcuv.modelo.dao.CambioDAO;
import sistemadcuv.modelo.dao.TipoArtefactoDAO;
import sistemadcuv.modelo.pojo.Archivo;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.modelo.pojo.SolicitudDeCambio;
import sistemadcuv.modelo.pojo.TipoArtefacto;
import sistemadcuv.utils.Utilidades;


public class FXMLRegistroDeCambioSolicitudController implements Initializable {

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
    private Label lbNombreProyecto;
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
    private Button btnAgregar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ComboBox<TipoArtefacto> cbTipoCambio;
    @FXML
    private Button bDescargar;
    @FXML
    private Button bGuardar;
    @FXML
    private Button bCerrar;
    private ResponsableDeProyecto responsableSesion;
    private SolicitudDeCambio solicitudAceptada;
    private ObservableList<Archivo> archivos;
    private ObservableList<TipoArtefacto> tiposDeCambios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tiposDeCambios = FXCollections.observableArrayList();
        configurarTablaArchivos();
        cargarInformacionTiposArtefactos();
    }    

    @FXML
    private void clicCrear(ActionEvent event) {
         if(!esCamposVacios()){
            guardarCambio();
        }else{
            Utilidades.mostrarAletarSimple(
                    "Campos vacios",
                    "Por favor asegurese de llenar "+
                    "los campos correspondientes", 
                    Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
    }

    @FXML
    private void clicDpInicio(ActionEvent event) {
    }

    @FXML
    private void clicAgregarArchivo(ActionEvent event) {
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
    }

    @FXML
    private void btnDescargarArchivos(ActionEvent event) {
    }

    @FXML
    private void btnGuardarCambios(ActionEvent event) {
    }

    @FXML
    private void btnCerrarVentana(ActionEvent event) {
    }

    void inicializarFormulario(ResponsableDeProyecto responsableSesion, SolicitudDeCambio solicitudAceptada,ObservableList<Archivo> archivos) {
        this.responsableSesion = responsableSesion;
        this.solicitudAceptada = solicitudAceptada;
        this.archivos = archivos;
        cargarInformacion();
        cargarArchivos();
    }

    private void cargarInformacion() {
        tfNombreCambio.setText(solicitudAceptada.getNombre());
        tfDescripcion.setText(solicitudAceptada.getDescripcion());
        tfImpacto.setText(solicitudAceptada.getImpacto());
        tfRazon.setText(solicitudAceptada.getRazon());
        lbDesarrollador.setText(solicitudAceptada.getNombreDesarrollador());
        lbNombreProyecto.setText(responsableSesion.getNombreProyecto());
        configurarCamposParaDesarrollador();
    }

    private void configurarCamposParaDesarrollador() {
        dpInicio.setValue(LocalDate.now());
        dpInicio.setDisable(true);
        dpFin.setDisable(true);
        tfEsfuerzo.setDisable(true);
    }

    private void configurarTablaArchivos(){
        colNombreArchivo.setCellValueFactory(new PropertyValueFactory("nombreArchivo"));
    }

    private void cargarArchivos() {
        tvArchivo.setItems(archivos);
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
            Utilidades.mostrarAletarSimple(
                    "Error",
                    "Error al cargar los tipos de cambios", 
                    Alert.AlertType.ERROR);
        }
    }
    private boolean esCamposVacios(){
        return tfNombreCambio.getText().trim().isEmpty() ||
                tfDescripcion.getText().trim().isEmpty() ||
                tfRazon.getText().trim().isEmpty() ||
                tfImpacto.getText().trim().isEmpty() ||
                cbTipoCambio.getSelectionModel().getSelectedItem() ==null;
    }    

    private void guardarCambio() {
        Cambio nuevoCambio = new Cambio();
        nuevoCambio.setNombre(tfNombreCambio.getText());
        nuevoCambio.setDescripcion(tfDescripcion.getText());
        nuevoCambio.setFechaInicio(dpInicio.getValue().toString());
        nuevoCambio.setImpacto(tfImpacto.getText());
        nuevoCambio.setRazonCambio(tfRazon.getText());
        nuevoCambio.setIdDesarrollador(solicitudAceptada.getIdDesarrollador());
        nuevoCambio.setIdSolicitud(solicitudAceptada.getIdSolicitud());
        nuevoCambio.setIdTipoCambio(cbTipoCambio.getSelectionModel().
                getSelectedItem().
                getIdArtefacto());
        HashMap<String, Object> respuesta = CambioDAO.registrarCambioDeSolicitud(nuevoCambio);
        if(!(boolean)respuesta.get("error")){
            if (archivos != null && !archivos.isEmpty()){
                for (Archivo archivo : archivos) {
                    actualizarArchivo(archivo.getIdArchivo(),
                            (int)respuesta.get("idCambio"));
                }  
            }
            Utilidades.mostrarAletarSimple(
                    "Registro exitoso",
                    (String) respuesta.get("mensaje"), 
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAletarSimple(
                    "Error",
                    (String) respuesta.get("mensaje"), 
                    Alert.AlertType.ERROR);
        }
    }

    private void actualizarArchivo(int idArchivo,int idCambio) {
        HashMap<String, Object> respuesta = ArchivoDAO.actualizarArchivo(idArchivo,idCambio);
            if( (boolean) respuesta.get("error")){
                Utilidades.mostrarAletarSimple(
                        "Error en el registro", 
                        (String) respuesta.get("mensaje"), 
                        Alert.AlertType.WARNING);   
            }
    }

    private void cerrarVentana() {
        Stage escenario = (Stage) dpFin.getScene().getWindow();
        escenario.close();
    }
}
