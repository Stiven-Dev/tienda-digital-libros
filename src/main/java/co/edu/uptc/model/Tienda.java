package co.edu.uptc.model;

import co.edu.uptc.dao.*;
import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.DetalleCompra;
import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tienda {
   private static final String                 rutaLog  = "data/operacion.log";
   private final        LibroDAO               libroDAO;
   private final        UsuarioDAO             usuarioDAO;
   private final        CarritoDAO             carritoDAO;
   private final        ComprasDAO             comprasDAO;
   private final        DetalleCompraDAO       detalleCompraDAO;
   private final        ArrayList<Libro>       librosLocales;
   private final        HashMap<Long, Integer> carritoActual;
   private final        ArrayList<Compra>      compraLocales;
   private final        Usuario                usuarioActual;
   private static       Tienda                 instance = null;

   private Tienda () {
      this.libroDAO         = new LibroDAO();
      this.usuarioDAO       = new UsuarioDAO();
      this.carritoDAO       = new CarritoDAO();
      this.comprasDAO       = new ComprasDAO();
      this.detalleCompraDAO = new DetalleCompraDAO();
      usuarioActual         = new Usuario();
      librosLocales         = new ArrayList<>();
      carritoActual         = new HashMap<>();
      compraLocales         = new ArrayList<>();
   }

   public static Tienda getInstance () {
      if (instance == null) {
         instance = new Tienda();
      }
      return instance;
   }

   public ArrayList<Libro> getLibrosLocales () {
      return librosLocales;
   }

   public Usuario getUsuarioActual () {
      return usuarioActual;
   }

   public ArrayList<Compra> getComprasLocales () {
      return compraLocales;
   }

   public HashMap<Long, Integer> getCarritoActual () {
      return carritoActual;
   }

   private void refrescarUsuarioActual () {
      Usuario usuario = obtenerUsuarioMedianteCorreo(usuarioActual.getCorreoElectronico());
      if (usuario == null) {
         return;
      }
      if (usuario != usuarioActual) {
         this.usuarioActual.setID(usuario.getID());
         this.usuarioActual.setNombreCompleto(usuario.getNombreCompleto());
         this.usuarioActual.setCorreoElectronico(usuario.getCorreoElectronico());
         this.usuarioActual.setDireccionEnvio(usuario.getDireccionEnvio());
         this.usuarioActual.setTelefonoContacto(usuario.getTelefonoContacto());
         this.usuarioActual.setTipoUsuario(usuario.getTipoUsuario());
         this.usuarioActual.setClaveAcceso(usuario.getClaveAcceso());
      }
   }

   private void refrescarComprasLocales () {
      compraLocales.clear();
      ArrayList<Compra> listaCompras = comprasDAO.obtenerComprasPorUsuarioID(usuarioActual.getID());
      compraLocales.addAll(listaCompras);
   }

   private void refrescarCarritoActual () {
      carritoActual.clear();
      carritoActual.putAll(carritoDAO.obtenerCarritoPorID(usuarioActual.getID()));
   }

   private void refrescarLibrosLocales () {
      librosLocales.clear();
      librosLocales.addAll(libroDAO.obtenerListaLibros());
   }

   public void setLibrosLocales (ArrayList<Libro> librosLocales) {
      this.librosLocales.clear();
      this.librosLocales.addAll(librosLocales);
   }

   public void setUsuarioActual (Usuario usuario) {
      this.usuarioActual.setID(usuario.getID());
      this.usuarioActual.setNombreCompleto(usuario.getNombreCompleto());
      this.usuarioActual.setCorreoElectronico(usuario.getCorreoElectronico());
      this.usuarioActual.setDireccionEnvio(usuario.getDireccionEnvio());
      this.usuarioActual.setTelefonoContacto(usuario.getTelefonoContacto());
      this.usuarioActual.setTipoUsuario(usuario.getTipoUsuario());
      this.usuarioActual.setClaveAcceso(usuario.getClaveAcceso());
   }

   public void setComprasLocales (ArrayList<Compra> compraLocales) {
      this.compraLocales.clear();
      this.compraLocales.addAll(compraLocales);
   }

   public void setCarritoActual (HashMap<Long, Integer> carritoActual) {
      this.carritoActual.clear();
      this.carritoActual.putAll(carritoActual);
   }

   public void refrescarDatosLocales () {
      refrescarLibrosLocales();
      if (usuarioActual.getID() < 1) {
         return;
      }
      if (usuarioActual.getTipoUsuario() != Usuario.ROLES.ADMIN) {
         refrescarCarritoActual();
         refrescarComprasLocales();
      }
      refrescarUsuarioActual();
   }

   //██╗░░░░░██╗██████╗░██████╗░░█████╗░░██████╗
   //██║░░░░░██║██╔══██╗██╔══██╗██╔══██╗██╔════╝
   //██║░░░░░██║██████╦╝██████╔╝██║░░██║╚█████╗░
   //██║░░░░░██║██╔══██╗██╔══██╗██║░░██║░╚═══██╗
   //███████╗██║██████╦╝██║░░██║╚█████╔╝██████╔╝
   //╚══════╝╚═╝╚═════╝░╚═╝░░╚═╝░╚════╝░╚═════╝░

   // Métodos relacionados con LIBRO DAO
   public Libro buscarLibro (long ISBN) {
      return libroDAO.buscarLibroISBN(ISBN);
   }

   public boolean eliminarLibro (long ISBN) {
      return libroDAO.eliminarLibro(ISBN);
   }

   public boolean actualizarLibro (Libro datosLibro) {
      return libroDAO.actualizarLibro(datosLibro);
   }

   public boolean agregarLibro (Libro datosLibro) {
      return libroDAO.agregarLibro(datosLibro);
   }

   public int unidadesDisponibles (long ISBN) {
      return libroDAO.obtenerUnidadesDisponibles(ISBN);
   }

   //██╗░░░██╗░██████╗██╗░░░██╗░█████╗░██████╗░██╗░█████╗░
   //██║░░░██║██╔════╝██║░░░██║██╔══██╗██╔══██╗██║██╔══██╗
   //██║░░░██║╚█████╗░██║░░░██║███████║██████╔╝██║██║░░██║
   //██║░░░██║░╚═══██╗██║░░░██║██╔══██║██╔══██╗██║██║░░██║
   //╚██████╔╝██████╔╝╚██████╔╝██║░░██║██║░░██║██║╚█████╔╝
   //░╚═════╝░╚═════╝░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░╚════╝░

   //Métodos relacionados con Usuario DAO
   public boolean registrarUsuario (Usuario usuarioARegistrar) {
      return usuarioDAO.validarRegistro(usuarioARegistrar);
   }

   public Usuario obtenerUsuarioMedianteCorreo (String correoElectronico) {
      return usuarioDAO.obtenerUsuarioMedianteCorreo(correoElectronico);
   }

   public float obtenerDescuentoTipoUsuario (Usuario.ROLES rol) {
      return usuarioDAO.obtenerDescuentoTipoUsuario(rol);
   }

   public Usuario actualizarDatosUsuario (Usuario nuevosDatosUsuario) {
      if (nuevosDatosUsuario == null || nuevosDatosUsuario.getID() < 1) {
         return null;
      }
      if (usuarioDAO.actualizarDatosUsuario(nuevosDatosUsuario)) {
         return nuevosDatosUsuario;
      }
      return null;
   }

   //░█████╗░░█████╗░██████╗░██████╗░██╗████████╗░█████╗░
   //██╔══██╗██╔══██╗██╔══██╗██╔══██╗██║╚══██╔══╝██╔══██╗
   //██║░░╚═╝███████║██████╔╝██████╔╝██║░░░██║░░░██║░░██║
   //██║░░██╗██╔══██║██╔══██╗██╔══██╗██║░░░██║░░░██║░░██║
   //╚█████╔╝██║░░██║██║░░██║██║░░██║██║░░░██║░░░╚█████╔╝
   //░╚════╝░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░░╚═╝░░░░╚════╝░

   //Métodos relacionados con Carrito DAO
   public double calcularValorTotalImpuesto () {
      double valorTotalImpuesto = 0;
      for (Map.Entry<Long, Integer> entry : carritoActual.entrySet()) {
         long   ISBN                = entry.getKey();
         int    cantidadLibro       = entry.getValue();
         double valorImpuestoUnidad = calcularValorImpuesto(buscarLibro(ISBN).getPrecioVenta());
         double valorImpuestoLibro  = valorImpuestoUnidad * cantidadLibro;
         valorTotalImpuesto += valorImpuestoLibro;
      }
      return valorTotalImpuesto;
   }

   public int obtenerCantidadLibros () {
      int cantidadTotalLibros = 0;
      for (Map.Entry<Long, Integer> entry : carritoActual.entrySet()) {
         cantidadTotalLibros += entry.getValue();
      }
      return cantidadTotalLibros;
   }

   public double calcularSubTotalVenta () {
      double subTotal = 0;
      for (Map.Entry<Long, Integer> entry : carritoActual.entrySet()) {
         double valorUnitario      = buscarLibro(entry.getKey()).getPrecioVenta();
         int    cantidad           = entry.getValue();
         double valorTotalPorLibro = calcularPrecioVentaPorLibro(valorUnitario, cantidad);
         subTotal += valorTotalPorLibro;
      }
      return subTotal;
   }

   public double calcularPrecioVentaPorLibro (double valorUnitario, int cantidad) {
      return valorUnitario * cantidad;
   }

   public double calcularValorImpuesto (double valorUnitario) {
      if (valorUnitario >= 50000) {
         return valorUnitario * 0.19;
      } else {
         return valorUnitario * 0.05;
      }
   }

   public double calcularPorcentajeImpuesto (double valorUnitarioConIVA) {
      //Si el valor unitario (con IVA incluido) es menor a 50.000, quiere decir que fue aplicado un impuesto del 5%, no es necesario calcularlo
      if (valorUnitarioConIVA < 50000) {
         return 0.05;
      }
      double  valorBase5  = valorUnitarioConIVA / 1.05;
      double  valorBase19 = valorUnitarioConIVA / 1.19;
      boolean puedeSer5   = (valorBase5 < 50000);
      boolean puedeSer19  = (valorBase19 >= 50000);

      if (puedeSer5 && !puedeSer19) {
         return 0.05;
      } else if (puedeSer19 && !puedeSer5) {
         return 0.19;
      } else {
         double total5        = valorBase5 * 1.05;
         double total19       = valorBase19 * 1.19;
         double differencia5  = Math.abs(total5 - valorUnitarioConIVA);
         double differencia19 = Math.abs(total19 - valorUnitarioConIVA);
         return (differencia5 <= differencia19) ? 0.05 : 0.19;
      }
   }

   //░█████╗░░█████╗░███╗░░░███╗██████╗░██████╗░░█████╗░
   //██╔══██╗██╔══██╗████╗░████║██╔══██╗██╔══██╗██╔══██╗
   //██║░░╚═╝██║░░██║██╔████╔██║██████╔╝██████╔╝███████║
   //██║░░██╗██║░░██║██║╚██╔╝██║██╔═══╝░██╔══██╗██╔══██║
   //╚█████╔╝╚█████╔╝██║░╚═╝░██║██║░░░░░██║░░██║██║░░██║
   //░╚════╝░░╚════╝░╚═╝░░░░░╚═╝╚═╝░░░░░╚═╝░░╚═╝╚═╝░░╚═╝

   //Métodos relacionados con Compra DAO
   public boolean comprasAsociadas (long ISBN) {
      return libroDAO.isVentasAsociadas(ISBN);
   }

   public ArrayList<DetalleCompra> obtenerDetallesCompraPorID (long IDcompra) {
      return detalleCompraDAO.obtenerDetallesCompraPorID(IDcompra);
   }

   //██████╗░░█████╗░░█████╗░██╗░██████╗
   //██╔══██╗██╔══██╗██╔══██╗╚█║██╔════╝
   //██║░░██║███████║██║░░██║░╚╝╚█████╗░
   //██║░░██║██╔══██║██║░░██║░░░░╚═══██╗
   //██████╔╝██║░░██║╚█████╔╝░░░██████╔╝
   //╚═════╝░╚═╝░░╚═╝░╚════╝░░░░╚═════╝░

   //Métodos relacionados con multiples relaciones con DAO's
   public double calcularTotalVenta () {
      Usuario.ROLES rol                 = usuarioActual.getTipoUsuario();
      double        porcentajeDescuento = obtenerDescuentoTipoUsuario(rol);
      double        totalVenta          = 0;
      for (Map.Entry<Long, Integer> entry : carritoActual.entrySet()) {
         long   ISBN                = entry.getKey();
         Libro  libro               = buscarLibro(ISBN);
         double valorUnitarioConIVA = libro.getPrecioVenta();
         double porcentajeImpuesto  = calcularPorcentajeImpuesto(valorUnitarioConIVA);
         double valorBase           = valorUnitarioConIVA / (1 + porcentajeImpuesto);
         double valorFinal          = obtenerValorConDescuentoConIva(valorBase, porcentajeDescuento);
         int    cantidad            = entry.getValue();
         totalVenta += valorFinal * cantidad;
      }
      return totalVenta;
   }

   public Usuario validarCredenciales (Usuario datosUsuario) {
      Usuario usuario = usuarioDAO.validarCredencialesLogin(datosUsuario);
      if (usuario != null) {
         //Se obtienen las Compras del usuario
         ArrayList<Compra> listaCompras = comprasDAO.obtenerComprasPorUsuarioID(usuario.getID());
         compraLocales.clear();
         if (listaCompras != null && !listaCompras.isEmpty()) {
            compraLocales.addAll(listaCompras);
         }

         //Se obtienen los libros del carrito del usuario
         HashMap<Long, Integer> carrito = carritoDAO.obtenerCarritoPorID(usuario.getID());
         carritoActual.clear();
         if (carrito != null && !carrito.isEmpty()) {
            carritoActual.putAll(carrito);
         }
         return usuario;
      }
      return null;
   }

   public double obtenerValorConDescuentoSinIva (double valorBase, double porcentajeDescuento) {
      return valorBase * (1 - porcentajeDescuento); //Valor con Descuento, pero sin IVA
   }

   public double obtenerValorConDescuentoConIva (double valorBase, double porcentajeDescuento) {
      double valorConDescuentoSinIva = valorBase * (1 - porcentajeDescuento);
      return valorConDescuentoSinIva + calcularValorImpuesto(valorConDescuentoSinIva); //Valor con Descuento y con IVA incluido
   }

   public void mostrarFactura (Compra compraSeleccionada) {
      FacturaDAO facturaDAO = new FacturaDAO(this, compraSeleccionada);
      facturaDAO.setVisible(true);
   }

   public static void agregarLog (String mensaje) {
      try (FileWriter writer = new FileWriter(rutaLog, true)) {
         // Se añade una nueva línea antes de la nueva línea
         LocalDateTime fechaHoraActual     = LocalDateTime.now();
         String        fechaHoraFormateada = String.format("-%d/%d/%d %d:%d > ", fechaHoraActual.getDayOfMonth(), fechaHoraActual.getMonthValue(), fechaHoraActual.getYear(), fechaHoraActual.getHour(), fechaHoraActual.getMinute());
         writer.write(System.lineSeparator() + fechaHoraFormateada + mensaje);
      } catch (IOException e) {
         System.err.println("Error al escribir en el log: " + e.getMessage());
      }
   }

   public void eliminarLibroDelCarrito (long ISBN, long ID) {
      carritoDAO.eliminarLibroDelCarrito(ISBN, ID);
   }

   public void efectuarCompraEfectivo (HashMap<Long, Integer> carrito, Usuario usuario) {
      Compra compra = new Compra();
      compra.setIDasociado(usuario.getID());
      ArrayList<DetalleCompra> listaArticulos = getListaArticulos(carrito);
      compra.setLibrosComprados(listaArticulos);
      compra.setValorCompra(calcularTotalVenta());
      //Se omite definir el metodo de pago, ya que es Efectivo por defecto
      double porcentajeDescuento = obtenerDescuentoTipoUsuario(usuario.getTipoUsuario());
      long   IDcompra            = comprasDAO.registrarCompra(compra, porcentajeDescuento);
      if (IDcompra < 0) {
         return;
      }
      detalleCompraDAO.registrarDetallesCompra(listaArticulos, compra.getIDasociado(), IDcompra);
   }

   private ArrayList<DetalleCompra> getListaArticulos (HashMap<Long, Integer> carrito) {
      ArrayList<DetalleCompra> listaArticulos = new ArrayList<>();
      for (Map.Entry<Long, Integer> entry : carrito.entrySet()) {
         long          ISBN                = entry.getKey();
         int           cantidadLibro       = entry.getValue();
         Libro         libro               = buscarLibro(ISBN);
         double        valorUnitario       = libro.getPrecioVenta();
         double        valorImpuestoUnidad = calcularValorImpuesto(valorUnitario);
         DetalleCompra detalleCompra       = new DetalleCompra();
         detalleCompra.setTitulo(libro.getTitulo());
         detalleCompra.setCantidad(cantidadLibro);
         detalleCompra.setValorUnitario(valorUnitario + valorImpuestoUnidad);
         detalleCompra.setISBNasociado(ISBN);
         listaArticulos.add(detalleCompra);
      }
      return listaArticulos;
   }
}