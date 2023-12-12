package sistemadcuv.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.utils.Utilidades;


public class FXMLRegistroDeCambioController implements Initializable {

    @FXML
    private Button btnCrear;
    @FXML
    private TextField tfNombreCambio;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private TextField tfRazon;
    @FXML
    private TextField tfImpacto;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lNombreProyecto;
    @FXML
    private Label lbDesarrollador;
    @FXML
    private TextField tfEsfuerzo;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFin;
    @FXML
    private TableView<?> tvArchivo;
    @FXML
    private TableColumn<?, ?> colNombreArchivo;
    @FXML
    private Button bAgregar;
    @FXML
    private Button btnEliminar;
    private Desarrollador desarrolladorSesion;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarCampoEsfuerzo();
    }    

    @FXML
    private void clicCrear(ActionEvent event) {
        if(!esCamposVacios()){
            
        }else{
            Utilidades.mostrarAletarSimple("Campos vacios",
                    "Por favor asegurese de llenar "
                            + "los campos correspondientes", 
                    Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean cerrarVentana = Utilidades.mostrarDialogoConfirmacion("Cancelar proceso", 
                    "¿Desea cancelar el registro? tenga en cuenta que se perderá la información");                            
            if(cerrarVentana){
                Stage escenario = (Stage) btnCancelar.getScene().getWindow();
                escenario.close();
            }
    }

    @FXML
    private void btnAgregarArchivo(ActionEvent event) {
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
    }

    void inicializarFormulario(Desarrollador desarrolladorSesion) {
        this.desarrolladorSesion = desarrolladorSesion;
        lNombreProyecto.setText(desarrolladorSesion.getNombreProyecto());
        lbDesarrollador.setText(desarrolladorSesion.getNombreCompleto());
    }
    private boolean esCamposVacios(){
        return tfNombreCambio.getText().trim().isEmpty() ||
                tfDescripcion.getText().trim().isEmpty() ||
                tfEsfuerzo.getText().trim().isEmpty() ||
                dpInicio.getValue() == null ||
                dpFin.getValue() == null ||
                tfRazon.getText().trim().isEmpty() ||
                tfImpacto.getText().trim().isEmpty() ;
    }

    private void configurarCampoEsfuerzo() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    } else {
                        return null;
                    }
                });
        tfEsfuerzo.setTextFormatter(textFormatter);
    }
    
}
