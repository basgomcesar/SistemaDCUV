package sistemadcuv.modelo.pojo;


public class TipoArtefacto {
    private int idArtefacto;
    private String nombreTipoArtefacto;

    public TipoArtefacto() {
    }

    public void setIdArtefacto(int idArtefacto) {
        this.idArtefacto = idArtefacto;
    }

    public void setNombreTipoArtefacto(String nombreTipoArtefacto) {
        this.nombreTipoArtefacto = nombreTipoArtefacto;
    }

    public int getIdArtefacto() {
        return idArtefacto;
    }

    public String getNombreTipoArtefacto() {
        return nombreTipoArtefacto;
    }

    @Override
    public String toString() {
        return  nombreTipoArtefacto;
    }
}
