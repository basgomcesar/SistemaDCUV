package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.Materia;


public class MateriaDAO {
    public static HashMap<String, Object> obtenerMaterias(){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idExperieciaEducativa, nombreExperiencia from experieciaeducativa;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Materia> materias = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Materia materia = new Materia();
                    materia.setIdMateria(resultado.getInt("idExperieciaEducativa"));
                    materia.setNombreMateria(resultado.getString("nombreExperiencia"));
                    materias.add(materia);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("materias", materias);
            } catch (SQLException ex) {
                respuesta.put("materias", "Error al obtener las materias");
            }
        }else{
            respuesta.put("mensaje", "Error al acceder a la base de datos,"
                    + "intenta mas tarde.");
        }
        return respuesta;
    }
}
