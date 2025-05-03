package co.edu.uptc.entity;

import jakarta.persistence.*;

@Entity @Table(name = "libro") public class Libro {
   @Id
   @Column(name = "ISBN", unique = true, nullable = false)
   private long     ISBN;
   @Column(name = "titulo", nullable = false)
   private String   titulo;
   @Column(name = "autores", nullable = false)
   private String   autores;
   @Column(name = "anioPublicacion", nullable = false)
   private int      anioPublicacion;
   @Column(name = "genero", nullable = false)
   private String   genero;
   @Column(name = "numeroPaginas", nullable = false)
   private int      numeroPaginas;
   @Column(name = "precioVenta", nullable = false)
   private double   precioVenta;
   @Column(name = "cantidadDisponible", nullable = false)
   private int      cantidadDisponible;
   @Enumerated(EnumType.STRING)
   @Column(name = "formato", nullable = false)
   private FORMATOS FORMATO;

   public enum FORMATOS {
      DIGITAL,
      IMPRESO
   }

   public Libro () {
   }

   public Libro (long ISBN, String titulo, String autores, int anioPublicacion, String genero, int numeroPaginas, double precioVenta, int cantidadDisponible, FORMATOS FORMATO) {
      this.ISBN               = ISBN;
      this.titulo             = titulo;
      this.autores            = autores;
      this.anioPublicacion    = anioPublicacion;
      this.genero             = genero;
      this.numeroPaginas      = numeroPaginas;
      this.precioVenta        = precioVenta;
      this.cantidadDisponible = cantidadDisponible;
      this.FORMATO            = FORMATO;
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

   public FORMATOS getFormato () {
      return FORMATO;
   }

   public void setFormato (FORMATOS FORMATOS) {
      this.FORMATO = FORMATOS;
   }
}