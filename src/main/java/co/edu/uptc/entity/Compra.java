package co.edu.uptc.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Entidad que representa una compra realizada por un usuario.
 */
public class Compra {
   /**
    * Formato estándar para mostrar la fecha de la compra.
    */
   public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MMMM/yyyy HH:mm");
   /**
    * ID único de la compra. PK (Primary Key).
    */
   private long IDcompra;
   /**
    * ID del usuario asociado a la compra (FK).
    */
   private long IDasociado;
   /**
    * Fecha y hora en que se realizó la compra.
    */
   private LocalDateTime fechaCompra;
   /**
    * Porcentaje de descuento aplicado a la compra.
    */
   private double porcentajeDescuento;
   /**
    * Método de pago utilizado en la compra.
    */
   private METODO_PAGO metodoPago;
   /**
    * Valor total de la compra (calculado en código, no en BD).
    */
   private double valorCompra;
   /**
    * Cantidad total de libros comprados (calculado en código, no en BD).
    */
   private int cantidadCompra;
   /**
    * Lista de detalles de los libros comprados.
    */
   private ArrayList<DetalleCompra> librosComprados;

   public Compra () {}

   public long getIDcompra () {
      return IDcompra;
   }

   public void setIDcompra (long IDcompra) {
      this.IDcompra = IDcompra;
   }

   public long getIDasociado () {
      return IDasociado;
   }

   public void setIDasociado (long IDasociado) {
      this.IDasociado = IDasociado;
   }

   public String getFechaCompra () {
      return fechaCompra.format(FORMATO_FECHA);
   }

   public void setFechaCompra (LocalDateTime fechaCompra) {
      this.fechaCompra = fechaCompra;
   }

   public double getValorCompra () {
      return valorCompra;
   }

   public void setValorCompra (double valorCompra) {
      this.valorCompra = valorCompra;
   }

   public int getCantidadCompra () {
      return cantidadCompra;
   }

   public void setCantidadCompra (int cantidadCompra) {
      this.cantidadCompra = cantidadCompra;
   }

   public ArrayList<DetalleCompra> getLibrosComprados () {
      return librosComprados;
   }

   public void setLibrosComprados (ArrayList<DetalleCompra> librosComprados) {
      this.librosComprados = librosComprados;
   }

   public double getPorcentajeDescuento () {
      return porcentajeDescuento;
   }

   public void setPorcentajeDescuento (double porcentajeDescuento) {
      this.porcentajeDescuento = porcentajeDescuento;
   }

   public METODO_PAGO getMetodoPago () {
      return metodoPago;
   }

   public void setMetodoPago (METODO_PAGO metodoPago) {
      this.metodoPago = metodoPago;
   }

   /**
    * Enum que representa los métodos de pago disponibles.
    */
   public enum METODO_PAGO {
      EFECTIVO("Efectivo"),
      TARJETA_DEBITO("Tarjeta Débito"),
      TARJETA_CREDITO("Tarjeta Crédito");
      private final String name;

      METODO_PAGO (String name) {
         this.name = name;
      }

      public String getName () {
         return name;
      }
   }
}