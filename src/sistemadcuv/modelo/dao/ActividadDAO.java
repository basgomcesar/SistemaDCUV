package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Actividad;

public class ActividadDAO {

    public static HashMap<String, Object> registrarActividadPendiente(Actividad nuevaActividad) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO actividad(titulo,descripcion, "+
                         "Desarrollador_idDesarrollador, EstadoAsignacion_idEstadoAsignacion, "+
                         "fechaInicio,fechaFin) " +
                    "VALUES (? ,? ,? ,1 ,? ,?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, 
                        Statement.RETURN_GENERATED_KEYS);
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
                    respuesta.put("mensaje", "Actividad creada con exito");
                }else{                    
                    respuesta.put("mensaje", "Error al registrar");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: al registrar una actividad");
            }
        }else{
            respuesta.put("mensaje", "Error al acceder a la base de datos, "
                    + "intenta más tarde");
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
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, 
                        Statement.RETURN_GENERATED_KEYS);
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
                    respuesta.put("mensaje", "Actividad creada con exito");
                }else{                    
                    respuesta.put("mensaje", "Error: al registrar una actividad");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: " );
            }
        }else{
            respuesta.put("mensaje", "Error al acceder a la base de datos, "
                    + "intenta más tarde");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerListadoActividades(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT a.idActividad, a.titulo, a.descripcion, a.Desarrollador_idDesarrollador AS idDesarrollador, d.nombreCompleto AS nombreDesarrollador, \n" +
                                    "EstadoAsignacion_idEstadoAsignacion AS idEstado, ea.nombreEstado AS estado," +
                                    "DATE_FORMAT(fechaInicio, '%d/%m/%Y') AS 'fechaInicio', " +
                                    "DATE_FORMAT(fechaFin, '%d/%m/%Y') AS 'fechaFin' " +
                                    "FROM actividad a " +
                                    "INNER JOIN Desarrollador d ON a.Desarrollador_idDesarrollador = d.idDesarrollador " +
                                    "INNER JOIN estadoAsignacion ea ON a.EstadoAsignacion_idEstadoAsignacion = ea.idEstadoAsignacion";
                
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Actividad> actividades = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Actividad actividad = new Actividad();
                    actividad.setIdActividad(resultado.getInt("idActividad"));
                    actividad.setTitulo(resultado.getString("titulo"));
                    actividad.setDescripcion(resultado.getString("descripcion"));
                    actividad.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    actividad.setDesarrollador(resultado.getString("nombreDesarrollador"));
                    actividad.setIdEstado(resultado.getInt("idEstado"));
                    actividad.setEstado(resultado.getString("estado"));
                    actividad.setFechaInicio(resultado.getString("fechaInicio"));
                    actividad.setFechaFin(resultado.getString("fechaFin"));
                    actividades.add(actividad);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("actividades", actividades);
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
    
    public static HashMap<String, Object> obtenerListadoActividadesDesarrollador(int idDesarrollador){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT a.idActividad, a.titulo, a.descripcion, a.Desarrollador_idDesarrollador AS idDesarrollador, d.nombreCompleto AS nombreDesarrollador, \n" +
                                    "EstadoAsignacion_idEstadoAsignacion AS idEstado,ea.nombreEstado AS estado, " +
                                    "DATE_FORMAT(fechaInicio, '%d/%m/%Y') AS 'fechaInicio', \n" +
                                    "DATE_FORMAT(fechaFin, '%d/%m/%Y') AS 'fechaFin' \n" +
                                    "FROM actividad a \n" +
                                    "INNER JOIN Desarrollador d ON a.Desarrollador_idDesarrollador = d.idDesarrollador\n" +
                                    "INNER JOIN estadoAsignacion ea ON a.EstadoAsignacion_idEstadoAsignacion = ea.idEstadoAsignacion" +
                                    "WHERE Desarrollador_idDesarrollador = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idDesarrollador);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Actividad> actividades = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Actividad actividad = new Actividad();
                    actividad.setIdActividad(resultado.getInt("idActividad"));
                    actividad.setTitulo(resultado.getString("titulo"));
                    actividad.setDescripcion(resultado.getString("descripcion"));
                    actividad.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    actividad.setDesarrollador(resultado.getString("nombreDesarrollador"));
                    actividad.setIdEstado(resultado.getInt("idEstado"));
                    actividad.setEstado(resultado.getString("estado"));
                    actividad.setFechaInicio(resultado.getString("fechaInicio"));
                    actividad.setFechaFin(resultado.getString("fechaFin"));
                    actividades.add(actividad);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("actividades", actividades);
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
