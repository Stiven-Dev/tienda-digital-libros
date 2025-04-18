package co.edu.uptc.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Compra{
	private       long              ID_Compra;
	private       int               CID_Asociado;
	private       LocalDateTime     fechaCompra;
	private       double            valorCompra;
	private       int               cantidadCompra;
	private final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MMMM/yyyy HH:mm");

	public Compra (){}

	public Compra (int CID_Asociado, LocalDateTime fechaCompra, double valorCompra, int cantidadCompra){
		this.CID_Asociado   = CID_Asociado;
		this.fechaCompra    = fechaCompra;
		this.valorCompra    = valorCompra;
		this.cantidadCompra = cantidadCompra;
	}

	public long getID_Compra (){
		return ID_Compra;
	}

	public void setID_Compra (long ID_Compra){
		this.ID_Compra = ID_Compra;
	}

	public int getCID_Asociado (){
		return CID_Asociado;
	}

	public void setCID_Asociado (int CID_Asociado){
		this.CID_Asociado = CID_Asociado;
	}

	public String getFechaCompraString (){
		return obtenerFechaString();
	}

	public LocalDateTime getFechaCompraLocalDateTime (){
		return fechaCompra;
	}

	public void setFechaCompra (String fechaCompra){
		this.fechaCompra = obtenerFechaLocalDateTime(fechaCompra);
	}

	public double getValorCompra (){
		return valorCompra;
	}

	public void setValorCompra (double valorCompra){
		this.valorCompra = valorCompra;
	}

	public int getCantidadCompra (){
		return cantidadCompra;
	}

	public void setCantidadCompra (int cantidadCompra){
		this.cantidadCompra = cantidadCompra;
	}

	private LocalDateTime obtenerFechaLocalDateTime (String fechaString){
		return LocalDateTime.parse(fechaString, FORMATO_FECHA);
	}

	private String obtenerFechaString (){
		return fechaCompra.format(FORMATO_FECHA);
	}
}
