package co.edu.uptc.dao;

import co.edu.uptc.entity.DetalleCompra;
import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DetalleCompraDAO {
   public DetalleCompraDAO () {}

   public ArrayList<DetalleCompra> obtenerDetallesCompraPorID (long IDcompra) {
      String consultaSQL = "SELECT * FROM DETALLE_COMPRA WHERE ID_compra = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, IDcompra);
         ArrayList<DetalleCompra> detallesCompra = new ArrayList<>();
         ResultSet                resultSet      = preparedStatement.executeQuery();
         while (resultSet.next()) {
            DetalleCompra detalleCompra = new DetalleCompra();
            detalleCompra.setIDdetalle(resultSet.getLong("ID_detalle"));
            detalleCompra.setIDcompra(resultSet.getLong("ID_compra"));
            detalleCompra.setTitulo(resultSet.getString("titulo"));
            detalleCompra.setCantidad(resultSet.getInt("cantidad"));
            detalleCompra.setValorUnitario(resultSet.getDouble("valor_Unitario"));
            detalleCompra.setISBNasociado(resultSet.getLong("ISBN_asociado"));
            detallesCompra.add(detalleCompra);
         }
         Tienda.agregarLog("Detalles de compra obtenidos para ID: " + IDcompra);
         return detallesCompra;
      } catch (Exception e) {
         Tienda.agregarLog("Error al obtener detalles de compra para ID: " + IDcompra);
      }
      return null;
   }

   public void registrarDetallesCompra (ArrayList<DetalleCompra> listaArticulos, long IDasociado, long IDcompra) {
      String consultaSQL = "INSERT INTO DETALLE_COMPRA (ID_compra, ISBN_asociado, titulo, cantidad, valor_unitario) VALUES (?, ?, ?, ?, ?)";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         for (DetalleCompra detalleCompra : listaArticulos) {
            preparedStatement.setLong(1, IDcompra);
            preparedStatement.setLong(2, detalleCompra.getISBNasociado());
            preparedStatement.setString(3, detalleCompra.getTitulo());
            preparedStatement.setInt(4, detalleCompra.getCantidad());
            preparedStatement.setDouble(5, detalleCompra.getValorUnitario());
            preparedStatement.executeUpdate();
         }
         Tienda.agregarLog(String.format("ID_compra %d registrada para ID_asociado %d", IDcompra, IDasociado));
      } catch (Exception e) {
         Tienda.agregarLog("Error al registrar detalles de compra para ID_asociado: " + IDasociado);
      }
   }

   private PreparedStatement getPreparedStatement (String consultaSQL) throws Exception {
      Connection connection = ConnectionToDB.getInstance().getConnection();
      return connection.prepareStatement(consultaSQL);
   }
}