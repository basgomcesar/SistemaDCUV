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
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemadcuv.interfaces.InitializableVentana;
import sistemadcuv.modelo.dao.CambioDAO;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.observador.ObservadorCambios;
import sistemadcuv.utils.Utilidades;


public class FXMLListadoDeCambiosController implements Initializable,InitializableVentana,ObservadorCambios  {

    @FXML
    private Label lbUsuarioActivo;
    private Desarrollador desarrolladorSesion;
    private ResponsableDeProyecto responsableSesion;
    @FXML
    private TableView<Cambio> tvCambios;
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
    private TextField tfNombre;
    @FXML
    private DatePicker dpFechaDesde;
    @FXML
    private DatePicker dpFechaHasta;
    
    private ObservableList<Cambio> cambios;
    @FXML
    private Button btnRegistrarCambio;
    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter formatoRegistro = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }
    
    @Override
    public void inicializarInformacion(Desarrollador desarrollador, ResponsableDeProyecto responsable) {
        this.desarrolladorSesion = desarrollador;
        this.responsableSesion = responsable;
        cargarInformacionCambios(desarrollador, responsable);
        if(desarrollador != null){
            lbUsuarioActivo.setText("Desarrollador: " + 
                    desarrollador.getNombreCompleto());
        }else{
            lbUsuarioActivo.setText("Responsable: " +
                    responsable.getNombreCompleto());
            btnRegistrarCambio.setVisible(false);
        }
    }
    
    private void configurarTabla(){
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estado"));
        this.colDesarrollador.setCellValueFactory(new PropertyValueFactory("desarrollador"));
        this.colFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        this.colFechaFin.setCellValueFactory(new PropertyValueFactory("fechaFin"));
        tvCambios.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2) {
            btnVerDetalles();
        }
        });
    }
    
    private void cargarInformacionCambios(Desarrollador desarrollador, ResponsableDeProyecto responsable){
        HashMap<String, Object> respuesta = new HashMap<>();
        if(desarrollador != null){
            respuesta = CambioDAO.obtenerListadoCambiosDesarrollador(
                    desarrolladorSesion.getIdDesarrollador());
        } else{
            respuesta = CambioDAO.obtenerListadoCambios();
        }
        
        if(!(boolean) respuesta.get("error")){
            cambios = FXCollections.observableArrayList();
            ArrayList<Cambio> lista = (ArrayList<Cambio>) respuesta.get("cambios");
            cambios.addAll(lista);
            tvCambios.setItems(cambios);
            FilteredList<Cambio> filtradoBusquedas = new FilteredList<>(cambios, p-> true);
            busquedaTablaFechas(filtradoBusquedas);
            busquedaTablaNombre(filtradoBusquedas);
        }else{
            Utilidades.mostrarAletarSimple(
                    "Error", 
                    respuesta.get("mensaje").toString(), 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void busquedaTablaNombre(FilteredList<Cambio> filtradoBusquedas){
        if(cambios.size() >= 1){
            tfNombre.textProperty().addListener(new ChangeListener<String>(){
                
                @Override
                public void changed(ObservableValue<? extends String> observable, 
                        String oldValue, String newValue) {
                    filtradoBusquedas.setPredicate(nombreFiltro -> {
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
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
            SortedList<Cambio> sortedListaCambios = new SortedList<>(filtradoBusquedas);
            sortedListaCambios.comparatorProperty().bind(tvCambios.comparatorProperty());
            tvCambios.setItems(sortedListaCambios);
        }
    }
    
    private void busquedaTablaFechas(FilteredList<Cambio> filtradoBusquedas){        
        if (cambios.size() >= 1) {

            dpFechaDesde.valueProperty().addListener((observable, oldValue, newValue) -> {
                filtrarCambiosPorRangoFecha(filtradoBusquedas);
            });

            dpFechaHasta.valueProperty().addListener((observable, oldValue, newValue) -> {
                filtrarCambiosPorRangoFecha(filtradoBusquedas);
            });

            SortedList<Cambio> sortedListaFechas = new SortedList<>(filtradoBusquedas);
            sortedListaFechas.comparatorProperty().bind(tvCambios.comparatorProperty());
            tvCambios.setItems(sortedListaFechas);
        }
    }

    private void filtrarCambiosPorRangoFecha(FilteredList<Cambio> filtradoFechas) {
        filtradoFechas.setPredicate(fechasFiltro -> {
            LocalDate fechaInicio = dpFechaDesde.getValue();
            LocalDate fechaFin = dpFechaHasta.getValue();

            if (fechaInicio == null && fechaFin == null) {
                return true;
            }

            LocalDate fechaCambio = LocalDate.parse(fechasFiltro.getFechaInicio(), formatoFecha);

            if (fechaInicio != null && fechaFin != null) {
                return fechaCambio != null && (fechaCambio.isEqual(fechaInicio) || fechaCambio.isEqual(fechaFin) ||
                        (fechaCambio.isAfter(fechaInicio) && fechaCambio.isBefore(fechaFin)));
            } else if (fechaInicio != null) {
                return fechaCambio != null && fechaCambio.isEqual(fechaInicio);
            } else {
                return fechaCambio != null && fechaCambio.isEqual(fechaFin);
            }
        });
    }
    @FXML
    private void clicRegistrarCambio(ActionEvent event) {
        Utilidades.mostrarAletarSimple("Advertencia",
                "Recuerde que solo puede hacer cambios de menor impacto, "
                        + "de lo contrario tendra que "
                        + "generar una solicitud de cambio", 
                Alert.AlertType.INFORMATION);
        irVentanaFormularioCambio(null);
    }

    private void irVentanaFormularioCambio(Cambio cambioSeleccionado) {
        try{
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLRegistroDeCambio.fxml");
            Parent vista = loader.load();
            Scene escena = new Scene(vista);
            FXMLRegistroDeCambioController controller = loader.getController();
            controller.inicializarFormulario(cambioSeleccionado, desarrolladorSesion,responsableSesion, this);
            
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Formulario cambio");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        }catch(IOException ex){
            Utilidades.mostrarAletarSimple(
                    "Error al cargar la ventana", 
                    "Ha ocurrido un error al cargar la ventana", 
                    Alert.AlertType.WARNING);
        }
    }

    @Override
    public void operacionExitosa(String tipoOperacion, String nombre) {
        cargarInformacionCambios(this.desarrolladorSesion,
                this.responsableSesion);
    }

    @FXML
    private void btnVerDetalles() {
        Cambio cambioSeleccionado = tvCambios.getSelectionModel().getSelectedItem();
        irVentanaFormularioCambio(cambioSeleccionado);
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
    private void btnLimpiarBusqueda(ActionEvent event) {
        tfNombre.clear();
        dpFechaDesde.setValue(null);
        dpFechaHasta.setValue(null);
    }

    
}
