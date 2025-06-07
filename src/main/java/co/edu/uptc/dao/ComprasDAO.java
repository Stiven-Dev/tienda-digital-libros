package co.edu.uptc.dao;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.DetalleCompra;
import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * DAO para operaciones relacionadas con las compras en la base de datos.
 */
public class ComprasDAO {
   private final DetalleCompraDAO detalleCompraDAO = new DetalleCompraDAO();

   /**
    * Metodo que obtiene todas las compras realizadas por un usuario dado su ID.
    *
    * @param IDasociado ID del usuario asociado a las compras.
    *
    * @return ArrayList de objetos Compra, o null si hay error.
    */
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

   /**
    * Metodo que calcula el valor total de la compra con descuento aplicado.
    *
    * @param listaItemsCompra Lista de detalles de compra.
    *
    * @return Valor total de la compra con el descuento ya aplicado.
    */
   private double calcularValorCompraConDescuento (ArrayList<DetalleCompra> listaItemsCompra) {
      double valorCompraConDescuento = 0;
      for (DetalleCompra compraItem : listaItemsCompra) {
         valorCompraConDescuento += (compraItem.getValorUnitario() * compraItem.getCantidad());
      }
      return valorCompraConDescuento;
   }

   /**
    * Metodo que aplica el descuento a los items de la compra.
    *
    * @param listaItemsCompra    Lista de detalles de compra.
    * @param porcentajeDescuento Porcentaje de descuento a aplicar (ej: 0.15 para 15%).
    */
   private void aplicarDescuentoItemsCompra (ArrayList<DetalleCompra> listaItemsCompra, double porcentajeDescuento) {
      for (DetalleCompra compraItem : listaItemsCompra) {
         double valorBase     = obtenerValorBaseSinImpuesto(compraItem.getValorUnitario());
         double valorUnitario = valorBase * (1 - porcentajeDescuento);
         compraItem.setValorUnitario(valorUnitario + Tienda.getInstance().calcularValorImpuesto(valorUnitario));
      }
   }

   /**
    * Metodo que obtiene el valor base sin impuesto de un valor unitario.
    *
    * @param valorUnitario Valor unitario con impuesto.
    *
    * @return Valor base sin impuesto.
    */
   private double obtenerValorBaseSinImpuesto (double valorUnitario) {
      double porcentajeImpuesto = Tienda.getInstance().calcularPorcentajeImpuesto(valorUnitario);
      valorUnitario /= (1 + porcentajeImpuesto);
      return valorUnitario;
   }

   /**
    * Metodo que obtiene la sumatoria de valor y cantidad de una lista de detalles de compra.
    *
    * @param detallesCompra Lista de detalles de compra.
    *
    * @return Objeto Compra con el valor y cantidad total calculados.
    */
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

   /**
    * Metodo que registra una compra en la base de datos.
    *
    * @param compra              Objeto Compra a registrar.
    * @param porcentajeDescuento Porcentaje de descuento aplicado a la compra.
    *
    * @return ID generado de la compra registrada, o -1 si hay error.
    */
   public long registrarCompra (Compra compra, double porcentajeDescuento) {
      if (compra.getLibrosComprados().isEmpty()) {
         Tienda.agregarLog("No se puede registrar una compra sin libros");
         return -1;
      }
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