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
        // TODO
        //PROVICIONAL
        tfUsuario.setText("zs20123456");
        tfContrasenia.setText("1234");
        
    }    

    @FXML
    private void iniciarSesion(ActionEvent event) {
        HashMap<String, Object> usuarioAutentificado = null ;
        if(!esCamposVacios()){
            String usuario = tfUsuario.getText().trim();
            String contrasenia = tfContrasenia.getText().trim();
            if(Utilidades.matriculaValida(usuario)){
                usuarioAutentificado = DesarrolladorDAO.verificarSesionDesarrollador(usuario, contrasenia); 
                Desarrollador desarrollador = (Desarrollador) usuarioAutentificado.get("desarrollador");
                if(!(desarrollador== null))
                    irPantallaPrincipalDesarrollador(desarrollador);
                else
                    Utilidades.mostrarAletarSimple("Usuario incorrecto", 
                        "Lo siento, las credenciales proporcionadas no son válidas.\n"+
                        "Por favor, asegúrate de ingresar el nombre de usuario y la contraseña correctos", 
                        Alert.AlertType.INFORMATION);
            }else{
            if(usuario.matches("\\d+")){
                usuarioAutentificado = ResponsableDAO.verificarSesionResponsable(Integer.parseInt(usuario),contrasenia);
                ResponsableDeProyecto responsable = (ResponsableDeProyecto) usuarioAutentificado.get("responsable");
                if(!(responsable == null ))
                    irPantallaPrincipalResponsable(responsable);
                else
                    Utilidades.mostrarAletarSimple("Usuario incorrecto", 
                        "Lo siento, las credenciales proporcionadas no son válidas.\n"+
                        "Por favor, asegúrate de ingresar el nombre de usuario y la contraseña correctos", 
                        Alert.AlertType.INFORMATION);
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
        return tfUsuario.getText().trim().isEmpty() || tfContrasenia.getText().trim().isEmpty();
    }


    private void irPantallaPrincipalDesarrollador(Desarrollador desarrollador) {
        Utilidades.mostrarAletarSimple("Bienvenida", "Bienvenido al sistema desarrollador "+
                desarrollador.getNombreCompleto(), Alert.AlertType.INFORMATION);
        Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
        Utilidades.irVentanaActividades(escenarioBase, desarrollador, null);
        
        
    }

    private void irPantallaPrincipalResponsable(ResponsableDeProyecto responsable) {
        Utilidades.mostrarAletarSimple("Bienvenida", "Bienvenido al sistema responsable "+
            responsable.getNombreCompleto(), Alert.AlertType.INFORMATION);
        Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();        
        Utilidades.irVentanaActividades(escenarioBase, null, responsable);
    }
    
}
