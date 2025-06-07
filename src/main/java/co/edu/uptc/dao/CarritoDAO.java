package co.edu.uptc.dao;

import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * DAO para operaciones relacionadas con el carrito de compras en la base de datos.
 */
public class CarritoDAO {
   /**
    * Metodo que obtiene el carrito de compras de un usuario por su ID.
    * @param ID ID del usuario asociado al carrito.
    * @return HashMap con ISBN como clave y cantidad como valor, o null si hay error.
    */
   public HashMap<Long, Integer> obtenerCarritoPorID (long ID) {
      HashMap<Long, Integer> carrito     = new HashMap<>();
      String                 consultaSQL = "SELECT ISBN, cantidad FROM CARRITO WHERE ID = ?";
      try {
         PreparedStatement preparedStatement = getPreparedStatement(consultaSQL);
         preparedStatement.setLong(1, ID);
         ResultSet resultSet = preparedStatement.executeQuery();
         while (resultSet.next()) {
            long ISBN     = resultSet.getLong("ISBN");
            int  cantidad = resultSet.getInt("cantidad");
            carrito.put(ISBN, cantidad);
         }
         return carrito;
      } catch (Exception e) {
         Tienda.agregarLog("Error al obtener el carrito: " + ID);
      }
      return null;
   }

   /**
    * Metodo que elimina un libro del carrito de un usuario en la base de datos.
    * @param ISBN ISBN del libro a eliminar.
    * @param ID ID del usuario asociado al carrito.
    */
   public void eliminarLibroDelCarrito (long ISBN, long ID) {
      String consultaSQL = "DELETE FROM carrito USE INDEX (ID_doble) WHERE ISBN = ? AND ID = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, ISBN);
         preparedStatement.setLong(2, ID);
         preparedStatement.executeUpdate();
         Tienda.agregarLog(String.format("Libro %d eliminado del carrito %d ", ISBN, ID));
      } catch (Exception e) {
         Tienda.agregarLog(String.format("Error al eliminar %d del carrito %d: %s", ISBN, ID, e.getMessage()));
      }
   }

   /**
    * Metodo privado para obtener un PreparedStatement a partir de una consulta SQL.
    * @param consultaSQL Consulta SQL a preparar.
    * @return PreparedStatement listo para usar.
    * @throws Exception Si ocurre un error de conexión o SQL.
    */
   private PreparedStatement getPreparedStatement (String consultaSQL) throws Exception {
      Connection connection = ConnectionToDB.getInstance().getConnection();
      return connection.prepareStatement(consultaSQL);
   }
}