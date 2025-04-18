package co.edu.uptc.entity;

public class Libro {
   private long    ISBN;
   private String  titulo;
   private String  autores;
   private int     anioPublicacion;
   private String  categoria;
   private String  editorial;
   private int     numeroPaginas;
   private double  precioVenta;
   private int     cantidadDisponible;
   private FORMATO formato;

   public enum FORMATO {
      DIGITAL, IMPRESO
   }

   public Libro () {
   }

   public Libro (long ISBN,
                 String titulo,
                 String autores,
                 int anioPublicacion,
                 String categoria,
                 String editorial,
                 int numeroPaginas,
                 double precioVenta,
                 int cantidadDisponible,
                 FORMATO formato) {
      this.ISBN               = ISBN;
      this.titulo             = titulo;
      this.autores            = autores;
      this.anioPublicacion    = anioPublicacion;
      this.categoria          = categoria;
      this.editorial          = editorial;
      this.numeroPaginas      = numeroPaginas;
      this.precioVenta        = precioVenta;
      this.cantidadDisponible = cantidadDisponible;
      this.formato            = formato;
   }

   public long getISBN () {
      return ISBN;
   }

   public void setISBN (long ISBN) {
      this.ISBN = ISBN;
   }

   public String getTitulo () {
      return titulo;
   }

   public void setTitulo (String titulo) {
      this.titulo = titulo;
   }

   public String getAutores () {
      return autores;
   }

   public void setAutores (String autores) {
      this.autores = autores;
   }

   public int getAnioPublicacion () {
      return anioPublicacion;
   }

   public void setAnioPublicacion (int anioPublicacion) {
      this.anioPublicacion = anioPublicacion;
   }

   public String getCategoria () {
      return categoria;
   }

   public void setCategoria (String categoria) {
      this.categoria = categoria;
   }

   public String getEditorial () {
      return editorial;
   }

   public void setEditorial (String editorial) {
      this.editorial = editorial;
   }

   public int getNumeroPaginas () {
      return numeroPaginas;
   }

   public void setNumeroPaginas (int numeroPaginas) {
      this.numeroPaginas = numeroPaginas;
   }

   public double getPrecioVenta () {
      return precioVenta;
   }

   public void setPrecioVenta (double precioVenta) {
      this.precioVenta = precioVenta;
   }

   public int getCantidadDisponible () {
      return cantidadDisponible;
   }

   public void setCantidadDisponible (int cantidadDisponible) {
      this.cantidadDisponible = cantidadDisponible;
   }

   public FORMATO getFormato () {
      return formato;
   }

   public void setFormato (FORMATO formato) {
      this.formato = formato;
   }
}