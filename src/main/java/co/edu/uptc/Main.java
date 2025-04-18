package co.edu.uptc;

import co.edu.uptc.gui.VentanaPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
   public static void main (String[] args) {
      //TODO
      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DATABASE");
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      new VentanaPrincipal();
   }
}