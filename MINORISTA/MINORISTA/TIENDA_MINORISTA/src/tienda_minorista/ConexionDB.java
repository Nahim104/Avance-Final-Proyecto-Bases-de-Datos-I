package tienda_minorista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionDB {

    

    public Connection conectar() {
        Connection conexion = null;
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión con la base de datos
            String url = "jdbc:mysql://tiendadb1.c3zkqgxrrnas.us-east-1.rds.amazonaws.com:3306/MINORISTA";
            String usuario = "admin";
            String contraseña = "Kokunsuper140401";
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            // Mostrar mensaje de conexión exitosa
            //JOptionPane.showMessageDialog(null, "¡Conexión exitosa a la base de datos!");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el driver: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return conexion;
    }


}


///DatabaseConnection
