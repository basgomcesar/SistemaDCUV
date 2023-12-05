package sistemadcuv.modelo.pojo;

public class Archivo {
    private byte[] archivo;
    private String nombreArchivo;

    public Archivo() {
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
