package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.Materia;
import sistemadcuv.modelo.pojo.Periodo;

public class PeriodoDAO {
    public static HashMap<String, Object> obtenerPeriodoActual(String fechaActual){
        System.out.println("antes del error 1 ");
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idPeriodo, fechaInicio, fechaFin "
                        + "FROM periodo "
                        + "WHERE ? BETWEEN fechaInicio AND fechaFin ";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setString(1, fechaActual);
                ResultSet resultado = prepararSentencia.executeQuery();
                Periodo periodoActual = new Periodo();
                respuesta.put("error", false);
                while(resultado.next()){
                    periodoActual.setIdPeriodo(resultado.getInt("idPeriodo"));
                    periodoActual.setFechaInicio(resultado.getString("fechaInicio"));
                    periodoActual.setFechaFin(resultado.getString("fechaFin"));
                }
                conexionBD.close();
                respuesta.put("error", false);
                respuesta.put("periodo", periodoActual);
            } catch (SQLException ex) {
                respuesta.put("periodo", "Error: al obtener el periodo actual");
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion,"
                    + "por favor intentelo mas tarde.");
        }
        return respuesta;
    }
}
