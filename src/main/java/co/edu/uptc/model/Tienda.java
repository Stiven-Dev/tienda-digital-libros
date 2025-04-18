package co.edu.uptc.modelo;

import javax.json.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tienda{
	private static final String RUTA_CARRITOS = "persistencia/CARRITOS.json";
	private static final String RUTA_LIBROS   = "persistencia/LIBROS.json";
	private static final String RUTA_USUARIOS = "persistencia/USUARIOS.json";
	private static final String RUTA_COMPRAS  = "persistencia/COMPRAS.json";

	public Tienda (){
	}

	public static int obtenerNuevoCID (){
		int        totalUsuarios = 0;
		JsonObject jsonUsuarios;
		try (InputStream inputStream = new FileInputStream(RUTA_USUARIOS); JsonReader reader = Json.createReader(inputStream)){
			jsonUsuarios = reader.readObject();
		}catch (Exception e){
			System.err.println(e.getMessage());
			return - 1;
		}

		for (String rolActual : jsonUsuarios.keySet()){
			JsonArray usuariosDelRol = jsonUsuarios.getJsonArray(rolActual);
			totalUsuarios += usuariosDelRol.size();
		}
		return totalUsuarios + 1;
	}

	//	public static void guardarCarritoDeCompras (int CID, HashMap<Long, Integer> carritoDeCompras){
	//		JsonObject jsonActual;
	//		try (InputStream inputStream = new FileInputStream(RUTA_CARRITOS); JsonReader reader = Json.createReader(inputStream)){
	//			jsonActual = reader.readObject();
	//		}catch (Exception e){
	//			System.err.println(e.getMessage());
	//			return;
	//		}
	//
	//		JsonArrayBuilder carritosBuilder    = Json.createArrayBuilder();
	//		JsonArray        carritosExistentes = jsonActual.getJsonArray("CARRITOS");
	//		for (JsonValue carrito : carritosExistentes){
	//			carritosBuilder.add(carrito);
	//		}
	//		carritosBuilder.add(convertirCarritoDeComprasAJson(CID, carritoDeCompras)).build();
	//
	//		JsonObject jsonFinal = Json.createObjectBuilder().add("CARRITOS", carritosBuilder).build();
	//		try (OutputStream outputStream = new FileOutputStream(RUTA_CARRITOS); JsonWriter writer = Json.createWriter(outputStream)){
	//			writer.writeObject(jsonFinal);
	//		}catch (Exception e){
	//			System.err.println(e.getMessage());
	//		}
	//	}

	private static JsonObject convertirCarritoDeComprasAJson (int CID, HashMap<Long, Integer> carritoDeCompras){
		JsonArrayBuilder articulosBuilder = Json.createArrayBuilder();
		for (Map.Entry<Long, Integer> entry : carritoDeCompras.entrySet()){
			articulosBuilder.add(Json.createObjectBuilder().add("ISBN", entry.getKey()).add("cantidad", entry.getValue()));
			articulosBuilder.build();
		}
		return Json.createObjectBuilder().add("CID", CID).add("ARTICULOS", articulosBuilder).build();
	}

	private static JsonObject convertirUsuarioAJson (Usuario usuario){
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add("nombreCompleto", usuario.getNombreCompleto());
		jsonObjectBuilder.add("correoElectronico", usuario.getCorreoElectronico());
		jsonObjectBuilder.add("direccionEnvio", usuario.getDireccionEnvio());
		jsonObjectBuilder.add("telefonoContacto", usuario.getTelefonoContacto());
		jsonObjectBuilder.add("claveAcceso", String.valueOf(usuario.getClaveAcceso()));
		jsonObjectBuilder.add("CID", usuario.getCID());
		return jsonObjectBuilder.build();
	}

	public static ROLES obtenerTipoUsuario (String correoElectronico){
		return Usuario.validarRolUsuario(correoElectronico);
	}

	public boolean agregarLibroArchivo (Object[] datos){
		long   ISBN               = (long) datos[0];
		String titulo             = (String) datos[1];
		String autor              = (String) datos[2];
		int    anioPublicacion    = (int) datos[3];
		String genero             = (String) datos[4];
		String editorial          = (String) datos[5];
		int    numeroPaginas      = (int) datos[6];
		double precioVenta        = (double) datos[7];
		int    cantidadDisponible = (int) datos[8];
		String formato            = (String) datos[9];
		Libro  libro              = new Libro(ISBN, titulo, autor, anioPublicacion, genero, editorial, numeroPaginas, precioVenta, cantidadDisponible, formato);
		if (buscarLibro(ISBN) == null){
			escribirLibroEnArchivo(libro);
			return true;
		}
		return false;
	}

	private void escribirLibroEnArchivo (Libro libro){
		JsonObject jsonActual;
		try (InputStream inputStream = new FileInputStream(RUTA_LIBROS); JsonReader reader = Json.createReader(inputStream)){
			jsonActual = reader.readObject();
		}catch (Exception e){
			System.err.println(e.getMessage());
			return;
		}
		JsonArray        librosArray  = jsonActual.getJsonArray("LIBROS");
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for (JsonValue libroActual : librosArray){
			arrayBuilder.add(libroActual);
		}
		arrayBuilder.add(convertirLibroAJson(libro));
		arrayBuilder.build();
		JsonObject jsonFinal = Json.createObjectBuilder().add("LIBROS", arrayBuilder).build();

		try (OutputStream outputStream = new FileOutputStream(RUTA_USUARIOS); JsonWriter writer = Json.createWriter(outputStream)){
			writer.writeObject(jsonFinal);
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
	}

	private JsonObject convertirLibroAJson (Libro libro){
		JsonObjectBuilder libroJson = Json.createObjectBuilder();
		libroJson.add("ISBN", libro.getIsbn());
		libroJson.add("titulo", libro.getTitulo());
		libroJson.add("autores", libro.getAutores());
		libroJson.add("añoPublicacion", libro.getAnioPublicacion());
		libroJson.add("categoria", libro.getCategoria());
		libroJson.add("editorial", libro.getEditorial());
		libroJson.add("numeroPaginas", libro.getNumeroPaginas());
		libroJson.add("precioVenta", libro.getPrecioVenta());
		libroJson.add("cantidadDisponible", libro.getCantidadDisponible());
		libroJson.add("formato", libro.getFormato());
		return libroJson.build();
	}

	private Object[] obtenerLibro (long ISBN, JsonArray libros){
		for (JsonObject libro : libros.getValuesAs(JsonObject.class)){
			if (libro.getJsonNumber("ISBN").longValue() == ISBN){
				return new Object[]{libro.getJsonNumber("ISBN").longValue(),
				                    libro.getString("titulo"),
				                    libro.getString("autores"),
				                    libro.getInt("añoPublicacion"),
				                    libro.getString("categoria"),
				                    libro.getString("editorial"),
				                    libro.getInt("numPaginas"),
				                    libro.getJsonNumber("precioVenta").doubleValue(),
				                    libro.getInt("cantidadInventario"),
				                    libro.getString("formato")
				};
			}
		}
		return null;
	}

	private ArrayList<Libro> obtenerListaDeLibros (JsonArray librosJson){
		ArrayList<Libro> listaDeLibros = new ArrayList<>();
		for (JsonObject libroObject : librosJson.getValuesAs(JsonObject.class)){
			Libro libro = new Libro();
			libro.setIsbn(libroObject.getJsonNumber("ISBN").longValue());
			libro.setTitulo(libroObject.getString("titulo"));
			libro.setAutores(libroObject.getString("autores"));
			libro.setAnioPublicacion(libroObject.getInt("añoPublicacion"));
			libro.setCategoria(libroObject.getString("categoria"));
			libro.setEditorial(libroObject.getString("editorial"));
			libro.setNumeroPaginas(libroObject.getInt("numPaginas"));
			libro.setPrecioVenta(libroObject.getJsonNumber("precioVenta").doubleValue());
			libro.setCantidadDisponible(libroObject.getInt("cantidadInventario"));
			libro.setFormato(libroObject.getString("formato"));
			listaDeLibros.add(libro);
		}
		return listaDeLibros;
	}

	public Object[][] getDataVectorLibros (){
		Object[][] dataVectorLibros = null;
		try (InputStream inputStream = new FileInputStream(RUTA_LIBROS); JsonReader reader = Json.createReader(inputStream)){
			JsonObject       jsonObject    = reader.readObject();
			JsonArray        libros        = jsonObject.getJsonArray("LIBROS");
			ArrayList<Libro> listaDeLibros = obtenerListaDeLibros(libros);
			dataVectorLibros = new Object[libros.size()][11];
			for (int i = 0; i < listaDeLibros.size(); i++){
				Libro libro = listaDeLibros.get(i);
				dataVectorLibros[i][0]  = libro.getIsbn();
				dataVectorLibros[i][1]  = libro.getTitulo();
				dataVectorLibros[i][2]  = libro.getAutores();
				dataVectorLibros[i][3]  = libro.getCategoria();
				dataVectorLibros[i][4]  = libro.getNumeroPaginas();
				dataVectorLibros[i][5]  = libro.getEditorial();
				dataVectorLibros[i][6]  = libro.getAnioPublicacion();
				dataVectorLibros[i][7]  = libro.getFormato();
				dataVectorLibros[i][8]  = libro.getPrecioVenta();
				dataVectorLibros[i][9]  = libro.getCantidadDisponible();
				dataVectorLibros[i][10] = false;
			}
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
		return dataVectorLibros;
	}

	public Object[] buscarLibro (long ISBN){
		try{
			InputStream inputStream = new FileInputStream(RUTA_LIBROS);
			JsonReader  reader      = Json.createReader(inputStream);
			JsonObject  jsonObject  = reader.readObject();

			JsonArray libros = jsonObject.getJsonArray("LIBROS");
			return obtenerLibro(ISBN, libros);
		}catch (IOException e){
			System.err.println(e.getMessage());
			return null;
		}
	}

	public boolean validarUsuarioLogin (String correoElectronico, String claveAcceso){
		Usuario usuario = obtenerUsuario(correoElectronico);

		if (usuario == null){
			return false;
		}

		StringBuilder pswd = new StringBuilder();
		for (char c : usuario.getClaveAcceso()){
			pswd.append(c);
		}
		return pswd.toString().equals(claveAcceso);
	}

	private Usuario obtenerUsuario (String correoElectronico){
		if (! usuarioExiste(correoElectronico)){
			return null;
		}
		ROLES ROL = Usuario.validarRolUsuario(correoElectronico);

		try (InputStream inputStream = new FileInputStream(RUTA_USUARIOS); JsonReader reader = Json.createReader(inputStream)){
			JsonObject jsonObject     = reader.readObject();
			JsonArray  usuariosDelRol = jsonObject.getJsonArray(ROL.name());

			//Se crea un usuario vacio y, se llenan los datos que ya se tienen
			Usuario usuarioLocal = new Usuario();
			usuarioLocal.setCorreoElectronico(correoElectronico);
			usuarioLocal.setRolUsuario(ROL);

			for (JsonObject usuario : usuariosDelRol.getValuesAs(JsonObject.class)){
				if (usuario.getString("correoElectronico").equals(correoElectronico)){
					usuarioLocal.setNombreCompleto(usuario.getString("nombreCompleto"));
					usuarioLocal.setDireccionEnvio(usuario.getString("direccionEnvio"));
					usuarioLocal.setTelefonoContacto(usuario.getJsonNumber("telefonoContacto").longValue());
					usuarioLocal.setClaveAcceso(usuario.getString("claveAcceso").toCharArray());
					usuarioLocal.setCID(usuario.getInt("CID"));
					usuarioLocal.setCarritoDeCompras(getCarritoDeCompras(usuario.getInt("CID")));
					return usuarioLocal;
				}
			}
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	/// El metodo obtiene los datos sin el campo claveAcceso ni el campo carritoDeCompras, ya que es meramente informativo
	public Object[] obtenerUsuarioSeguro (String correoElectronico){
		ROLES ROL = Usuario.validarRolUsuario(correoElectronico);
		try (InputStream inputStream = new FileInputStream(RUTA_USUARIOS); JsonReader reader = Json.createReader(inputStream)){
			JsonObject jsonObject     = reader.readObject();
			JsonArray  usuariosDelRol = jsonObject.getJsonArray(ROL.name());
			for (JsonObject usuario : usuariosDelRol.getValuesAs(JsonObject.class)){
				if (usuario.getString("correoElectronico").equals(correoElectronico)){
					return new Object[]{usuario.getString("nombreCompleto"),
					                    usuario.getString("correoElectronico"),
					                    usuario.getString("direccionEnvio"),
					                    usuario.getJsonNumber("telefonoContacto").longValue(),
					                    usuario.getInt("CID")
					};
				}
			}
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	private HashMap<Long, Integer> getCarritoDeCompras (int CID){
		try (InputStream inputStream = new FileInputStream(RUTA_CARRITOS); JsonReader reader = Json.createReader(inputStream)){
			JsonObject jsonObject = reader.readObject();
			JsonArray  carritos   = jsonObject.getJsonArray("CARRITOS");
			for (JsonObject carritoActual : carritos.getValuesAs(JsonObject.class)){
				if (carritoActual.getInt("CID") == CID){
					return obtenerMapLibrosCarrito(carritoActual);
				}
			}
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	private HashMap<Long, Integer> obtenerMapLibrosCarrito (JsonObject carritoActual){
		HashMap<Long, Integer> librosCarrito  = new HashMap<>(0);
		JsonArray              librosActuales = carritoActual.getJsonArray("ARTICULOS");
		for (JsonObject libro : librosActuales.getValuesAs(JsonObject.class)){
			long ISBN     = libro.getJsonNumber("ISBN").longValue();
			int  cantidad = libro.getInt("cantidad");
			librosCarrito.put(ISBN, cantidad);
		}
		return librosCarrito;
	}

	public boolean usuarioExiste (String correoElectronico){
		try (InputStream inputStream = new FileInputStream(RUTA_USUARIOS); JsonReader reader = Json.createReader(inputStream)){
			JsonObject jsonObject = reader.readObject();

			ROLES ROL = Usuario.validarRolUsuario(correoElectronico);

			JsonArray usuariosDelRol = jsonObject.getJsonArray(ROL.name());
			for (JsonObject usuario : usuariosDelRol.getValuesAs(JsonObject.class)){
				if (usuario.getString("correoElectronico").equals(correoElectronico)){
					return true;
				}
			}
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
		return false;
	}

	@SuppressWarnings("unchecked") //Se valida previamente que el objeto en el indice 6 es un HashMap<Long, Integer>
	public void registrarUsuario (Object[] datosUsuario){
		Usuario usuario = new Usuario((String) datosUsuario[0], //Nombre Completo
		                              (String) datosUsuario[1], //Correo Electronico
		                              (String) datosUsuario[2], //Direccion
		                              (long) datosUsuario[3],   //Telefono
		                              (char[]) datosUsuario[4], //Clave de Acceso
		                              (int) datosUsuario[5],    //CID
		                              (HashMap<Long, Integer>) datosUsuario[6] //Carrito de Compra
		);
		guardarDatosUsuario(usuario);
	}

	private boolean guardarDatosUsuario (Usuario usuario){
		JsonObject jsonActual;
		try (InputStream inputStream = new FileInputStream(RUTA_USUARIOS); JsonReader reader = Json.createReader(inputStream)){
			jsonActual = reader.readObject();
		}catch (Exception e){
			System.err.println(e.getMessage());
			return false;
		}

		ROLES            ROL          = Usuario.validarRolUsuario(usuario.getCorreoElectronico());
		JsonArray        usuariosRol  = jsonActual.getJsonArray(ROL.name());
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

		for (JsonValue usuarioRol : usuariosRol){
			arrayBuilder.add(usuarioRol);
		}
		arrayBuilder.add(convertirUsuarioAJson(usuario));

		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		for (String key : jsonActual.keySet()){
			if (key.equals(ROL.name())){
				jsonObjectBuilder.add(key, arrayBuilder.build());
			}else{
				jsonObjectBuilder.add(key, jsonActual.get(key));
			}
		}

		JsonObject jsonFinal = jsonObjectBuilder.build();
		try (OutputStream outputStream = new FileOutputStream(RUTA_USUARIOS); JsonWriter writer = Json.createWriter(outputStream)){
			writer.writeObject(jsonFinal);
		}catch (Exception e){
			System.err.println(e.getMessage());
			return false;
		}
		//guardarCarritoDeCompras(obtenerNuevoCID(), usuario.getCarritoDeCompras());
		return true;
	}

	private float obtenerPorcentajeDescuento (ROLES rol){
		return rol == ROLES.PREMIUM ? 0.85f : 0.19f;
	}

	public double calcularSubTotalVenta (DefaultTableModel model){
		double    totalVentaNeto       = 0;
		final int columnaValorUnitario = 3;
		final int columnaCantidad      = 5;
		for (int fila = 0; fila < model.getRowCount(); fila++){
			double valorUnitario = (double) model.getValueAt(fila, columnaValorUnitario);
			int    cantidad      = (int) model.getValueAt(fila, columnaCantidad);
			totalVentaNeto += calcularPrecioVenta(valorUnitario, cantidad);
		}
		return totalVentaNeto;
	}

	public double calcularTotalVenta (double subTotal, ROLES rol){
		return subTotal * obtenerPorcentajeDescuento(rol);
	}

	public double calcularPrecioVenta (double valorUnitario, int cantidad){
		double valorImpuesto = calcularValorImpuesto(valorUnitario);
		return (valorUnitario + valorImpuesto) * cantidad;
	}

	public double calcularValorImpuesto (double precioUnidad){
		if (precioUnidad >= 50000){
			return precioUnidad * 0.19;
		}else{
			return precioUnidad * 0.05;
		}
	}

	public boolean actualizarLibro (Object[] datos){
		long ISBN = (long) datos[0];
		if (! eliminarLibro(ISBN)) return false;
		agregarLibroArchivo(datos);
		return true;
	}

	public boolean eliminarLibro (long ISBN){
		Object libroObject = buscarLibro(ISBN);
		//Se verifica que se obtenga un libro válido
		if (libroObject == null){
			return false;
		}
		//Se verifica que no exista ventas asociadas al libro para poder eliminarlo
		if (ventasAsociadasLibro(ISBN)){
			return false;
		}

		JsonObject jsonActual;
		try (InputStream inputStream = new FileInputStream(RUTA_LIBROS); JsonReader reader = Json.createReader(inputStream)){
			jsonActual = reader.readObject();
		}catch (Exception e){
			System.err.println(e.getMessage());
			return false;
		}

		JsonArray        librosExistentes = jsonActual.getJsonArray("LIBROS");
		JsonArrayBuilder librosBuilder    = Json.createArrayBuilder();

		for (JsonValue libroValue : librosExistentes){
			JsonObject libro      = (JsonObject) libroValue;
			long       ISBNActual = libro.getJsonNumber("ISBN").longValue();
			if (ISBNActual == ISBN){
				continue;
			}
			librosBuilder.add(libro);
		}

		librosBuilder.build();
		JsonObject nuevoJson = Json.createObjectBuilder().add("LIBROS", librosBuilder).build();
		try (OutputStream outputStream = new FileOutputStream(RUTA_LIBROS); JsonWriter writer = Json.createWriter(outputStream)){
			writer.writeObject(nuevoJson);
		}catch (Exception e){
			System.err.println("Error al escribir en LIBROS.json: " + e.getMessage());
		}
		return true;
	}

	public boolean ventasAsociadasLibro (long ISBN){
		//TODO
		return true;
	}

	public boolean crearCuenta (Object[] datosUsuario){
		Usuario usuario = new Usuario();
		usuario.setNombreCompleto((String) datosUsuario[0]);
		usuario.setCorreoElectronico((String) datosUsuario[1]);
		usuario.setDireccionEnvio((String) datosUsuario[2]);
		usuario.setTelefonoContacto((long) datosUsuario[3]);
		usuario.setClaveAcceso((char[]) datosUsuario[4]);
		usuario.setRolUsuario(Usuario.validarRolUsuario(usuario.getCorreoElectronico()));
		usuario.setCID(obtenerNuevoCID());
		return guardarDatosUsuario(usuario);
	}

	public static long obtenerNuevoIDCompras (){
		try{
			InputStream inputStream = new FileInputStream(RUTA_COMPRAS);
			JsonReader  reader      = Json.createReader(inputStream);
			JsonObject  jsonObject  = reader.readObject();
			JsonArray   compras     = jsonObject.getJsonArray("COMPRAS");
			return ((long) compras.size()) + 1;
		}catch (Exception e){
			System.err.println(e.getMessage());
			return - 1;
		}
	}

	public Object[][] getDataVectorCompras (int CID){
		Object[][] dataVectorCompras = null;
		try (InputStream inputStream = new FileInputStream(RUTA_COMPRAS); JsonReader reader = Json.createReader(inputStream)){
			JsonObject        jsonObject     = reader.readObject();
			JsonArray         compras        = jsonObject.getJsonArray("COMPRAS");
			ArrayList<Compra> listaDeCompras = obtenerListaTotalDeCompras(compras);
			listaDeCompras = filtrarPorCID(listaDeCompras, CID);
			Collections.reverse(listaDeCompras);
			dataVectorCompras = new Object[listaDeCompras.size()][4];
			for (int i = 0; i < listaDeCompras.size(); i++){
				Compra compra = listaDeCompras.get(i);
				dataVectorCompras[i][0] = compra.getID_Compra();
				dataVectorCompras[i][1] = compra.getCantidadCompra();
				dataVectorCompras[i][2] = compra.getValorCompra();
				dataVectorCompras[i][3] = compra.getFechaCompraString();
			}
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
		return dataVectorCompras;
	}

	private ArrayList<Compra> filtrarPorCID (ArrayList<Compra> listaTotalDeCompras, int CID){
		ArrayList<Compra> listaFiltrada = new ArrayList<>();
		for (Compra locCompra : listaTotalDeCompras){
			if (locCompra.getCID_Asociado() == CID){
				listaFiltrada.add(locCompra);
			}
		}
		return listaFiltrada;
	}

	private ArrayList<Compra> obtenerListaTotalDeCompras (JsonArray comprasArrayJson){
		ArrayList<Compra> listaDeCompras = new ArrayList<>();
		for (JsonObject compraObject : comprasArrayJson.getValuesAs(JsonObject.class)){
			Compra compra = new Compra();
			compra.setCID_Asociado(compraObject.getInt("CID_Asociado"));
			compra.setID_Compra(compraObject.getJsonNumber("ID_Compra").longValue());
			compra.setCantidadCompra(compraObject.getInt("cantidadCompra"));
			compra.setValorCompra(compraObject.getJsonNumber("valorCompra").doubleValue());
			compra.setFechaCompra(compraObject.getString("fechaCompra"));
			listaDeCompras.add(compra);
		}
		return listaDeCompras;
	}
}
