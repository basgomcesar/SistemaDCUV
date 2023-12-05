package sistemadcuv.controladores;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.DesarrolladorDAO;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.observador.ObservadorDesarrolladores;
import sistemadcuv.utils.Utilidades;

public class FXMLRegistrarDesarrolladorController implements Initializable {

    private int idProyecto;
    @FXML
    private TextField tfNombreCompleto;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfContrasenia;
    @FXML
    private Spinner<Integer> spSemestre;
    private ObservadorDesarrolladores observador;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarSppinerSemestre();
    }    

    public void inicializarInformacion(int idProyecto,ObservadorDesarrolladores observador) {
        this.idProyecto = idProyecto;
        this.observador = observador;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        if(!hayCamposLlenos())
            cerrarVentana();
        else if (Utilidades.mostrarDialogoConfirmacion("Dialogo de confirmación",
                "¿Desea cancelar el registro? tenga en cuenta  que se perdera la información"))
            cerrarVentana();
            
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if(!esCamposVacios()){
            if(Utilidades.matriculaValida(tfMatricula.getText().trim())){
                if(Utilidades.correoValido(tfCorreo.getText())){
                    registrarDesarrollador();
                }else{
                    Utilidades.mostrarAletarSimple("Correo invalido", 
                            "Por favor ingrese un correo valido",
                            Alert.AlertType.INFORMATION);
                }
            }else{
                Utilidades.mostrarAletarSimple("Matricula invalida", 
                        "Por favor ingrese una matrícula valida Ej. zs21013897", 
                        Alert.AlertType.INFORMATION);
            }
        }else{
            Utilidades.mostrarAletarSimple("Campos vacios", 
                    "Por favor llene los campos vacíos antes de guardar al desarrollador",
                    Alert.AlertType.INFORMATION);
        }
    }

    private boolean esCamposVacios() {
        return tfNombreCompleto.getText().trim().isEmpty() || 
                spSemestre.getValue() == null ||
                tfMatricula.getText().trim().isEmpty() ||
                tfCorreo.getText().trim().isEmpty() ||
                tfContrasenia.getText().trim().isEmpty();
    }

    private void cerrarVentana() {
        Stage escenario = (Stage) tfCorreo.getScene().getWindow();
        escenario.close();
    }
    private boolean hayCamposLlenos(){
        return !tfNombreCompleto.getText().trim().isEmpty() || 
                !(spSemestre.getValue() == null) ||
                !tfMatricula.getText().trim().isEmpty() ||
                !tfCorreo.getText().trim().isEmpty() ||
                !tfContrasenia.getText().trim().isEmpty();
    }
    private void configurarSppinerSemestre(){
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1);
        spSemestre.setValueFactory(valueFactory);
    }

    private void registrarDesarrollador() {
        Desarrollador desarrollador = new Desarrollador();
        desarrollador.setNombreCompleto(tfNombreCompleto.getText().trim());
        desarrollador.setCorreo(tfCorreo.getText().trim());
        desarrollador.setMatricula(tfMatricula.getText().trim());
        desarrollador.setContrasenia(tfContrasenia.getText().trim());
        desarrollador.setSemestre(spSemestre.getValue());
        desarrollador.setIdProyecto(idProyecto);
        HashMap<String, Object> resultado = DesarrolladorDAO.registrarDesarrollador(desarrollador);
        if(!((boolean) resultado.get("error"))){
            Utilidades.mostrarAletarSimple("Mensaje de exito", 
                    (String) resultado.get("mensaje"), 
                    Alert.AlertType.INFORMATION);
            observador.operacionExitosa("Operacion exitosa", desarrollador.getNombreCompleto() );
            cerrarVentana();
        }else
            Utilidades.mostrarAletarSimple("Error al registrar", (String) resultado.get("mensaje"), 
                    Alert.AlertType.ERROR);
    }
}
