package co.edu.uptc.entity;

import java.util.List;

public class Usuario {
   private long         ID;
   private String       nombreCompleto;
   private String       correoElectronico;
   private String       direccionEnvio;
   private long         telefonoContacto;
   private char[]       claveAcceso;
   private ROLES        tipoUsuario = ROLES.REGULAR;
   private Carrito      carrito;
   private List<Compra> compras;

   public enum ROLES {
      ADMIN,
      PREMIUM,
      REGULAR
   }

   public Usuario () {}

   public void setID (long ID) {
      this.ID = ID;
   }

   public long getID () {
      return ID;
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

   public ROLES getTipoUsuario () {
      return tipoUsuario;
   }

   public void setTipoUsuario (ROLES tipoUsuario) {
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