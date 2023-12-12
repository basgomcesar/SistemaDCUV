package sistemadcuv.controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.ArchivoDAO;
import sistemadcuv.modelo.dao.SolicitudDAO;
import sistemadcuv.modelo.pojo.Archivo;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.SolicitudDeCambio;
import sistemadcuv.observador.ObservadorSolicitudes;
import sistemadcuv.utils.Utilidades;

public class FXMLRegistroDeSolicitudDeCambioController implements Initializable {

    @FXML
    private Label lbFechaRegistro;
    @FXML
    private Label lbEstado;
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
    private TableView<Archivo> tvArchivo;
    @FXML
    private TableColumn colNombreArchivo;
    @FXML
    private Label lbNombreProyecto;
    @FXML
    private Label lbNumSolicitud;
    @FXML
    private Label lbSolicitante;
    
    private Desarrollador desarrolladorSesion;
    private ObservadorSolicitudes observador;
    private String estiloError = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 2;";
    private String estiloNormal = "-fx-border-width: 0;";
    private SolicitudDeCambio solicitudEdicion;
    private LocalDate fechaActual;
    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter formatoRegistro = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private int totalSolicitudes;
    private Archivo documentoSol;
    private ObservableList<Archivo> archivos;
    @FXML
    private Line lnAprobado;
    @FXML
    private Label lbAprobadoPor;
    @FXML
    private Label lbFechaAprovacion;
    @FXML
    private Line lnFechaAprovacion;
    @FXML
    private Button bRegistrar;
    @FXML
    private Button bCancelar;
    @FXML
    private Button bAgregar;
    @FXML
    private Button bRechazar;
    @FXML
    private Button bAceptar;
    @FXML
    private Button bEliminarArchivo;
    @FXML
    private Button bDescargar;
    @FXML
    private Button bEliminarSolicitud;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        archivos = FXCollections.observableArrayList();
        configurarTablaArchivos();
        
    }
    
    public void inicializarFormulario(Desarrollador desarrolladorSesion, SolicitudDeCambio solicitudEdicion, 
            int totalSolicitudes, ObservadorSolicitudes observador){
        this.desarrolladorSesion = desarrolladorSesion;
        this.solicitudEdicion = solicitudEdicion;
        this.observador = observador;
        lbNombreProyecto.setText(desarrolladorSesion.getNombreProyecto());
        lbSolicitante.setText(desarrolladorSesion.getNombreCompleto());
        fechaActual = LocalDate.now();
        String fechaRegistro = fechaActual.format(formatoFecha);
        lbFechaRegistro.setText(fechaRegistro);
        this.totalSolicitudes = totalSolicitudes + 1;
        if(solicitudEdicion == null){
            configurarVentanaRegistrar();
        } else{
            cargarInformacionDetalles(solicitudEdicion);
        }
    }
    
    private void configurarVentanaRegistrar(){
        lnAprobado.setVisible(false);
        lnFechaAprovacion.setVisible(false);
        lbAprobadoPor.setVisible(false);
        lbFechaAprovacion.setVisible(false);
        bAceptar.setVisible(false);
        bRechazar.setVisible(false);
        bDescargar.setVisible(false);
        bEliminarSolicitud.setVisible(false);
        lbNumSolicitud.setText("Número de Solicitud: " +String.valueOf(this.totalSolicitudes));
        lbEstado.setText("Pendiente");
    }
    
    private void cargarInformacionDetalles(SolicitudDeCambio solicitudEdicion){
        if(solicitudEdicion.getAprobadoPor() == null){
            lbAprobadoPor.setText("Pendiente de revisión");
            lbFechaAprovacion.setText("Pendiente de revisión");
        } else{
            lbAprobadoPor.setText("Aprobado por: " + solicitudEdicion.getAprobadoPor());
            lbFechaAprovacion.setText("Fecha de aprobación: " + solicitudEdicion.getFechaAprobacion());
        }
        if(desarrolladorSesion != null){
            bAceptar.setVisible(false);
            bRechazar.setVisible(false);
        }
        if(solicitudEdicion.getIdEstado() != 1){
            bEliminarSolicitud.setVisible(false);
        }
        lbFechaRegistro.setText(solicitudEdicion.getFechaRegistro());
        lbNumSolicitud.setText("Número de Solicitud: " + solicitudEdicion.getNumSolicitud());
        bAgregar.setVisible(false);
        bCancelar.setVisible(false);
        bEliminarArchivo.setVisible(false);
        bRegistrar.setVisible(false);
        tfAccion.setText(solicitudEdicion.getAccionPropuesta());
        tfAccion.setEditable(false);
        tfDescripcion.setText(solicitudEdicion.getDescripcion());
        tfDescripcion.setEditable(false);
        tfImpacto.setText(solicitudEdicion.getImpacto());
        tfImpacto.setEditable(false);
        tfNombreSolicitud.setText(solicitudEdicion.getNombre());
        tfNombreSolicitud.setEditable(false);
        tfRazon.setText(solicitudEdicion.getRazon());
        tfRazon.setEditable(false);
        lbEstado.setText(solicitudEdicion.getEstado());
        cargarArchivos(solicitudEdicion.getIdSolicitud());
    }
    
    private void cargarArchivos(int idSolicitud){
        HashMap<String, Object> respuesta = new HashMap<>();
            respuesta = ArchivoDAO.obtenerArchivosSolicitud(idSolicitud);
        
        if(!(boolean) respuesta.get("error")){
            archivos = FXCollections.observableArrayList();
            ArrayList<Archivo> lista = (ArrayList<Archivo>) respuesta.get("archivos");
            archivos.addAll(lista);
            tvArchivo.setItems(archivos);
        }else{
            Utilidades.mostrarAletarSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void configurarTablaArchivos(){
        colNombreArchivo.setCellValueFactory(new PropertyValueFactory("nombreArchivo"));
    }

    @FXML
    private void btnRegistrar(ActionEvent event) {
        validarCampos();
    }
    
    private void registrarSolicitud(SolicitudDeCambio nuevaSolicitud) {
        HashMap<String, Object> respuesta = SolicitudDAO.registrarSolicitudDeCambio(nuevaSolicitud);
        if (!(boolean) respuesta.get("error")) {
            if (archivos != null && !archivos.isEmpty()) {
                for (Archivo archivo : archivos) {
                    archivo.setIdSolicitud((int) respuesta.get("idSolicitud"));
                    registrarArchivo(archivo);
                }
            Utilidades.mostrarAletarSimple("Registro exitoso", 
                        (String)respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
                observador.operacionExitosa("Registro", nuevaSolicitud.getNombre());
                cerrarVentana();
            } else {
                Utilidades.mostrarAletarSimple("Registro exitoso",
                        (String) respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
                observador.operacionExitosa("Registro", nuevaSolicitud.getNombre());
                cerrarVentana();
            }
        } else {
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
                solicitudValida.setIdEstado(1);
                solicitudValida.setNumSolicitud(totalSolicitudes);
                solicitudValida.setFechaRegistro(fechaRegistro);
                solicitudValida.setIdDesarrollador(desarrolladorSesion.getIdDesarrollador());
                solicitudValida.setNombreDesarrollador(desarrolladorSesion.getNombreCompleto());
                if(solicitudEdicion == null){
                    registrarSolicitud(solicitudValida);
                }else{
                    
                }   
            }
        } catch (NumberFormatException ex) {
            Utilidades.mostrarAletarSimple("Error al enviar", "Ocurrió un error al enviar la informacion", 
                    Alert.AlertType.WARNING);
            ex.printStackTrace();
        }
    }
    
    private void registrarArchivo(Archivo archivo){
        HashMap<String, Object> respuesta = ArchivoDAO.registrarArchivoDeSolicitud(archivo);
            if( (boolean) respuesta.get("error")){
                Utilidades.mostrarAletarSimple("Error en el registro", 
                        (String) respuesta.get("mensaje"), Alert.AlertType.WARNING);   
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
        try {
            FileChooser dialogoSeleccion = new FileChooser();
            dialogoSeleccion.setTitle("Selecciona el archivo");
            String etiquetaTipoDato = "Archivo PDF(*.pdf)";
            String extensionArchivo = "*.PDF";
            FileChooser.ExtensionFilter filtroSeleccion =
                    new FileChooser.ExtensionFilter(etiquetaTipoDato, extensionArchivo);
            dialogoSeleccion.getExtensionFilters().add(filtroSeleccion);
            Stage escenarioBase = (Stage) lbEstado.getScene().getWindow();
            List<File> archivosSeleccionados = dialogoSeleccion.showOpenMultipleDialog(escenarioBase);

            if (archivosSeleccionados != null) {
                for (File archivo : archivosSeleccionados) {
                    documentoSol = new Archivo();
                    documentoSol.setArchivo(Files.readAllBytes(archivo.toPath()));
                    documentoSol.setNombreArchivo(archivo.getName());
                    archivos.add(documentoSol);
                }
                tvArchivo.setItems(archivos);
            }
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("ERROR AL CARGAR", "Ha ocurrido un error"
                    + " al cargar los archivos", Alert.AlertType.WARNING);
        }
    }

    private void cerrarVentana(){
        Stage escenario = (Stage) tfNombreSolicitud.getScene().getWindow();
        escenario.close();
    }
    @FXML
    private void btnRechazarSolicitud(ActionEvent event) {
        solicitudEdicion.setIdEstado(3);
        modificarEstado(solicitudEdicion);
    }

    @FXML
    private void btnAceptarSolicitud(ActionEvent event) {
        solicitudEdicion.setIdEstado(2);
        modificarEstado(solicitudEdicion);
    }
    
    private void modificarEstado(SolicitudDeCambio solicitud){
        HashMap<String, Object> respuesta = SolicitudDAO.modificarEstadoSolicitud(solicitud);
            if( !(boolean) respuesta.get("error")){
                Utilidades.mostrarAletarSimple("Estado modificado", 
                        (String)respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
                observador.operacionExitosa("Modificacion", solicitud.getNombre());
                cerrarVentana();
            }else{
                Utilidades.mostrarAletarSimple("Error al modificar", 
                        (String) respuesta.get("mensaje"), Alert.AlertType.WARNING);
            }
    }

    @FXML
    private void btnEliminarArchivo(ActionEvent event) {
        Archivo archivoSeleccionado = tvArchivo.getSelectionModel().getSelectedItem();
        if (archivoSeleccionado != null) {
            archivos.remove(archivoSeleccionado);
            tvArchivo.setItems(archivos);
            Utilidades.mostrarAletarSimple("Eliminación exitosa", "Archivo eliminado correctamente", Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarAletarSimple("Error al eliminar", "Selecciona un archivo de la tabla para eliminar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnDescargarArchivos(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecciona la carpeta de destino");
        Stage escenarioBase = (Stage) lbEstado.getScene().getWindow();
        File carpetaDestino = directoryChooser.showDialog(escenarioBase);

        if (carpetaDestino != null) {
            try {
                for (Archivo archivo : archivos) {
                    File archivoDestino = new File(carpetaDestino.getAbsolutePath(), archivo.getNombreArchivo());
                    Files.write(archivoDestino.toPath(), archivo.getArchivo());
                }

                Utilidades.mostrarAletarSimple("Descarga exitosa", "Archivos descargados correctamente en " + carpetaDestino.getAbsolutePath(), Alert.AlertType.INFORMATION);
            } catch (IOException ex) {
                Utilidades.mostrarAletarSimple("Error al descargar", "Ha ocurrido un error al descargar los archivos", Alert.AlertType.WARNING);
                ex.printStackTrace();
            }
        }
    }
    
    @FXML
    private void btnEliminarSolicitud(ActionEvent event) {
        boolean eliminarSolicitud = Utilidades.mostrarDialogoConfirmacion("Cancelar proceso", 
                    "¿Estás seguro de que desea eliminar esta solicitud?");                            
            if(eliminarSolicitud){
                if(archivos.size() > 0){
                    HashMap<String, Object> respuesta = ArchivoDAO.eliminarArchivosSolicitud(solicitudEdicion.getIdSolicitud());
                    if( !(boolean) respuesta.get("error")){
                        eliminarSolicitud(solicitudEdicion.getIdSolicitud());
                    }else{
                        Utilidades.mostrarAletarSimple("Error al eliminar", 
                                (String) respuesta.get("mensaje"), Alert.AlertType.WARNING);
                    }
                } else{
                    eliminarSolicitud(solicitudEdicion.getIdSolicitud());
                }
            }
    }
    
    private void eliminarSolicitud(int idSolicitud){
        HashMap<String, Object> respuesta = SolicitudDAO.eliminarSolicitud(idSolicitud);
                if( !(boolean) respuesta.get("error")){
                    Utilidades.mostrarAletarSimple("Solicitud Eliminada", 
                            (String)respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
                    observador.operacionExitosa("Eliminacion", solicitudEdicion.getNombre());
                    cerrarVentana();
                }else{
                    Utilidades.mostrarAletarSimple("Error al eliminar", 
                            (String) respuesta.get("mensaje"), Alert.AlertType.WARNING);
                }
    }
}