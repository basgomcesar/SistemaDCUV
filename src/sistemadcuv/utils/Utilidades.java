package sistemadcuv.utils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sistemadcuv.controladores.FXMLInicioSesionController;
import sistemadcuv.controladores.FXMLRegistroDeActividadController;
import sistemadcuv.interfaces.InitializableVentana;
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
            Pattern patronCorreo = Pattern.compile(
                    "([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher matchPatron = patronCorreo.matcher(correo);
            
            return matchPatron.find();
        } else{
            return false;
        }
    }
    public static boolean matriculaValida(String matricula){
        String formato = "zs\\d{8}";
        return matricula.matches(formato);
    }
    public static boolean mostrarDialogoConfirmacion(String titulo, String mensaje) {
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setContentText(mensaje);
        alertaConfirmacion.setHeaderText(null);
        Optional<ButtonType> botonClic = alertaConfirmacion.showAndWait(); 
        
        return (botonClic.get() == ButtonType.OK);
    }
    public static void irAVentana(Stage escenarioBase, Desarrollador desarrollador, ResponsableDeProyecto responsable, String vista, String titulo) {
        try {
            FXMLLoader loader = cargarVista("vistas/" + vista);
            Parent vistaFXML = loader.load();
            Scene escena = new Scene(vistaFXML);
            Object controladorObj = loader.getController();
            if (controladorObj instanceof InitializableVentana) {
                InitializableVentana controlador = (InitializableVentana) controladorObj;
                controlador.inicializarInformacion(desarrollador, responsable);
            }
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle(titulo);
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("Error al cargar ventana",
                    ex.getMessage(),
                    Alert.AlertType.INFORMATION);
        }
    }
    public static String obtenerFechaActual(){
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fechaActual.format(formato);
    }

    public static String formatearFecha(String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        String nombreMes = fecha.getMonth().getDisplayName(
                java.time.format.TextStyle.FULL, Locale.getDefault());
        String fechaFormateada = String.format("%d de %s de %d",
                fecha.getDayOfMonth(), nombreMes, fecha.getYear());
        return fechaFormateada;
    }
    public static void irInicioDeSesion(Stage escenario){
        try{
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLInicioSesion.fxml");
            Parent vista = loader.load();
            Scene escena = new Scene(vista);
            FXMLInicioSesionController controller = loader.getController();
            
            escenario.setScene(escena);
            escenario.setTitle("Inicio de Sesión");
            escenario.show();
        }catch(IOException ex){
            mostrarAletarSimple(
                    "Error al cargar la ventana",
                    "Se ha producido un error al intentar cargar la ventana",
                    Alert.AlertType.ERROR);
        }
    }
    public static StringConverter<LocalDate> formateaDatePicker(){
            StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        return converter;
    }
}
