package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;

public class PantallaPrincipal extends JPanel {
   private final Evento                evento;
   private final VentanaPrincipal      ventanaPrincipal;
   private final JTabbedPane           panelPrincipal;
   private final JPanel                panelHeader = new JPanel(new GridBagLayout());
   private final GridBagConstraints    gbc         = new GridBagConstraints();
   private       PanelLibros           panelLibros;
   private       PanelCarrito          panelCarrito; //Solo para Usuarios
   private       PanelPerfil           panelPerfil;
   private       PanelActualizarLibro  panelActualizarLibro; //Solo para los administradores
   private       PanelEliminarLibro    panelEliminarLibro; //Solo para los administradores
   private       PanelCrearCuentas     panelCrearCuentas; //Solo para los administradores
   private       PanelHistorialCompras panelHistorialCompras; //Se activa luego de Iniciar Sesión
   private       Usuario               datosUsuario;

   public PantallaPrincipal (VentanaPrincipal ventanaPrincipal, Evento evento) {
      this.evento           = evento;
      this.ventanaPrincipal = ventanaPrincipal;
      setLayout(new BorderLayout());
      gbc.insets = new Insets(5, 5, 5, 5);
      gbc.fill   = GridBagConstraints.BOTH;

      //panelHeader;
      panelPrincipal = new JTabbedPane();
      Font fuentePestania = new Font("Arial", Font.BOLD, 15);
      panelPrincipal.setFont(fuentePestania);
      ventanaPrincipal.add(this);
      inicializarPanelesFijos();
   }

   private void inicializarPanelesFijos () {
      inicializarPanelLibros();
      panelPrincipal.addTab("Lista de Libros", panelLibros);
      inicializarPanelCarrito();
      panelPrincipal.addTab("PanelCarrito", panelCarrito);
      inicializarBotonPerfil();
      panelPrincipal.addTab("Perfil", panelPerfil);
      add(panelPrincipal, BorderLayout.CENTER);
   }

   public void agregarPanelesSegunRol (Usuario.ROLES rol) {
      if (rol.equals(Usuario.ROLES.ADMIN)) {
         inicalizarPanelesAdministrador();
         panelPrincipal.addTab("Crear Cuentas", panelCrearCuentas);
         return;
      }
      inicializarPanelHistorialCompras();
      panelPrincipal.addTab("Historial de Compras", panelHistorialCompras);
   }

   private void inicalizarPanelesAdministrador () {
      mostrarSoloPanelesAdmin();
      panelActualizarLibro = new PanelActualizarLibro(evento);//TODO ELIMINAR PANEL
      panelEliminarLibro   = new PanelEliminarLibro(evento);//TODO ELIMINAR PANEL
      panelCrearCuentas    = new PanelCrearCuentas(evento);
   }

   private void mostrarSoloPanelesAdmin () {
      panelLibros.reasignarFuncionalidadAdmin();
      panelPrincipal.remove(panelCarrito);
      panelCarrito = null;
      revalidate();
      repaint();
   }

   private void inicializarPanelLibros () {
      panelLibros = new PanelLibros(evento);
      panelLibros.refrescarLista(ventanaPrincipal);
   }

   private void inicializarPanelCarrito () {
      panelCarrito = new PanelCarrito(ventanaPrincipal, evento);
   }

   private void inicializarBotonPerfil () {
      panelPerfil = new PanelPerfil(this, evento);
//      ProfileButton profileButton = new ProfileButton(evento);
//      profileButton.setActionCommand(Evento.EVENTO.LOGIN_SIGNUP.name());
//      profileButton.addActionListener(evento);

//      add(profileButton, gbc);
   }

   private void inicializarPanelHistorialCompras () {
      panelHistorialCompras = new PanelHistorialCompras(ventanaPrincipal, evento);
   }

   PanelLibros getPanelLibros () {
      return panelLibros;
   }

   PanelPerfil getPanelPerfil () {
      return panelPerfil;
   }

   void iniciarSesion (Usuario datosUsuario) {
      this.datosUsuario = datosUsuario;
      //panelPerfil.setDatosUsuario(datosUsuario);
      ventanaPrincipal.eliminarDialogLoginSignUp();
      agregarPanelesSegunRol(datosUsuario.getTipoUsuario());
   }

   PanelActualizarLibro getPanelActualizarLibro () {
      return panelActualizarLibro;
   }

   PanelCarrito getPanelCarrito () {
      return panelCarrito;
   }

   PanelEliminarLibro getPanelEliminarLibro () {
      return panelEliminarLibro;
   }

   PanelCrearCuentas getPanelCrearCuentas () {
      return panelCrearCuentas;
   }

   public void mostrarDialogAgregarLibro () {
      DialogAgregarLibro dialogAgregarLibro = new DialogAgregarLibro(evento);
      dialogAgregarLibro.setLocationRelativeTo(this);
      dialogAgregarLibro.setVisible(true);
   }
}