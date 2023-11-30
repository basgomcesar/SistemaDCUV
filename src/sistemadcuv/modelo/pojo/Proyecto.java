package sistemadcuv.modelo.pojo;

public class Proyecto {
    
    private String nombre;
    private String estado;
    private String fechaInicio;
    private String fechaFin;

    public Proyecto() {
    }

    public Proyecto(String nombre, String estado, String fechaInicio, String fechaFin) {
        this.nombre = nombre;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
