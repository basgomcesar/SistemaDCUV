package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Desarrollador;

public class ProyectoDAO {
    public static HashMap<String, Object> obtenerDesarrolladoresPorProyecto(int idProyecto){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idDesarrollador, nombreCompleto, semestre, matricula, nombreEstado as estado, contrasenia, Proyecto_idProyecto, correo " +
                    "FROM desarrollador, estadoDesarrollador " +
                    "WHERE Proyecto_idProyecto = ? AND nombreestado = 'Activo'";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idProyecto);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Desarrollador> desarrolladores = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Desarrollador desarrollador = new Desarrollador();
                    desarrollador.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    desarrollador.setNombreCompleto(resultado.getString("nombreCompleto"));
                    desarrollador.setSemestre(resultado.getInt("semestre"));
                    desarrollador.setMatricula(resultado.getString("matricula"));
                    desarrollador.setEstado(resultado.getString("estado"));
                    desarrollador.setContrasenia(resultado.getString("contrasenia"));
                    desarrollador.setIdProyecto(resultado.getInt("Proyecto_idProyecto"));
                    desarrollador.setCorreo(resultado.getString("correo"));
                    desarrolladores.add(desarrollador);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("desarrolladores", desarrolladores);
            } catch (SQLException ex) {
                respuesta.put("desarrolladores", "Error "+ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion,"
                    + "por favor intentelo mas tarde.");
        }
        return respuesta;
    }
}
