package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                String consulta = "SELECT idSolicitudDeCambio, nombreSolicitudDeCambio, numeroSolicitud, estatus, "
                        + "DATE_FORMAT(fechaCreacion, '%d/%m/%Y') AS 'fechaCreacion', \n" +
                          "DATE_FORMAT(fechaDeAprobacion, '%d/%m/%Y') AS 'fechaDeAprobacion', "
                        + "Desarrollador_idDesarrollador AS idDesarrollador, d.nombreCompleto AS desarrollador\n" +
                          "FROM SolicitudDeCambio\n" +
                          "INNER JOIN desarrollador d ON Desarrollador_idDesarrollador = d.idDesarrollador";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<SolicitudDeCambio> solicitudes = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    SolicitudDeCambio solicitud = new SolicitudDeCambio();
                    solicitud.setIdSolicitud(resultado.getInt("idSolicitudDeCambio"));
                    solicitud.setNombre(resultado.getString("nombreSolicitudDeCambio"));
                    solicitud.setNumSolicitud(resultado.getInt("numeroSolicitud"));
                    solicitud.setEstatus(resultado.getString("estatus"));
                    solicitud.setFechaRegistro(resultado.getString("fechaCreacion"));
                    solicitud.setFechaAprobacion(resultado.getString("fechaDeAprobacion"));
                    solicitud.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    solicitud.setNombreDesarrollador(resultado.getString("desarrollador"));
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
                String consulta = "SELECT idSolicitudDeCambio, nombreSolicitudDeCambio, numeroSolicitud, estatus, "
                        + "DATE_FORMAT(fechaCreacion, '%d/%m/%Y') AS 'fechaCreacion', \n" +
                          "DATE_FORMAT(fechaDeAprobacion, '%d/%m/%Y') AS 'fechaDeAprobacion', "
                        + "Desarrollador_idDesarrollador AS idDesarrollador, d.nombreCompleto AS desarrollador\n" +
                          "FROM SolicitudDeCambio\n" +
                          "INNER JOIN desarrollador d ON Desarrollador_idDesarrollador = d.idDesarrollador "
                        + "WHERE idDesarrollador = ?";
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
                    solicitud.setEstatus(resultado.getString("estatus"));
                    solicitud.setFechaRegistro(resultado.getString("fechaCreacion"));
                    solicitud.setFechaAprobacion(resultado.getString("fechaDeAprobacion"));
                    solicitud.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    solicitud.setNombreDesarrollador(resultado.getString("desarrollador"));
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
                    "numeroSolicitud, razon, estatus, fechaCreacion, impacto,accionPropuesta, Desarrollador_idDesarrollador) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, nuevaSolicitud.getNombre());
                prepararSentencia.setString(2, nuevaSolicitud.getDescripcion());
                prepararSentencia.setInt(3, nuevaSolicitud.getNumSolicitud());
                prepararSentencia.setString(4, nuevaSolicitud.getRazon());
                prepararSentencia.setString(5, nuevaSolicitud.getEstatus());
                prepararSentencia.setString(6, nuevaSolicitud.getFechaRegistro());
                prepararSentencia.setString(7, nuevaSolicitud.getImpacto());
                prepararSentencia.setString(8, nuevaSolicitud.getAccionPropuesta());
                prepararSentencia.setInt(9, nuevaSolicitud.getIdDesarrollador());
                int filasAfectadas = prepararSentencia.executeUpdate();
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
}
