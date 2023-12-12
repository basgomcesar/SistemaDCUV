package sistemadcuv.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sistemadcuv.modelo.pojo.Archivo;

public class FXMLRegistroDeActividadController implements Initializable {

    @FXML
    private TextField tfNombreSolicitud;
    @FXML
    private TextField tfAccion;
    @FXML
    private TableView<Archivo> tvArchivo;
    @FXML
    private TableColumn<?, ?> colNombreArchivo;
    @FXML
    private Label lNombreProyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnCrear(ActionEvent event) {
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
    }

    @FXML
    private void btnAgregarArchivo(ActionEvent event) {
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
    }

    @FXML
    private void btnAsignarDesarrollador(ActionEvent event) {
    }
    
}
