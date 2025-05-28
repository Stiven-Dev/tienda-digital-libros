package co.edu.uptc.entity;

import java.util.HashMap;

public class Carrito {
   private long                 IDAsociado = 0;
   private Usuario              usuario;
   private HashMap<Long, Libro> articulos;

   public Carrito () {
   }

   public void setIDAsociado (long IDAsociado) {
      this.IDAsociado = IDAsociado;
   }

   public long getIDAsociado () {
      return IDAsociado;
   }

   public void setUsuario (Usuario usuario) {
      this.usuario = usuario;
   }

   public Usuario getUsuario () {
      return usuario;
   }

   public void setArticulos (HashMap<Long, Libro> articulos) {
      this.articulos = articulos;
   }

   public HashMap<Long, Libro> getArticulos () {
      return articulos;
   }
}