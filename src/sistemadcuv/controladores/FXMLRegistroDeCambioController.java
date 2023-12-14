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
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import sistemadcuv.modelo.dao.ArchivoDAO;
import sistemadcuv.modelo.dao.CambioDAO;
import sistemadcuv.modelo.dao.TipoArtefactoDAO;
import sistemadcuv.modelo.pojo.Archivo;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.modelo.pojo.TipoArtefacto;
import sistemadcuv.observador.ObservadorCambios;
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
    private Label lbNombreProyecto;
    @FXML
    private Label lbDesarrollador;
    @FXML
    private TextField tfEsfuerzo;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFin;
    @FXML
    private TableView<Archivo> tvArchivo;
    @FXML
    private TableColumn colNombreArchivo;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEliminar;
    private Desarrollador desarrolladorSesion;
    private ResponsableDeProyecto responsableSesion;
    @FXML
    private ComboBox<TipoArtefacto> cbTipoCambio;
    private ObservableList<TipoArtefacto> tiposDeCambios;
    private Archivo documentoCambio;
    private ObservableList<Archivo> archivos;
    private ObservadorCambios observador;
    private Cambio cambioSeleccion;
    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @FXML
    private Button bDescargar;
    @FXML
    private Button bGuardar;
    @FXML
    private Button bCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tiposDeCambios = FXCollections.observableArrayList();
        archivos = FXCollections.observableArrayList();
        configurarCampoEsfuerzo();
        cargarInformacionTiposArtefactos();
        configurarTablaArchivos();
    }    
    private void configurarTablaArchivos(){
        colNombreArchivo.setCellValueFactory(new PropertyValueFactory("nombreArchivo"));
    }
    @FXML
    private void clicCrear(ActionEvent event) {
        if(!esCamposVacios()){
            guardarCambio();
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
                cerrarVentana();
            }
    }


    @FXML
    private void clicEliminar(ActionEvent event) {
        Archivo archivoSeleccionado = tvArchivo.getSelectionModel().getSelectedItem();
        if (archivoSeleccionado != null) {
            archivos.remove(archivoSeleccionado);
            tvArchivo.setItems(archivos);
            Utilidades.mostrarAletarSimple("Eliminación exitosa", "Archivo eliminado correctamente", Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarAletarSimple("Error al eliminar", "Selecciona un archivo de la tabla para eliminar", Alert.AlertType.WARNING);
        }   
    }

    void inicializarFormulario(Cambio cambioSeleccion, Desarrollador desarrolladorSesion,ResponsableDeProyecto responsableSesion , ObservadorCambios observador) {
        this.responsableSesion = responsableSesion;
        this.cambioSeleccion = cambioSeleccion;
        this.desarrolladorSesion = desarrolladorSesion;
        this.observador = observador;
        if(desarrolladorSesion != null){
            lbNombreProyecto.setText(desarrolladorSesion.getNombreProyecto());
            lbDesarrollador.setText(desarrolladorSesion.getNombreCompleto());
        } else{
            lbNombreProyecto.setText(responsableSesion.getNombreProyecto());
            lbDesarrollador.setText(cambioSeleccion.getDesarrollador());
        }
        
        if(cambioSeleccion != null){
            cargarDetallesCambio(cambioSeleccion);
        } else{
            configurarDatePicker();
            bDescargar.setVisible(false);
            bGuardar.setVisible(false);
            bCerrar.setVisible(false);
        }
        
    }
    
    private void cargarDetallesCambio(Cambio cambioSeleccion){
        btnAgregar.setVisible(false);
        btnCancelar.setVisible(false);
        btnCrear.setVisible(false);
        btnEliminar.setVisible(false);
        tfDescripcion.setText(cambioSeleccion.getDescripcion());
        tfDescripcion.setEditable(false);
        tfEsfuerzo.setText(Integer.toString(cambioSeleccion.getEsfuerzo()));
        tfImpacto.setText(cambioSeleccion.getImpacto());
        tfImpacto.setEditable(false);
        tfNombreCambio.setText(cambioSeleccion.getNombre());
        tfNombreCambio.setEditable(false);
        tfRazon.setText(cambioSeleccion.getRazonCambio());
        tfRazon.setEditable(false);
        LocalDate fechaInicio = LocalDate.parse(cambioSeleccion.getFechaInicio(), formatoFecha);
        dpInicio.setValue(fechaInicio);
        dpInicio.setDisable(true);
        int posicionTipoArtefacto = buscarTipoArtefacto(this.cambioSeleccion.getIdTipoCambio());
        cbTipoCambio.getSelectionModel().select(posicionTipoArtefacto);
        cbTipoCambio.setDisable(true);
        cargarArchivos(cambioSeleccion.getIdCambio());
        if(responsableSesion != null||cambioSeleccion.getIdEstado() != 1){
            deshabilitarEdicion(cambioSeleccion);
        }else{
            habilitarFechaFin();
        }

    }
    
    private void deshabilitarEdicion(Cambio cambioSeleccion){
        if(cambioSeleccion.getFechaFin() != null){
            LocalDate fechaFin = LocalDate.parse(cambioSeleccion.getFechaFin(), formatoFecha);
            dpFin.setValue(fechaFin);
        }
            dpFin.setEditable(false);
            dpFin.setDisable(true);
            tfEsfuerzo.setEditable(false);
            bGuardar.setVisible(false);
    }
    
    private void cargarArchivos(int idCambio){
        HashMap<String, Object> respuesta = new HashMap<>();
            respuesta = ArchivoDAO.obtenerArchivosCambio(idCambio);
        
        if(!(boolean) respuesta.get("error")){
            archivos = FXCollections.observableArrayList();
            ArrayList<Archivo> lista = (ArrayList<Archivo>) respuesta.get("archivos");
            archivos.addAll(lista);
            tvArchivo.setItems(archivos);
        }else{
            Utilidades.mostrarAletarSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean esCamposVacios(){
        return tfNombreCambio.getText().trim().isEmpty() ||
                tfDescripcion.getText().trim().isEmpty() ||
                tfEsfuerzo.getText().trim().isEmpty() ||
                dpInicio.getValue() == null ||
                dpFin.getValue() == null ||
                tfRazon.getText().trim().isEmpty() ||
                tfImpacto.getText().trim().isEmpty() ||
                cbTipoCambio.getSelectionModel().getSelectedItem() ==null;
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

    private void cargarInformacionTiposArtefactos() {
        HashMap<String, Object> resultado = TipoArtefactoDAO.
                obtenerTiposDeArtefactos();
        if(!(boolean) resultado.get("error")){
            ArrayList<TipoArtefacto> tiposCambio = (ArrayList<TipoArtefacto>) resultado.
                    get("tiposArtefactos");
            tiposDeCambios.addAll(tiposCambio);
            cbTipoCambio.setItems(tiposDeCambios);
        }else{
            Utilidades.mostrarAletarSimple("Error",
                    "Error al cargar los tipos de cambios", 
                    Alert.AlertType.ERROR);
        }
    }

    private void habilitarFechaFin() {
        this.dpFin.setDisable(false);
        this.dpFin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(dpInicio.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Estilo para las fechas deshabilitadas
                }
            }
        });
    }
    @FXML
    private void clicAgregarArchivo(ActionEvent event) {
        try {
            FileChooser dialogoSeleccion = new FileChooser();
            dialogoSeleccion.setTitle("Selecciona el archivo");
            String etiquetaTipoDato = "Archivo PDF(*.pdf)";
            String extensionArchivo = "*.PDF";
            FileChooser.ExtensionFilter filtroSeleccion =
                    new FileChooser.ExtensionFilter(etiquetaTipoDato, extensionArchivo);
            dialogoSeleccion.getExtensionFilters().add(filtroSeleccion);
            Stage escenarioBase = (Stage) btnCancelar.getScene().getWindow();
            List<File> archivosSeleccionados = dialogoSeleccion.showOpenMultipleDialog(escenarioBase);

            if (archivosSeleccionados != null) {
                for (File archivo : archivosSeleccionados) {
                    documentoCambio = new Archivo();
                    documentoCambio.setArchivo(Files.readAllBytes(archivo.toPath()));
                    documentoCambio.setNombreArchivo(archivo.getName());
                    archivos.add(documentoCambio);
                }
                tvArchivo.setItems(archivos);
            }
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("ERROR AL CARGAR", "Ha ocurrido un error"
                    + " al cargar los archivos", Alert.AlertType.WARNING);
        }
    }
    private void configurarDatePicker() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("es", "ES"));
        dpInicio.setConverter(new LocalDateStringConverter(formatter,null));
        dpFin.setConverter(new LocalDateStringConverter(formatter,null));
        this.dpInicio.setEditable(false);
        this.dpFin.setDisable(true);
    }

    @FXML
    private void clicDpInicio(ActionEvent event) {
        habilitarFechaFin();
        if(dpFin.getValue()!=null){
            if(dpFin.getValue().isBefore(dpInicio.getValue()))
                    dpFin.setValue(dpInicio.getValue());
            this.dpFin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date.isBefore(dpInicio.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                });
        }
    }

    private void guardarCambio() {
        Cambio nuevoCambio = new Cambio();
        nuevoCambio.setNombre(tfNombreCambio.getText());
        nuevoCambio.setDescripcion(tfDescripcion.getText());
        nuevoCambio.setEsfuerzo(Integer.parseInt(tfEsfuerzo.getText()));
        nuevoCambio.setFechaInicio(dpInicio.getValue().toString());
        nuevoCambio.setFechaFin(dpFin.getValue().toString());
        nuevoCambio.setImpacto(tfImpacto.getText());
        nuevoCambio.setRazonCambio(tfRazon.getText());
        nuevoCambio.setIdDesarrollador(this.desarrolladorSesion.getIdDesarrollador());
        nuevoCambio.setIdTipoCambio(cbTipoCambio.getSelectionModel().
                getSelectedItem().
                getIdArtefacto());
        HashMap<String, Object> respuesta = CambioDAO.registrarCambioBajoImpacto(nuevoCambio);
        if(!(boolean)respuesta.get("error")){
            if (archivos != null && !archivos.isEmpty()){
                for (Archivo archivo : archivos) {
                    archivo.setIdCambio((int) respuesta.get("idCambio"));
                    registrarArchivo(archivo);
                }  
            }
            this.observador.operacionExitosa("Operacion exitosa", 
            nuevoCambio.getNombre() );
            Utilidades.mostrarAletarSimple("Registro exitoso", 
                    "El cambio se ha registrado ", 
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAletarSimple("Error", 
                    "Error al intentar guardar el cambio", 
                    Alert.AlertType.ERROR);
        }
    } 

    private void registrarArchivo(Archivo archivo) {
        HashMap<String, Object> respuesta = ArchivoDAO.registrarArchivoDeCambio(archivo);
            if( (boolean) respuesta.get("error")){
                Utilidades.mostrarAletarSimple("Error en el registro", 
                        (String) respuesta.get("mensaje"), Alert.AlertType.WARNING);   
            }
    }
    
    private int buscarTipoArtefacto(int idTipoArtefacto){
        for (int i = 0; i < tiposDeCambios.size(); i++) {
            if(tiposDeCambios.get(i).getIdArtefacto() == idTipoArtefacto)
                return i;
        }
        return 0;
    }
    
    @FXML
    private void btnDescargarArchivos(ActionEvent event) {
        
        if(archivos.size() > 0){
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Selecciona la carpeta de destino");
            Stage escenarioBase = (Stage) lbDesarrollador.getScene().getWindow();
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
                }
            }
        }else{
            Utilidades.mostrarAletarSimple("Sin Archivos", "No existen archivos para descargar", Alert.AlertType.INFORMATION);
        }
    }

    private void cerrarVentana() {
        Stage escenario = (Stage) btnCancelar.getScene().getWindow();
        escenario.close();
    }

    @FXML
    private void btnGuardarCambios(ActionEvent event) {
        if(!esCamposVacios()){
            modificarCambio();
        }else{
            Utilidades.mostrarAletarSimple("Campos vacios",
                    "Por favor asegurese de llenar "
                            + "los campos correspondientes", 
                    Alert.AlertType.INFORMATION);
        }
    }
    
    private void modificarCambio(){
        cambioSeleccion.setEsfuerzo(Integer.parseInt(tfEsfuerzo.getText()));
        cambioSeleccion.setFechaFin(dpFin.getValue().toString());
        cambioSeleccion.setIdEstado(2);
        HashMap<String, Object> respuesta = CambioDAO.modificarCambio(cambioSeleccion);
            if( !(boolean) respuesta.get("error")){
                Utilidades.mostrarAletarSimple("Estado modificado", 
                        (String)respuesta.get("mensaje"), Alert.AlertType.INFORMATION);
                observador.operacionExitosa("Modificacion", cambioSeleccion.getNombre());
                cerrarVentana();
            }else{
                Utilidades.mostrarAletarSimple("Error al modificar", 
                        (String) respuesta.get("mensaje"), Alert.AlertType.WARNING);
            }
    }

    @FXML
    private void btnCerrarVentana(ActionEvent event) {
        cerrarVentana();
    }
    
}
