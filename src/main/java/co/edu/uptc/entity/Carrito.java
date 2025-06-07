package co.edu.uptc.entity;

import java.util.HashMap;

public class Carrito {
   private long                   ID;
   //<ISBN del libro, cantidad en Carrito>
   private HashMap<Long, Integer> articulos;

   public Carrito () {
   }

   public void setIDAsociado (long IDAsociado) {
      this.ID = IDAsociado;
   }

   public long getIDAsociado () {
      return ID;
   }

   public void setArticulos (HashMap<Long, Integer> articulos) {
      this.articulos = articulos;
   }

   public HashMap<Long, Integer> getArticulos () {
      return articulos;
   }
}