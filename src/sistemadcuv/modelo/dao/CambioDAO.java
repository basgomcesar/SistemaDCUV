package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Cambio;

public class CambioDAO {
    
    public static HashMap<String, Object> obtenerListadoCambios(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta ="SELECT idCambio, nombre, c.estado , DATE_FORMAT(fechaInicio, '%d/%m/%Y') AS 'fechaInicio', "
                        + "         DATE_FORMAT(fechaFin, '%d/%m/%Y') AS 'fechaFin', \n" +
                                    "Desarrollador_idDesarrollador AS idDesarrollador, d.nombreCompleto AS desarrollador\n" +
                                    "FROM cambio c\n" +
                                    "INNER JOIN desarrollador d ON Desarrollador_idDesarrollador = d.idDesarrollador";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Cambio> cambios = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Cambio cambio = new Cambio();
                    cambio.setIdCambio(resultado.getInt("idCambio"));
                    cambio.setNombre(resultado.getString("nombre"));
                    cambio.setEstado(resultado.getString("estado"));
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
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        } else{
            
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerListadoCambiosDesarrollador(int idDesarrollador){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta ="SELECT idCambio, nombre, c.estado , DATE_FORMAT(fechaInicio, '%d/%m/%Y') AS 'fechaInicio', "
                        + "DATE_FORMAT(fechaFin, '%d/%m/%Y') AS 'fechaFin', \n" +
                          "Desarrollador_idDesarrollador AS idDesarrollador, d.nombreCompleto AS desarrollador\n" +
                          "FROM cambio c\n" +
                          "INNER JOIN desarrollador d ON Desarrollador_idDesarrollador = d.idDesarrollador "
                        + "WHERE idDesarrollador = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idDesarrollador);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Cambio> cambios = new ArrayList();
                respuesta.put("error", false);
                while(resultado.next()){
                    Cambio cambio = new Cambio();
                    cambio.setIdCambio(resultado.getInt("idCambio"));
                    cambio.setNombre(resultado.getString("nombre"));
                    cambio.setEstado(resultado.getString("estado"));
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
                respuesta.put("mensaje", "Error: " + ex.getMessage());
            }
        } else{
            
        }
        return respuesta;
    }
}
