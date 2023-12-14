package sistemadcuv.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemadcuv.interfaces.InitializableVentana;
import sistemadcuv.modelo.dao.ActividadDAO;
import sistemadcuv.modelo.dao.CambioDAO;
import sistemadcuv.modelo.pojo.Actividad;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.observador.ObservadorActividades;
import sistemadcuv.utils.Utilidades;


public class FXMLListadoDeActividadesController implements Initializable,InitializableVentana,ObservadorActividades  {

    private Desarrollador desarrolladorSesion;
    private ResponsableDeProyecto responsableSesion;
    @FXML
    private Label lbUsuarioActivo;
    @FXML
    private TextField tfBarraBusqueda;
    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TableColumn colDesarrollador;
    @FXML
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colFechaFin;
    @FXML
    private Button btAgregarActividad;
    @FXML
    private TableView<Actividad> tvActividades;
    private ObservableList<Actividad> actividades;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarDatePicker();
        configurarTabla();
    }   
    

    @Override
    public void inicializarInformacion(Desarrollador desarrolladorSesion,ResponsableDeProyecto responsableSesion){
        this.desarrolladorSesion = desarrolladorSesion;
        this.responsableSesion = responsableSesion;
        cargarCampos();
        cargarInformacionActividades(desarrolladorSesion, responsableSesion);
    }

    private void cargarCampos() {
        if(this.desarrolladorSesion != null){
            btAgregarActividad.setVisible(false);
            lbUsuarioActivo.setText("Desarrollador: "+
                    desarrolladorSesion.getNombreCompleto());
        }else{
            lbUsuarioActivo.setText("Responsable: "+
                    responsableSesion.getNombreCompleto());
        }
    }
    
    private void configurarTabla(){
        this.colDesarrollador.setCellValueFactory(new PropertyValueFactory("desarrollador"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estado"));
        this.colFechaFin.setCellValueFactory(new PropertyValueFactory("fechaFin"));
        this.colFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory("titulo"));
    }
    
    private void cargarInformacionActividades(Desarrollador desarrollador, ResponsableDeProyecto responsable){
        HashMap<String, Object> respuesta = new HashMap<>();
        if(desarrollador != null){
            respuesta = ActividadDAO.obtenerListadoActividadesDesarrollador(
                    desarrolladorSesion.getIdDesarrollador());
        } else{
            respuesta = ActividadDAO.obtenerListadoActividades();
        }
        if(!(boolean) respuesta.get("error")){
            actividades = FXCollections.observableArrayList();
            ArrayList<Actividad> lista = (ArrayList<Actividad>) respuesta.get("actividades");
            actividades.addAll(lista);
            tvActividades.setItems(actividades);
        }else{
            Utilidades.mostrarAletarSimple(
                    "Error", 
                    respuesta.get("mensaje").toString(), 
                    Alert.AlertType.ERROR);
        }
    }

    @Override
    public void operacionExitosa(String tipoOperacion, String nombre) {
        cargarInformacionActividades(desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnActividades(ActionEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeActividades.fxml",
                "Listado de actividades");        
    }

    @FXML
    private void btnCambios(ActionEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeCambios.fxml",
                "Listado de cambios");
    }

    @FXML
    private void btnSolicitudes(ActionEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeSolicitudesDeCambio.fxml",
                "Listado de solicitudes de cambio");
    }

    @FXML
    private void btnDefectos(ActionEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeDefectos.fxml",
                "Listado de defectos");
    }

    @FXML
    private void btnParticipantes(ActionEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase,
                desarrolladorSesion,
                responsableSesion,
                "FXMLParticipantesDelProyecto.fxml",
                "Participantes del proyecto");
    }

    @FXML
    private void btnBitacora(ActionEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLBitacoraDeCambiosGeneral.fxml",
                "Bitacora general de cambios");
    }

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        Utilidades.irInicioDeSesion((Stage)
                lbUsuarioActivo.getScene().getWindow());
    }

    @FXML
    private void btnAgregarActividad(ActionEvent event) {
        try{
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLRegistroDeActividad.fxml");
            Parent vista = loader.load();
            Scene escena = new Scene(vista);
            FXMLRegistroDeActividadController controller = loader.getController();

            controller.inicializarFormulario(responsableSesion,this);
            
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Registrar actividad");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ex){
            Utilidades.mostrarAletarSimple("Error al cargar la ventana", "Ha ocurrido un error al cargar la ventana", Alert.AlertType.WARNING);
        }        
    }

    private void configurarDatePicker() {
        dpDesde.setConverter(Utilidades.formateaDatePicker());
        dpHasta.setConverter(Utilidades.formateaDatePicker());
    }

}
