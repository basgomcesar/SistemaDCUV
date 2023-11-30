package sistemadcuv.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.utils.Utilidades;


public class FXMLListadoDeActividadesController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
    private void btnSolicitudes(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaSolicitudes(escenarioBase, desarrolladorSesion, responsableSesion);
    }
    public void inicializarInformacion(Desarrollador desarrolladorSesion,ResponsableDeProyecto responsableSesion){
        this.desarrolladorSesion = desarrolladorSesion;
        this.responsableSesion = responsableSesion;
        cargarInformacionUsuario();
    }

    private void cargarInformacionUsuario() {
        
    }

    @FXML
    private void clicAgregarActividad(ActionEvent event) {
    }

}
