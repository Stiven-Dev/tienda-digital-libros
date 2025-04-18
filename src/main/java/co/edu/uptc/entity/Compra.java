package co.edu.uptc.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Compra {
   private       long              ID_Compra;
   private       int               ID_Asociado;
   private       LocalDateTime     fechaCompra;
   private       double            valorCompra;
   private       int               cantidadCompra;
   private final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MMMM/yyyy HH:mm");

   public Compra () {}

   public Compra (long ID_Compra, int ID_Asociado, LocalDateTime fechaCompra, double valorCompra, int cantidadCompra) {
      this.ID_Compra      = ID_Compra;
      this.ID_Asociado    = ID_Asociado;
      this.fechaCompra    = fechaCompra;
      this.valorCompra    = valorCompra;
      this.cantidadCompra = cantidadCompra;
   }

   public long getID_Compra () {
      return ID_Compra;
   }

   public void setID_Compra (long ID_Compra) {
      this.ID_Compra = ID_Compra;
   }

   public int getID_Asociado () {
      return ID_Asociado;
   }

   public void setID_Asociado (int ID_Asociado) {
      this.ID_Asociado = ID_Asociado;
   }

   public LocalDateTime getFechaCompra () {
      return fechaCompra;
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

   //Metodo auxiliar para mostrar la fecha en un formato especifico
   public String getFechaCompraString () {
      return fechaCompra.format(FORMATO_FECHA);
   }
}