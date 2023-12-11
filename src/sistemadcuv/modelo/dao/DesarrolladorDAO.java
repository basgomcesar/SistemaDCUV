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
                String consulta = "SELECT idDesarrollador, nombreCompleto, " +
                    "semestre, matricula ,correo , nombreEstado as estado, contrasenia, Proyecto_idProyecto,nombre " +
                    "FROM " +
                    "desarrollador, proyecto, estadoUsuario " +
                    "WHERE matricula = ? AND contrasenia = ? AND Proyecto_idProyecto = idProyecto and nombreEstado = 'Activo'";
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
    public static HashMap<String,Object> verificarAsignaciones(Desarrollador desarrollador){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "select " +
                    "(select count(*) from cambio c,estadoAsignacion ea where ea.idEstadoAsignacion = c.EstadoAsignacion_idEstadoAsignacion and c.Desarrollador_idDesarrollador = d.idDesarrollador and ea.nombreEstado = 'pendiente')+ " +
                    "(select count(*) from actividad a,estadoAsignacion ea where ea.idEstadoAsignacion = a.EstadoAsignacion_idEstadoAsignacion and a.Desarrollador_idDesarrollador = d.idDesarrollador and ea.nombreEstado = 'pendiente') "+
                    " AS total_asignaciones_pendientes " +
                    "FROM desarrollador d " +
                    "where " +
                    " d.idDesarrollador = ?;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, desarrollador.getIdDesarrollador());
                ResultSet resultado = prepararSentencia.executeQuery();
                if(resultado.next()){
                    respuesta.put("asignaciones", resultado.getInt("total_asignaciones_pendientes"));
                }else{
                    respuesta.put("mensaje", "Las credenciales son incorrectas");
                }
                conexionBD.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                respuesta.put("asignaciones", "Error "+ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion,"
                    + "por favor intentelo mas tarde.");
        }
        return respuesta;
    }
    public static HashMap<String, Object> eliminarDesarrollador(Desarrollador desarrollador){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "UPDATE desarrollador SET EstadoDesarrollador_idEstadoDesarrollador = 2 "  +
                                  "WHERE idDesarrollador = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, desarrollador.getIdDesarrollador());
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas > 0){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Desarrollador eliminado con exito");
                }else{
                    respuesta.put("mensaje", "Hubo un error al intentar modificar la informacion del paciente, por favor intentalo mas tarde");
                }
                respuesta.put("error", false);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: "+ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion,"
                    + "por favor intentelo mas tarde.");
        }
        return respuesta;
    }
    public static HashMap<String, Object> registrarDesarrollador(Desarrollador desarrollador){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);        
        Connection conexionBD = ConexionBD.obtenerConexion();       
        if(conexionBD != null){
            try {
                String consulta = "INSERT INTO desarrollador " +
                    "(nombreCompleto,semestre,matricula,contrasenia,Proyecto_idProyecto,correo,Periodo_idPeriodo, " +
                    "estadoDesarrollador_idEstadoDesarrollador,ExperieciaEducativa_idExperieciaEducativa) " +
                    "VALUES (?,?,?,?,?,?,?,1,?); ";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setString(1, desarrollador.getNombreCompleto());
                prepararSentencia.setInt(2, desarrollador.getSemestre());
                prepararSentencia.setString(3, desarrollador.getMatricula());
                prepararSentencia.setString(4, desarrollador.getContrasenia());
                prepararSentencia.setInt(5, desarrollador.getIdProyecto());
                prepararSentencia.setString(6, desarrollador.getCorreo());
                prepararSentencia.setInt(7, desarrollador.getIdPeriodo());
                prepararSentencia.setInt(8, desarrollador.getIdMateria());
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas > 0){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El desarrollador fue guardado con exito");
                }else{
                    respuesta.put("mensaje", "Hubo un error al intentar registrar la informacion del desarrollador,"
                            + "  por favor intentalo mas tarde");
                }
                respuesta.put("error", false);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: "+ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion,"
                    + "por favor intentelo mas tarde.");
        }
        return respuesta;
    }
}
