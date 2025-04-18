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
   @Column(name = "anio_publicacion", nullable = false)
   private int      anioPublicacion;
   @Column(name = "categoria", nullable = false)
   private String   categoria;
   @Column(name = "editorial", nullable = false)
   private String   editorial;
   @Column(name = "numero_paginas", nullable = false)
   private int      numeroPaginas;
   @Column(name = "precio_venta", nullable = false)
   private double   precioVenta;
   @Column(name = "cantidad_disponible", nullable = false)
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

   public Libro (long ISBN,
                 String titulo,
                 String autores,
                 int anioPublicacion,
                 String categoria,
                 String editorial,
                 int numeroPaginas,
                 double precioVenta,
                 int cantidadDisponible,
                 FORMATOS FORMATO) {
      this.ISBN               = ISBN;
      this.titulo             = titulo;
      this.autores            = autores;
      this.anioPublicacion    = anioPublicacion;
      this.categoria          = categoria;
      this.editorial          = editorial;
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

   public FORMATOS getFormato () {
      return FORMATO;
   }

   public void setFormato (FORMATOS FORMATOS) {
      this.FORMATO = FORMATOS;
   }
}