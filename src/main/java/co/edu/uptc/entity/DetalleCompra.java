package co.edu.uptc.entity;

/**
 * Entidad que representa el detalle de un libro comprado en una compra.
 */
public class DetalleCompra {
   /**
    * ID único del detalle de compra.
    */
   private long   IDdetalle;
   /**
    * ID de la compra asociada.
    */
   private long   IDcompra;
   /**
    * Título del libro comprado.
    */
   private String titulo;
   /**
    * Cantidad de unidades compradas de este libro.
    */
   private int    cantidad;
   /**
    * Valor unitario del libro al momento de la compra.
    */
   private double valorUnitario;
   /**
    * ISBN del libro asociado a este detalle.
    */
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