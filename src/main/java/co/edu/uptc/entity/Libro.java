package co.edu.uptc.modelo;

public class Libro{
	private long   isbn;
	private String titulo;
	private String autores;
	private int    anioPublicacion;
	private String categoria;
	private String editorial;
	private int    numeroPaginas;
	private double precioVenta;
	private int    cantidadDisponible;
	private String formato;

	public Libro (){
	}

	public Libro (
			long isbn,
			String titulo,
			String autores,
			int anioPublicacion,
			String categoria,
			String editorial,
			int numeroPaginas,
			double precioVenta,
			int cantidadDisponible,
			String formato
	){
		this.isbn               = isbn;
		this.titulo             = titulo;
		this.autores            = autores;
		this.anioPublicacion    = anioPublicacion;
		this.categoria          = categoria;
		this.editorial          = editorial;
		this.numeroPaginas      = numeroPaginas;
		this.precioVenta        = precioVenta;
		this.cantidadDisponible = cantidadDisponible;
		this.formato            = formato.toUpperCase();
	}

	public long getIsbn (){
		return isbn;
	}

	public void setIsbn (long paramIsbn){
		isbn = paramIsbn;
	}

	public String getTitulo (){
		return titulo;
	}

	public void setTitulo (String paramTitulo){
		titulo = paramTitulo;
	}

	public String getAutores (){
		return autores;
	}

	public void setAutores (String paramAutores){
		autores = paramAutores;
	}

	public int getAnioPublicacion (){
		return anioPublicacion;
	}

	public void setAnioPublicacion (int paramAnioPublicacion){
		anioPublicacion = paramAnioPublicacion;
	}

	public String getCategoria (){
		return categoria;
	}

	public void setCategoria (String paramCategoria){
		categoria = paramCategoria;
	}

	public String getEditorial (){
		return editorial;
	}

	public void setEditorial (String paramEditorial){
		editorial = paramEditorial;
	}

	public int getNumeroPaginas (){
		return numeroPaginas;
	}

	public void setNumeroPaginas (int paramNumeroPaginas){
		numeroPaginas = paramNumeroPaginas;
	}

	public double getPrecioVenta (){
		return precioVenta;
	}

	public void setPrecioVenta (double paramPrecioVenta){
		precioVenta = paramPrecioVenta;
	}

	public int getCantidadDisponible (){
		return cantidadDisponible;
	}

	public void setCantidadDisponible (int paramCantidadDisponible){
		cantidadDisponible = paramCantidadDisponible;
	}

	public String getFormato (){
		return formato.toUpperCase();
	}

	public void setFormato (String paramFormato){
		formato = paramFormato.toUpperCase();
	}
}