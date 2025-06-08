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
    *
    * @param ID ID del usuario asociado al carrito.
    *
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
    *
    * @param ISBN ISBN del libro a eliminar.
    * @param ID   ID del usuario asociado al carrito.
    */
   public void eliminarLibroDelCarrito (long ISBN, long ID) {
      String consultaSQL = "DELETE FROM carrito USE INDEX (ID_doble) WHERE ISBN = ? AND ID = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, ISBN);
         preparedStatement.setLong(2, ID);
         preparedStatement.executeUpdate();
      } catch (Exception e) {
         Tienda.agregarLog(String.format("Error al eliminar %d del carrito %d: %s", ISBN, ID, e.getMessage()));
      }
   }

   /**
    * Metodo privado para obtener un PreparedStatement a partir de una consulta SQL.
    *
    * @param consultaSQL Consulta SQL a preparar.
    *
    * @return PreparedStatement listo para usar.
    *
    * @throws Exception Si ocurre un error de conexión o SQL.
    */
   private PreparedStatement getPreparedStatement (String consultaSQL) throws Exception {
      Connection connection = ConnectionToDB.getInstance().getConnection();
      return connection.prepareStatement(consultaSQL);
   }

   public void actualizarCarrito (HashMap<Long, Integer> carritoActual, long ID) {
      HashMap<Long, Integer> carritoEnDB = obtenerCarritoPorID(ID);
      if (carritoEnDB == null) carritoEnDB = new HashMap<>();
      try {
         for (Long ISBN : carritoActual.keySet()) {
            int cantidadActual = carritoActual.get(ISBN);
            if (!carritoEnDB.containsKey(ISBN)) {
               String insertSQL = "INSERT INTO carrito (ID, ISBN, cantidad) VALUES (?, ?, ?)";
               try (PreparedStatement preparedStatement = getPreparedStatement(insertSQL)) {
                  preparedStatement.setLong(1, ID);
                  preparedStatement.setLong(2, ISBN);
                  preparedStatement.setInt(3, cantidadActual);
                  preparedStatement.executeUpdate();
               }
            } else if (carritoEnDB.get(ISBN) != cantidadActual) {
               String updateSQL = "UPDATE carrito SET cantidad = ? WHERE ID = ? AND ISBN = ?";
               try (PreparedStatement ps = getPreparedStatement(updateSQL)) {
                  ps.setInt(1, cantidadActual);
                  ps.setLong(2, ID);
                  ps.setLong(3, ISBN);
                  ps.executeUpdate();
               }
            }
         }

         for (Long ISBN : carritoEnDB.keySet()) {
            if (!carritoActual.containsKey(ISBN)) {
               String deleteSQL = "DELETE FROM carrito WHERE ID = ? AND ISBN = ?";
               try (PreparedStatement ps = getPreparedStatement(deleteSQL)) {
                  ps.setLong(1, ID);
                  ps.setLong(2, ISBN);
                  ps.executeUpdate();
               }
            }
         }
      } catch (Exception e) {
         Tienda.agregarLog("Error al actualizar el carrito de " + ID + ": " + e.getMessage());
      }
   }
}