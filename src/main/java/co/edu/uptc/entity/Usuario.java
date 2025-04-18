package co.edu.uptc.modelo;

import java.util.HashMap;

public class Usuario{
	private String                 nombreCompleto;
	private String                 correoElectronico;
	private String                 direccionEnvio;
	private long                   telefonoContacto;
	private char[]                 claveAcceso;
	private ROLES                  rolUsuario;
	private HashMap<Long, Integer> carritoDeCompras;
	private int                    CID; //Carrito ID

	public Usuario (){}

	public Usuario (
			String nombreCompleto,
			String correoElectronico,
			String direccionEnvio,
			long telefonoContacto,
			char[] claveAcceso,
			int CID,
			HashMap<Long, Integer> carritoDeCompras
	){
		this.nombreCompleto    = nombreCompleto;
		this.correoElectronico = correoElectronico.toUpperCase();
		this.direccionEnvio    = direccionEnvio;
		this.telefonoContacto  = telefonoContacto;
		this.claveAcceso       = claveAcceso;
		this.rolUsuario        = validarRolUsuario(correoElectronico);
		this.CID               = CID;
		this.carritoDeCompras  = carritoDeCompras;
	}

	static ROLES validarRolUsuario (String correoElectronico){
		if (correoElectronico.matches("^(?i)admin.*$")){
			return ROLES.ADMIN;
		}else if (correoElectronico.matches("^(?i)premium.*$")){
			return ROLES.PREMIUM;
		}
		return ROLES.REGULAR;
	}

	String getNombreCompleto (){
		return nombreCompleto;
	}

	void setNombreCompleto (String paramNombreCompleto){
		nombreCompleto = paramNombreCompleto;
	}

	String getCorreoElectronico (){
		return correoElectronico;
	}

	void setCorreoElectronico (String paramCorreoElectronico){
		correoElectronico = paramCorreoElectronico;
	}

	String getDireccionEnvio (){
		return direccionEnvio;
	}

	void setDireccionEnvio (String paramDireccionEnvio){
		direccionEnvio = paramDireccionEnvio;
	}

	long getTelefonoContacto (){
		return telefonoContacto;
	}

	void setTelefonoContacto (long paramTelefonoContacto){
		telefonoContacto = paramTelefonoContacto;
	}

	char[] getClaveAcceso (){
		return claveAcceso;
	}

	public void setClaveAcceso (char[] paramClaveAcceso){
		claveAcceso = paramClaveAcceso;
	}

	public ROLES getRolUsuario (){
		return rolUsuario;
	}

	public void setRolUsuario (ROLES rolUsuario){
		this.rolUsuario = rolUsuario;
	}

	public HashMap<Long, Integer> getCarritoDeCompras (){
		return carritoDeCompras;
	}

	public void setCarritoDeCompras (HashMap<Long, Integer> carritoDeCompras){
		this.carritoDeCompras = carritoDeCompras;
	}

	public int getCID (){
		return CID;
	}

	public void setCID (int CID){
		this.CID = CID;
	}
}