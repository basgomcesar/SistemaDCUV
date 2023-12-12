package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
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
}
