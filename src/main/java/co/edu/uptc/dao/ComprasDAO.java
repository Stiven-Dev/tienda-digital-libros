package co.edu.uptc.dao;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.DetalleCompra;
import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ComprasDAO {
   private final DetalleCompraDAO detalleCompraDAO = new DetalleCompraDAO();

   public ArrayList<Compra> obtenerComprasPorUsuarioID (long IDasociado) {
      ArrayList<Compra> compras     = new ArrayList<>();
      String            consultaSQL = "SELECT * FROM COMPRAS WHERE ID_asociado = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, IDasociado);
         ResultSet resultSet = preparedStatement.executeQuery();
         while (resultSet.next()) {
            Compra compra = new Compra();
            compra.setIDcompra(resultSet.getLong("ID_compra"));
            compra.setIDasociado(resultSet.getInt("ID_asociado"));
            compra.setFechaCompra(resultSet.getTimestamp("fecha_Compra").toLocalDateTime());
            compra.setPorcentajeDescuento(resultSet.getDouble("porcentaje_Descuento"));
            compra.setMetodoPago(Compra.METODO_PAGO.valueOf(resultSet.getString("metodo_Pago")));

            ArrayList<DetalleCompra> listaItemsCompra = detalleCompraDAO.obtenerDetallesCompraPorID(compra.getIDcompra());
            compra.setLibrosComprados(listaItemsCompra);

            Compra       sumatoriaCompra     = obtenerSumatoriaCompra(listaItemsCompra);
            final double porcentajeDescuento = compra.getPorcentajeDescuento();
            if (porcentajeDescuento > 0) {
               aplicarDescuentoItemsCompra(listaItemsCompra, porcentajeDescuento);
               compra.setValorCompra(calcularValorCompraConDescuento(listaItemsCompra));
            } else {
               compra.setValorCompra(sumatoriaCompra.getValorCompra());
            }
            compra.setCantidadCompra(sumatoriaCompra.getCantidadCompra());
            compras.add(compra);
         }
         Tienda.agregarLog("Compras obtenidas para el ID: " + IDasociado);
         return compras;
      } catch (Exception e) {
         Tienda.agregarLog("Error al obtener compras ID: " + IDasociado);
      }
      return null;
   }

   private double calcularValorCompraConDescuento (ArrayList<DetalleCompra> listaItemsCompra) {
      double valorCompraConDescuento = 0;
      for (DetalleCompra compraItem : listaItemsCompra) {
         valorCompraConDescuento += (compraItem.getValorUnitario() * compraItem.getCantidad());
      }
      return valorCompraConDescuento;
   }

   private void aplicarDescuentoItemsCompra (ArrayList<DetalleCompra> listaItemsCompra, double porcentajeDescuento) {
      for (DetalleCompra compraItem : listaItemsCompra) {
         double valorBase     = obtenerValorBaseSinImpuesto(compraItem.getValorUnitario());
         double valorUnitario = valorBase * (1 - porcentajeDescuento);
         compraItem.setValorUnitario(valorUnitario + Tienda.getInstance().calcularValorImpuesto(valorUnitario));
      }
   }

   private double obtenerValorBaseSinImpuesto (double valorUnitario) {
      double porcentajeImpuesto = Tienda.getInstance().calcularPorcentajeImpuesto(valorUnitario);
      valorUnitario /= (1 + porcentajeImpuesto);
      return valorUnitario;
   }

   private Compra obtenerSumatoriaCompra (ArrayList<DetalleCompra> detallesCompra) {
      double subTotalCompra      = 0;
      int    cantidadTotalCompra = 0;
      Compra sumatoriaCompra     = new Compra();
      for (DetalleCompra itemCompra : detallesCompra) {
         subTotalCompra += itemCompra.getValorUnitario() * itemCompra.getCantidad();
         cantidadTotalCompra += itemCompra.getCantidad();
      }
      sumatoriaCompra.setValorCompra(subTotalCompra);
      sumatoriaCompra.setCantidadCompra(cantidadTotalCompra);
      return sumatoriaCompra;
   }

   private PreparedStatement getPreparedStatement (String consultaSQL) throws Exception {
      Connection connection = ConnectionToDB.getInstance().getConnection();
      return connection.prepareStatement(consultaSQL);
   }

   public long registrarCompra (Compra compra, double porcentajeDescuento) {
      String consultaSQL = "INSERT INTO COMPRAS (ID_asociado, porcentaje_Descuento) VALUES (?, ?)";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, compra.getIDasociado());
         preparedStatement.setDouble(2, porcentajeDescuento);
         preparedStatement.executeUpdate();
         try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
               long IDcompra = generatedKeys.getLong(1);
               Tienda.agregarLog(String.format("Compra registrada para el ID %d (ID_compra %d)", compra.getIDasociado(), IDcompra));
               return IDcompra;
            }
         }
      } catch (Exception e) {
         Tienda.agregarLog("Error al registrar compra para el ID: " + compra.getIDasociado());
      }
      return -1;
   }
}