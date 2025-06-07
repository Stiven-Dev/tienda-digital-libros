package co.edu.uptc.entity;

/**
 * Entidad que representa un libro en la tienda digital.
 */
public class Libro {
   /**
    * ISBN único del libro. PK (Primary Key).
    */
   private long     ISBN;
   /**
    * Título del libro.
    */
   private String   titulo;
   /**
    * Autores del libro.
    */
   private String   autores;
   /**
    * Año de publicación del libro.
    */
   private int      anioPublicacion;
   /**
    * Género literario del libro.
    */
   private String   genero;
   /**
    * Editorial del libro.
    */
   private String   editorial;
   /**
    * Número de páginas del libro.
    */
   private int      numeroPaginas;
   /**
    * Precio de venta del libro.
    */
   private double   precioVenta;
   /**
    * Cantidad disponible en inventario.
    */
   private int      cantidadDisponible;
   /**
    * Formato del libro (DIGITAL o IMPRESO).
    */
   private FORMATOS FORMATO;

   public Libro () {
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

   public String getGenero () {
      return genero;
   }

   public void setGenero (String genero) {
      this.genero = genero;
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

   public FORMATOS getFORMATO () {
      return FORMATO;
   }

   public void setFORMATO (FORMATOS FORMATOS) {
      this.FORMATO = FORMATOS;
   }

   /**
    * Enum que representa los formatos posibles de un libro.
    */
   public enum FORMATOS {
      DIGITAL,
      IMPRESO
   }
}