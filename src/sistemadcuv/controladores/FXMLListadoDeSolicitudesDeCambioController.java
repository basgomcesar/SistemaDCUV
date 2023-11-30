package sistemadcuv.controladores;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.SolicitudDAO;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.modelo.pojo.SolicitudDeCambio;
import sistemadcuv.utils.Utilidades;

public class FXMLListadoDeSolicitudesDeCambioController implements Initializable {

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
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }
    
    public void inicializarInformacion(Desarrollador desarrollador, ResponsableDeProyecto responsable) {
        this.desarrolladorSesion = desarrollador;
        this.responsableSesion = responsable;
        cargarInformacionSolicitudes(desarrollador, responsable);
        if(desarrollador != null){
        lbUsuarioActivo.setText("Usuario: " + desarrollador.getNombreCompleto());
        } else{
        lbUsuarioActivo.setText("Usuario: " + responsable.getNombreCompleto());    
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
        Utilidades.irVentanaActividades(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnCambios(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaListadoDeCambios(escenarioBase, desarrolladorSesion, responsableSesion);
    }


    @FXML
    private void btnDefectos(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaDefectos(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnParticipantes(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaParticipantes(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnBitacora(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaBitacoraGeneral(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnRegistrarSolicitud(ActionEvent event) {
    }
    
}
