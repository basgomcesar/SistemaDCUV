package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Actividad;

public class ActividadDAO {

    public static HashMap<String, Object> registrarActividadPendiente(Actividad nuevaActividad) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO actividad(titulo,descripcion, "
                        + "Desarrollador_idDesarrollador, EstadoAsignacion_idEstadoAsignacion,fechaInicio,fechaFin) " +
                    "VALUES (? ,? ,? ,1 ,? ,?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, nuevaActividad.getTitulo());
                prepararSentencia.setString(2, nuevaActividad.getDescripcion());
                prepararSentencia.setInt(3, nuevaActividad.getIdDesarrollador());
                prepararSentencia.setString(4, nuevaActividad.getFechaInicio());
                prepararSentencia.setString(5, nuevaActividad.getFechaFin());
                int filasAfectadas = prepararSentencia.executeUpdate();
                ResultSet generatedKeys = prepararSentencia.getGeneratedKeys();
                if (generatedKeys.next()){
                    int idActividad = generatedKeys.getInt(1);
                    respuesta.put("idActividad", idActividad);
                }
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Actividad registrada");
                }else{                    
                    respuesta.put("mensaje", "Error al registrar");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion, "
                    + "intentalo más tarde");
        }
        return respuesta;
    }

    public static HashMap<String, Object> registrarActividadSinAsignar(Actividad nuevaActividad) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO actividad(titulo,descripcion, "
                        + " EstadoAsignacion_idEstadoAsignacion,fechaInicio,fechaFin) " +
                    "VALUES (? ,? ,3 ,? ,?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, nuevaActividad.getTitulo());
                prepararSentencia.setString(2, nuevaActividad.getDescripcion());
                prepararSentencia.setString(3, nuevaActividad.getFechaInicio());
                prepararSentencia.setString(4, nuevaActividad.getFechaFin());
                int filasAfectadas = prepararSentencia.executeUpdate();
                ResultSet generatedKeys = prepararSentencia.getGeneratedKeys();
                if (generatedKeys.next()){
                    int idActividad = generatedKeys.getInt(1);
                    respuesta.put("idActividad", idActividad);
                }
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Actividad registrada");
                }else{                    
                    respuesta.put("mensaje", "Error al registrar");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion, "
                    + "intentalo más tarde");
        }
        return respuesta;
    }
    
}
