package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Desarrollador;

public class DesarrolladorDAO {
    public static HashMap<String, Object> verificarSesionDesarrollador(String usuario,String contrasenia){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idDesarrollador, nombreCompleto, "+
                        "semestre, matricula ,correo , estado, contrasenia, Proyecto_idProyecto,nombre \n" +
                        "FROM " +
                        "desarrollador, proyecto \n" +
                        "WHERE matricula = ? AND contrasenia = ? AND Proyecto_idProyecto = idProyecto";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setString(1, usuario);
                prepararSentencia.setString(2, contrasenia);
                ResultSet resultado = prepararSentencia.executeQuery();
                if(resultado.next()){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Credenciales correctas");
                    Desarrollador desarrollador = new Desarrollador();                    
                    desarrollador.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    desarrollador.setNombreCompleto(resultado.getString("nombreCompleto"));
                    desarrollador.setSemestre(resultado.getInt("semestre"));
                    desarrollador.setMatricula("matricula");
                    desarrollador.setEstado(resultado.getString("estado"));
                    desarrollador.setContrasenia(resultado.getString("contrasenia"));
                    desarrollador.setIdProyecto(resultado.getInt("Proyecto_idProyecto"));
                    desarrollador.setNombreProyecto(resultado.getString("nombre"));
                    desarrollador.setCorreo(resultado.getString("correo"));
                    respuesta.put("desarrollador", desarrollador);
                }else{
                    respuesta.put("mensaje", "Las credenciales son incorrectas");
                }
                conexionBD.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                respuesta.put("desarrollador", "Error "+ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion,"
                    + "por favor intentelo mas tarde.");
        }
        return respuesta;
    }
}
