package co.edu.uptc.model;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;
import co.edu.uptc.entity.Usuario.ROLES;
import co.edu.uptc.gui.PanelCarrito;
import co.edu.uptc.gui.PanelLibros;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Tienda {
   public Tienda () {
   }

   //   public static int obtenerNuevoCID () {
//      int        totalUsuarios = 0;
//      JsonObject jsonUsuarios;
//      try (InputStream inputStream = new FileInputStream(RUTA_USUARIOS);
//           JsonReader reader = Json.createReader(inputStream)
//      ) {
//         jsonUsuarios = reader.readObject();
//      } catch (Exception e) {
//         System.err.println(e.getMessage());
//         return -1;
//      }
//      for (String rolActual : jsonUsuarios.keySet()) {
//         JsonArray usuariosDelRol = jsonUsuarios.getJsonArray(rolActual);
//         totalUsuarios += usuariosDelRol.size();
//      }
//      return totalUsuarios + 1;
//   }
//   //	public static void guardarCarritoDeCompras (int CID, HashMap<Long, Integer> carritoDeCompras){
//   //		JsonObject jsonActual;
//   //		try (InputStream inputStream = new FileInputStream(RUTA_CARRITOS); JsonReader reader = Json.createReader(inputStream)){
//   //			jsonActual = reader.readObject();
//   //		}catch (Exception e){
//   //			System.err.println(e.getMessage());
//   //			return;
//   //		}
//   //
//   //		JsonArrayBuilder carritosBuilder    = Json.createArrayBuilder();
//   //		JsonArray        carritosExistentes = jsonActual.getJsonArray("CARRITOS");
//   //		for (JsonValue carrito : carritosExistentes){
//   //			carritosBuilder.add(carrito);
//   //		}
//   //		carritosBuilder.add(convertirCarritoDeComprasAJson(CID, carritoDeCompras)).build();
//   //
//   //		JsonObject jsonFinal = Json.createObjectBuilder().add("CARRITOS", carritosBuilder).build();
//   //		try (OutputStream outputStream = new FileOutputStream(RUTA_CARRITOS); JsonWriter writer = Json.createWriter(outputStream)){
//   //			writer.writeObject(jsonFinal);
//   //		}catch (Exception e){
//   //			System.err.println(e.getMessage());
//   //		}
//   //	}
//
//   private static JsonObject convertirCarritoDeComprasAJson (int CID, HashMap<Long, Integer> carritoDeCompras) {
//      JsonArrayBuilder articulosBuilder = Json.createArrayBuilder();
//      for (Map.Entry<Long, Integer> entry : carritoDeCompras.entrySet()) {
//         articulosBuilder.add(Json.createObjectBuilder().add("ISBN", entry.getKey()).add("cantidad", entry.getValue()));
//         articulosBuilder.build();
//      }
//      return Json.createObjectBuilder().add("CID", CID).add("ARTICULOS", articulosBuilder).build();
//   }
//
//   private static JsonObject convertirUsuarioAJson (Usuario usuario) {
//      JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
//      jsonObjectBuilder.add("nombreCompleto", usuario.getNombreCompleto());
//      jsonObjectBuilder.add("correoElectronico", usuario.getCorreoElectronico());
//      jsonObjectBuilder.add("direccionEnvio", usuario.getDireccionEnvio());
//      jsonObjectBuilder.add("telefonoContacto", usuario.getTelefonoContacto());
//      jsonObjectBuilder.add("claveAcceso", String.valueOf(usuario.getClaveAcceso()));
//      jsonObjectBuilder.add("CID", usuario.getCID());
//      return jsonObjectBuilder.build();
//   }
//
//   public static ROLES obtenerTipoUsuario (String correoElectronico) {
//      return Usuario.validarRolUsuario(correoElectronico);
//   }
//

//   private void escribirLibroEnArchivo (Libro libro) {
//      Operacion.registrarLibro(libro);
//   }

   //
//   private JsonObject convertirLibroAJson (Libro libro) {
//      JsonObjectBuilder libroJson = Json.createObjectBuilder();
//      libroJson.add("ISBN", libro.getIsbn());
//      libroJson.add("titulo", libro.getTitulo());
//      libroJson.add("autores", libro.getAutores());
//      libroJson.add("añoPublicacion", libro.getAnioPublicacion());
//      libroJson.add("categoria", libro.getCategoria());
//      libroJson.add("editorial", libro.getEditorial());
//      libroJson.add("numeroPaginas", libro.getNumeroPaginas());
//      libroJson.add("precioVenta", libro.getPrecioVenta());
//      libroJson.add("cantidadDisponible", libro.getCantidadDisponible());
//      libroJson.add("formato", libro.getFormato());
//      return libroJson.build();
//   }
//
//   private Object[] obtenerLibro (long ISBN, JsonArray libros) {
//      for (JsonObject libro : libros.getValuesAs(JsonObject.class)) {
//         if (libro.getJsonNumber("ISBN").longValue() == ISBN) {
//            return new Object[] {libro.getJsonNumber("ISBN").longValue(),
//                                 libro.getString("titulo"),
//                                 libro.getString("autores"),
//                                 libro.getInt("añoPublicacion"),
//                                 libro.getString("categoria"),
//                                 libro.getString("editorial"),
//                                 libro.getInt("numPaginas"),
//                                 libro.getJsonNumber("precioVenta").doubleValue(),
//                                 libro.getInt("cantidadInventario"),
//                                 libro.getString("formato")
//            };
//         }
//      }
//      return null;
//   }
//

   public Object[][] getDataVectorLibros () {
      ArrayList<Libro> listaLibros = Operacion.obtenerListaLibros();
      if (listaLibros.isEmpty()) return null;
      Object[][] dataVectorLibros = new Object[listaLibros.size()][11];
      for (int i = 0; i < listaLibros.size(); i++) {
         Libro libro = listaLibros.get(i);
         //Se valida que el libro tenga suficientes unidades para ser mostrado al público
         if (libro.getCantidadDisponible() < 1) continue;
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.ISBN.getIndex()]      = libro.getISBN();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.TITULO.getIndex()]    = libro.getTitulo();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.AUTORES.getIndex()]   = libro.getAutores();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.GENERO.getIndex()]    = libro.getGenero();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.PAGINAS.getIndex()]   = libro.getNumeroPaginas();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.EDITORIAL.getIndex()] = libro.getEditorial();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.ANIO.getIndex()]      = libro.getAnioPublicacion();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.FORMATO.getIndex()]   = libro.getFORMATO();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.PRECIO.getIndex()]    = libro.getPrecioVenta();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.CANTIDAD.getIndex()]  = libro.getCantidadDisponible();
         dataVectorLibros[i][PanelLibros.NOMBRE_COLUMNAS.AGREGAR.getIndex()]   = false;
      }
      return dataVectorLibros;
   }

   public Libro buscarLibro (long ISBN) {
      return Operacion.obtenerLibroISBN(ISBN);
   }
//
//   public boolean validarUsuarioLogin (String correoElectronico, String claveAcceso) {
//      Usuario usuario = obtenerUsuario(correoElectronico);
//      if (usuario == null) {
//         return false;
//      }
//      StringBuilder pswd = new StringBuilder();
//      for (char c : usuario.getClaveAcceso()) {
//         pswd.append(c);
//      }
//      return pswd.toString().equals(claveAcceso);
//   }
//
//   private Usuario obtenerUsuario (String correoElectronico) {
//      if (!usuarioExiste(correoElectronico)) {
//         return null;
//      }
//      ROLES ROL = Usuario.validarRolUsuario(correoElectronico);
//      try (InputStream inputStream = new FileInputStream(RUTA_USUARIOS);
//           JsonReader reader = Json.createReader(inputStream)
//      ) {
//         JsonObject jsonObject     = reader.readObject();
//         JsonArray  usuariosDelRol = jsonObject.getJsonArray(ROL.name());
//         //Se crea un usuario vacio y, se llenan los datos que ya se tienen
//         Usuario usuarioLocal = new Usuario();
//         usuarioLocal.setCorreoElectronico(correoElectronico);
//         usuarioLocal.setRolUsuario(ROL);
//         for (JsonObject usuario : usuariosDelRol.getValuesAs(JsonObject.class)) {
//            if (usuario.getString("correoElectronico").equals(correoElectronico)) {
//               usuarioLocal.setNombreCompleto(usuario.getString("nombreCompleto"));
//               usuarioLocal.setDireccionEnvio(usuario.getString("direccionEnvio"));
//               usuarioLocal.setTelefonoContacto(usuario.getJsonNumber("telefonoContacto").longValue());
//               usuarioLocal.setClaveAcceso(usuario.getString("claveAcceso").toCharArray());
//               usuarioLocal.setCID(usuario.getInt("CID"));
//               usuarioLocal.setCarritoDeCompras(getCarritoDeCompras(usuario.getInt("CID")));
//               return usuarioLocal;
//            }
//         }
//      } catch (Exception e) {
//         System.err.println(e.getMessage());
//      }
//      return null;
//   }
//
//   /// El metodo obtiene los datos sin el campo claveAcceso ni el campo carritoDeCompras, ya que es meramente informativo
//   public Object[] obtenerUsuarioSeguro (String correoElectronico) {
//      ROLES ROL = Usuario.validarRolUsuario(correoElectronico);
//      try (InputStream inputStream = new FileInputStream(RUTA_USUARIOS);
//           JsonReader reader = Json.createReader(inputStream)
//      ) {
//         JsonObject jsonObject     = reader.readObject();
//         JsonArray  usuariosDelRol = jsonObject.getJsonArray(ROL.name());
//         for (JsonObject usuario : usuariosDelRol.getValuesAs(JsonObject.class)) {
//            if (usuario.getString("correoElectronico").equals(correoElectronico)) {
//               return new Object[] {usuario.getString("nombreCompleto"), usuario.getString("correoElectronico"), usuario.getString("direccionEnvio"), usuario.getJsonNumber(
//                     "telefonoContacto").longValue(), usuario.getInt("CID")
//               };
//            }
//         }
//      } catch (Exception e) {
//         System.err.println(e.getMessage());
//      }
//      return null;
//   }
//
//   private HashMap<Long, Integer> getCarritoDeCompras (int CID) {
//      try (InputStream inputStream = new FileInputStream(RUTA_CARRITOS);
//           JsonReader reader = Json.createReader(inputStream)
//      ) {
//         JsonObject jsonObject = reader.readObject();
//         JsonArray  carritos   = jsonObject.getJsonArray("CARRITOS");
//         for (JsonObject carritoActual : carritos.getValuesAs(JsonObject.class)) {
//            if (carritoActual.getInt("CID") == CID) {
//               return obtenerMapLibrosCarrito(carritoActual);
//            }
//         }
//      } catch (Exception e) {
//         System.err.println(e.getMessage());
//      }
//      return null;
//   }
//
//   private HashMap<Long, Integer> obtenerMapLibrosCarrito (JsonObject carritoActual) {
//      HashMap<Long, Integer> librosCarrito  = new HashMap<>(0);
//      JsonArray              librosActuales = carritoActual.getJsonArray("ARTICULOS");
//      for (JsonObject libro : librosActuales.getValuesAs(JsonObject.class)) {
//         long ISBN     = libro.getJsonNumber("ISBN").longValue();
//         int  cantidad = libro.getInt("cantidad");
//         librosCarrito.put(ISBN, cantidad);
//      }
//      return librosCarrito;
//   }
//

   //
//   @SuppressWarnings("unchecked") //Se valida previamente que el objeto en el indice 6 es un HashMap<Long, Integer>
//   public void registrarUsuario (Object[] datosUsuario) {
//      Usuario usuario = new Usuario((String) datosUsuario[0], //Nombre Completo
//                                    (String) datosUsuario[1], //Correo Electronico
//                                    (String) datosUsuario[2], //Direccion
//                                    (long) datosUsuario[3],   //Telefono
//                                    (char[]) datosUsuario[4], //Clave de Acceso
//                                    (int) datosUsuario[5],    //CID
//                                    (HashMap<Long, Integer>) datosUsuario[6] //Carrito de Compra
//      );
//      guardarDatosUsuario(usuario);
//   }
   private float obtenerPorcentajeDescuento (ROLES rol) {
      return rol == ROLES.PREMIUM ? 0.15f : 0;
   }

   public double calcularSubTotalVenta (DefaultTableModel model) {
      double    subTotalVenta        = 0;
      final int columnaValorUnitario = PanelCarrito.NOMBRE_COLUMNAS.VALOR_UNITARIO.getIndex();
      final int columnaCantidad      = PanelCarrito.NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      for (int fila = 0; fila < model.getRowCount(); fila++) {
         double valorUnitario = (double) model.getValueAt(fila, columnaValorUnitario);
         int    cantidad      = (int) model.getValueAt(fila, columnaCantidad);
         subTotalVenta += calcularPrecioVenta(valorUnitario, cantidad);
      }
      return subTotalVenta;
   }

   public double calcularTotalVenta (double subTotal, ROLES rol) {
      return subTotal * (1 - obtenerPorcentajeDescuento(rol));
   }

   public double calcularPrecioVenta (double valorUnitario, int cantidad) {
      return valorUnitario * cantidad;
   }

   public double calcularValorImpuesto (double precioUnidad) {
      if (precioUnidad >= 50000) {
         return precioUnidad * 0.19;
      } else {
         return precioUnidad * 0.05;
      }
   }

   public HashMap<Long, Libro> getLibrosLocales () {
      ArrayList<Libro>     listaLibros = Operacion.obtenerListaLibros();
      HashMap<Long, Libro> mapLibros   = new HashMap<>(listaLibros.size());
      for (Libro libro : listaLibros) {
         Long ISBN = libro.getISBN();
         mapLibros.put(ISBN, libro);
      }
      return mapLibros;
   }

   public boolean registrarUsuario (Usuario usuario) {
      return Operacion.registrarUsuario(usuario);
   }

   public Usuario validarDatosLogin (Usuario datosLogin) {
      String  correoElectronico = datosLogin.getCorreoElectronico();
      char[]  claveAcceso       = datosLogin.getClaveAcceso();
      Usuario usuarioObtenido   = obtenerUsuarioMedianteCorreo(correoElectronico);
      if (usuarioObtenido == null) {
         return null;
      }
      if (Arrays.equals(usuarioObtenido.getClaveAcceso(), claveAcceso)) {
         return usuarioObtenido;
      }
      return null;
   }

   public Usuario obtenerUsuarioMedianteCorreo (String correoElectronico) {
      return Operacion.obtenerUsuarioMedianteCorreo(correoElectronico);
   }

   public boolean actualizarLibro (Libro datosLibro) {
      long  ISBN  = datosLibro.getISBN();
      Libro libro = buscarLibro(ISBN);
      if (libro != null) {
         actualizarLibro(datosLibro);
         return true;
      }
      return false;
   }

   public boolean ventasAsociadas (long IBSN) {
      return Operacion.ventasAsociadas(IBSN);
   }

   public void eliminarLibro (long ISBN) {
      Libro libro = buscarLibro(ISBN);
      Operacion.eliminarLibro(libro);
   }

   public boolean crearCuenta (Usuario usuario) {
      if (obtenerUsuarioMedianteCorreo(usuario.getCorreoElectronico()) != null) {
         return false;
      }
      return registrarUsuario(usuario);
   }

   //
//   public static long obtenerNuevoIDCompras () {
//      try {
//         InputStream inputStream = new FileInputStream(RUTA_COMPRAS);
//         JsonReader  reader      = Json.createReader(inputStream);
//         JsonObject  jsonObject  = reader.readObject();
//         JsonArray   compras     = jsonObject.getJsonArray("COMPRAS");
//         return ((long) compras.size()) + 1;
//      } catch (Exception e) {
//         System.err.println(e.getMessage());
//         return -1;
//      }
//   }
//
   public Object[][] getDataVectorCompras (long ID) {
      return Operacion.obtenerListaCompras();
   }

   public double calcularValorTotalImpuesto (DefaultTableModel model) {
      double    valorTotalImpuesto   = 0;
      final int columnaValorImpuesto = PanelCarrito.NOMBRE_COLUMNAS.VALOR_IMPUESTO.getIndex();
      final int columnaCantidad      = PanelCarrito.NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      for (int i = 0; i < model.getRowCount(); i++) {
         double valorImpuestoUnidad = (double) model.getValueAt(i, columnaValorImpuesto);
         int    cantidadLibro       = (int) model.getValueAt(i, columnaCantidad);
         double valorImpuestoLibro  = valorImpuestoUnidad * cantidadLibro;
         valorTotalImpuesto += valorImpuestoLibro;
      }
      return valorTotalImpuesto;
   }

   public int obtenerCantidadLibros (DefaultTableModel model) {
      int       cantidadTotalLibros = 0;
      final int columnaCantidad     = PanelCarrito.NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      for (int i = 0; i < model.getRowCount(); i++) {
         int cantidadLibro = (int) model.getValueAt(i, columnaCantidad);
         cantidadTotalLibros += cantidadLibro;
      }
      return cantidadTotalLibros;
   }

   public void actualizarDatosUsuario (Usuario nuevosDatosUsuario) {
      Operacion.actualizarDatosUsuario(nuevosDatosUsuario);
   }

//
//   private ArrayList<Compra> filtrarPorCID (ArrayList<Compra> listaTotalDeCompras, int CID) {
//      ArrayList<Compra> listaFiltrada = new ArrayList<>();
//      for (Compra locCompra : listaTotalDeCompras) {
//         if (locCompra.getCID_Asociado() == CID) {
//            listaFiltrada.add(locCompra);
//         }
//      }
//      return listaFiltrada;
//   }
//
//   private ArrayList<Compra> obtenerListaTotalDeCompras (JsonArray comprasArrayJson) {
//      ArrayList<Compra> listaDeCompras = new ArrayList<>();
//      for (JsonObject compraObject : comprasArrayJson.getValuesAs(JsonObject.class)) {
//         Compra compra = new Compra();
//         compra.setCID_Asociado(compraObject.getInt("CID_Asociado"));
//         compra.setID_Compra(compraObject.getJsonNumber("ID_Compra").longValue());
//         compra.setCantidadCompra(compraObject.getInt("cantidadCompra"));
//         compra.setValorCompra(compraObject.getJsonNumber("valorCompra").doubleValue());
//         compra.setFechaCompra(compraObject.getString("fechaCompra"));
//         listaDeCompras.add(compra);
//      }
//      return listaDeCompras;
//   }
}