package co.edu.uptc.dao;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LibroDAO {
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
         int filasAfectadas = preparedStatement.executeUpdate();
         if (filasAfectadas > 0) {
            Tienda.agregarLog("Libro agregado correctamente: " + libro.getTitulo());
            return true;
         } else {
            Tienda.agregarLog("No se pudo agregar el libro: " + libro.getTitulo());
            return false;
         }
      } catch (Exception e) {
         Tienda.agregarLog(e.getMessage());
      }
      return false;
   }

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

   private static PreparedStatement getPreparedStatement (String consultaSQL) throws SQLException {
      Connection connection = ConnectionToDB.getInstance().getConnection();
      return connection.prepareStatement(consultaSQL);
   }

   public int obtenerUnidadesDisponibles (long ISBN) {
      int    unidadesDisponibles = 0;
      String consultaSQL         = "SELECT cantidad_Disponible FROM libro WHERE ISBN = ?";
      try (PreparedStatement preparedStatement = getPreparedStatement(consultaSQL)) {
         preparedStatement.setLong(1, ISBN);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            unidadesDisponibles = resultSet.getInt("cantidad_Disponible");
         }
         Tienda.agregarLog(String.format("%d Unidades disponibles de %d ", unidadesDisponibles, ISBN));
      } catch (SQLException e) {
         Tienda.agregarLog(e.getMessage());
      }
      return unidadesDisponibles;
   }
}