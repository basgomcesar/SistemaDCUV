package sistemadcuv.modelo.pojo;

public class Actividad {
    
    private String titulo;
    private String descripcion;
    private String estado;
    private String fechaInicio;
    private String fechaFin;
    private String desarrollador;
    private int idDesarrollador;
    private int idActividad;
    private int idEstado;
    
    public Actividad() {
    }

    public Actividad(String titulo, String descripcion, String estado, String fechaInicio, String fechaFin, String desarrollador, int idDesarrollador, int idActividad, int idEstado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.desarrollador = desarrollador;
        this.idDesarrollador = idDesarrollador;
        this.idActividad = idActividad;
        this.idEstado = idEstado;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
    
    public void setIdDesarrollador(int idDesarrollador) {
        this.idDesarrollador = idDesarrollador;
    }

    public int getIdDesarrollador() {
        return idDesarrollador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
    
}
