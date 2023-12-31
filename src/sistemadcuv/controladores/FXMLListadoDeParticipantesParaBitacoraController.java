package sistemadcuv.controladores;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sistemadcuv.interfaces.InitializableVentana;
import sistemadcuv.modelo.dao.ProyectoDAO;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.utils.Utilidades;

public class FXMLListadoDeParticipantesParaBitacoraController implements Initializable,InitializableVentana  {

    private Desarrollador desarrolladorSesion;
    private ResponsableDeProyecto responsableSesion;
    private ObservableList<Desarrollador> desarrolladores;
    @FXML
    private Label lbUsuarioActivo;
    @FXML
    private TableView<Desarrollador> tvParticipantes;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TextField tfBusquedaNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    
    private void configurarTabla() {
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombreCompleto"));
        this.colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
    }
    private void cargarInformacion(){
        HashMap<String, Object> respuesta = ProyectoDAO.
                obtenerDesarrolladoresPorProyecto(responsableSesion.getIdProyecto());
        if(!(boolean) respuesta.get("error")){
            desarrolladores = FXCollections.observableArrayList();
            ArrayList<Desarrollador> desarrolladorDAO = (ArrayList<Desarrollador>) respuesta.
                    get("desarrolladores");
            desarrolladores.addAll(desarrolladorDAO);
            tvParticipantes.setItems(desarrolladores);
            busquedaTablaNombre();
        }else{
            Utilidades.mostrarAletarSimple(
                    "Error de carga", 
                    (String) respuesta.get("mensaje"),
                    Alert.AlertType.ERROR);
        }
    }


    @Override
    public void inicializarInformacion(Desarrollador desarrolladorSesion, ResponsableDeProyecto responsableSesion) {
        this.desarrolladorSesion = desarrolladorSesion;
        this.responsableSesion = responsableSesion;
        lbUsuarioActivo.setText("Responsable: "+
                responsableSesion.getNombreCompleto());
        cargarInformacion();
    }


    @FXML
    private void btnBitacoraDesarrollador(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Desarrollador desarrollador = tvParticipantes.getSelectionModel().getSelectedItem();
            irVentanaBitacoraPorDesarrollador(desarrollador);
        }
    }

    private void irVentanaBitacoraPorDesarrollador(Desarrollador desarrollador) {
        try {
            FXMLLoader load = Utilidades.cargarVista("vistas/FXMLBitacoraDeCambiosDesarrollador.fxml");
            Parent vista = load.load();
            Scene escena = new Scene(vista);
            FXMLBitacoraDeCambiosDesarrolladorController controlador = load.getController();
            controlador.inicializarInformacion(desarrollador,responsableSesion);
            Stage escenario = (Stage) lbUsuarioActivo.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Bitacora de cambios desarrollador");
            escenario.show();
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple(
                    "Error al cargar la ventana",
                    "Ha ocurrido un error al cargar la ventana", 
                    Alert.AlertType.WARNING);
        } 
    }
    private void busquedaTablaNombre(){
        if(!desarrolladores.isEmpty()){
            FilteredList<Desarrollador> filtradoNombre = new FilteredList<>(desarrolladores, p -> true);
            tfBusquedaNombre.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed (ObservableValue<? extends String> observable,
                    String oldValue, String newValue){
                    filtradoNombre.setPredicate(nombreFiltro ->{
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String lowerNewValue = newValue.toLowerCase();
                        if(nombreFiltro.getNombreCompleto().trim().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }else if(nombreFiltro.getNombreCompleto().trim().toLowerCase().contains(newValue)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Desarrollador> sortedDesarrolladores = new SortedList<>(filtradoNombre);
            sortedDesarrolladores.comparatorProperty().bind(tvParticipantes.comparatorProperty());
            tvParticipantes.setItems(sortedDesarrolladores);
        }
    }
    @FXML
    private void btnBitacoraGeneral(MouseEvent event) {
        btnBitacora(null);
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
    
    
}
