package sistemadcuv.controladores;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.DesarrolladorDAO;
import sistemadcuv.modelo.dao.ResponsableDAO;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.utils.Utilidades;



public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField tfContrasenia;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void iniciarSesion(ActionEvent event) {
        HashMap<String, Object> respuesta = null ;
        if(!esCamposVacios()){
            String usuario = tfUsuario.getText().trim();
            String contrasenia = tfContrasenia.getText().trim();
            if(Utilidades.matriculaValida(usuario)){
                respuesta = DesarrolladorDAO.verificarSesionDesarrollador(usuario, contrasenia); 
                verificarDesarrollador(respuesta);
            }else{
                if(usuario.matches("\\d+")){
                    respuesta = ResponsableDAO.verificarSesionResponsable(Integer.parseInt(usuario),contrasenia);
                    verificarResponsable(respuesta);
                }else
                    Utilidades.mostrarAletarSimple("Usuario incorrecto", 
                        "Lo siento, las credenciales proporcionadas no son válidas.\n"+
                        "Por favor, asegúrate de ingresar el nombre de usuario y la contraseña correctos", 
                        Alert.AlertType.INFORMATION);
            }
        }else{
            Utilidades.mostrarAletarSimple("Campos vacios", 
                    "Por favor llene los campos faltantes", 
                    Alert.AlertType.INFORMATION);
        }
    }

    private boolean esCamposVacios() {
        return tfUsuario.getText().trim().isEmpty() ||
                tfContrasenia.getText().trim().isEmpty();
    }


    private void irPantallaPrincipalDesarrollador(Desarrollador desarrollador) {
        Utilidades.mostrarAletarSimple("Bienvenida", "Bienvenido al sistema desarrollador "+
                desarrollador.getNombreCompleto(), Alert.AlertType.INFORMATION);
        Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
        Utilidades.irAVentana(escenarioBase, 
                desarrollador, 
                null,
                "FXMLListadoDeActividades.fxml",
                "Listado de actividades");
        
    }

    private void irPantallaPrincipalResponsable(ResponsableDeProyecto responsable) {
        Utilidades.mostrarAletarSimple("Bienvenida", "Bienvenido al sistema responsable "+
            responsable.getNombreCompleto(), Alert.AlertType.INFORMATION);
        Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();        
        Utilidades.irAVentana(escenarioBase, 
                null, 
                responsable,
                "FXMLListadoDeActividades.fxml",
                "Listado de actividades");
    }

    private void verificarDesarrollador(HashMap<String, Object> respuesta) {
        if(!(boolean)respuesta.get("error")){
            Desarrollador desarrollador = (Desarrollador) respuesta.get("desarrollador");
            if(!(desarrollador== null))
                irPantallaPrincipalDesarrollador(desarrollador);
            else
                Utilidades.mostrarAletarSimple("Usuario incorrecto", 
                    "Lo siento, las credenciales proporcionadas no son válidas.\n"+
                    "Por favor, asegúrate de ingresar el nombre de usuario y la contraseña correctos", 
                    Alert.AlertType.INFORMATION);
            
        }else{
            Utilidades.mostrarAletarSimple(
                    
                    "Error",
                    (String) respuesta.get("mensaje"),
                    Alert.AlertType.ERROR);
        }
    }

    private void verificarResponsable(HashMap<String, Object> respuesta) {
        if(!(boolean)respuesta.get("error")){
            ResponsableDeProyecto responsable = (ResponsableDeProyecto) respuesta.get("responsable");
            if(!(responsable == null ))
                irPantallaPrincipalResponsable(responsable);
        else
            Utilidades.mostrarAletarSimple(
                    "Usuario incorrecto", 
                "Lo siento, las credenciales proporcionadas no son válidas.\n"+
                "Por favor, asegúrate de ingresar el nombre de usuario y la contraseña correctos", 
                Alert.AlertType.INFORMATION);
        }else
            Utilidades.mostrarAletarSimple(
                    "Error",
                    (String) respuesta.get("mensaje"),
                    Alert.AlertType.ERROR);
    }
    
}
