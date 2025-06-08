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

/**
 * Ventana principal de la aplicación de la tienda digital de libros.
 * Gestiona la inicialización de la interfaz gráfica y la interacción con la lógica de negocio.
 */
public class VentanaPrincipal extends JFrame {
   /**
    * Manejador de eventos de la aplicación.
    */
   private final Evento                 evento;
   /**
    * Instancia de la tienda (lógica de negocio).
    */
   private final Tienda                 tienda;
   /**
    * Lista local de libros disponibles.
    */
   private final ArrayList<Libro>       librosLocales;
   /**
    * Lista local de compras realizadas.
    */
   private final ArrayList<Compra>      compraLocales;
   /**
    * Carrito de compras actual del usuario.
    */
   private final HashMap<Long, Integer> carritoActual;
   /**
    * Usuario actualmente autenticado.
    */
   private final Usuario                usuarioActual;
   /**
    * Diálogo de login y registro.
    */
   private       DialogLoginSignup      dialogLoginSignup;
   /**
    * Panel principal de la aplicación.
    */
   private       PantallaPrincipal      pantallaPrincipal;
   /**
    * Metodo de pago seleccionado.
    */
   private       Compra.METODO_PAGO     metodoPago;
   /**
    * Respuesta de confirmación para ciertas acciones.
    */
   private       boolean                respuesta = false;

   /**
    * Constructor de la ventana principal. Inicializa la tienda y la interfaz gráfica.
    */
   public VentanaPrincipal () {
      Tienda.agregarLog("-----Iniciando la aplicación-----");
      Tienda.resetInstance();
      evento        = new Evento(this);
      tienda        = Tienda.getInstance();
      usuarioActual = tienda.getUsuarioActual();
      librosLocales = tienda.getLibrosLocales();
      carritoActual = tienda.getCarritoActual();
      compraLocales = tienda.getComprasLocales();
      inicializarFrame();
   }

   /**
    * Inicializa el frame principal y configura el cierre seguro de la aplicación.
    */
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
      setSize(1000, 800);
      setLocationRelativeTo(null);
      setResizable(true);
      setVisible(true);
   }

   /**
    * Obtiene el diálogo de login y registro.
    *
    * @return Diálogo de login y registro.
    */
   public DialogLoginSignup getDialogLoginSignUp () {
      return dialogLoginSignup;
   }

   /**
    * Valida y registra un nuevo usuario en el sistema.
    */
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

   /**
    * Obtiene los datos de registro del usuario desde el diálogo.
    *
    * @return Usuario con los datos de registro.
    */
   private Usuario getDatosSignUp () {
      return getDialogLoginSignUp().getDatosRegistro();
   }

   /**
    * Agrega un libro seleccionado al carrito de compras.
    */
   void agregarLibroCarrito () {
      long ISBN = pantallaPrincipal.getPanelLibros().getLibroSeleccionado().getISBN();
      pantallaPrincipal.getPanelCarrito().agregarArticulo(ISBN);
      final int cantidadLibros = pantallaPrincipal.getPanelCarrito().getCantidadLibros();
      pantallaPrincipal.getCartBooksButton().setText(String.valueOf(cantidadLibros));
   }

   /**
    * Cierra la aplicación de forma segura, guardando datos y cerrando la conexión a la base de datos.
    */
   private void salirFormaSegura () {
      tienda.actualizarCarrito();
      dispose();
      try {
         ConnectionToDB.getInstance().closeConnection();
      } catch (SQLException e) {
         Tienda.agregarLog("Error al cerrar la conexión a la base de datos");
      }
      Tienda.agregarLog("-----Saliendo de la aplicación-----");
      System.exit(0);
   }

   /**
    * Muestra el diálogo de login y registro.
    */
   void mostrarPanelLoginSignUp () {
      dialogLoginSignup = new DialogLoginSignup(this, evento);
      dialogLoginSignup.setVisible(true);
   }

   /**
    * Actualiza los datos de un libro en el sistema.
    */
   void actualizarLibro () {
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

   /**
    * Obtiene el subtotal de la venta actual.
    *
    * @return Subtotal de la venta.
    */
   double obtenerSubTotalVenta () {
      return tienda.calcularSubTotalVenta();
   }

   /**
    * Obtiene el total de la venta actual, incluyendo impuestos y descuentos.
    *
    * @return Total de la venta.
    */
   double obtenerTotalVenta () {
      return tienda.calcularTotalVenta();
   }

   /**
    * Calcula el precio total de un producto, incluyendo impuestos y descuentos.
    *
    * @param valorUnitario Precio unitario del producto.
    * @param cantidad      Cantidad de productos.
    *
    * @return Precio total del producto.
    */
   double obtenerPrecioTotalProducto (double valorUnitario, int cantidad) {
      return tienda.calcularPrecioVentaPorLibro(valorUnitario, cantidad);
   }

   /**
    * Calcula el valor del impuesto para un precio dado.
    *
    * @param valorBase Precio del producto sin IVA.
    *
    * @return Valor del impuesto.
    */
   double obtenerPrecioImpuesto (double valorBase) {
      return tienda.calcularValorImpuesto(valorBase);
   }

   /**
    * Calcula el porcentaje de impuesto para un precio dado.
    *
    * @param valorUnitario Precio del producto.
    *
    * @return Porcentaje de impuesto.
    */
   double obtenerPorcentajeImpuesto (double valorUnitario) {
      return tienda.calcularPorcentajeImpuesto(valorUnitario);
   }

   /**
    * Elimina el diálogo de login y registro.
    */
   void eliminarDialogLoginSignUp () {
      dialogLoginSignup.dispose();
   }

   /**
    * Elimina un libro del sistema.
    */
   void eliminarLibro () {
      DialogEliminarLibro dialogEliminarLibro = pantallaPrincipal.getPanelLibros().getDialogEliminarLibro();
      long                ISBN                = dialogEliminarLibro.getLibroAEliminar().getISBN();
      if (tienda.comprasAsociadas(ISBN)) {
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

   /**
    * Crea una cuenta de usuario con los datos proporcionados.
    */
   void crearCuenta () {
      DialogAdministrarCuentas dialogAdministrarCuentas = pantallaPrincipal.getDialogAdministrarCuentas();
      String                   mensajeDeError           = dialogAdministrarCuentas.obtenerMensajeDeError();
      if (!mensajeDeError.isBlank()) {
         dialogAdministrarCuentas.setMensajeDeError(mensajeDeError);
         return;
      }
      Usuario datosUsuario = dialogAdministrarCuentas.getDatosUsuario();
      if (tienda.registrarUsuario(datosUsuario)) {
         JOptionPane.showMessageDialog(this, "Cuenta creada correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(this, "Error al crear la cuenta", "Error", JOptionPane.ERROR_MESSAGE);
      }
   }

   /**
    * Valida si el correo electrónico proporcionado está disponible para registro.
    */
   void validarCorreoDisponible () {
      DialogAdministrarCuentas dialogAdministrarCuentas = pantallaPrincipal.getDialogAdministrarCuentas();
      String                   correo                   = dialogAdministrarCuentas.getCorreo();
      if (correo.isBlank()) {
         String mensajeDeError = "Correo incompleto o inválido";
         dialogAdministrarCuentas.setMensajeDeError(mensajeDeError);
         dialogAdministrarCuentas.limpiarCampos();
         return;
      }
      Usuario usuario = tienda.obtenerUsuarioMedianteCorreo(correo);
      if (usuario != null) {
         dialogAdministrarCuentas.setMensajeDeError("Ya hay un usuario con ese correo");
         dialogAdministrarCuentas.setDatosUsuario(usuario);
         return;
      }
      dialogAdministrarCuentas.limpiarCampos();
      dialogAdministrarCuentas.setMensajeDisponible();
   }

   /**
    * Procesa el pago de la compra actual.
    */
   void pagar () {
      if (!pantallaPrincipal.getSesionIniciada()) {
         JOptionPane.showMessageDialog(null, "Debe iniciar sesion para pagar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      DialogConfirmarCompra dialogConfirmarCompra = new DialogConfirmarCompra(this, carritoActual, metodoPago, tienda.obtenerDescuentoTipoUsuario(usuarioActual.getTipoUsuario()));
      dialogConfirmarCompra.setVisible(true);
      if (respuesta) {
         if (tienda.efectuarCompra(metodoPago)) {
            pantallaPrincipal.getPanelCarrito().vaciarCarrito();
            JOptionPane.showMessageDialog(this, "Compra realizada correctamente, puedes ver el detalle en tu historial de Compras", "Información", JOptionPane.INFORMATION_MESSAGE);
         }
         respuesta = false;
      }
   }

   /**
    * Calcula el valor con descuento sin incluir IVA.
    *
    * @param valorBase           Valor base del producto.
    * @param porcentajeDescuento Porcentaje de descuento a aplicar.
    *
    * @return Valor con descuento sin IVA.
    */
   double obtenerValorConDescuentoSinIva (double valorBase, double porcentajeDescuento) {
      return tienda.obtenerValorConDescuentoSinIva(valorBase, porcentajeDescuento);
   }

   /**
    * Obtiene la lista de compras realizadas.
    *
    * @return Lista de compras.
    */
   ArrayList<Compra> obtenerListaCompras () {
      return tienda.getComprasLocales();
   }

   /**
    * Valida y procesa el inicio de sesión de un usuario.
    */
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

   /**
    * Valida las credenciales de inicio de sesión proporcionadas.
    *
    * @param datosLogin Datos de inicio de sesión.
    *
    * @return Usuario validado o null si las credenciales son incorrectas.
    */
   private Usuario validarLogin (Usuario datosLogin) {
      return tienda.validarCredenciales(datosLogin);
   }

   /**
    * Obtiene el valor total de impuestos para la venta actual.
    *
    * @return Valor total de impuestos.
    */
   double obtenerValorTotalImpuesto () {
      return tienda.calcularValorTotalImpuesto();
   }

   /**
    * Obtiene la cantidad total de libros en el carrito.
    *
    * @return Cantidad de libros.
    */
   int obtenerCantidadLibrosEnCarrito () {
      return tienda.obtenerCantidadLibrosEnCarrito();
   }

   /**
    * Busca y devuelve un libro mediante su ISBN.
    *
    * @param ISBN ISBN del libro a buscar.
    *
    * @return Libro encontrado o null si no existe.
    */
   Libro obtenerLibroMedianteISBN (long ISBN) {
      return tienda.buscarLibro(ISBN);
   }

   /**
    * Verifica si el usuario actual está registrado en el sistema.
    *
    * @return Verdadero si el usuario está registrado, falso en caso contrario.
    */
   boolean usuarioRegistrado () {
      Usuario usuario = pantallaPrincipal.getDatosUsuario();
      if (usuario == null) {
         return false;
      }
      return usuario.getTipoUsuario() != Usuario.ROLES.ADMIN;
   }

   /**
    * Muestra el diálogo para agregar un nuevo libro.
    */
   void agregarLibro () {
      pantallaPrincipal.mostrarDialogAgregarLibro();
   }

   /**
    * Actualiza los datos del cliente (usuario) en el sistema.
    */
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

   /**
    * Cierra la sesión actual y reinicia la ventana de la aplicación.
    */
   void cerrarSesion () {
      dispose();
      tienda.actualizarCarrito();
      new VentanaPrincipal();
      revalidate();
      repaint();
   }

   /**
    * Registra un nuevo libro en el sistema.
    */
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

   /**
    * Refresca los datos locales de la tienda.
    */
   void refrescar () {
      tienda.refrescarDatosLocales();
   }

   /**
    * Obtiene la lista de libros disponibles en la tienda.
    *
    * @return Lista de libros.
    */
   ArrayList<Libro> getLibrosLocales () {
      return librosLocales;
   }

   /**
    * Obtiene el usuario actualmente autenticado.
    *
    * @return Usuario actual.
    */
   Usuario getUsuarioActual () {
      return usuarioActual;
   }

   /**
    * Obtiene la lista de compras realizadas por el usuario.
    *
    * @return Lista de compras.
    */
   ArrayList<Compra> getComprasLocales () {
      return compraLocales;
   }

   /**
    * Obtiene el carrito de compras actual del usuario.
    *
    * @return Carrito de compras.
    */
   HashMap<Long, Integer> getCarritoActual () {
      return carritoActual;
   }

   /**
    * Muestra los detalles de la compra seleccionada.
    */
   public void mostrarDetallesCompra () {
      tienda.mostrarFactura(pantallaPrincipal.getCompraSeleccionada());
   }

   /**
    * Establece la compra seleccionada para ver detalles.
    *
    * @param compraSeleccionada Compra a establecer como seleccionada.
    */
   public void setCompraSeleccionada (Compra compraSeleccionada) {
      pantallaPrincipal.setCompraSeleccionada(compraSeleccionada);
   }

   /**
    * Obtiene la lista de detalles de una compra mediante su ID.
    *
    * @param IDcompra ID de la compra.
    *
    * @return Lista de detalles de la compra.
    */
   public ArrayList<DetalleCompra> obtenerListaDetallesCompraPorID (long IDcompra) {
      return tienda.obtenerDetallesCompraPorID(IDcompra);
   }

   /**
    * Obtiene la cantidad de unidades disponibles de un libro mediante su ISBN.
    *
    * @param ISBN ISBN del libro.
    *
    * @return Cantidad de unidades disponibles.
    */
   public int unidadesDisponibles (long ISBN) {
      return tienda.unidadesDisponibles(ISBN);
   }

   /**
    * Elimina un libro del carrito de compras.
    *
    * @param ISBN ISBN del libro a eliminar.
    * @param ID   ID del detalle de compra asociado.
    */
   public void eliminarLibroDelCarrito (long ISBN, long ID) {
      tienda.eliminarLibroDelCarrito(ISBN, ID);
   }

   /**
    * Establece el metodo de pago para la compra actual.
    *
    * @param metodoPago Metodo de pago a establecer.
    */
   public void setMetodoPago (Compra.METODO_PAGO metodoPago) {
      this.metodoPago = metodoPago;
   }

   /**
    * Establece la respuesta de confirmación para acciones como pago.
    *
    * @param respuesta Respuesta de confirmación.
    */
   public void setRespuesta (boolean respuesta) {
      this.respuesta = respuesta;
   }

   /**
    * Actualiza los datos del cliente desde el panel de administración.
    * Valida los datos y actualiza el usuario en la tienda.
    */
   public void actualizarDatosClienteAdmin () {
      DialogAdministrarCuentas dialogAdministrarCuentas = pantallaPrincipal.getDialogAdministrarCuentas();
      String                   mensajeError             = dialogAdministrarCuentas.obtenerMensajeDeError();
      if (!mensajeError.isBlank()) {
         dialogAdministrarCuentas.setMensajeDeError(mensajeError);
         return;
      }
      Usuario usuarioAValidar = dialogAdministrarCuentas.getDatosUsuario();
      if (tienda.actualizarDatosUsuario(usuarioAValidar) != null) {
         JOptionPane.showMessageDialog(this, "Datos actualizados correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
         dialogAdministrarCuentas.dispose();
      }
   }
}