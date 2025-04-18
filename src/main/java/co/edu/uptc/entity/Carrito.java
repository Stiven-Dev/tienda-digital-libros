package co.edu.uptc.entity;

import jakarta.persistence.*;

import java.util.HashMap;

@Entity @Table(name = "carrito") public class Carrito {
   @Id
   @JoinColumn(name = "ID")
   private long                 ID;
   @OneToOne
   @MapsId
   @JoinColumn(name = "ID")
   private Usuario              usuario;
   @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
   @JoinColumn(name = "ID")
   @MapKeyColumn(name = "ISBN")
   private HashMap<Long, Libro> articulos;

   public Carrito () {
   }

   public long getID () {
      return usuario.getID();
   }

   public void setUsuario (Usuario usuario) {
      this.usuario = usuario;
   }

   public HashMap<Long, Libro> getArticulos () {
      return articulos;
   }

   public void setArticulos (HashMap<Long, Libro> articulos) {
      this.articulos = articulos;
   }
}