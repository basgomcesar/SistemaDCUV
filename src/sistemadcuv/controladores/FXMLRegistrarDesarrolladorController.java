package sistemadcuv.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FXMLRegistrarDesarrolladorController implements Initializable {

    private int idProyecto;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void inicializarInformacion(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
    }
    
}
