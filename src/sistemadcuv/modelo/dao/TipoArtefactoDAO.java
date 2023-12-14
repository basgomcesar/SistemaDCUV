package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.modelo.pojo.TipoArtefacto;


public class TipoArtefactoDAO {
    public static HashMap<String, Object> obtenerTiposDeArtefactos(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta ="SELECT idTipoArtefacto,nombreTipoArtefacto FROM sistemadcuv.tipoartefacto;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<TipoArtefacto> tiposArtefactos = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    TipoArtefacto tipoArtefacto = new TipoArtefacto();
                    tipoArtefacto.setIdArtefacto(resultado.getInt("idTipoArtefacto"));
                    tipoArtefacto.setNombreTipoArtefacto(
                            resultado.getString("nombreTipoArtefacto"));
                    tiposArtefactos.add(tipoArtefacto);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("tiposArtefactos", tiposArtefactos);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        } else{
            respuesta.put("mensaje", 
                    "Error al acceder a la base de datos,"
                            + "intenta mas tarde");
        }
        return respuesta;
    }
}
