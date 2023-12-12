package sistemadcuv.modelo.pojo;

public class Archivo {
    private int idArchivo;
    private byte[] archivo;
    private String nombreArchivo;
    private int idSolicitud;
    private int idCambio;
    private int idActividad;

    public Archivo() {
    }

    public Archivo(int idArchivo, byte[] archivo, String nombreArchivo, int idSolicitud, int idCambio, int idActividad) {
        this.idArchivo = idArchivo;
        this.archivo = archivo;
        this.nombreArchivo = nombreArchivo;
        this.idSolicitud = idSolicitud;
        this.idCambio = idCambio;
        this.idActividad = idActividad;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdCambio() {
        return idCambio;
    }

    public void setIdCambio(int idCambio) {
        this.idCambio = idCambio;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }
}
