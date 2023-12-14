package sistemadcuv.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.DesarrolladorDAO;
import sistemadcuv.modelo.dao.MateriaDAO;
import sistemadcuv.modelo.dao.PeriodoDAO;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.Materia;
import sistemadcuv.modelo.pojo.Periodo;
import sistemadcuv.observador.ObservadorDesarrolladores;
import sistemadcuv.utils.Utilidades;

public class FXMLRegistrarDesarrolladorController {
    private ObservableList<Materia> observableMaterias;
    private int idProyecto;
    private Periodo periodoActual;
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
    @FXML
    private ComboBox<Materia> cbMateria;
    @FXML
    private Label lbFechaInicio;
    @FXML
    private Label lbFechaFin;

    public void inicializarInformacion(int idProyecto,ObservadorDesarrolladores observador) {
        configurarSppinerSemestre();
        cargarInformacionMaterias();
        cargarPeriodo();
        this.idProyecto = idProyecto;
        this.observador = observador;
    }
    private void cargarInformacionMaterias(){
        HashMap<String, Object> resultado = MateriaDAO.obtenerMaterias();
        if(!(boolean)resultado.get("error")){
            observableMaterias = FXCollections.observableArrayList();
            ArrayList<Materia> materias = (ArrayList<Materia>) resultado.get("materias");
            observableMaterias.addAll(materias);
            cbMateria.setItems(observableMaterias);
        }else
            Utilidades.mostrarAletarSimple("Error materias",
                    (String) resultado.get("mensaje"), 
                    Alert.AlertType.ERROR);
    }
    @FXML
    public void clicCancelar(ActionEvent event) {
        if(!hayCamposLlenos())
            cerrarVentana();
        else if (Utilidades.mostrarDialogoConfirmacion(
                "Dialogo de confirmación",
                "¿Desea cancelar el registro? tenga en cuenta  que se perdera la información"))
            cerrarVentana();
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if(!esCamposVacios()){
            if(Utilidades.matriculaValida(tfMatricula.getText().trim())){
                if(!esMatriculaExistente()){
                    if(Utilidades.correoValido(tfCorreo.getText())){
                        registrarDesarrollador();
                    }else{
                        Utilidades.mostrarAletarSimple("Correo invalido", 
                                "Por favor ingrese un correo valido",
                                Alert.AlertType.INFORMATION);
                    }
                }else{
                    Utilidades.mostrarAletarSimple(
                            "Matricula existente", 
                            "La matricula que ingreso le pertenece a "
                                    + "un desarrollador existente", 
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
        desarrollador.setSemestre(spSemestre.getValue());
        desarrollador.setMatricula(tfMatricula.getText().trim());
        desarrollador.setContrasenia(tfContrasenia.getText().trim());
        desarrollador.setIdProyecto(idProyecto);
        desarrollador.setCorreo(tfCorreo.getText().trim());
        desarrollador.setIdPeriodo(periodoActual.getIdPeriodo());
        desarrollador.setIdMateria(cbMateria.getSelectionModel().
                getSelectedItem().getIdMateria());
        HashMap<String, Object> resultado = DesarrolladorDAO.
                registrarDesarrollador(desarrollador);
        if(!((boolean) resultado.get("error"))){
            Utilidades.mostrarAletarSimple("Mensaje de exito", 
                    (String) resultado.get("mensaje"), 
                    Alert.AlertType.INFORMATION);
            observador.operacionExitosa("Operacion exitosa", 
                    desarrollador.getNombreCompleto() );
            cerrarVentana();
        }else
            Utilidades.mostrarAletarSimple("Error al registrar", 
                    (String) resultado.get("mensaje"), 
                    Alert.AlertType.ERROR);
    }
    private void cargarPeriodo() {
        HashMap<String, Object> resultado = PeriodoDAO.
                obtenerPeriodoActual(Utilidades.obtenerFechaActual());
        if(!(boolean)resultado.get("error")){
            periodoActual = (Periodo) resultado.get("periodo");
            lbFechaInicio.setText(Utilidades.formatearFecha(periodoActual.
                    getFechaInicio()));
            lbFechaFin.setText(Utilidades.formatearFecha(periodoActual.
                    getFechaFin()));
        }else{
            Utilidades.mostrarAletarSimple(
                    "Error al cargar periodo",
                    "Hubo un error al cargar el periodo", 
                    Alert.AlertType.ERROR);
        }
    }

    private boolean esMatriculaExistente() {
        HashMap<String, Object> resultado = DesarrolladorDAO.
        verificarMatriculaExistente(tfMatricula.getText().trim());
        if(!((boolean) resultado.get("error"))){
            if( (int) resultado.get("numMatricula") > 0){
                return true;
            }else{
                return false;
            }
        }else
            Utilidades.mostrarAletarSimple("Error al verificar matricula", 
                    (String) resultado.get("mensaje"), 
                    Alert.AlertType.ERROR);
        return false;
    }

}
