package co.edu.uptc.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Compra {
   //PK
   private             long                     IDcompra;
   private             long                     IDasociado; //No se muestra al Usuario
   private             LocalDateTime            fechaCompra;
   private             double                   porcentajeDescuento;
   private             METODO_PAGO              metodoPago;
   private             double                   valorCompra; //No existe en la base de datos, se calcula en el código
   private             int                      cantidadCompra; //No existe en la base de datos, se calcula en el código
   private             ArrayList<DetalleCompra> librosComprados;
   public static final DateTimeFormatter        FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MMMM/yyyy HH:mm");

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

   public enum METODO_PAGO {
      //TODO: Implementar métodos de pago
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