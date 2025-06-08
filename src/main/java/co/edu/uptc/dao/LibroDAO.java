package co.edu.uptc.dao;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DAO para operaciones relacionadas con los libros en la base de datos.
 */
public class LibroDAO {
   /**
    * Metodo privado para obtener un PreparedStatement a partir de una consulta SQL.
    *
    * @param consultaSQL Consulta SQL a preparar.
    *
    * @return PreparedStatement listo para usar.
    *
    * @throws SQLException Si ocurre un error de conexión o SQL.
    */
   private static PreparedStatement getPreparedStatement (String consultaSQL) throws SQLException {
      Connection connection = ConnectionToDB.getInstance().getConnection();
      return connection.prepareStatement(consultaSQL);
   }

   /**
    * Metodo que descuenta unidades de libros en la base de datos según la lista de artículos.
    *
    * @param listaArticulos HashMap con ISBN como clave y cantidad a descontar como valor.
    */
   public void descontarUnidades (HashMap<Long, Integer> listaArticulos) {
      String consultaSQL = "UPDATE libro SET cantidad_Disponible = cantidad_Disponible - ? WHERE ISBN = ? AND cantidad_Disponible >= ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         for (Long ISBN : listaArticulos.keySet()) {
            int cantidadADescontar = listaArticulos.get(ISBN);
            preparedStatement.setInt(1, cantidadADescontar);
            preparedStatement.setLong(2, ISBN);
            preparedStatement.setInt(3, cantidadADescontar);
            preparedStatement.executeUpdate();
         }
      } catch (SQLException e) {
         Tienda.agregarLog(e.getMessage());
      }
   }

   /**
    * Metodo que reintegra unidades de libros en la base de datos según la lista de artículos.
    *
    * @param mapArticulos HashMap con ISBN como clave y cantidad a reintegrar como valor.
    */
   public void reintegrarUnidadesLibro (HashMap<Long, Integer> mapArticulos) {
      String consultaSQL = "UPDATE libro SET cantidad_Disponible = cantidad_Disponible + ? WHERE ISBN = ? ";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         for (Long ISBN : mapArticulos.keySet()) {
            int cantidadADescontar = mapArticulos.get(ISBN);
            preparedStatement.setInt(1, cantidadADescontar);
            preparedStatement.setLong(2, ISBN);
            preparedStatement.addBatch();
         }
         preparedStatement.executeBatch();
         Tienda.agregarLog("Unidades reintegradas correctamente");
      } catch (SQLException e) {
         Tienda.agregarLog(e.getMessage());
      }
   }

   /**
    * Metodo que agrega un libro a la base de datos.
    *
    * @param libro Objeto Libro a agregar.
    *
    * @return true si el libro fue agregado correctamente, false en caso contrario.
    */
   public boolean agregarLibro (Libro libro) {
      String consultSQL = "INSERT INTO LIBRO (ISBN, titulo, autores, anio_Publicacion, genero, editorial, numero_Paginas, precio_Venta, cantidad_Disponible, formato) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultSQL)) {
         preparedStatement.setLong(1, libro.getISBN());
         preparedStatement.setString(2, libro.getTitulo());
         preparedStatement.setString(3, libro.getAutores());
         preparedStatement.setInt(4, libro.getAnioPublicacion());
         preparedStatement.setString(5, libro.getGenero());
         preparedStatement.setString(6, libro.getEditorial());
         preparedStatement.setInt(7, libro.getNumeroPaginas());
         preparedStatement.setDouble(8, libro.getPrecioVenta());
         preparedStatement.setInt(9, libro.getCantidadDisponible());
         preparedStatement.setString(10, libro.getFORMATO().name());
         preparedStatement.executeUpdate();
         Tienda.agregarLog("Libro agregado correctamente: " + libro.getTitulo());
         return true;
      } catch (Exception e) {
         Tienda.agregarLog(e.getMessage());
      }
      return false;
   }

   /**
    * Metodo que actualiza los datos de un libro en la base de datos.
    *
    * @param datosLibro Objeto Libro con los datos actualizados.
    *
    * @return true si el libro fue actualizado correctamente, false en caso contrario.
    */
   public boolean actualizarLibro (Libro datosLibro) {
      String consultaSQL = "UPDATE LIBRO SET titulo = ?, autores = ?, anio_Publicacion = ?, genero = ?, editorial = ?, numero_Paginas = ?, precio_Venta = ?, cantidad_Disponible = ?, formato = ? WHERE ISBN = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setString(1, datosLibro.getTitulo());
         preparedStatement.setString(2, datosLibro.getAutores());
         preparedStatement.setInt(3, datosLibro.getAnioPublicacion());
         preparedStatement.setString(4, datosLibro.getGenero());
         preparedStatement.setString(5, datosLibro.getEditorial());
         preparedStatement.setInt(6, datosLibro.getNumeroPaginas());
         preparedStatement.setDouble(7, datosLibro.getPrecioVenta());
         preparedStatement.setInt(8, datosLibro.getCantidadDisponible());
         preparedStatement.setString(9, datosLibro.getFORMATO().name());
         preparedStatement.setLong(10, datosLibro.getISBN());
         int filasAfectadas = preparedStatement.executeUpdate();
         if (filasAfectadas > 0) {
            Tienda.agregarLog("Libro actualizado correctamente: " + datosLibro.getTitulo());
            return true;
         } else {
            Tienda.agregarLog("No se actualizó el libro con ISBN: " + datosLibro.getISBN());
            return false;
         }
      } catch (Exception e) {
         Tienda.agregarLog(e.getMessage());
      }
      return false;
   }

   /**
    * Metodo que busca un libro por su ISBN en la base de datos.
    *
    * @param ISBN ISBN del libro a buscar.
    *
    * @return Objeto Libro si se encuentra, null en caso contrario.
    */
   public Libro buscarLibroISBN (long ISBN) {
      Libro  libro       = null;
      String consultaSQL = "SELECT * FROM libro WHERE ISBN = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, ISBN);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            libro = new Libro();
            libro.setISBN(resultSet.getLong("ISBN"));
            libro.setTitulo(resultSet.getString("titulo"));
            libro.setAutores(resultSet.getString("autores"));
            libro.setAnioPublicacion(resultSet.getInt("anio_Publicacion"));
            libro.setGenero(resultSet.getString("genero"));
            libro.setEditorial(resultSet.getString("editorial"));
            libro.setNumeroPaginas(resultSet.getInt("numero_Paginas"));
            libro.setPrecioVenta(resultSet.getDouble("precio_Venta"));
            libro.setCantidadDisponible(resultSet.getInt("cantidad_Disponible"));
            libro.setFORMATO(Libro.FORMATOS.valueOf(resultSet.getString("formato")));
         }
      } catch (Exception e) {
         Tienda.agregarLog(e.getMessage());
      }
      return libro;
   }

   /**
    * Metodo que elimina un libro de la base de datos por su ISBN.
    *
    * @param ISBN ISBN del libro a eliminar.
    *
    * @return true si el libro fue eliminado correctamente, false en caso contrario.
    */
   public boolean eliminarLibro (long ISBN) {
      String consultaSQL = "DELETE FROM libro WHERE ISBN = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, ISBN);
         int filasAfectadas = preparedStatement.executeUpdate();
         if (filasAfectadas > 0) {
            Tienda.agregarLog("Libro eliminado correctamente con ISBN: " + ISBN);
            return true;
         } else {
            Tienda.agregarLog("No se eliminó el libro con ISBN: " + ISBN);
            return false;
         }
      } catch (Exception e) {
         Tienda.agregarLog(e.getMessage());
      }
      return false;
   }

   /**
    * Metodo que obtiene la lista de todos los libros en la base de datos.
    *
    * @return ArrayList de objetos Libro.
    */
   public ArrayList<Libro> obtenerListaLibros () {
      String           consultaSQL = "SELECT * FROM libro";
      ArrayList<Libro> listaLibros = new ArrayList<>();
      Libro            libro;
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         ResultSet resultSet = preparedStatement.executeQuery();
         while (resultSet.next()) {
            libro = new Libro();
            libro.setISBN(resultSet.getLong("ISBN"));
            libro.setTitulo(resultSet.getString("titulo"));
            libro.setAutores(resultSet.getString("autores"));
            libro.setAnioPublicacion(resultSet.getInt("anio_Publicacion"));
            libro.setGenero(resultSet.getString("genero"));
            libro.setEditorial(resultSet.getString("editorial"));
            libro.setNumeroPaginas(resultSet.getInt("numero_Paginas"));
            libro.setPrecioVenta(resultSet.getDouble("precio_Venta"));
            libro.setCantidadDisponible(resultSet.getInt("cantidad_Disponible"));
            libro.setFORMATO(Libro.FORMATOS.valueOf(resultSet.getString("formato")));
            listaLibros.add(libro);
         }
         resultSet.close();
         Tienda.agregarLog("Lista de libros obtenida correctamente");
      } catch (Exception e) {
         Tienda.agregarLog(e.getMessage());
      }
      return listaLibros;
   }

   /**
    * Metodo que verifica si existen ventas asociadas a un libro por su ISBN.
    *
    * @param IBSN ISBN del libro a verificar.
    *
    * @return true si existen ventas asociadas, false en caso contrario.
    */
   public boolean isVentasAsociadas (long IBSN) {
      boolean compraAsociada = false;
      String  consultaSQL    = "SELECT EXISTS(SELECT 1 FROM compras WHERE ISBN_asociado = ? LIMIT 1) AS compra_asociada";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, IBSN);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            compraAsociada = resultSet.getBoolean("compra_asociada");
            if (compraAsociada) {
               Tienda.agregarLog("Compra Asociada a ISBN: " + IBSN);
            }
            return compraAsociada;
         }
      } catch (SQLException e) {
         Tienda.agregarLog(e.getMessage());
      }
      return compraAsociada;
   }

   /**
    * Metodo que obtiene la cantidad de unidades disponibles de un libro por su ISBN.
    *
    * @param ISBN ISBN del libro.
    *
    * @return Número de unidades disponibles.
    */
   public int obtenerUnidadesDisponibles (long ISBN) {
      String consultaSQL = "SELECT cantidad_Disponible FROM libro WHERE ISBN = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, ISBN);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            return resultSet.getInt("cantidad_Disponible");
         }
      } catch (SQLException e) {
         Tienda.agregarLog(e.getMessage());
      }
      return 0;
   }
}