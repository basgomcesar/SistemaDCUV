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
import sistemadcuv.modelo.pojo.SolicitudDeCambio;

public class SolicitudDAO {
    
    public static HashMap<String, Object> obtenerListadoSolicitudes(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT sc.numeroSolicitud, sc.nombreSolicitudDeCambio, es.nombreEstado AS estado, es.idEstadoSolicitud AS idEstado,\n" +
                                    "d.idDesarrollador, d.nombreCompleto AS nombreDesarrollador, sc.idSolicitudDeCambio,\n" +
                                    "DATE_FORMAT(sc.fechaCreacion, '%d/%m/%Y') AS fechaCreacion,\n" +
                                    "DATE_FORMAT(sc.fechaDeAprobacion, '%d/%m/%Y') AS fechaDeAprobacion,\n" +
                                    "sc.razon, sc.descripcion, sc.impacto,\n" +
                                    "sc.accionPropuesta, rp.idResponsableDelProyecto, rp.nombreCompleto AS nombreResponsableProyecto\n" +
                                    "FROM SolicitudDeCambio sc\n" +
                                    "INNER JOIN Desarrollador d ON sc.Desarrollador_idDesarrollador = d.idDesarrollador\n" +
                                    "INNER JOIN EstadoSolicitud es ON sc.EstadoSolicitud_idEstadoSolicitud = es.idEstadoSolicitud\n" +
                                    "LEFT JOIN ResponsableDelProyecto rp ON sc.ResponsableDelProyecto_idResponsableDelProyecto = rp.idResponsableDelProyecto";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<SolicitudDeCambio> solicitudes = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    SolicitudDeCambio solicitud = new SolicitudDeCambio();
                    solicitud.setIdSolicitud(resultado.getInt("idSolicitudDeCambio"));
                    solicitud.setNombre(resultado.getString("nombreSolicitudDeCambio"));
                    solicitud.setNumSolicitud(resultado.getInt("numeroSolicitud"));
                    solicitud.setIdEstado(resultado.getInt("idEstado"));
                    solicitud.setEstado(resultado.getString("estado"));
                    solicitud.setFechaRegistro(resultado.getString("fechaCreacion"));
                    solicitud.setFechaAprobacion(resultado.getString("fechaDeAprobacion"));
                    solicitud.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    solicitud.setNombreDesarrollador(resultado.getString("nombreDesarrollador"));
                    solicitud.setNombreResponsable(resultado.getString("nombreResponsableProyecto"));
                    solicitud.setDescripcion(resultado.getString("descripcion"));
                    solicitud.setRazon(resultado.getString("razon"));
                    solicitud.setImpacto(resultado.getString("impacto"));
                    solicitud.setAccionPropuesta(resultado.getString("accionPropuesta"));
                    solicitudes.add(solicitud);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("solicitudes", solicitudes);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        } else{
            
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerListadoSolicitudesDesarrollador(int idDesarrollador){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT sc.numeroSolicitud, sc.nombreSolicitudDeCambio, es.nombreEstado AS estado, es.idEstadoSolicitud AS idEstado,\n" +
                                    "d.idDesarrollador, d.nombreCompleto AS nombreDesarrollador, sc.idSolicitudDeCambio,\n" +
                                    "DATE_FORMAT(sc.fechaCreacion, '%d/%m/%Y') AS fechaCreacion,\n" +
                                    "DATE_FORMAT(sc.fechaDeAprobacion, '%d/%m/%Y') AS fechaDeAprobacion,\n" +
                                    "sc.razon, sc.descripcion, sc.impacto,\n" +
                                    "sc.accionPropuesta, rp.idResponsableDelProyecto, rp.nombreCompleto AS nombreResponsableProyecto\n" +
                                    "FROM SolicitudDeCambio sc\n" +
                                    "INNER JOIN Desarrollador d ON sc.Desarrollador_idDesarrollador = d.idDesarrollador\n" +
                                    "INNER JOIN EstadoSolicitud es ON sc.EstadoSolicitud_idEstadoSolicitud = es.idEstadoSolicitud\n" +
                                    "LEFT JOIN ResponsableDelProyecto rp ON sc.ResponsableDelProyecto_idResponsableDelProyecto = rp.idResponsableDelProyecto\n" +
                                    "WHERE sc.Desarrollador_idDesarrollador = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idDesarrollador);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<SolicitudDeCambio> solicitudes = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    SolicitudDeCambio solicitud = new SolicitudDeCambio();
                    solicitud.setIdSolicitud(resultado.getInt("idSolicitudDeCambio"));
                    solicitud.setNombre(resultado.getString("nombreSolicitudDeCambio"));
                    solicitud.setNumSolicitud(resultado.getInt("numeroSolicitud"));
                    solicitud.setIdEstado(resultado.getInt("idEstado"));
                    solicitud.setEstado(resultado.getString("estado"));
                    solicitud.setFechaRegistro(resultado.getString("fechaCreacion"));
                    solicitud.setFechaAprobacion(resultado.getString("fechaDeAprobacion"));
                    solicitud.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    solicitud.setNombreDesarrollador(resultado.getString("nombreDesarrollador"));
                    solicitud.setNombreResponsable(resultado.getString("nombreResponsableProyecto"));
                    solicitud.setDescripcion(resultado.getString("descripcion"));
                    solicitud.setRazon(resultado.getString("razon"));
                    solicitud.setImpacto(resultado.getString("impacto"));
                    solicitud.setAccionPropuesta(resultado.getString("accionPropuesta"));
                    solicitudes.add(solicitud);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("solicitudes", solicitudes);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        } else{
            
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> registrarSolicitudDeCambio(SolicitudDeCambio nuevaSolicitud){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO solicituddecambio(nombreSolicitudDeCambio, descripcion, " +
                    "numeroSolicitud, razon, EstadoSolicitud_idEstadoSolicitud, fechaCreacion, " +
                    "impacto,accionPropuesta, Desarrollador_idDesarrollador) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, nuevaSolicitud.getNombre());
                prepararSentencia.setString(2, nuevaSolicitud.getDescripcion());
                prepararSentencia.setInt(3, nuevaSolicitud.getNumSolicitud());
                prepararSentencia.setString(4, nuevaSolicitud.getRazon());
                prepararSentencia.setInt(5, nuevaSolicitud.getIdEstado());
                prepararSentencia.setString(6, nuevaSolicitud.getFechaRegistro());
                prepararSentencia.setString(7, nuevaSolicitud.getImpacto());
                prepararSentencia.setString(8, nuevaSolicitud.getAccionPropuesta());
                prepararSentencia.setInt(9, nuevaSolicitud.getIdDesarrollador());
                int filasAfectadas = prepararSentencia.executeUpdate();
                ResultSet generatedKeys = prepararSentencia.getGeneratedKeys();
                if (generatedKeys.next()){
                    int idSolicitud = generatedKeys.getInt(1);
                    respuesta.put("idSolicitud", idSolicitud);
                }
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Solicitud de cambio registrada");
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
    
    public static HashMap<String, Object> modificarEstadoSolicitud(SolicitudDeCambio modificarSolicitud){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "UPDATE solicituddecambio SET EstadoSolicitud_idEstadoSolicitud = ?, "
                        + "fechaDeAprobacion = ?, ResponsableDelProyecto_idResponsableDelProyecto = ? "
                        + "WHERE idSolicitudDeCambio = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, modificarSolicitud.getIdEstado());
                prepararSentencia.setString(2, modificarSolicitud.getFechaAprobacion());
                prepararSentencia.setInt(3, modificarSolicitud.getIdResponsable());
                prepararSentencia.setInt(4, modificarSolicitud.getIdSolicitud());
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Estado modificado");
                }else{                    
                    respuesta.put("mensaje", "Error al modificar");
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
    
    public static HashMap<String, Object> eliminarSolicitud(int idSolicitud){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "DELETE FROM solicituddecambio "
                        + "WHERE idSolicitudDeCambio = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idSolicitud);
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Solicitud eliminada");
                }else{                    
                    respuesta.put("mensaje", "Error al eliminar");
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
