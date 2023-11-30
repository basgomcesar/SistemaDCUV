package sistemadcuv.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sistemadcuv.controladores.FXMLBitacoraDeCambiosGeneralController;
import sistemadcuv.controladores.FXMLListadoDeActividadesController;
import sistemadcuv.controladores.FXMLListadoDeCambiosController;
import sistemadcuv.controladores.FXMLListadoDeDefectosController;
import sistemadcuv.controladores.FXMLListadoDeSolicitudesDeCambioController;
import sistemadcuv.controladores.FXMLParticipantesDelProyectoController;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;

public class Utilidades {
    
        public static void mostrarAletarSimple(String titulo, String mensaje, Alert.AlertType tipo){
        Alert alertaSimple = new Alert(tipo);
        alertaSimple.setTitle(titulo);
        alertaSimple.setHeaderText(null);
        alertaSimple.setContentText(mensaje);
        alertaSimple.showAndWait();
    }
    
    public static FXMLLoader cargarVista(String rutaFXML) throws IOException{
        URL url = sistemadcuv.SistemaDCUV.class.getResource(rutaFXML);
        return new FXMLLoader(url);
    }
    
    public static boolean correoValido(String correo){        
        if(correo != null && !correo.isEmpty()){
            Pattern patronCorreo = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher matchPatron = patronCorreo.matcher(correo);
            
            return matchPatron.find();
        } else{
            return false;
        }
    }
    
    public static boolean mostrarDialogoConfirmacion(String titulo, String mensaje) {
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setContentText(mensaje);
        alertaConfirmacion.setHeaderText(null);
        Optional<ButtonType> botonClic = alertaConfirmacion.showAndWait(); 
        
        return (botonClic.get() == ButtonType.OK);
    }
    public static void irVentanaActividades(Stage escenarioBase,Desarrollador desarrollador, ResponsableDeProyecto responsable){
        try {
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLListadoDeActividades.fxml");
            Parent vista = loader.load();
            FXMLListadoDeActividadesController controlador = loader.getController();
            controlador.inicializarInformacion(desarrollador, responsable);
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle("Listado de actividades");
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("Error al cargar ventana", 
                    ex.getMessage(), 
                    Alert.AlertType.INFORMATION);
        }
    }
    public static void irVentanaListadoDeCambios(Stage escenarioBase,Desarrollador desarrollador, ResponsableDeProyecto responsable){
        try {
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLListadoDeCambios.fxml");
            Parent vista = loader.load();
            FXMLListadoDeCambiosController controlador = loader.getController();
            controlador.inicializarInformacion(desarrollador, responsable);
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle("Listado de cambios");
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("Error al cargar ventana", 
                    ex.getMessage(), 
                    Alert.AlertType.INFORMATION);
        }
    }
    public static void irVentanaSolicitudes(Stage escenarioBase,Desarrollador desarrollador, ResponsableDeProyecto responsable){
        try {
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLListadoDeSolicitudesDeCambio.fxml");
            Parent vista = loader.load();
            FXMLListadoDeSolicitudesDeCambioController controlador = loader.getController();
            controlador.inicializarInformacion(desarrollador, responsable);
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle("Listado de solicitudes de cambio");
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAletarSimple("Error al cargar ventana", 
                    ex.getMessage(), 
                    Alert.AlertType.INFORMATION);
        }
    } 
    public static void irVentanaDefectos(Stage escenarioBase,Desarrollador desarrollador, ResponsableDeProyecto responsable){
        try {
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLListadoDeDefectos.fxml");
            Parent vista = loader.load();
            FXMLListadoDeDefectosController controlador = loader.getController();
            controlador.inicializarInformacion(desarrollador, responsable);
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle("Listado de defectos");
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("Error al cargar ventana", 
                    ex.getMessage(), 
                    Alert.AlertType.INFORMATION);
        }
    }    
    public static void irVentanaBitacoraGeneral(Stage escenarioBase,Desarrollador desarrollador, ResponsableDeProyecto responsable){
        try {
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLBitacoraDeCambiosGeneral.fxml");
            Parent vista = loader.load();
            FXMLBitacoraDeCambiosGeneralController controlador = loader.getController();
            controlador.inicializarInformacion(desarrollador, responsable);
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle("Bitacora general de cambios");
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("Error al cargar ventana", 
                    ex.getMessage(), 
                    Alert.AlertType.INFORMATION);
        }
    } 
        public static void irVentanaParticipantes(Stage escenarioBase,Desarrollador desarrollador, ResponsableDeProyecto responsable){
        try {
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLParticipantesDelProyecto.fxml");
            Parent vista = loader.load();
            FXMLParticipantesDelProyectoController controlador = loader.getController();
            controlador.inicializarInformacion(desarrollador, responsable);
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle("Participantes del proyecto");
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("Error al cargar ventana", 
                    ex.getMessage(), 
                    Alert.AlertType.INFORMATION);
        }
    } 
}
