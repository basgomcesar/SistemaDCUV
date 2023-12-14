package sistemadcuv.controladores;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.ProyectoDAO;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.observador.ObservadorParticipantes;
import sistemadcuv.utils.Utilidades;

public class FXMLListaDeParticipantesDelProyectoController implements Initializable {

    @FXML
    private TableView<Desarrollador> tvDesarrolladores;
    @FXML
    private TableColumn colDesarrollador;
    @FXML
    private TableColumn colMatricula;
    private ObservadorParticipantes observador;
    private ObservableList<Desarrollador> desarrolladores;
    private ResponsableDeProyecto responsableSesion;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    

    @FXML
    private void clicCerrar(ActionEvent event) {
        cerrarVentana();
    }

    private void configurarTabla() {
        this.colDesarrollador.setCellValueFactory(new PropertyValueFactory("nombreCompleto"));
        this.colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
    }

    void inicializarFormulario(ResponsableDeProyecto responsable,ObservadorParticipantes observador) {
        this.observador = observador;
        this.responsableSesion = responsable;
        obtenerInformacion(responsableSesion.getIdProyecto());
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
        }else{
            Utilidades.mostrarAletarSimple(
                    "Error de carga",
                    (String) respuesta.get("mensaje"),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicElegirDesarrollador(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Desarrollador desarrollador = tvDesarrolladores.getSelectionModel().getSelectedItem();
            observador.desarrolladorSeleccion(desarrollador);
            cerrarVentana();
        }
    }

    private void cerrarVentana() {
        Stage escenario = (Stage) tvDesarrolladores.getScene().getWindow();
        escenario.close();
    }
}
