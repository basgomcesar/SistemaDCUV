package sistemadcuv.controladores;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.CambioDAO;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.modelo.pojo.SolicitudDeCambio;
import sistemadcuv.utils.Utilidades;


public class FXMLBitacoraDeCambiosGeneralController implements Initializable {

    private Desarrollador desarrolladorSesion;
    private ResponsableDeProyecto responsableSesion;
    @FXML
    private Label lbUsuarioActivo;
    @FXML
    private TableView<Cambio> tvCambios;
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
    @FXML
    private TextField tfNombre;
    
    private ObservableList<Cambio> cambios;
    private SortedList<Cambio> sortedListaCambios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    

    public void inicializarInformacion(Desarrollador desarrollador, ResponsableDeProyecto responsable) {
        this.desarrolladorSesion = desarrollador;
        this.responsableSesion = responsable;
        cargarInformacionCambios(desarrollador, responsable);
        if(desarrollador != null){
            lbUsuarioActivo.setText("Usuario: " + desarrollador.getNombreCompleto());
        }else{
            lbUsuarioActivo.setText("Usuario: " + responsable.getNombreCompleto());
        }
    }
    
    private void configurarTabla(){
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estado"));
        this.colDesarrollador.setCellValueFactory(new PropertyValueFactory("desarrollador"));
        this.colFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        this.colFechaFin.setCellValueFactory(new PropertyValueFactory("fechaFin"));
    }
    
    private void cargarInformacionCambios(Desarrollador desarrollador, ResponsableDeProyecto responsable){
        HashMap<String, Object> respuesta = new HashMap<>();
        if(desarrollador != null){
            respuesta = CambioDAO.obtenerListadoCambiosDesarrollador(desarrolladorSesion.getIdDesarrollador());
        } else{
            respuesta = CambioDAO.obtenerListadoCambios();
        }
        
        if(!(boolean) respuesta.get("error")){
            cambios = FXCollections.observableArrayList();
            ArrayList<Cambio> lista = (ArrayList<Cambio>) respuesta.get("cambios");
            cambios.addAll(lista);
            tvCambios.setItems(cambios);
            FilteredList<Cambio> filtradoBusquedas = new FilteredList<>(cambios, p-> true);
            busquedaTablaNombre(filtradoBusquedas);
        }else{
            Utilidades.mostrarAletarSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void busquedaTablaNombre(FilteredList<Cambio> filtradoBusquedas){
        if(cambios.size() > 0){
            tfNombre.textProperty().addListener(new ChangeListener<String>(){
                
                @Override
                public void changed(ObservableValue<? extends String> observable, 
                        String oldValue, String newValue) {
                    filtradoBusquedas.setPredicate(nombreFiltro -> {
                        //CASO DEFAULT
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        //CRITERIO DE EVALUACION
                        String lowerNewValue = newValue.toLowerCase();
                        if(nombreFiltro.getNombre().toLowerCase().contains(lowerNewValue)){
                            return true;
                        } else if(nombreFiltro.getNombre().toLowerCase().contains(newValue)){
                            return true;
                        }
                        return false;
                    });
                }
                
            });
            sortedListaCambios = new SortedList<>(filtradoBusquedas);
            sortedListaCambios.comparatorProperty().bind(tvCambios.comparatorProperty());
            tvCambios.setItems(sortedListaCambios);
        }
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

    @FXML
    private void btnExportarBitacoraCambios(ActionEvent event) throws DocumentException {
        DirectoryChooser directorioSeleccion = new DirectoryChooser();
        File directorio = directorioSeleccion.showDialog(tfNombre.getScene().getWindow());

        try {
            if (directorio != null) {
                String rutaArchivo = directorio.getAbsolutePath() + "/BitacoraDeCambios.pdf";
                Document documento = new Document();
                documento.setMargins(5, 5, 40, 30);
                PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
                documento.open();

                LocalDate fechaActual = LocalDate.now();
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String fechaDocumento = fechaActual.format(formatoFecha);
                documento.add(new Paragraph("\tBitácora de Cambios"));
                documento.add(new Paragraph("\t"+fechaDocumento));

                PdfPTable tabla = new PdfPTable(5);

                float[] columnWidths = {10f, 6f, 8f, 6f, 6f};
                tabla.setWidths(columnWidths);

                tabla.setSpacingBefore(10f);
                tabla.setSpacingAfter(10f);
                
                tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                tabla.addCell(getCell("Nombre", true));
                tabla.addCell(getCell("Estado", true));
                tabla.addCell(getCell("Desarrollador", true));
                tabla.addCell(getCell("Fecha Inicio", true));
                tabla.addCell(getCell("Fecha Fin", true));

                for (Cambio cambio : sortedListaCambios) {
                    tabla.addCell(getCell(cambio.getNombre(), false));
                    tabla.addCell(getCell(cambio.getEstado(), false));
                    tabla.addCell(getCell(cambio.getDesarrollador(), false));
                    tabla.addCell(getCell(cambio.getFechaInicio(), false));
                    tabla.addCell(getCell(cambio.getFechaFin(), false));
                }

                documento.add(tabla);

                documento.close();
                Utilidades.mostrarAletarSimple("Archivo Exportado", "La información se exportó correctamente"
                        + " en el directorio: " + rutaArchivo, Alert.AlertType.INFORMATION);
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAletarSimple("Error de Exportación", "No se ha podido guardar el archivo.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnBitacoraPorDesarrollador(MouseEvent event) {
        try {
            Stage escenarioBase = (Stage) tfNombre.getScene().getWindow();
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLListadoDeParticipantesParaBitacora.fxml");
            Parent vista = loader.load();
            FXMLListadoDeParticipantesParaBitacoraController controlador = loader.getController();
            controlador.inicializarInformacion(desarrolladorSesion, responsableSesion);
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle("Listado de participantes para bitacora");
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("Error al cargar ventana", 
                    ex.getMessage(), 
                    Alert.AlertType.INFORMATION);
        }
    }
    
    private PdfPCell getCell(String contenido, boolean esEncabezado) {
        PdfPCell cell = new PdfPCell(new Paragraph(contenido));

        cell.setHorizontalAlignment(esEncabezado ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_CENTER);
        cell.setBackgroundColor(esEncabezado ? BaseColor.LIGHT_GRAY : BaseColor.WHITE);

        return cell;
    }
}
