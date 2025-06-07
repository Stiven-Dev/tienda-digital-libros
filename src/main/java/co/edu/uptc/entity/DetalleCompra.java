package co.edu.uptc.entity;

public class DetalleCompra {
   private long   IDdetalle;
   private long   IDcompra;
   private String titulo;
   private int    cantidad;
   private double valorUnitario;
   private long   ISBNasociado;

   public DetalleCompra () {}

   public long getIDdetalle () {
      return IDdetalle;
   }

   public void setIDdetalle (long IDdetalle) {
      this.IDdetalle = IDdetalle;
   }

   public long getIDcompra () {
      return IDcompra;
   }

   public void setIDcompra (long IDcompra) {
      this.IDcompra = IDcompra;
   }

   public String getTitulo () {
      return titulo;
   }

   public void setTitulo (String titulo) {
      this.titulo = titulo;
   }

   public int getCantidad () {
      return cantidad;
   }

   public void setCantidad (int cantidad) {
      this.cantidad = cantidad;
   }

   public double getValorUnitario () {
      return valorUnitario;
   }

   public void setValorUnitario (double valorUnitario) {
      this.valorUnitario = valorUnitario;
   }

   public long getISBNasociado () {
      return ISBNasociado;
   }

   public void setISBNasociado (long ISBNasociado) {
      this.ISBNasociado = ISBNasociado;
   }
}