package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.DetalleCompra;
import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;
import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class VentanaPrincipal extends JFrame {
   private final Evento                 evento;
   private final Tienda                 tienda;
   private final ArrayList<Libro>       librosLocales;
   private final ArrayList<Compra>      compraLocales;
   private final HashMap<Long, Integer> carritoActual;
   private final Usuario                usuarioActual;
   private       DialogLoginSignup      dialogLoginSignup;
   private       PantallaPrincipal      pantallaPrincipal;
   private       Compra.METODO_PAGO     metodoPago;

   public VentanaPrincipal () {
      Tienda.agregarLog("-----Iniciando la aplicación-----");
      evento        = new Evento(this);
      tienda        = Tienda.getInstance();
      usuarioActual = tienda.getUsuarioActual();
      librosLocales = tienda.getLibrosLocales();
      carritoActual = tienda.getCarritoActual();
      compraLocales = tienda.getComprasLocales();
      inicializarFrame();
   }

   private void inicializarFrame () {
      setTitle("Tienda Digital de Libros");
      //Esto para que no se cierre la ventana de golpe, sino que primero guarde y luego cierre`
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter() {
         @Override public void windowClosing (WindowEvent e) {
            salirFormaSegura();
         }
      });
      this.pantallaPrincipal = new PantallaPrincipal(this, evento);
      setSize(1000, 600);
      setLocationRelativeTo(null);
      setResizable(true);
      setVisible(true);
   }

   public DialogLoginSignup getDialogLoginSignUp () {
      return dialogLoginSignup;
   }

   void validarRegistro () {
      Usuario usuarioARegistrar = getDatosSignUp();
      if (tienda.registrarUsuario(usuarioARegistrar)) {
         JOptionPane.showMessageDialog(this, "Usuario registrado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
         refrescar();
         pantallaPrincipal.iniciarSesion();
      } else {
         JOptionPane.showMessageDialog(this, "Error al registrar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
      }
   }

   private Usuario getDatosSignUp () {
      return getDialogLoginSignUp().getDatosRegistro();
   }

   void agregarLibroCarrito () {
      long ISBN = pantallaPrincipal.getPanelLibros().getLibroSeleccionado().getISBN();
      pantallaPrincipal.getPanelCarrito().agregarArticulo(ISBN);
      final int cantidadLibros = pantallaPrincipal.getPanelCarrito().getCantidadLibros();
      pantallaPrincipal.getCartBooksButton().setText(String.valueOf(cantidadLibros));
   }

   private void salirFormaSegura () {
      //TODO
      //pantallaPrincipal.getPanelCarrito().actualizarCarritoArchivo();
      dispose();
      try {
         ConnectionToDB.getInstance().closeConnection();
         Tienda.agregarLog("Conexión a la base de datos cerrada correctamente");
      } catch (SQLException e) {
         Tienda.agregarLog("Error al cerrar la conexión a la base de datos");
      }
      Tienda.agregarLog("-----Saliendo de la aplicación-----");
      System.exit(0);
   }

   void mostrarPanelLoginSignUp () {
      dialogLoginSignup = new DialogLoginSignup(this, evento);
      dialogLoginSignup.setVisible(true);
   }

   void actualizarLibro () {
      System.out.println("Actualizando libro...");
      final DialogActualizarLibro dialogActualizarLibro = pantallaPrincipal.getPanelLibros().getDialogActualizarLibro();
      final String                mensajeError          = dialogActualizarLibro.obtenerMensajeDeError();
      if (!mensajeError.isBlank()) {
         return;
      }

      Libro libro = dialogActualizarLibro.getDatosLibro();
      if (tienda.actualizarLibro(libro)) {
         JOptionPane.showMessageDialog(this, "Libro actualizado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         refrescar();
      } else {
         JOptionPane.showMessageDialog(this, "No fue posible actualizar el Libro", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      }
      dialogActualizarLibro.dispose();
   }

   double obtenerSubTotalVenta () {
      return tienda.calcularSubTotalVenta();
   }

   double obtenerTotalVenta () {
      return tienda.calcularTotalVenta();
   }

   double obtenerPrecioTotalProducto (double valorUnitario, int cantidad) {
      return tienda.calcularPrecioVentaPorLibro(valorUnitario, cantidad);
   }

   double obtenerPrecioImpuesto (double valorUnitario) {
      return tienda.calcularValorImpuesto(valorUnitario);
   }

   void eliminarDialogLoginSignUp () {
      dialogLoginSignup.dispose();
   }

   void eliminarLibro () {
      DialogEliminarLibro dialogEliminarLibro = pantallaPrincipal.getPanelLibros().getDialogEliminarLibro();
      long                ISBN                = dialogEliminarLibro.getLibro().getISBN();
      if (tienda.comprasAsociadas(ISBN)) {
         Tienda.agregarLog("Intento de eliminar libro con ventas asociadas: " + ISBN);
         JOptionPane.showMessageDialog(this, "No es posible eliminar el libro, tiene ventas asociadas", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         dialogEliminarLibro.dispose();
         return;
      }
      if (tienda.eliminarLibro(ISBN)) {
         JOptionPane.showMessageDialog(this, "Libro eliminado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         refrescar();
      } else {
         JOptionPane.showMessageDialog(this, "Ocurrió un error al eliminar el Libro", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      }
      dialogEliminarLibro.dispose();
   }

   void crearCuenta () {
      Usuario datosUsuario = pantallaPrincipal.getPanelCrearCuentas().getDatosUsuario();
      if (tienda.registrarUsuario(datosUsuario)) {
         JOptionPane.showMessageDialog(this, "Cuenta creada correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(this, "Error al crear la cuenta", "Error", JOptionPane.ERROR_MESSAGE);
      }
   }

   void validarCorreoDisponible () {
      String correo = pantallaPrincipal.getPanelCrearCuentas().getCorreo();
      if (correo.isBlank()) {
         pantallaPrincipal.getPanelCrearCuentas().setMensajeDeError("Debe rellenar el campo Correo Electronico");
         return;
      }
      if (tienda.obtenerUsuarioMedianteCorreo(correo) != null) {
         pantallaPrincipal.getPanelCrearCuentas().setMensajeDeError("Ya hay un usuario con ese correo");
         return;
      }
      pantallaPrincipal.getPanelCrearCuentas().setMensajeDisponible();
   }

   void pagar () {
      if (!pantallaPrincipal.getSesionIniciada()) {
         JOptionPane.showMessageDialog(null, "Debe iniciar sesion para pagar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      DialogConfirmarCompra dialogConfirmarCompra = new DialogConfirmarCompra(this, evento, metodoPago, carritoActual);
      dialogConfirmarCompra.setVisible(true);
      if (dialogConfirmarCompra.getRespuesta()) {
         tienda.efectuarCompra(carritoActual, usuarioActual, metodoPago);
         pantallaPrincipal.getPanelCarrito().vaciarCarrito();
      }
   }

   ArrayList<Compra> obtenerListaCompras () {
      return tienda.getComprasLocales();
   }

   void validarInicioSesion () {
      Usuario datosUsuario = getDialogLoginSignUp().getDatosLogin();
      if (datosUsuario.getCorreoElectronico().isBlank() || datosUsuario.getClaveAcceso().length == 0) {
         JOptionPane.showMessageDialog(this, "Debe rellenar todos los campos", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      Usuario usuario = validarLogin(datosUsuario);
      if (usuario == null) {
         JOptionPane.showMessageDialog(this, "Correo o clave incorrectos", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      tienda.setUsuarioActual(usuario);
      pantallaPrincipal.iniciarSesion();
   }

   private Usuario validarLogin (Usuario datosLogin) {
      return tienda.validarCredenciales(datosLogin);
   }

   double obtenerValorTotalImpuesto () {
      return tienda.calcularValorTotalImpuesto();
   }

   int obtenerCantidadLibros () {
      return tienda.obtenerCantidadLibros();
   }

   Libro obtenerLibroMedianteISBN (long ISBN) {
      return tienda.buscarLibro(ISBN);
   }

   boolean usuarioRegistrado () {
      Usuario usuario = pantallaPrincipal.getDatosUsuario();
      if (usuario == null) {
         return false;
      }
      return usuario.getTipoUsuario() != Usuario.ROLES.ADMIN;
   }

   void dialogAgregarLibro () {
      pantallaPrincipal.mostrarDialogAgregarLibro();
   }

   void actualizarDatosCliente () {
      pantallaPrincipal.getDialogPerfil().setMensajeDeError();
      if (!pantallaPrincipal.getDialogPerfil().obtenerMensajeDeError().isBlank()) {
         return;
      }
      String clave = JOptionPane.showInputDialog(this, "Ingrese su contraseña Actual", "Confirmación de contraseña", javax.swing.JOptionPane.QUESTION_MESSAGE);
      if (clave == null || clave.isBlank()) {
         JOptionPane.showMessageDialog(this, "Contraseña Incorrecta", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      Usuario usuarioAValidar = new Usuario();
      usuarioAValidar.setCorreoElectronico(usuarioActual.getCorreoElectronico());
      usuarioAValidar.setClaveAcceso(clave.toCharArray());
      if (tienda.validarCredenciales(usuarioAValidar) == null) {
         JOptionPane.showMessageDialog(this, "Contraseña Incorrecta", "Contraseña Incorrecta", JOptionPane.ERROR_MESSAGE);
         return;
      }
      Usuario nuevosDatosUsuario = pantallaPrincipal.getDialogPerfil().getNuevosDatosUsuario();
      if (tienda.actualizarDatosUsuario(nuevosDatosUsuario) != null) {
         tienda.setUsuarioActual(nuevosDatosUsuario);
         pantallaPrincipal.getDialogPerfil().dispose();
         JOptionPane.showMessageDialog(this, "Datos actualizados correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
         refrescar();
      }
   }

   void cerrarSesion () {
      dispose();
      new VentanaPrincipal();
      revalidate();
      repaint();
   }

   void registrarLibro () {
      DialogAgregarLibro dialogAgregarLibro = pantallaPrincipal.getPanelLibros().getDialogAgregarLibro();
      String             mensajeError       = dialogAgregarLibro.obtenerMensajeDeError();
      dialogAgregarLibro.setMensajeDeError();
      if (!mensajeError.isBlank()) {
         return;
      }
      Libro libro = dialogAgregarLibro.getDatosLibro();
      if (tienda.agregarLibro(libro)) {
         JOptionPane.showMessageDialog(this, "Libro registrado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         refrescar();
      } else {
         JOptionPane.showMessageDialog(this, "Error al registrar el libro", "Alerta", JOptionPane.ERROR_MESSAGE);
      }
      dialogAgregarLibro.dispose();
   }

   void refrescar () {
      tienda.refrescarDatosLocales();
   }

   ArrayList<Libro> getLibrosLocales () {
      return librosLocales;
   }

   Usuario getUsuarioActual () {
      return usuarioActual;
   }

   ArrayList<Compra> getComprasLocales () {
      return compraLocales;
   }

   HashMap<Long, Integer> getCarritoActual () {
      return carritoActual;
   }

   public void mostrarDetallesCompra () {
      tienda.mostrarFactura(pantallaPrincipal.getCompraSeleccionada());
   }

   public void setCompraSeleccionada (Compra compraSeleccionada) {
      pantallaPrincipal.setCompraSeleccionada(compraSeleccionada);
   }

   public ArrayList<DetalleCompra> obtenerListaDetallesCompraPorID (long IDcompra) {
      return tienda.obtenerDetallesCompraPorID(IDcompra);
   }

   public int unidadesDisponibles (long ISBN) {
      return tienda.unidadesDisponibles(ISBN);
   }

   public void eliminarLibroDelCarrito (long ISBN, long ID) {
      tienda.eliminarLibroDelCarrito(ISBN, ID);
   }

   public void setMetodoPago (Compra.METODO_PAGO metodoPago) {
      this.metodoPago = metodoPago;
   }
}