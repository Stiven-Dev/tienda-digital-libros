package co.edu.uptc.dao;

import co.edu.uptc.entity.Usuario;
import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UsuarioDAO {
   public float obtenerDescuentoTipoUsuario (Usuario.ROLES rol) {
      return rol == Usuario.ROLES.PREMIUM ? 0.15f : 0;
   }

   public boolean registrarUsuario (Usuario usuario) {
      String consultaSQL = "INSERT INTO USUARIO (nombre_Completo, correo_Electronico, direccion_Envio, telefono_Contacto, clave_Acceso, tipoUsuario) VALUES (?, ?, ?, ?, ?, ?)";
      try {
         PreparedStatement preparedStatement = getPreparedStatement(consultaSQL);
         preparedStatement.setString(1, usuario.getNombreCompleto());
         preparedStatement.setString(2, usuario.getCorreoElectronico());
         if (usuario.getDireccionEnvio() == null || usuario.getDireccionEnvio().isBlank()) {
            usuario.setDireccionEnvio("No especificada");
         }
         preparedStatement.setString(3, usuario.getDireccionEnvio());
         if (usuario.getTelefonoContacto() < 3000000000L) {
            usuario.setTelefonoContacto(0L);
         }
         preparedStatement.setLong(4, usuario.getTelefonoContacto());
         preparedStatement.setString(5, new String(usuario.getClaveAcceso()));
         preparedStatement.setString(6, usuario.getTipoUsuario().name());
         preparedStatement.executeUpdate();
         preparedStatement.close();
         Tienda.agregarLog("Usuario registrado: " + usuario.getCorreoElectronico());
      } catch (Exception e) {
         Tienda.agregarLog(e.getMessage());
         return false;
      }
      return true;
   }

   public Usuario obtenerUsuarioMedianteCorreo (String correoElectronico) {
      Usuario usuario     = null;
      String  consultaSQL = "SELECT * FROM USUARIO WHERE correo_Electronico = ?";
      try {
         PreparedStatement preparedStatement = getPreparedStatement(consultaSQL);
         preparedStatement.setString(1, correoElectronico);
         try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
               usuario = new Usuario();
               usuario.setID(resultSet.getLong("ID"));
               usuario.setNombreCompleto(resultSet.getString("nombre_Completo"));
               usuario.setCorreoElectronico(resultSet.getString("correo_Electronico"));
               usuario.setDireccionEnvio(resultSet.getString("direccion_Envio"));
               usuario.setTelefonoContacto(resultSet.getLong("telefono_Contacto"));
               usuario.setClaveAcceso(resultSet.getString("clave_Acceso").toCharArray());
               usuario.setTipoUsuario(Usuario.ROLES.valueOf(resultSet.getString("tipo_Usuario")));
            }
         }
         preparedStatement.close();
      } catch (Exception e) {
         Tienda.agregarLog(e.getMessage());
      }
      return usuario;
   }

   public boolean validarRegistro (Usuario usuario) {
      if (obtenerUsuarioMedianteCorreo(usuario.getCorreoElectronico()) != null) {
         Tienda.agregarLog("Registro fallido: " + usuario.getCorreoElectronico() + " ya está registrado.");
         JOptionPane.showMessageDialog(null, "El correo electrónico ya está registrado.", "Error de registro", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      return registrarUsuario(usuario);
   }

   public boolean actualizarDatosUsuario (Usuario nuevosDatosUsuario) {
      StringBuilder consultaSQL         = new StringBuilder("UPDATE USUARIO SET nombre_Completo = ?, correo_Electronico = ?");
      boolean       actualizarDireccion = nuevosDatosUsuario.getDireccionEnvio() != null && !nuevosDatosUsuario.getDireccionEnvio().isBlank();
      boolean       actualizarTelefono  = nuevosDatosUsuario.getTelefonoContacto() > 3000000000L;
      boolean       actualizarClave     = nuevosDatosUsuario.getClaveAcceso() != null && nuevosDatosUsuario.getClaveAcceso().length >= 8;
      if (actualizarDireccion) {
         consultaSQL.append(", direccion_Envio = ?");
      }
      if (actualizarTelefono) {
         consultaSQL.append(", telefono_Contacto = ?");
      }
      if (actualizarClave) {
         consultaSQL.append(", clave_Acceso = ?");
      }
      consultaSQL.append(" WHERE ID = ?");
      try {
         PreparedStatement preparedStatement = getPreparedStatement(consultaSQL.toString());
         preparedStatement.setString(1, nuevosDatosUsuario.getNombreCompleto());
         preparedStatement.setString(2, nuevosDatosUsuario.getCorreoElectronico());
         int parametroIndex = 3;
         if (actualizarDireccion) {
            preparedStatement.setString(parametroIndex, nuevosDatosUsuario.getDireccionEnvio());
            parametroIndex++;
         }
         if (actualizarTelefono) {
            preparedStatement.setLong(parametroIndex, nuevosDatosUsuario.getTelefonoContacto());
            parametroIndex++;
         }
         if (actualizarClave) {
            preparedStatement.setString(parametroIndex, new String(nuevosDatosUsuario.getClaveAcceso()));
            parametroIndex++;
         }
         preparedStatement.setLong(parametroIndex, nuevosDatosUsuario.getID());
         int filasActualizadas = preparedStatement.executeUpdate();
         preparedStatement.close();
         if (filasActualizadas > 0) {
            Tienda.agregarLog("Se actualiza el usuario: " + nuevosDatosUsuario.getCorreoElectronico());
            if (actualizarClave) {
               Tienda.agregarLog("Contraseña actualizada para: " + nuevosDatosUsuario.getCorreoElectronico());
            }
            return true;
         }
      } catch (SQLException e) {
         Tienda.agregarLog(e.getMessage());
      }
      return false;
   }

   public Usuario validarCredencialesLogin (Usuario datosUsuario) {
      Usuario usuario = obtenerUsuarioMedianteCorreo(datosUsuario.getCorreoElectronico());
      if (usuario == null) {
         return null;
      }
      return Arrays.equals(usuario.getClaveAcceso(), datosUsuario.getClaveAcceso()) ? usuario : null;
   }

   private static PreparedStatement getPreparedStatement (String consultaSQL) throws SQLException {
      Connection connection = ConnectionToDB.getInstance().getConnection();
      return connection.prepareStatement(consultaSQL);
   }
}