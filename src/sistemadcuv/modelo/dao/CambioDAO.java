package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javafx.scene.control.Alert;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.utils.Utilidades;

public class CambioDAO {
    
    public static HashMap<String, Object> obtenerListadoCambios(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta ="SELECT idCambio, nombre,descripcion, impacto, razonCambio, TipoArtefacto_idTipoArtefacto AS idTipoArtefacto, esfuerzo, \n" +
                                    "ea.nombreEstado as estado , EstadoAsignacion_idEstadoAsignacion as idEstado, DATE_FORMAT(fechaInicio, '%d/%m/%Y') AS 'fechaInicio',\n" +
                                    "DATE_FORMAT(fechaFin, '%d/%m/%Y') AS 'fechaFin',\n" +
                                    "Desarrollador_idDesarrollador AS idDesarrollador, d.nombreCompleto AS desarrollador\n" +
                                    "FROM cambio c\n" +
                                    "INNER JOIN desarrollador d ON Desarrollador_idDesarrollador = d.idDesarrollador\n" +
                                    "INNER JOIN estadoAsignacion ea ON ea.idEstadoAsignacion = EstadoAsignacion_idEstadoAsignacion";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Cambio> cambios = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Cambio cambio = new Cambio();
                    cambio.setIdCambio(resultado.getInt("idCambio"));
                    cambio.setNombre(resultado.getString("nombre"));
                    cambio.setDescripcion(resultado.getString("descripcion"));
                    cambio.setImpacto(resultado.getString("impacto"));
                    cambio.setRazonCambio(resultado.getString("razonCambio"));
                    cambio.setIdTipoCambio(resultado.getInt("idTipoArtefacto"));
                    cambio.setEsfuerzo(resultado.getInt("esfuerzo"));
                    cambio.setEstado(resultado.getString("estado"));
                    cambio.setIdEstado(resultado.getInt("idEstado"));
                    cambio.setFechaInicio(resultado.getString("fechaInicio"));
                    cambio.setFechaFin(resultado.getString("fechaFin"));
                    cambio.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    cambio.setDesarrollador(resultado.getString("desarrollador"));
                    cambios.add(cambio);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("cambios", cambios);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: al intentar obtener los cambios");
            }
        } else{
            respuesta.put("mensaje", "Error al acceder a la base de datos, "
                    + "intenta mas tarde.");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerListadoCambiosDesarrollador(int idDesarrollador){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta ="SELECT idCambio, nombre,descripcion, impacto, razonCambio, TipoArtefacto_idTipoArtefacto AS idTipoArtefacto, esfuerzo, \n" +
                                    "ea.nombreEstado as estado , EstadoAsignacion_idEstadoAsignacion as idEstado, DATE_FORMAT(fechaInicio, '%d/%m/%Y') AS 'fechaInicio',\n" +
                                    "DATE_FORMAT(fechaFin, '%d/%m/%Y') AS 'fechaFin',\n" +
                                    "Desarrollador_idDesarrollador AS idDesarrollador, d.nombreCompleto AS desarrollador\n" +
                                    "FROM cambio c\n" +
                                    "INNER JOIN desarrollador d ON Desarrollador_idDesarrollador = d.idDesarrollador\n" +
                                    "INNER JOIN estadoAsignacion ea ON ea.idEstadoAsignacion = EstadoAsignacion_idEstadoAsignacion\n" +
                                    "WHERE idDesarrollador = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idDesarrollador);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Cambio> cambios = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Cambio cambio = new Cambio();
                    cambio.setIdCambio(resultado.getInt("idCambio"));
                    cambio.setNombre(resultado.getString("nombre"));
                    cambio.setDescripcion(resultado.getString("descripcion"));
                    cambio.setImpacto(resultado.getString("impacto"));
                    cambio.setRazonCambio(resultado.getString("razonCambio"));
                    cambio.setIdTipoCambio(resultado.getInt("idTipoArtefacto"));
                    cambio.setEsfuerzo(resultado.getInt("esfuerzo"));
                    cambio.setEstado(resultado.getString("estado"));
                    cambio.setIdEstado(resultado.getInt("idEstado"));
                    cambio.setFechaInicio(resultado.getString("fechaInicio"));
                    cambio.setFechaFin(resultado.getString("fechaFin"));
                    cambio.setIdDesarrollador(resultado.getInt("idDesarrollador"));
                    cambio.setDesarrollador(resultado.getString("desarrollador"));
                    cambios.add(cambio);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("cambios", cambios);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: al intentar obtener los cambios" );
            }
        } else{
            respuesta.put("mensaje", "Error al acceder a la base de datos, "
                    + "intenta mas tarde.");
        }
        return respuesta;
    }
    public static HashMap<String, Object> registrarCambioBajoImpacto(Cambio nuevoCambio){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO cambio(nombre, descripcion, " +
                    "esfuerzo, fechaInicio, fechaFin, impacto, " +
                    "razonCambio,Desarrollador_idDesarrollador, " +
                    "estadoasignacion_idEstadoAsignacion, tipoartefacto_idTipoArtefacto) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 2,?) ;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, 
                        Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, nuevoCambio.getNombre());
                prepararSentencia.setString(2, nuevoCambio.getDescripcion());
                prepararSentencia.setInt(3, nuevoCambio.getEsfuerzo());
                prepararSentencia.setString(4, nuevoCambio.getFechaInicio());
                prepararSentencia.setString(5, nuevoCambio.getFechaFin());
                prepararSentencia.setString(6, nuevoCambio.getImpacto());
                prepararSentencia.setString(7, nuevoCambio.getRazonCambio());
                prepararSentencia.setInt(8, nuevoCambio.getIdDesarrollador());
                prepararSentencia.setInt(9, nuevoCambio.getIdTipoCambio());
                int filasAfectadas = prepararSentencia.executeUpdate();
                ResultSet generatedKeys = prepararSentencia.getGeneratedKeys();
                if (generatedKeys.next()){
                    int idCambio = generatedKeys.getInt(1);
                    respuesta.put("idCambio", idCambio);
                }
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El cambio ha sido guardado en la base de datos");
                }else{                    
                    respuesta.put("mensaje", "Error al registrar");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: al intentar guardar el cambio" );
            }
        }else{
            respuesta.put("mensaje", "Error al acceder a la base de datos, intenta más tarde");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> modificarCambio(Cambio modificarCambio){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "UPDATE cambio SET esfuerzo = ?, "
                        + "fechaFin = ?, estadoasignacion_idestadoasignacion = ? "
                        + "WHERE idCambio = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, modificarCambio.getEsfuerzo());
                prepararSentencia.setString(2, modificarCambio.getFechaFin());
                prepararSentencia.setInt(3, modificarCambio.getIdEstado());
                prepararSentencia.setInt(4, modificarCambio.getIdCambio());
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Estado modificado");
                }else{                    
                    respuesta.put("mensaje", "Error al modificar");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: al modificar el estado del cambio");
            }
        }else{
            respuesta.put("mensaje", "Error al acceder a la base de datos, "
                    + "intenta más tarde");
        }
        return respuesta;
    }

    public static HashMap<String, Object> registrarCambioDeSolicitud(Cambio nuevoCambio) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO cambio(nombre, descripcion,fechaInicio, " +
                    "impacto, " +
                    "razonCambio,Desarrollador_idDesarrollador, " +
                    "estadoasignacion_idEstadoAsignacion, tipoartefacto_idTipoArtefacto, "+
                    "SolicitudDeCambio_idSolicitudDeCambio) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 1,?,?) ;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, 
                        Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, nuevoCambio.getNombre());
                prepararSentencia.setString(2, nuevoCambio.getDescripcion());
                prepararSentencia.setString(3, nuevoCambio.getFechaInicio());
                prepararSentencia.setString(4, nuevoCambio.getImpacto());
                prepararSentencia.setString(5, nuevoCambio.getRazonCambio());
                prepararSentencia.setInt(6, nuevoCambio.getIdDesarrollador());
                prepararSentencia.setInt(7, nuevoCambio.getIdTipoCambio());
                prepararSentencia.setInt(8, nuevoCambio.getIdSolicitud());
                int filasAfectadas = prepararSentencia.executeUpdate();
                ResultSet generatedKeys = prepararSentencia.getGeneratedKeys();
                if (generatedKeys.next()){
                    int idCambio = generatedKeys.getInt(1);
                    respuesta.put("idCambio", idCambio);
                }
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El cambio ha sido guardado en la base de datos");
                }else{                    
                    respuesta.put("mensaje", "Error al registrar");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: al intentar guardar el cambio" );
            }
        }else{
            respuesta.put("mensaje", "Error al acceder a la base de datos, intenta más tarde");
        }
        return respuesta;
    }
}
