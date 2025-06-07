package co.edu.uptc.entity;

import java.util.HashMap;

/**
 * Entidad que representa el carrito de compras de un usuario.
 */
public class Carrito {
   /**
    * ID del usuario asociado al carrito.
    */
   private long ID;
   /**
    * Mapa de artículos en el carrito: ISBN del libro como clave y cantidad como valor.
    */
   private HashMap<Long, Integer> articulos;

   public Carrito () {
   }

   public long getIDAsociado () {
      return ID;
   }

   public void setIDAsociado (long IDAsociado) {
      this.ID = IDAsociado;
   }

   public HashMap<Long, Integer> getArticulos () {
      return articulos;
   }

   public void setArticulos (HashMap<Long, Integer> articulos) {
      this.articulos = articulos;
   }
}