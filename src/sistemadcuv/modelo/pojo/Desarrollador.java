package sistemadcuv.modelo.pojo;

public class Desarrollador {
    private int idDesarrollador;
    private String nombreCompleto;
    private String estado;
    private String matricula;
    private String correo;
    private int semestre;
    private String contrasenia;
    private int idProyecto;
    private String nombreProyecto;
    private int idPeriodo;
    private int idMateria;
    
    public Desarrollador() {
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public void setIdDesarrollador(int idDesarrollador) {
        this.idDesarrollador = idDesarrollador;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getIdDesarrollador() {
        return idDesarrollador;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
        
}
