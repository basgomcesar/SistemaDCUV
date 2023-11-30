package sistemadcuv.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import sistemadcuv.modelo.ConexionBD;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;

public class ResponsableDAO {
    public static HashMap<String, Object> verificarSesionResponsable(int usuario,String contrasenia){
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.obtenerConexion();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idResponsableDelProyecto, nombreCompleto, "+
                        "numeroDePersonal, correoElectronico, contrasenia, Proyecto_idProyecto, nombre \n" +
                        "FROM ResponsableDelProyecto, proyecto \n" +
                        "WHERE numeroDePersonal = ? AND contrasenia = ? AND  Proyecto_idProyecto = idProyecto";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, usuario);
                prepararSentencia.setString(2, contrasenia);
                ResultSet resultado = prepararSentencia.executeQuery();
                if(resultado.next()){
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Credenciales correctas");
                    ResponsableDeProyecto responsable = new ResponsableDeProyecto();                    
                    responsable.setIdResponsable(resultado.getInt("idResponsableDelProyecto"));
                    responsable.setNombreCompleto(resultado.getString("nombreCompleto"));
                    responsable.setNumeroPersonal(resultado.getInt("numeroDePersonal"));
                    responsable.setCorreoElectronico(resultado.getString("correoElectronico"));
                    responsable.setIdProyecto(resultado.getInt("Proyecto_idProyecto"));
                    responsable.setNombreProyecto(resultado.getString("nombre"));
                    respuesta.put("responsable", responsable);
                }else{
                    respuesta.put("mensaje", "Las credenciales son incorrectas");
                }
                conexionBD.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                respuesta.put("responsable", "Error "+ex.getMessage());
            }
        }else{
            respuesta.put("mensaje", "Por el momento no hay conexion,"
                    + "por favor intentelo mas tarde.");
        }
        return respuesta;
    }
}
