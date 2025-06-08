package co.edu.uptc.util;

import co.edu.uptc.model.Tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para la gestión de la conexión a la base de datos.
 * Implementa el patrón Singleton para asegurar una única conexión activa.
 */
public class ConnectionToDB {
   /**
    * Nombre de la base de datos.
    */
   private static final String         DB         = "tienda_digital_libros";
   /**
    * URL de conexión JDBC.
    */
   private static final String         URL        = "jdbc:mysql://localhost:3306/" + DB;
   /**
    * Usuario de la base de datos.
    */
   private static final String         USER       = "root";
   /**
    * Contraseña de la base de datos.
    */
   private static final String         PASS       = "";
   /**
    * Instancia única de la conexión (Singleton).
    */
   private static       ConnectionToDB instance   = null;
   /**
    * Objeto Connection activo.
    */
   private static       Connection     connection = null;

   private ConnectionToDB () {
      try {
         Class.forName("org.mariadb.jdbc.Driver");
         connection = DriverManager.getConnection(URL, USER, PASS);
      } catch (SQLException | ClassNotFoundException e) {
         Tienda.agregarLog(e.getMessage());
      }
   }

   /**
    * Obtiene la instancia única de ConnectionToDB.
    *
    * @return Instancia Singleton de ConnectionToDB.
    */
   public static ConnectionToDB getInstance () {
      if (instance == null) {
         instance = new ConnectionToDB();
      }
      return instance;
   }

   /**
    * Obtiene la conexión activa a la base de datos.
    *
    * @return Objeto Connection activo.
    */
   public Connection getConnection () {
      return connection;
   }

   /**
    * Cierra la conexión a la base de datos si está activa.
    *
    * @throws SQLException Si ocurre un error al cerrar la conexión.
    */
   public void closeConnection () throws SQLException {
      if (connection != null && !connection.isClosed()) {
         connection.close();
      }
   }
}