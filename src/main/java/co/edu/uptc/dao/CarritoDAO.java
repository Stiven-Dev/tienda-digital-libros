package co.edu.uptc.dao;

import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class CarritoDAO {
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

   private PreparedStatement getPreparedStatement (String consultaSQL) throws Exception {
      Connection connection = ConnectionToDB.getInstance().getConnection();
      return connection.prepareStatement(consultaSQL);
   }
}