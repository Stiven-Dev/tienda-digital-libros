package co.edu.uptc.gui;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;
import co.edu.uptc.model.Tienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
      setLocationRelativeTo(null);
      //Esto para que no se cierre la ventana de golpe, sino que primero guarde y luego cierre
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter() {
         @Override public void windowClosing (WindowEvent e) {
            salirFormaSegura();
         }
      });
      this.pantallaPrincipal = new PantallaPrincipal(this, evento);
      setVisible(true);
      setResizable(true);
      setSize(1400, 600);
   }

   public DialogLoginSignup getDialogLoginSignUp () {
      return dialogLoginSignup;
   }

   void validarRegistro () {
      Usuario usuario           = getDatosSignUp();
      String  correoElectronico = usuario.getCorreoElectronico();
      if (obtenerUsuarioMedianteCorreo(correoElectronico) != null) {
         pantallaPrincipal.getPanelCrearCuentas().setMensajeDeError("Ya existe un usuario con ese correo");
         return;
      }
      tienda.crearCuenta(usuario);
      pantallaPrincipal.iniciarSesion(tienda.obtenerUsuarioMedianteCorreo(correoElectronico));
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
      final int              columnaISBN      = PanelCarrito.NOMBRE_COLUMNAS.ISBN.getIndex();
      final int              columnaAgregar   = PanelCarrito.NOMBRE_COLUMNAS.AGREGAR.getIndex();
      for (int fila = 0; fila < tablaLibros.getRowCount(); fila++) {
         boolean valorAgregar = (boolean) tablaLibros.getValueAt(fila, columnaAgregar);
         if (valorAgregar) {
            long ISBN = (long) tablaLibros.getValueAt(fila, columnaISBN);
            if (!carritoDeCompras.containsKey(ISBN)) {
               carritoDeCompras.put(ISBN, 1);
               pantallaPrincipal.getPanelCarrito().agregarArticulo(ISBN);
            } else {
               pantallaPrincipal.getPanelCarrito().aumentarCantidad(ISBN);
            }
            tablaLibros.setValueAt(false, fila, columnaAgregar);
         }
      }
   }

   void agregarLibroArchivo () {
      if (tienda.agregarLibroArchivo(pantallaPrincipal.getDatosLibroNuevo())) {
         JOptionPane.showMessageDialog(this, "Libro agregado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(this, "Libro ya existe", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      }
   }

   private void salirFormaSegura () {
      //pantallaPrincipal.getPanelCarrito().actualizarCarritoArchivo();
      dispose();
      System.exit(0);
   }

   void mostrarPanelLoginSignUp () {
      dialogLoginSignup = new DialogLoginSignup(this, evento);
      dialogLoginSignup.setVisible(true);
   }

   void buscarLibroActualizar () {
      long     ISBN  = pantallaPrincipal.getPanelActualizarLibro().getISBN();
      Object[] datos = tienda.buscarLibro(ISBN);
      if (datos != null) {
         pantallaPrincipal.getPanelActualizarLibro().setDatosLibro(datos);
      }
   }

   void actualizarLibro () {
      Object[] datos = pantallaPrincipal.getPanelActualizarLibro().getDatosLibro();
      if (tienda.actualizarLibro(datos)) {
         JOptionPane.showMessageDialog(this, "Libro actualizado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(this, "Libro no actualizado", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      }
   }

   double obtenerPrecioVentaTotal (DefaultTableModel model) {
      return tienda.calcularSubTotalVenta(model);
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
      if (tienda.eliminarLibro(ISBN)) {
         JOptionPane.showMessageDialog(this, "Libro eliminado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(this, "No es posible eliminar el libro", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      }
   }

   void buscarLibroEliminar () {
      long ISBN = pantallaPrincipal.getPanelEliminarLibro().getISBN();
      if (ISBN == -1) {
         pantallaPrincipal.getPanelEliminarLibro().setMensajeDeError("Debe rellenar el campo ISBN");
         return;
      }
      Object[] datosObtenidos = tienda.buscarLibro(ISBN);
      if (datosObtenidos != null) {
         pantallaPrincipal.getPanelEliminarLibro().setDatosLibro(datosObtenidos);
      }
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
      pantallaPrincipal.getPanelCrearCuentas().setMensajeDisponible("Usuario Disponible");
   }

   void pagarEfectivo () {
      if (LOGIN_CORRECTO) {
         JOptionPane.showMessageDialog(null, "Pago en efectivo", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(null, "Debe iniciar sesion para pagar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      }
      //TODO
   }

   void pagarTarjeta () {
      if (LOGIN_CORRECTO) {
         JOptionPane.showMessageDialog(null, "Pago con Tarjeta", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(null, "Debe iniciar sesion para pagar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
      }
      //TODO
   }

   Object[][] obtenerListaCompras (int ID) {
      return tienda.getDataVectorCompras(ID);
   }

   HashMap<Long, Libro> obtenerMapLibros () {
      return tienda.getLibrosLocales();
   }

   public void validarInicioSesion () {
      Usuario usuario = getDialogLoginSignUp().getDatosLogin();
      if (usuario.getCorreoElectronico().isBlank() || usuario.getClaveAcceso().length == 0) {
         JOptionPane.showMessageDialog(this, "Debe rellenar todos los campos", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      usuario = obtenerUsuarioMedianteCorreo(usuario.getCorreoElectronico());
      if (usuario == null) {
         return;
      }
      pantallaPrincipal.iniciarSesion(usuario);
   }

   private Usuario obtenerUsuarioMedianteCorreo (String correoElectronico) {
      return tienda.obtenerUsuarioMedianteCorreo(correoElectronico);
   }

   public void setLibrosLocales (HashMap<Long, Libro> libroHashMap) {
      pantallaPrincipal.getPanelCarrito().setLibrosLocales(libroHashMap);
   }
}