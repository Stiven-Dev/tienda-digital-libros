package co.edu.uptc.gui;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;
import co.edu.uptc.model.Tienda;
import co.edu.uptc.util.ConnectionToDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;

public class VentanaPrincipal extends JFrame {
   private final Evento            evento;
   private final Tienda            tienda;
   private       DialogLoginSignup dialogLoginSignup;
   private       PantallaPrincipal pantallaPrincipal;

   public VentanaPrincipal () {
      evento = new Evento(this);
      tienda = new Tienda();
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
      String  correoElectronico = usuarioARegistrar.getCorreoElectronico();
      if (obtenerUsuarioMedianteCorreo(correoElectronico) != null) {
         JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese correo", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      tienda.crearCuenta(usuarioARegistrar);
      pantallaPrincipal.iniciarSesion(usuarioARegistrar);
   }

   private Usuario getDatosSignUp () {
      return getDialogLoginSignUp().getDatosRegistro();
   }

   Object[][] obtenerVectorLibros () {
      return tienda.getDataVectorLibros();
   }

   void agregarLibroCarrito () {
      DefaultTableModel      tablaLibros      = pantallaPrincipal.getPanelLibros().getTableModel();
      HashMap<Long, Integer> carritoDeCompras = pantallaPrincipal.getPanelCarrito().getCarritoDeComprasTemporal();
      final int              columnaISBN      = PanelLibros.NOMBRE_COLUMNAS.ISBN.getIndex();
      final int              columnaAgregar   = PanelLibros.NOMBRE_COLUMNAS.AGREGAR.getIndex();
      for (int fila = 0; fila < tablaLibros.getRowCount(); fila++) {
         boolean agregar = (Boolean) tablaLibros.getValueAt(fila, columnaAgregar);
         if (agregar) {
            long ISBN = (long) tablaLibros.getValueAt(fila, columnaISBN);
            if (!carritoDeCompras.containsKey(ISBN)) {
               pantallaPrincipal.getPanelCarrito().agregarArticulo(ISBN);
            } else {
               pantallaPrincipal.getPanelCarrito().aumentarCantidad(ISBN);
            }
            tablaLibros.setValueAt(false, fila, columnaAgregar);
         }
      }
   }

   private void salirFormaSegura () {
      //TODO
      //pantallaPrincipal.getPanelCarrito().actualizarCarritoArchivo();
      dispose();
      try {
         ConnectionToDB.getInstance().closeConnection();
      } catch (SQLException e) {
         System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
      }

      System.exit(0);
   }

   void mostrarPanelLoginSignUp () {
      dialogLoginSignup = new DialogLoginSignup(this, evento);
      dialogLoginSignup.setVisible(true);
   }

   void buscarLibroParaPanelModificaciones (JPanel panel) {
      long                  ISBN;
      PanelLibroModificable panelLibroModificable;
      switch (panel.getName()) {
         case "PanelEliminarLibro" -> {
            ISBN                  = pantallaPrincipal.getPanelEliminarLibro().getISBN();
            panelLibroModificable = pantallaPrincipal.getPanelEliminarLibro();
         }
         case "PanelActualizarLibro" -> {
            ISBN                  = pantallaPrincipal.getPanelActualizarLibro().getISBN();
            panelLibroModificable = pantallaPrincipal.getPanelActualizarLibro();
         }
         default -> {
            return;
         }
      }
      Libro libro = tienda.buscarLibro(ISBN);
      if (libro != null) {
         panelLibroModificable.setDatosLibro(libro);
      }
   }

   void actualizarLibro () {
      Libro datos = pantallaPrincipal.getPanelActualizarLibro().getDatosLibro();
      if (tienda.actualizarLibro(datos)) {
         JOptionPane.showMessageDialog(this, "Libro actualizado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(this, "Libro no actualizado", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      }
   }

   double obtenerSubTotalVenta (DefaultTableModel model) {
      return tienda.calcularSubTotalVenta(model);
   }

   double obtenerTotalVenta (double subTotal) {
      return tienda.calcularTotalVenta(subTotal, pantallaPrincipal.getPanelPerfil().getDatosUsuario().getTipoUsuario());
   }

   double obtenerPrecioTotalProducto (double valorUnitario, int cantidad) {
      return tienda.calcularPrecioVenta(valorUnitario, cantidad);
   }

   double obtenerPrecioImpuesto (double valorUnitario) {
      return tienda.calcularValorImpuesto(valorUnitario);
   }

   void eliminarDialogLoginSignUp () {
      dialogLoginSignup.dispose();
   }

   void eliminarLibro () {
      long ISBN = pantallaPrincipal.getPanelEliminarLibro().getISBN();
      if (tienda.ventasAsociadas(ISBN)) {
         JOptionPane.showMessageDialog(this, "No es posible eliminar el libro", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      tienda.eliminarLibro(ISBN);
      JOptionPane.showMessageDialog(this, "Libro eliminado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
   }

   void crearCuenta () {
      Usuario datosUsuario = pantallaPrincipal.getPanelCrearCuentas().getDatosUsuario();
      if (tienda.crearCuenta(datosUsuario)) {
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

   void pagarEfectivo () {
      if (pantallaPrincipal.getPanelPerfil().getDatosUsuario() == null) {
         JOptionPane.showMessageDialog(null, "Debe iniciar sesion para pagar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      JOptionPane.showMessageDialog(null, "Pago en efectivo", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      //TODO
   }

   void pagarTarjeta () {
      if (pantallaPrincipal.getPanelPerfil().getDatosUsuario() == null) {
         JOptionPane.showMessageDialog(null, "Debe iniciar sesion para pagar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      JOptionPane.showMessageDialog(null, "Pago con Tarjeta", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      //TODO
   }

   Object[][] obtenerListaCompras () {
      long ID = pantallaPrincipal.getPanelPerfil().getDatosUsuario().getID();
      return tienda.getDataVectorCompras(ID);
   }

   void validarInicioSesion () {
      Usuario usuario = getDialogLoginSignUp().getDatosLogin();
      if (usuario.getCorreoElectronico().isBlank() || usuario.getClaveAcceso().length == 0) {
         JOptionPane.showMessageDialog(this, "Debe rellenar todos los campos", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      usuario = validarLogin(usuario);
      if (usuario == null) {
         return;
      }
      pantallaPrincipal.iniciarSesion(usuario);
   }

   private Usuario validarLogin (Usuario datosLogin) {
      return tienda.validarDatosLogin(datosLogin);
   }

   private Usuario obtenerUsuarioMedianteCorreo (String correoElectronico) {
      return tienda.obtenerUsuarioMedianteCorreo(correoElectronico);
   }

   double obtenerValorTotalImpuesto (DefaultTableModel model) {
      return tienda.calcularValorTotalImpuesto(model);
   }

   public int obtenerCantidadLibros (DefaultTableModel model) {
      return tienda.obtenerCantidadLibros(model);
   }

   public Libro obtenerLibroMedianteISBN (long ISBN) {
      return tienda.buscarLibro(ISBN);
   }

   public boolean usuarioRegistrado () {
      Usuario usuario = pantallaPrincipal.getPanelPerfil().getDatosUsuario();
      if (usuario == null) {
         return false;
      }
      return usuario.getTipoUsuario() != Usuario.ROLES.ADMIN;
   }

   public void agregarLibro () {
      pantallaPrincipal.mostrarDialogAgregarLibro();
   }

   public void actualizarDatosCliente () {
      //TODO
   }

   public void cerrarSesion () {
      //TODO
   }

   public void registrarLibro () {
      //TODO
   }
}