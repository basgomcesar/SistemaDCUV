package sistemadcuv.modelo.pojo;

public class Cambio {
    
    private int idCambio;
    private String accionPropuesta;
    private String descripcion;
    private String esfuerzo;
    private String estado;
    private String fechaInicio;
    private String fechaFin;
    private String impacto;
    private String nombre;
    private String razonCambio;
    private String tipo;
    private int idDesarrollador;
    private String desarrollador;

    public Cambio() {
    }

    public Cambio(int idCambio, String accionPropuesta, String descripcion, String esfuerzo, String estado, String fechaInicio, String fechaFin, String impacto, String nombre, String razonCambio, String tipo, int idDesarrollador, String desarrollador) {
        this.idCambio = idCambio;
        this.accionPropuesta = accionPropuesta;
        this.descripcion = descripcion;
        this.esfuerzo = esfuerzo;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.impacto = impacto;
        this.nombre = nombre;
        this.razonCambio = razonCambio;
        this.tipo = tipo;
        this.idDesarrollador = idDesarrollador;
        this.desarrollador = desarrollador;
    }

    public int getIdDesarrollador() {
        return idDesarrollador;
    }

    public void setIdDesarrollador(int idDesarrollador) {
        this.idDesarrollador = idDesarrollador;
    }

    public int getIdCambio() {
        return idCambio;
    }

    public void setIdCambio(int idCambio) {
        this.idCambio = idCambio;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }
    
    public String getAccionPropuesta() {
        return accionPropuesta;
    }

    public void setAccionPropuesta(String accionPropuesta) {
        this.accionPropuesta = accionPropuesta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEsfuerzo() {
        return esfuerzo;
    }

    public void setEsfuerzo(String esfuerzo) {
        this.esfuerzo = esfuerzo;
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

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonCambio() {
        return razonCambio;
    }

    public void setRazonCambio(String razonCambio) {
        this.razonCambio = razonCambio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
       
}
