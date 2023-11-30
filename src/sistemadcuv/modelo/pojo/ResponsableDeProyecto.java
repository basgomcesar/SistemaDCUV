package sistemadcuv.modelo.pojo;

public class ResponsableDeProyecto {
    
    private int idResponsable;
    private String nombreCompleto;
    private int numeroPersonal;
    private String correoElectronico;
    private String contrasenia;
    private int idProyecto;
    private String nombreProyecto;

    public ResponsableDeProyecto() {
    }

    public ResponsableDeProyecto(String nombreCompleto, int numeroPersonal, String correoElectronico) {
        this.nombreCompleto = nombreCompleto;
        this.numeroPersonal = numeroPersonal;
        this.correoElectronico = correoElectronico;
    }

    public int getIdResponsable() {
        return idResponsable;
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

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getNumeroPersonal() {
        return numeroPersonal;
    }

    public void setNumeroPersonal(int numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

}
