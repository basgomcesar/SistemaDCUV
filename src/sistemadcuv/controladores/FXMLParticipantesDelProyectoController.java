package sistemadcuv.controladores;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.ProyectoDAO;
import sistemadcuv.utils.Utilidades;
import javafx.util.Callback;
import sistemadcuv.interfaces.InitializableVentana;
import sistemadcuv.modelo.dao.DesarrolladorDAO;
import sistemadcuv.observador.ObservadorDesarrolladores;

public class FXMLParticipantesDelProyectoController implements Initializable,ObservadorDesarrolladores,InitializableVentana  {


    @FXML
    private Label lbUsuarioActivo;
    private Desarrollador desarrolladorSesion;
    private ResponsableDeProyecto responsableSesion;
    private ObservableList<Desarrollador> desarrolladores;
    @FXML
    private TableView<Desarrollador> tvDesarrolladores;
    @FXML
    private TableColumn<Desarrollador, String> colNombreDesarrollador;
    @FXML
    private TableColumn<Desarrollador, String> colCorreo;
    @FXML
    private TableColumn<Desarrollador, String> colEliminar;
    @FXML
    private Button btnAgregarParticipante;
    @FXML
    private TextField tfBusquedaNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    

    @Override
    public void inicializarInformacion(Desarrollador desarrolladorSesion, ResponsableDeProyecto responsableSesion) {
        this.desarrolladorSesion = desarrolladorSesion;
        this.responsableSesion = responsableSesion;
        cargarInformacion();

    }

    private void cargarInformacion() {
        if(desarrolladorSesion == null){
            lbUsuarioActivo.setText("Responsable - "+responsableSesion.getNombreCompleto());
            obtenerInformacion(responsableSesion.getIdProyecto());
        }else{
            lbUsuarioActivo.setText("Desarrollador - "+desarrolladorSesion.getNombreCompleto());
            obtenerInformacion(desarrolladorSesion.getIdProyecto());
            btnAgregarParticipante.setVisible(false);
            colEliminar.setVisible(false);
        }
    }

    private void configurarTabla() {
        this.colNombreDesarrollador.setCellValueFactory(new PropertyValueFactory("nombreCompleto"));
        this.colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        configurarColumnaEliminar();
    }
    private void obtenerInformacion(int idProyecto){
        HashMap<String, Object> respuesta = ProyectoDAO.
                obtenerDesarrolladoresActivosPorProyecto(idProyecto);
        if(!(boolean) respuesta.get("error")){
            desarrolladores = FXCollections.observableArrayList();
            ArrayList<Desarrollador> desarrolladorDAO = (ArrayList<Desarrollador>) respuesta.get("desarrolladores");
            ArrayList<Desarrollador> lista = desarrolladorDAO;
            desarrolladores.addAll(lista);
            tvDesarrolladores.setItems(desarrolladores);
            busquedaTablaNombre();
        }else{
            Utilidades.mostrarAletarSimple("Error de carga", (String) respuesta.get("mensaje"),
                    Alert.AlertType.ERROR);
        }
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

    @FXML
    private void btnSolicitudes(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrolladorSesion, 
                responsableSesion, 
                "FXMLListadoDeSolicitudesDeCambio.fxml",
                "Listado de solicitudes de cambio");
    }

    @FXML
    private void clicAgregarParticipante(ActionEvent event) {
        try {
            FXMLLoader load = Utilidades.cargarVista("vistas/FXMLRegistrarDesarrollador.fxml");
            Parent vista = load.load();
            Scene escena = new Scene(vista);
            FXMLRegistrarDesarrolladorController controlador = load.getController();
            controlador.inicializarInformacion(responsableSesion.getIdProyecto(),this);
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Registrar desarrollador");
            escenario.show();
            escenario.setOnCloseRequest(event1 -> {
                    event1.consume();
                    controlador.clicCancelar(null);
        });
        } catch (IOException ex) {
            Logger.getLogger(FXMLParticipantesDelProyectoController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    private void configurarColumnaEliminar(){
        Callback<TableColumn<Desarrollador, String>, TableCell<Desarrollador, String>> cellFoctory = (TableColumn<Desarrollador, String> param) -> {
            final TableCell<Desarrollador, String> cell = new TableCell<Desarrollador, String>(){
                @Override
                public void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);
                    if(empty){
                        setGraphic(null);
                        setText(null);
                    }else{
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                Desarrollador desarrollador = tvDesarrolladores.getSelectionModel().
                                        getSelectedItem();
                                if(Utilidades.mostrarDialogoConfirmacion(
                                        
                                        "Alerta de confirmación ", 
                                        "¿Esta seguro de eliminar al desarrollador " + 
                                                desarrollador.getNombreCompleto()+
                                                "? ya que al eliminar no podra deshacer los cambios"))
                                    eliminarDesarrollador(desarrollador);
                        });
                        HBox managebtn = new HBox( deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        colEliminar.setCellFactory(cellFoctory);
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
            sortedDesarrolladores.comparatorProperty().bind(tvDesarrolladores.comparatorProperty());
            tvDesarrolladores.setItems(sortedDesarrolladores);
        }
    }
    private void eliminarDesarrollador(Desarrollador desarrollador){
        HashMap<String, Object> respuesta = DesarrolladorDAO.verificarAsignaciones(desarrollador);
        int numeroDeAsignaciones = (int) respuesta.get("asignaciones");
        if(numeroDeAsignaciones > 0){
            Utilidades.mostrarAletarSimple(
                    "Advertencia asignaciones",
                    "No puede eliminar un desarrollador con asignaciones PENDIENTES ",
                    Alert.AlertType.INFORMATION);
        }else{
             respuesta = DesarrolladorDAO.eliminarDesarrollador(desarrollador);
             if(!((boolean)respuesta.get("error"))){
                 Utilidades.mostrarAletarSimple("Mensaje de exito", 
                         (String) respuesta.get("mensaje"), 
                         Alert.AlertType.INFORMATION);
                 cargarInformacion();
             }else{
                 Utilidades.mostrarAletarSimple("Mensaje de error",
                         (String)respuesta.get("mensaje"), 
                         Alert.AlertType.INFORMATION);
             }
        }
    }

    @Override
    public void operacionExitosa(String tipoOperacion, String nombre) {
        cargarInformacion();
    }

}
