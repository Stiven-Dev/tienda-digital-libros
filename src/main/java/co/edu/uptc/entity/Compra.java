package co.edu.uptc.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity @Table(name = "compra") public class Compra {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private       long              ID_Compra;
   @ManyToOne
   @JoinColumn(name = "ID_Usuario", nullable = false)
   private       int               ID_Asociado;
   @Column(name = "fechaCompra", nullable = false)
   private       LocalDateTime     fechaCompra;
   @Column(name = "valorCompra", nullable = false)
   private       double            valorCompra;
   @Column(name = "cantidadCompra", nullable = false)
   private       int               cantidadCompra;
   @Transient
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