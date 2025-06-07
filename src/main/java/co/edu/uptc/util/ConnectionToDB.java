package co.edu.uptc.util;

import co.edu.uptc.model.Tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDB {
   private static final String         DB         = "tienda_digital_libros";
   private static final String         URL        = "jdbc:mysql://localhost:3306/" + DB;
   private static final String         USER       = "root";
   private static final String         PASS       = "";
   private static       ConnectionToDB instance   = null;
   private static       Connection     connection = null;

   private ConnectionToDB () {
      try {
         Class.forName("org.mariadb.jdbc.Driver");
         connection = DriverManager.getConnection(URL, USER, PASS);
         if (connection != null) {
            Tienda.agregarLog("Conexión a base de datos " + DB + " OK");
         }
      } catch (SQLException | ClassNotFoundException e) {
         Tienda.agregarLog(e.getMessage());
      }
   }

   public static ConnectionToDB getInstance () {
      if (instance == null) {
         instance = new ConnectionToDB();
      }
      return instance;
   }

   public Connection getConnection () {
      return connection;
   }

   public void closeConnection () throws SQLException {
      if (connection != null && !connection.isClosed()) {
         connection.close();
         Tienda.agregarLog("Conexión cerrada correctamente");
      } else {
         Tienda.agregarLog("La conexión ya está cerrada o es nula");
      }
   }
}