package co.edu.uptc.entity;

import java.util.List;

public class Usuario {
   private long         ID;
   private String       nombreCompleto;
   private String       correoElectronico;
   private String       direccionEnvio;
   private long         telefonoContacto;
   private char[]       claveAcceso;
   private TIPO_USUARIO tipoUsuario;
   private Carrito      carrito;
   private List<Compra> compras;

   public enum TIPO_USUARIO {
      ADMIN, PREMIUM, REGULAR
   }

   public Usuario () {}

   public Usuario (long ID,
                   String nombreCompleto,
                   String correoElectronico,
                   String direccionEnvio,
                   long telefonoContacto,
                   char[] claveAcceso,
                   TIPO_USUARIO tipoUsuario,
                   Carrito carrito,
                   List<Compra> compras) {
      this.ID                = ID;
      this.nombreCompleto    = nombreCompleto;
      this.correoElectronico = correoElectronico;
      this.direccionEnvio    = direccionEnvio;
      this.telefonoContacto  = telefonoContacto;
      this.claveAcceso       = claveAcceso;
      this.tipoUsuario       = tipoUsuario;
      this.carrito           = carrito;
      this.compras           = compras;
   }

   public long getID () {
      return ID;
   }

   public void setID (long ID) {
      this.ID = ID;
   }

   public String getNombreCompleto () {
      return nombreCompleto;
   }

   public void setNombreCompleto (String nombreCompleto) {
      this.nombreCompleto = nombreCompleto;
   }

   public String getCorreoElectronico () {
      return correoElectronico;
   }

   public void setCorreoElectronico (String correoElectronico) {
      this.correoElectronico = correoElectronico;
   }

   public String getDireccionEnvio () {
      return direccionEnvio;
   }

   public void setDireccionEnvio (String direccionEnvio) {
      this.direccionEnvio = direccionEnvio;
   }

   public long getTelefonoContacto () {
      return telefonoContacto;
   }

   public void setTelefonoContacto (long telefonoContacto) {
      this.telefonoContacto = telefonoContacto;
   }

   public char[] getClaveAcceso () {
      return claveAcceso;
   }

   public void setClaveAcceso (char[] claveAcceso) {
      this.claveAcceso = claveAcceso;
   }

   public TIPO_USUARIO getTipoUsuario () {
      return tipoUsuario;
   }

   public void setTipoUsuario (TIPO_USUARIO tipoUsuario) {
      this.tipoUsuario = tipoUsuario;
   }

   public Carrito getCarrito () {
      return carrito;
   }

   public void setCarrito (Carrito carrito) {
      this.carrito = carrito;
   }

   public List<Compra> getCompras () {
      return compras;
   }

   public void setCompras (List<Compra> compras) {
      this.compras = compras;
   }
}