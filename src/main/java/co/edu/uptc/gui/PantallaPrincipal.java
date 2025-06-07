package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PantallaPrincipal extends JPanel {
   private final Evento                 evento;
   private final VentanaPrincipal       ventanaPrincipal;
   private final JPanel                 panelHeader     = new JPanel(new GridBagLayout());
   private final GridBagConstraints     gbcMain         = new GridBagConstraints();
   private final JLabel                 labelHeader     = new JLabel("Tienda Digital de Libros");
   private final Color                  backGroundColor = new Color(97, 158, 210);
   private       PanelLibros            panelLibros;
   private       PanelCarrito           panelCarrito; //Solo para Usuarios
   private       ProfileButton          profileButton;
   private       PanelCrearCuentas      panelCrearCuentas; //Solo para los administradores
   private       DialogHistorialCompras dialogHistorialCompras; //Solo para Usuarios
   private       DialogPerfil           dialogPerfil; //Solo para Usuarios
   private final JPanel                 panelCentral    = new JPanel(new CardLayout());
   private       boolean                isCartVisible   = false;
   private       CartBooksButton        cartBooksButton;
   private final Usuario                usuarioActual;
   private final ArrayList<Libro>       librosLocales;
   private final ArrayList<Compra>      compraLocales;
   private final HashMap<Long, Integer> carritoActual;
   private       Compra                 compraSeleccionada;
   private       boolean                sesionIniciada  = false;

   public PantallaPrincipal (VentanaPrincipal ventanaPrincipal, Evento evento) {
      this.evento           = evento;
      this.ventanaPrincipal = ventanaPrincipal;
      this.usuarioActual    = ventanaPrincipal.getUsuarioActual();
      this.librosLocales = ventanaPrincipal.getLibrosLocales();
      this.compraLocales = ventanaPrincipal.getComprasLocales();
      this.carritoActual = ventanaPrincipal.getCarritoActual();
      this.setBackground(backGroundColor);
      setLayout(new GridBagLayout());
      gbcMain.fill    = GridBagConstraints.BOTH;
      gbcMain.insets  = new Insets(0, 5, 5, 5);
      gbcMain.gridx   = 0;
      gbcMain.gridy   = 0;
      gbcMain.weightx = 1;
      gbcMain.weighty = 0.1f;

      //Valores constantes de la cabecera
      final GridBagConstraints gbcHeader = new GridBagConstraints();
      gbcHeader.insets = new Insets(0, 5, 0, 5);
      gbcHeader.fill   = GridBagConstraints.BOTH;
      gbcHeader.gridx  = 0;

      //Configuración de la cabecera
      gbcHeader.weightx = 0.7f;
      panelHeader.setBackground(backGroundColor);
      labelHeader.setHorizontalAlignment(JLabel.CENTER);
      labelHeader.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 25));
      labelHeader.setForeground(Color.WHITE);
      panelHeader.add(labelHeader, gbcHeader);

      //Boton de Carrito
      gbcHeader.gridx++;
      gbcHeader.weightx = 0.15f;
      cartBooksButton   = new CartBooksButton();
      cartBooksButton.addActionListener(_ -> toggleCartLibros());
      panelHeader.add(cartBooksButton, gbcHeader);

      //Boton de Perfil
      gbcHeader.gridx++;
      profileButton = new ProfileButton(ventanaPrincipal, evento);

      profileButton.addActionListener(_ -> {
         if (sesionIniciada) {
            return;
         }
         if (usuarioActual.getID() > 0) {
            profileButton.removeActionListener(evento);
            iniciarSesion();
            sesionIniciada = true;
         }
      });
      panelHeader.add(profileButton, gbcHeader);

      ventanaPrincipal.add(this);
      gbcHeader.gridy   = 0;
      gbcHeader.weighty = 0.1f;
      add(panelHeader, gbcMain);

      inicializarPanelCentral();
   }

   private void inicializarPanelCentral () {
      panelLibros = new PanelLibros(this, evento);
      panelLibros.refrescarLista(ventanaPrincipal);
      panelCentral.add(panelLibros, "LIBROS");
      panelCarrito = new PanelCarrito(ventanaPrincipal, evento);
      panelCentral.add(panelCarrito, "CARRITO");

      gbcMain.gridy++;
      gbcMain.weighty = 0.9f;
      add(panelCentral, gbcMain);
      ((CardLayout) panelCentral.getLayout()).show(panelCentral, "LIBROS");
   }

   private void toggleCartLibros () {
      CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
      if (!isCartVisible) {
         cardLayout.show(panelCentral, "CARRITO");
         cartBooksButton.setText("Home");
         isCartVisible = true;
      } else {
         cardLayout.show(panelCentral, "LIBROS");
         cartBooksButton.setText(String.valueOf(getPanelCarrito().getCantidadLibros()));
         isCartVisible = false;
      }
      revalidate();
      repaint();
   }

   private void funcionesAdministrador () {
      CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
      cardLayout.show(panelCentral, "LIBROS");
      panelHeader.remove(cartBooksButton);
      cardLayout.removeLayoutComponent(panelCarrito);
      panelLibros.reasignarFuncionalidadAdmin();
      panelCrearCuentas = new PanelCrearCuentas(evento); //TODO Crear panel de crear cuentas
      revalidate();
      repaint();
   }

   private void activarFuncionesPerfil () {
      profileButton.iniciarSesion(usuarioActual);
   }

   private void inicializarPanelHistorialCompras () {
      dialogHistorialCompras = new DialogHistorialCompras(ventanaPrincipal, evento);
//      CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
//      cardLayout.addLayoutComponent(panelHistorialCompras, "HISTORIAL_COMPRAS");
   }

   public DialogHistorialCompras getDialogHistorialCompras () {
      return dialogHistorialCompras;
   }

   PanelLibros getPanelLibros () {
      return panelLibros;
   }

   void iniciarSesion () {
      ventanaPrincipal.eliminarDialogLoginSignUp();
      ventanaPrincipal.refrescar();
      refrescarInterfaz();
      agregarFuncionalidadSegunRol();
      sesionIniciada = true;
   }

   private void agregarFuncionalidadSegunRol () {
      if (Usuario.ROLES.ADMIN.equals(usuarioActual.getTipoUsuario())) {
         funcionesAdministrador();
         //TODO Crear panel de crear cuentas
      } else {
         inicializarPanelHistorialCompras();
      }
      activarFuncionesPerfil();
      panelCarrito.refrescarLista(usuarioActual);
   }

   PanelCarrito getPanelCarrito () {
      return panelCarrito;
   }

   PanelCrearCuentas getPanelCrearCuentas () {
      return panelCrearCuentas;
   }

   DialogPerfil getDialogPerfil () {
      return profileButton.getDialogPerfil();
   }

   public void mostrarDialogAgregarLibro () {
      DialogAgregarLibro dialogAgregarLibro = new DialogAgregarLibro(evento);
      dialogAgregarLibro.setVisible(true);
   }

   public Usuario getDatosUsuario () {
      return usuarioActual;
   }

   public CartBooksButton getCartBooksButton () {
      return cartBooksButton;
   }

   public ArrayList<Libro> getLibrosLocales () {
      return ventanaPrincipal.getLibrosLocales();
   }

   public void setCompraSeleccionada (Compra compraSeleccionada) {
      this.compraSeleccionada = compraSeleccionada;
   }

   public Compra getCompraSeleccionada () {
      return compraSeleccionada;
   }

   public void refrescarInterfaz () {
      int cantidadLibros = ventanaPrincipal.obtenerCantidadLibros();
      CartBooksButton.setCount(cantidadLibros);
      cartBooksButton.setText(String.valueOf(cantidadLibros));
      revalidate();
      repaint();
   }

   public boolean getSesionIniciada () {
      return sesionIniciada;
   }
}