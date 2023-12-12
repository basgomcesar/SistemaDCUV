package sistemadcuv.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemadcuv.interfaces.InitializableVentana;
import sistemadcuv.modelo.dao.SolicitudDAO;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.modelo.pojo.SolicitudDeCambio;
import sistemadcuv.observador.ObservadorSolicitudes;
import sistemadcuv.utils.Utilidades;

public class FXMLListadoDeSolicitudesDeCambioController implements Initializable, ObservadorSolicitudes,InitializableVentana  {

    private Desarrollador desarrolladorSesion;
    private ResponsableDeProyecto responsableSesion;
    @FXML
    private Label lbUsuarioActivo;
    @FXML
    private TableView<SolicitudDeCambio> tvListadoSolicitudes;
    @FXML
    private TableColumn colNumSolicitud;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TableColumn colDesarrollador;
    @FXML
    private TableColumn colFechaRegistro;
    @FXML
    private TableColumn colFechaAprobacion;
    @FXML
    private TextField tfNombre;
    @FXML
    private DatePicker dpFechaDesde;
    @FXML
    private DatePicker dpFechaHasta;
    
    private ObservableList<SolicitudDeCambio> solicitudes;
    private SolicitudDeCambio solicitudEdicion;
    @FXML
    private Button bRegistrar;
    private int totalSolicitudes;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }
    
    @Override
    public void inicializarInformacion(Desarrollador desarrollador, ResponsableDeProyecto responsable) {
        this.desarrolladorSesion = desarrollador;
        this.responsableSesion = responsable;
        cargarInformacionSolicitudes(desarrollador, responsable);
        if(desarrollador != null){
        lbUsuarioActivo.setText("Usuario: " + desarrollador.getNombreCompleto());
        totalSolicitudes = solicitudes.size();
        } else{
        lbUsuarioActivo.setText("Usuario: " + responsable.getNombreCompleto());
        bRegistrar.setVisible(false);
        }
        
    }
    
    public void configurarTabla(){
        this.colNumSolicitud.setCellValueFactory(new PropertyValueFactory("numSolicitud"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        this.colDesarrollador.setCellValueFactory(new PropertyValueFactory("nombreDesarrollador"));
        this.colFechaRegistro.setCellValueFactory(new PropertyValueFactory("fechaRegistro"));
        this.colFechaAprobacion.setCellValueFactory(new PropertyValueFactory("fechaAprobacion"));
    }
    
    public void cargarInformacionSolicitudes(Desarrollador desarrollador, ResponsableDeProyecto responsable){
        HashMap<String, Object> respuesta = new HashMap<>();
        if(desarrollador != null){
            respuesta = SolicitudDAO.obtenerListadoSolicitudesDesarrollador(desarrolladorSesion.getIdDesarrollador());
        } else{
            respuesta = SolicitudDAO.obtenerListadoSolicitudes();
        }
        
        if(!(boolean) respuesta.get("error")){
            solicitudes = FXCollections.observableArrayList();
            ArrayList<SolicitudDeCambio> lista = (ArrayList<SolicitudDeCambio>) respuesta.get("solicitudes");
            solicitudes.addAll(lista);
            tvListadoSolicitudes.setItems(solicitudes);
            FilteredList<SolicitudDeCambio> filtradoBusquedas = new FilteredList<>(solicitudes, p-> true);
            busquedaTablaFechas(filtradoBusquedas);
            busquedaTablaNombre(filtradoBusquedas);
        }else{
            Utilidades.mostrarAletarSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void busquedaTablaNombre(FilteredList<SolicitudDeCambio> filtradoBusquedas){
        if(solicitudes.size() > 0){
            tfNombre.textProperty().addListener(new ChangeListener<String>(){
                
                @Override
                public void changed(ObservableValue<? extends String> observable, 
                        String oldValue, String newValue) {
                    filtradoBusquedas.setPredicate(nombreFiltro -> {
                        //CASO DEFAULT
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        //CRITERIO DE EVALUACION
                        String lowerNewValue = newValue.toLowerCase();
                        if(nombreFiltro.getNombre().toLowerCase().contains(lowerNewValue)){
                            return true;
                        } else if(nombreFiltro.getNombre().toLowerCase().contains(newValue)){
                            return true;
                        }
                        return false;
                    });
                }
                
            });
            SortedList<SolicitudDeCambio> sortedListaSolicitudes = new SortedList<>(filtradoBusquedas);
            sortedListaSolicitudes.comparatorProperty().bind(tvListadoSolicitudes.comparatorProperty());
            tvListadoSolicitudes.setItems(sortedListaSolicitudes);
        }
    }
    
    private void busquedaTablaFechas(FilteredList<SolicitudDeCambio> filtradoBusquedas){        
        if (solicitudes.size() > 0) {

            dpFechaDesde.valueProperty().addListener((observable, oldValue, newValue) -> {
                filtrarSolicitudesPorRangoFecha(filtradoBusquedas);
            });

            dpFechaHasta.valueProperty().addListener((observable, oldValue, newValue) -> {
                filtrarSolicitudesPorRangoFecha(filtradoBusquedas);
            });

            SortedList<SolicitudDeCambio> sortedListaFechas = new SortedList<>(filtradoBusquedas);
            sortedListaFechas.comparatorProperty().bind(tvListadoSolicitudes.comparatorProperty());
            tvListadoSolicitudes.setItems(sortedListaFechas);
        }
    }

    private void filtrarSolicitudesPorRangoFecha(FilteredList<SolicitudDeCambio> filtradoFechas) {
        filtradoFechas.setPredicate(fechasFiltro -> {
            LocalDate fechaInicio = dpFechaDesde.getValue();
            LocalDate fechaFin = dpFechaHasta.getValue();

            // CASO DEFAULT
            if (fechaInicio == null && fechaFin == null) {
                return true;
            }

            // CRITERIO DE EVALUACION
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaSolicitud = LocalDate.parse(fechasFiltro.getFechaRegistro(), formatoFecha);

            if (fechaInicio != null && fechaFin != null) {
                return fechaSolicitud != null && (fechaSolicitud.isEqual(fechaInicio) || fechaSolicitud.isEqual(fechaFin) ||
                        (fechaSolicitud.isAfter(fechaInicio) && fechaSolicitud.isBefore(fechaFin)));
            } else if (fechaInicio != null) {
                return fechaSolicitud != null && fechaSolicitud.isEqual(fechaInicio);
            } else {
                return fechaSolicitud != null && fechaSolicitud.isEqual(fechaFin);
            }
        });
    }

    @FXML
    private void btnActividades(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeActividades.fxml",
                "Listado de actividades");
    }

    @FXML
    private void btnCambios(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeCambios.fxml",
                "Listado de cambios");
    }


    @FXML
    private void btnDefectos(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeDefectos.fxml",
                "Listado de defectos");
    }

    @FXML
    private void btnParticipantes(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase,
                desarrolladorSesion,
                responsableSesion,
                "FXMLParticipantesDelProyecto.fxml",
                "Participantes del proyecto");
    }

    @FXML
    private void btnBitacora(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLBitacoraDeCambiosGeneral.fxml",
                "Bitacora general de cambios");
    }

    private void btnSolicitudes(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeSolicitudesDeCambio.fxml",
                "Listado de solicitudes de cambio");
    }
    

    @FXML
    private void btnRegistrarSolicitud(ActionEvent event) {
        try{
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLRegistroDeSolicitudDeCambio.fxml");
            Parent vista = loader.load();
            Scene escena = new Scene(vista);
            FXMLRegistroDeSolicitudDeCambioController controller = loader.getController();
            controller.inicializarFormulario(desarrolladorSesion, solicitudEdicion, totalSolicitudes, this);
            
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Registrar Solicitud de Cambio");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void operacionExitosa(String tipoOperacion, String nombre) {
        cargarInformacionSolicitudes(desarrolladorSesion, responsableSesion);
    }
    
}
