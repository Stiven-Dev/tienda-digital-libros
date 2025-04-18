package co.edu.uptc.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity @Table(name = "usuario") public class Usuario {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long         ID;
   @Column(name = "nombre_completo", nullable = false)
   private String       nombreCompleto;
   @Column(name = "correo_electronico", nullable = false)
   private String       correoElectronico;
   @Column(name = "direccion_envio", nullable = false)
   private String       direccionEnvio;
   @Column(name = "telefono_contacto", nullable = false)
   private long         telefonoContacto;
   @Column(name = "clave_acceso", nullable = false)
   private char[]       claveAcceso;
   @Enumerated(EnumType.STRING)
   @Column(name = "tipo_usuario", nullable = false)
   private ROLES        tipoUsuario;
   @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
   private Carrito      carrito;
   @OneToMany(mappedBy = "ID_Asociado", cascade = CascadeType.ALL)
   private List<Compra> compras;

   public enum ROLES {
      ADMIN,
      PREMIUM,
      REGULAR
   }

   public Usuario () {}

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