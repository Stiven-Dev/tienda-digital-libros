package co.edu.uptc.util;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
   private final String     URL  = "jdbc:mysql://localhost:3306/tiendaDigitalLibros";
   private final String     USER = "root";
   private final String     PASS = "root";
   private       Connection connection;

   public Connection(){
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         connection = (Connection) DriverManager.getConnection(URL, USER, PASS);
      } catch (SQLException e) {
         System.err.println("Error al conectar a la base de datos: " + e.getMessage());
      } catch (ClassNotFoundException e) {
         System.err.println("Driver no encontrado: " + e.getMessage());
      }
   }

   public Connection getConnection () {
      return connection;
   }

   public void setConnection (Connection connection) {
      this.connection = connection;
   }

   public void connect () {
      try {
         if (connection.getConnection().isClosed()){
            System.out.println("Conexión cerrada, intentando reconectar...");
         }


         System.out.println("Conexión exitosa a la base de datos.");
      } catch (SQLException | ClassNotFoundException e) {
         System.err.println("Error al conectar a la base de datos: " + e.getMessage());
      }
   }
}