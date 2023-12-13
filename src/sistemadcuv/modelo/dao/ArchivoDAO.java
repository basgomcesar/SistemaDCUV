package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Archivo;

public class ArchivoDAO {
    public static HashMap<String, Object> registrarArchivoDeSolicitud(Archivo nuevoArchivo){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO archivo (nombreArchivo, archivo, SolicitudDeCambio_idSolicitudDeCambio) \n" +
                                    "VALUES(?, ?, ?);";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, nuevoArchivo.getNombreArchivo());
                prepararSentencia.setBytes(2, nuevoArchivo.getArchivo());
                prepararSentencia.setInt(3, nuevoArchivo.getIdSolicitud());
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Archivo(s) guardado(s) en la solicitud de cambio");
                }else{                    
                    respuesta.put("mensaje", "Error al guardar");
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
    public static HashMap<String, Object> registrarArchivoDeCambio(Archivo nuevoArchivo){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO archivo (nombreArchivo, archivo, Cambio_idCambio) " +
                                    "VALUES(?, ?, ?);";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, nuevoArchivo.getNombreArchivo());
                prepararSentencia.setBytes(2, nuevoArchivo.getArchivo());
                prepararSentencia.setInt(3, nuevoArchivo.getIdCambio());
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Archivo(s) guardado(s) en la solicitud de cambio");
                }else{                    
                    respuesta.put("mensaje", "Error al guardar");
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
    public static HashMap<String, Object> obtenerArchivosSolicitud(int idSolicitud){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idArchivo, nombreArchivo, archivo\n" +
                                    "FROM archivo\n" +
                                    "WHERE SolicitudDeCambio_idSolicitudDeCambio=?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idSolicitud);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Archivo> archivos = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Archivo archivo = new Archivo();
                    archivo.setIdSolicitud(resultado.getInt("idArchivo"));
                    archivo.setNombreArchivo(resultado.getString("nombreArchivo"));
                    archivo.setArchivo(resultado.getBytes("archivo"));
                    archivos.add(archivo);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("archivos", archivos);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        } else{
            
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> eliminarArchivosSolicitud(int idSolicitud){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "DELETE FROM archivo "
                        + "WHERE SolicitudDeCambio_idSolicitudDeCambio=?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idSolicitud);
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas >= 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Archivo(s) eliminado(s)");
                }else{                    
                    respuesta.put("mensaje", "Error al eliminar");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion, "
                    + "intentalo más tarde");
        }
        return respuesta;
    }

    public static HashMap<String, Object> registrarArchivoDeActividad(Archivo nuevoArchivo) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD!=null){
            try{
                String sentencia = "INSERT INTO archivo (nombreArchivo, archivo, Actividad_idActividad) " +
                                    "VALUES(?, ?, ?);";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, nuevoArchivo.getNombreArchivo());
                prepararSentencia.setBytes(2, nuevoArchivo.getArchivo());
                prepararSentencia.setInt(3, nuevoArchivo.getIdActividad());
                int filasAfectadas = prepararSentencia.executeUpdate();
                conexionBD.close();
                if(filasAfectadas == 1){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Archivo(s) guardado(s) en la actividad");
                }else{                    
                    respuesta.put("mensaje", "Error al guardar");
                }
            }catch(SQLException ex){
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion, "
                    + "intentalo más tarde");
    
    public static HashMap<String, Object> obtenerArchivosCambio(int idCambio){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idArchivo, nombreArchivo, archivo\n" +
                                    "FROM archivo\n" +
                                    "WHERE Cambio_idCambio=?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idCambio);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Archivo> archivos = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Archivo archivo = new Archivo();
                    archivo.setIdSolicitud(resultado.getInt("idArchivo"));
                    archivo.setNombreArchivo(resultado.getString("nombreArchivo"));
                    archivo.setArchivo(resultado.getBytes("archivo"));
                    archivos.add(archivo);
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("archivos", archivos);
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        } else{
            respuesta.put("mensaje", "Error al acceder a la base de datos, intenta más tarde.");
        }
        return respuesta;
    }
}
