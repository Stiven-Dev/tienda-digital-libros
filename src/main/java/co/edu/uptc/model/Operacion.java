package co.edu.uptc.model;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;
import co.edu.uptc.util.ConnectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Operacion {
   public static ArrayList<Libro> obtenerListaLibros () {
      ConnectionToDB   connectionToDB = ConnectionToDB.getInstance();
      ArrayList<Libro> listaLibros    = new ArrayList<>();
      Libro            libro;
      try (Statement statement = connectionToDB.getConnection().createStatement()) {
         ResultSet resultSet = statement.executeQuery("SELECT * FROM libro");
         while (resultSet.next()) {
            libro = new Libro();
            libro.setISBN(resultSet.getLong("ISBN"));
            libro.setTitulo(resultSet.getString("titulo"));
            libro.setAutores(resultSet.getString("autores"));
            libro.setAnioPublicacion(resultSet.getInt("anioPublicacion"));
            libro.setGenero(resultSet.getString("genero"));
            libro.setEditorial(resultSet.getString("editorial"));
            libro.setNumeroPaginas(resultSet.getInt("numeroPaginas"));
            libro.setPrecioVenta(resultSet.getDouble("precioVenta"));
            libro.setCantidadDisponible(resultSet.getInt("cantidadDisponible"));
            libro.setFORMATO(Libro.FORMATOS.valueOf(resultSet.getString("formato")));
            listaLibros.add(libro);
         }
         resultSet.close();
      } catch (Exception e) {
         System.err.println("Error al obtener la lista de libros: " + e.getMessage());
      }
      return listaLibros;
   }

   public static Usuario obtenerUsuarioMedianteCorreo (String correoElectronico) {
      Usuario usuario     = null;
      String  consultaSQL = String.format("SELECT * FROM USUARIO WHERE CORREOELECTRONICO = '%s'", correoElectronico);
      try {
         ConnectionToDB    connectionToDB    = ConnectionToDB.getInstance();
         Connection        connection        = connectionToDB.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(consultaSQL);
         try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
               usuario = new Usuario();
               usuario.setID(resultSet.getLong("ID"));
               usuario.setNombreCompleto(resultSet.getString("nombreCompleto"));
               usuario.setCorreoElectronico(resultSet.getString("correoElectronico"));
               usuario.setDireccionEnvio(resultSet.getString("direccionEnvio"));
               usuario.setTelefonoContacto(resultSet.getLong("telefonoContacto"));
               usuario.setClaveAcceso(resultSet.getString("claveAcceso").toCharArray());
               usuario.setTipoUsuario(Usuario.ROLES.valueOf(resultSet.getString("tipoUsuario")));
            }
         }
         preparedStatement.close();
      } catch (Exception e) {
         System.err.println("Error al obtener el usuario: " + e.getMessage());
      }
      return usuario;
   }

   public static boolean registrarUsuario (Usuario usuario) {
      //TODO
      return true;
   }

   public static Object[][] obtenerListaCompras () {
      Object[][] compras = null;
      //TODO

      return compras;
   }

   public static void registrarLibro (Libro libro) {
      //TODO
   }

   public static Libro obtenerLibroISBN (long ISBN) {
      Libro  libro       = null;
      String consultaSQL = String.format("SELECT * FROM libro WHERE ISBN = %d", ISBN);
      try {
         Connection        connection        = ConnectionToDB.getInstance().getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(consultaSQL);
         ResultSet         resultSet         = preparedStatement.executeQuery();
         if (resultSet.next()) {
            libro = new Libro();
            libro.setISBN(resultSet.getLong("ISBN"));
            libro.setTitulo(resultSet.getString("titulo"));
            libro.setAutores(resultSet.getString("autores"));
            libro.setAnioPublicacion(resultSet.getInt("anioPublicacion"));
            libro.setGenero(resultSet.getString("genero"));
            libro.setEditorial(resultSet.getString("editorial"));
            libro.setNumeroPaginas(resultSet.getInt("numeroPaginas"));
            libro.setPrecioVenta(resultSet.getDouble("precioVenta"));
            libro.setCantidadDisponible(resultSet.getInt("cantidadDisponible"));
            libro.setFORMATO(Libro.FORMATOS.valueOf(resultSet.getString("formato")));
         }
         preparedStatement.close();
      } catch (Exception e) {
         System.err.println("Error al obtener el libro: " + e.getMessage());
      }
      return libro;
   }

   public static boolean ventasAsociadas (long ISBN) {
      //TODO
      return false;
   }

   public static void eliminarLibro (Libro libro) {
      //TODO
   }

   public static void actualizarDatosUsuario (Usuario nuevosDatosUsuario) {
      //TODO actualizacion de datos del usuario
      if (nuevosDatosUsuario.getClaveAcceso().length >= 0) {
         //TODO validar si se cambia la contraseña o se deja la actual
      }
   }
}