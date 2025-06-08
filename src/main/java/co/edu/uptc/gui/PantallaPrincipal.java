package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Panel principal de la aplicación de la tienda digital de libros.
 * Gestiona la visualización de los diferentes paneles y diálogos según el rol del usuario.
 */
public class PantallaPrincipal extends JPanel {
   /**
    * Referencia al manejador de eventos de la aplicación.
    */
   private final Evento                 evento;
   /**
    * Referencia a la ventana principal.
    */
   private final VentanaPrincipal       ventanaPrincipal;
   /**
    * Panel de cabecera con el título de la tienda.
    */
   private final JPanel                 panelHeader     = new JPanel(new GridBagLayout());
   /**
    * Restricciones de layout para el panel principal.
    */
   private final GridBagConstraints     gbcMain         = new GridBagConstraints();
   /**
    * Etiqueta con el título de la tienda.
    */
   private final JLabel                 labelHeader     = new JLabel("Tienda Digital de Libros - UPTC", JLabel.CENTER);
   /**
    * Color de fondo personalizado.
    */
   private final Color                  backGroundColor = new Color(122, 252, 162, 90);
   /**
    * Panel central que contiene los diferentes paneles de la aplicación.
    */
   private final JPanel                 panelCentral    = new JPanel(new CardLayout());
   /**
    * Usuario actualmente autenticado.
    */
   private final Usuario                usuarioActual;
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
    * Panel de libros.
    */
   private       PanelLibros            panelLibros;
   /**
    * Panel del carrito de compras (solo para usuarios).
    */
   private       PanelCarrito  panelCarrito; //Solo para Usuarios
   /**
    * Botón de perfil de usuario.
    */
   private final ProfileButton profileButton;
   /**
    * Diálogo de perfil de usuario (solo para usuarios).
    */
   private       DialogPerfil  dialogPerfil; //Solo para Usuarios
   /**
    * Indica si el carrito está visible.
    */
   private       boolean         isCartVisible   = false;
   /**
    * Botón para mostrar el carrito de compras.
    */
   private final CartBooksButton cartBooksButton;
   /**
    * Compra actualmente seleccionada.
    */
   private       Compra          compraSeleccionada;
   /**
    * Indica si la sesión está iniciada.
    */
   private       boolean                sesionIniciada  = false;

   /**
    * Constructor del panel principal.
    *
    * @param ventanaPrincipal referencia a la ventana principal
    * @param evento           manejador de eventos
    */
   public PantallaPrincipal (VentanaPrincipal ventanaPrincipal, Evento evento) {
      this.evento           = evento;
      this.ventanaPrincipal = ventanaPrincipal;
      this.usuarioActual    = ventanaPrincipal.getUsuarioActual();
      this.librosLocales    = ventanaPrincipal.getLibrosLocales();
      this.compraLocales    = ventanaPrincipal.getComprasLocales();
      this.carritoActual    = ventanaPrincipal.getCarritoActual();
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
      gbcHeader.weightx = 0.5f;
      panelHeader.setBackground(backGroundColor);
      labelHeader.setHorizontalAlignment(JLabel.CENTER);
      labelHeader.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));
      labelHeader.setForeground(Color.WHITE);
      panelHeader.add(labelHeader, gbcHeader);

      gbcHeader.gridx++;
      gbcHeader.weightx = 0.1f;
      JButton botonRefrescar = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/refresh.png"))));
      botonRefrescar.setBackground(new Color(125, 178, 217, 255));
      botonRefrescar.setBorderPainted(false);
      botonRefrescar.addActionListener(_ -> {
         ventanaPrincipal.refrescar();
         panelLibros.refrescarLista(ventanaPrincipal);
         panelCarrito.refrescarLista(usuarioActual);
         refrescarInterfaz();
      });
      panelHeader.add(botonRefrescar, gbcHeader);

      //Boton de Carrito
      gbcHeader.gridx++;
      gbcHeader.weightx = 0.2f;
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

   /**
    * Inicializa el panel central con los paneles de libros y carrito.
    */
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

   /**
    * Alterna la visualización entre el panel de carrito y el panel de libros.
    */
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

   /**
    * Configura las funcionalidades específicas para el rol de administrador.
    */
   private void funcionesAdministrador () {
      CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
      cardLayout.show(panelCentral, "LIBROS");
      cardLayout.removeLayoutComponent(panelCarrito);
      panelHeader.remove(cartBooksButton);
      panelLibros.reasignarFuncionalidadAdmin();
      revalidate();
      repaint();
   }

   /**
    * Activa las funcionalidades del perfil de usuario.
    */
   private void activarFuncionesPerfil () {
      profileButton.iniciarSesion(usuarioActual);
   }

   /**
    * Retorna el panel de libros.
    *
    * @return panel de libros
    */
   PanelLibros getPanelLibros () {
      return panelLibros;
   }

   /**
    * Inicia la sesión del usuario y actualiza la interfaz.
    */
   void iniciarSesion () {
      ventanaPrincipal.eliminarDialogLoginSignUp();
      ventanaPrincipal.refrescar();
      refrescarInterfaz();
      agregarFuncionalidadSegunRol();
      sesionIniciada = true;
   }

   /**
    * Agrega funcionalidades según el rol del usuario.
    */
   private void agregarFuncionalidadSegunRol () {
      if (Usuario.ROLES.ADMIN.equals(usuarioActual.getTipoUsuario())) {
         funcionesAdministrador();
      }
      activarFuncionesPerfil();
      panelCarrito.refrescarLista(usuarioActual);
   }

   /**
    * Retorna el panel de carrito de compras.
    *
    * @return panel de carrito
    */
   PanelCarrito getPanelCarrito () {
      return panelCarrito;
   }

   /**
    * Retorna el diálogo de perfil de usuario.
    *
    * @return diálogo de perfil
    */
   DialogPerfil getDialogPerfil () {
      return profileButton.getDialogPerfil();
   }

   /**
    * Retorna el diálogo para administrar cuentas de usuario.
    *
    * @return diálogo de administración de cuentas
    */
   DialogAdministrarCuentas getDialogAdministrarCuentas () {
      return profileButton.getDialogAdministrarCuentas();
   }

   /**
    * Muestra el diálogo para agregar un libro.
    */
   public void mostrarDialogAgregarLibro () {
      DialogAgregarLibro dialogAgregarLibro = new DialogAgregarLibro(evento);
      dialogAgregarLibro.setVisible(true);
   }

   /**
    * Retorna los datos del usuario actual.
    *
    * @return usuario actual
    */
   public Usuario getDatosUsuario () {
      return usuarioActual;
   }

   /**
    * Retorna el botón del carrito de compras.
    *
    * @return botón del carrito
    */
   public CartBooksButton getCartBooksButton () {
      return cartBooksButton;
   }

   /**
    * Retorna la lista local de libros.
    *
    * @return lista de libros
    */
   public ArrayList<Libro> getLibrosLocales () {
      return ventanaPrincipal.getLibrosLocales();
   }

   /**
    * Retorna la compra seleccionada.
    *
    * @return compra seleccionada
    */
   public Compra getCompraSeleccionada () {
      return compraSeleccionada;
   }

   /**
    * Establece la compra seleccionada.
    *
    * @param compraSeleccionada compra a establecer
    */
   public void setCompraSeleccionada (Compra compraSeleccionada) {
      this.compraSeleccionada = compraSeleccionada;
   }

   /**
    * Refresca la interfaz gráfica y actualiza la cantidad de libros en el carrito.
    */
   public void refrescarInterfaz () {
      if (usuarioActual.getTipoUsuario() == Usuario.ROLES.ADMIN) {
         return;
      }
      int cantidadLibros = ventanaPrincipal.obtenerCantidadLibrosEnCarrito();
      CartBooksButton.setCount(cantidadLibros);
      if (!isCartVisible) {
         cartBooksButton.setText(String.valueOf(cantidadLibros));
      }

      panelCarrito.actualizarDatosCompra();
      revalidate();
      repaint();
   }

   /**
    * Indica si la sesión está iniciada.
    *
    * @return true si la sesión está iniciada, false en caso contrario
    */
   public boolean getSesionIniciada () {
      return sesionIniciada;
   }
}