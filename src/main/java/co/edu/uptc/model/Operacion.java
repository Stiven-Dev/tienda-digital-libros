package co.edu.uptc.model;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;
import co.edu.uptc.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Operacion {
   static final EntityManager entityManager = JPAUtil.getEntityManager();

   public static HashMap<Long, Libro> obtenerMapLibrosLocales () {
      HashMap<Long, Libro> librosMap = null;
      try {
         entityManager.getTransaction().begin();
         CriteriaBuilder      criteriaBuilder = entityManager.getCriteriaBuilder();
         CriteriaQuery<Libro> criteriaQuery   = criteriaBuilder.createQuery(Libro.class);
         criteriaQuery.select(criteriaQuery.from(Libro.class));
         List<Libro> libros = new ArrayList<>(entityManager.createQuery(criteriaQuery).getResultList());
         if (libros.isEmpty()) return null;
         librosMap = new HashMap<>();
         for (Libro libro : libros) {
            if (libro == null) break;
            if (libro.getCantidadDisponible() > 0) librosMap.put(libro.getISBN(), libro);
         }
         entityManager.getTransaction().commit();
      } catch (Exception e) {
         entityManager.getTransaction().rollback();
         System.err.println("Error al obtener libros: " + e.getMessage());
      }
      return librosMap;
   }

   public static Usuario obtenerUsuarioMedianteCorreo (String correoElectronico) {
      Usuario usuario = null;
      try {
         entityManager.getTransaction().begin();
         CriteriaBuilder        criteriaBuilder = entityManager.getCriteriaBuilder();
         CriteriaQuery<Usuario> criteriaQuery   = criteriaBuilder.createQuery(Usuario.class);
         Root<Usuario>          usuarioRoot     = criteriaQuery.from(Usuario.class);
         Predicate              condicion       = criteriaBuilder.equal(usuarioRoot.get("correoElectronico"), correoElectronico);
         criteriaQuery.select(usuarioRoot).where(condicion);
         usuario = entityManager.createQuery(criteriaQuery).getSingleResult();
         entityManager.getTransaction().commit();
      } catch (Exception e) {
         entityManager.getTransaction().rollback();
         System.err.println("Error al obtener usuario: " + e.getMessage());
      }
      return usuario;
   }

   public static boolean registrarUsuario (Usuario usuario) {
      try {
         entityManager.getTransaction().begin();
         entityManager.persist(usuario);
         entityManager.getTransaction().commit();
         return true;
      } catch (Exception e) {
         entityManager.getTransaction().rollback();
         System.err.println("Error al guardar usuario: " + e.getMessage());
         return false;
      }
   }
}