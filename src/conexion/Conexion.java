package conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Conexion {
    //Conexion local

    public static Connection conectar() {

        try {

            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/bd_heladeria", "root", "admin");
            return cn;
        } catch (SQLException e) {
            System.out.println("ERROR EN LA CONEXION LOCAL" + e);

        }
        return null;
    }
}
