package sistemadcuv.modelo.pojo;

public class Periodo {
    private int idPeriodo;
    private String fechaInicio;
    private String fechaFin;

    public Periodo() {
    }

    public Periodo(int idPeriodo, String fechaInicio, String fechaFin) {
        this.idPeriodo = idPeriodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }
}
