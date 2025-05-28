package co.edu.uptc.entity;

public class Libro {
   private long     ISBN;
   private String   titulo;
   private String   autores;
   private int      anioPublicacion;
   private String   genero;
   private String   editorial;
   private int      numeroPaginas;
   private double   precioVenta;
   private int      cantidadDisponible;
   private FORMATOS FORMATO;

   public enum FORMATOS {
      DIGITAL,
      IMPRESO
   }

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
}