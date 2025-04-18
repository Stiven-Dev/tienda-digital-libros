package co.edu.uptc.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
   private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("PERSISTENCE_UNIT");

   public static EntityManager getEntityManager () {
      return ENTITY_MANAGER_FACTORY.createEntityManager();
   }

   public static void closeFactory () {
      if (ENTITY_MANAGER_FACTORY.isOpen()) {
         ENTITY_MANAGER_FACTORY.close();
      }
   }
}