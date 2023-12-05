package sistemadcuv.controladores;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.SolicitudDAO;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.SolicitudDeCambio;
import sistemadcuv.observador.ObservadorSolicitudes;
import sistemadcuv.utils.Utilidades;

public class FXMLRegistroDeSolicitudDeCambioController implements Initializable {

    @FXML
    private Label lFechaRegistro;
    @FXML
    private Label lEstado;
    @FXML
    private TextField tfNombreSolicitud;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private TextField tfRazon;
    @FXML
    private TextField tfImpacto;
    @FXML
    private TextField tfAccion;
    @FXML
    private TableView<?> tvArchivo;
    @FXML
    private TableColumn colNombreArchivo;
    @FXML
    private Label lNombreProyecto;
    @FXML
    private Label lNumSolicitud;
    @FXML
    private Label lSolicitante;
    
    private Desarrollador desarrolladorSesion;
    private ObservadorSolicitudes observador;
    private String estiloError = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 2;";
    private String estiloNormal = "-fx-border-width: 0;";
    private SolicitudDeCambio solicitudEdicion;
    LocalDate fechaActual;
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatoRegistro = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private int totalSolicitudes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaArchivos();
    }
    
    public void inicializarFormulario(Desarrollador desarrolladorSesion, SolicitudDeCambio solicitudEdicion, 
            int totalSolicitudes, ObservadorSolicitudes observador){
        this.desarrolladorSesion = desarrolladorSesion;
        this.solicitudEdicion = solicitudEdicion;
        this.observador = observador;
        lNombreProyecto.setText(desarrolladorSesion.getNombreProyecto());
        lSolicitante.setText(desarrolladorSesion.getNombreCompleto());
        lEstado.setText("Pendiente");
        fechaActual = LocalDate.now();
        String fechaRegistro = fechaActual.format(formatoFecha);
        lFechaRegistro.setText(fechaRegistro);
        lNumSolicitud.setText(String.valueOf(totalSolicitudes + 1));
    }
    
    private void configurarTablaArchivos(){
        colNombreArchivo.setCellValueFactory(new PropertyValueFactory("nombre"));
        tvArchivo.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, 
                    Number newValue) {
                TableHeaderRow header = (TableHeaderRow) tvArchivo.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>(){
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, 
                            Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });        
    }

    @FXML
    private void btnRegistrar(ActionEvent event) {
        validarCampos();
    }
    
    private void registrarSolicitud(SolicitudDeCambio nuevaSolicitud){
        HashMap<String, Object> respuesta = SolicitudDAO.registrarSolicitudDeCambio(nuevaSolicitud);
            if( !(boolean) respuesta.get("error")){
                Utilidades.mostrarAletarSimple("Registro exitoso", 
                        (String)respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
                observador.operacionExitosa("Registro", nuevaSolicitud.getNombre());
                cerrarVentana();
            }else{
                Utilidades.mostrarAletarSimple("Error en el registro", 
                        (String) respuesta.get("mensaje"), Alert.AlertType.WARNING);
            }
    }
    private void establecerEstiloNormal(){        
        tfNombreSolicitud.setStyle(estiloNormal);
        tfImpacto.setStyle(estiloNormal);
        tfDescripcion.setStyle(estiloNormal);
        tfAccion.setStyle(estiloNormal);
        tfRazon.setStyle(estiloNormal);
    }
    
    private void validarCampos(){
        try {
            establecerEstiloNormal();
            boolean datosValidos = true;
            
            String nombre = tfNombreSolicitud.getText();
            String descripcion = tfDescripcion.getText();
            String impacto = tfImpacto.getText();
            String accion = tfAccion.getText();
            String razon = tfRazon.getText();
            String fechaRegistro = fechaActual.format(formatoRegistro);
            
            if(nombre.isEmpty()){
                tfNombreSolicitud.setStyle(estiloError);
                datosValidos = false;
            }
            
            if(descripcion.isEmpty()){
                tfDescripcion.setStyle(estiloError);
                datosValidos = false;
            }
            
            if(impacto.isEmpty()){
                tfImpacto.setStyle(estiloError);
                datosValidos = false;
            }
            
            if(accion.isEmpty()){
                tfAccion.setStyle(estiloError);
                datosValidos = false;
            }
            
            if(razon.isEmpty()){
                tfRazon.setStyle(estiloError);
                datosValidos = false;
            }
            
            if(datosValidos){
                SolicitudDeCambio solicitudValida = new SolicitudDeCambio();
                solicitudValida.setNombre(nombre);
                solicitudValida.setDescripcion(descripcion);
                solicitudValida.setImpacto(impacto);
                solicitudValida.setAccionPropuesta(accion);
                solicitudValida.setRazon(razon);
                solicitudValida.setEstatus(lEstado.getText());
                solicitudValida.setNumSolicitud(Integer.parseInt(lNumSolicitud.getText()));
                solicitudValida.setFechaRegistro(fechaRegistro);
                solicitudValida.setIdDesarrollador(desarrolladorSesion.getIdDesarrollador());
                solicitudValida.setNombreDesarrollador(desarrolladorSesion.getNombreCompleto());
                if(solicitudEdicion == null){
                    registrarSolicitud(solicitudValida);
                    System.out.println(solicitudValida.getNumSolicitud());
                }else{
                    
                }   
            }
        } catch (NumberFormatException ex) {
            Utilidades.mostrarAletarSimple("Error al enviar", "Ocurrió un error al enviar la informacion", 
                    Alert.AlertType.WARNING);
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        boolean cerrarVentana = Utilidades.mostrarDialogoConfirmacion("Cancelar proceso", 
                    "¿Estás seguro de que deseas salir? No se guardaran los datos ingresados");                            
            if(cerrarVentana){
        Stage escenario = (Stage) tfNombreSolicitud.getScene().getWindow();
        escenario.close();
            }
    }

    @FXML
    private void btnAgregarArchivo(ActionEvent event) {
        
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) tfNombreSolicitud.getScene().getWindow();
        escenario.close();
    }
}
