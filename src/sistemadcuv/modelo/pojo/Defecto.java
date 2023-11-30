package sistemadcuv.modelo.pojo;

public class Defecto {
    
    private String nombre;
    private String tipo;
    private String estado;
    private String descripcion;
    private int esfuerzo;
    private String fechaReporte;

    public Defecto() {
    }

    public Defecto(String nombre, String tipo, String estado, String descripcion, int esfuerzo, String fechaReporte) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.descripcion = descripcion;
        this.esfuerzo = esfuerzo;
        this.fechaReporte = fechaReporte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEsfuerzo() {
        return esfuerzo;
    }

    public void setEsfuerzo(int esfuerzo) {
        this.esfuerzo = esfuerzo;
    }

    public String getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(String fechaReporte) {
        this.fechaReporte = fechaReporte;
    }
    
}
