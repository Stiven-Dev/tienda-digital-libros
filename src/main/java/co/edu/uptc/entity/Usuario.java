package co.edu.uptc.entity;

/**
 * Entidad que representa un usuario del sistema.
 */
public class Usuario {
   /**
    * ID único del usuario.
    */
   private long   ID;
   /**
    * Nombre completo del usuario.
    */
   private String nombreCompleto;
   /**
    * Correo electrónico del usuario.
    */
   private String correoElectronico;
   /**
    * Dirección de envío del usuario.
    */
   private String direccionEnvio;
   /**
    * Teléfono de contacto del usuario.
    */
   private long   telefonoContacto;
   /**
    * Clave de acceso del usuario (encriptada o en texto plano).
    */
   private char[] claveAcceso;
   /**
    * Rol del usuario (ADMIN, PREMIUM, REGULAR).
    */
   private ROLES  tipoUsuario = ROLES.REGULAR;

   public Usuario () {}

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

   public ROLES getTipoUsuario () {
      return tipoUsuario;
   }

   public void setTipoUsuario (ROLES tipoUsuario) {
      this.tipoUsuario = tipoUsuario;
   }

   /**
    * Enum que representa los roles posibles de un usuario.
    */
   public enum ROLES {
      ADMIN,
      PREMIUM,
      REGULAR
   }
}