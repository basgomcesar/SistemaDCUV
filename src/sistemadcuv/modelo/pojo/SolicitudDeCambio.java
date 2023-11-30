package sistemadcuv.modelo.pojo;

public class SolicitudDeCambio {
    
    private int idSolicitud;
    private String nombre;
    private String accionPropuesta;
    private String descripcion;
    private String estatus;
    private String fechaRegistro;
    private String fechaAprobacion;
    private String impacto;    
    private int numSolicitud;
    private String razon;
    private int idDesarrollador;
    private String nombreDesarrollador;

    public SolicitudDeCambio() {
    }

    public SolicitudDeCambio(int idSolicitud, String nombre, String accionPropuesta, String descripcion, String estatus, String fechaRegistro, String fechaAprobacion, String impacto, int numSolicitud, String razon, int idDesarrollador, String nombreDesarrollador) {
        this.idSolicitud = idSolicitud;
        this.nombre = nombre;
        this.accionPropuesta = accionPropuesta;
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.fechaRegistro = fechaRegistro;
        this.fechaAprobacion = fechaAprobacion;
        this.impacto = impacto;
        this.numSolicitud = numSolicitud;
        this.razon = razon;
        this.idDesarrollador = idDesarrollador;
        this.nombreDesarrollador = nombreDesarrollador;
    }

    public int getIdDesarrollador() {
        return idDesarrollador;
    }

    public void setIdDesarrollador(int idDesarrollador) {
        this.idDesarrollador = idDesarrollador;
    }

    public String getNombreDesarrollador() {
        return nombreDesarrollador;
    }

    public void setNombreDesarrollador(String nombreDesarrollador) {
        this.nombreDesarrollador = nombreDesarrollador;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public int getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(int numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }
    
}
