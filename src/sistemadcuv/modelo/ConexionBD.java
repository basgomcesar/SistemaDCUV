package sistemadcuv.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import sistemadcuv.utils.Constantes;

public class ConexionBD {
    public static final String URL_CONEXION = "jdbc:mysql://" + Constantes.HOSTNAME + ":" + Constantes.PUERTO
            + "/" + Constantes.NOMBRE_BD + "?allowPublicKeyRetrieval=true&useSSL=false";
    
    public static Connection obtenerConexion(){
        Connection conexionBD = null;
        try{
            Class.forName(Constantes.DRIVER);
            conexionBD = DriverManager.getConnection(URL_CONEXION, Constantes.USUARIO, Constantes.PASSWORD);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException s){
            s.printStackTrace();
        }
        return conexionBD;
    }
}
