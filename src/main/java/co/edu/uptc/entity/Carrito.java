package co.edu.uptc.entity;

import java.util.HashMap;

public class Carrito {
   private long                 ID;
   private HashMap<Long, Libro> articulos;

   public Carrito () {}

   public Carrito (long ID, HashMap<Long, Libro> articulos) {
      this.ID        = ID;
      this.articulos = articulos;
   }

   public long getID () {
      return ID;
   }

   public void setID (long ID) {
      this.ID = ID;
   }

   public HashMap<Long, Libro> getArticulos () {
      return articulos;
   }

   public void setArticulos (HashMap<Long, Libro> articulos) {
      this.articulos = articulos;
   }
}