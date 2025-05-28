package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;

public class PantallaPrincipal extends JPanel {
   private final Evento                evento;
   private final VentanaPrincipal      ventanaPrincipal;
   private final JTabbedPane           panelPrincipal;
   private       PanelLibros           panelLibros;
   private       PanelCarrito          panelCarrito; //Solo para Usuarios
   private       PanelPerfil           panelPerfil;
   private       DialogAgregarLibro    dialogAgregarLibro; //Solo para los administradores
   private       PanelActualizarLibro  panelActualizarLibro; //Solo para los administradores
   private       PanelEliminarLibro    panelEliminarLibro; //Solo para los administradores
   private       PanelCrearCuentas     panelCrearCuentas; //Solo para los administradores
   private       PanelHistorialCompras panelHistorialCompras; //Se activa luego de Iniciar Sesión

   public PantallaPrincipal (VentanaPrincipal ventanaPrincipal, Evento evento) {
      this.evento           = evento;
      this.ventanaPrincipal = ventanaPrincipal;
      setLayout(new BorderLayout());
      panelPrincipal = new JTabbedPane();
      Font fuentePestania = new Font("Arial", Font.BOLD, 15);
      panelPrincipal.setFont(fuentePestania);
      ventanaPrincipal.add(this);
      agregarPanelesFijos();
   }

   private void agregarPanelesFijos () {
      inicializarPanelLibros();
      panelPrincipal.addTab("Lista de Libros", panelLibros);
      inicializarPanelCarrito();
      panelPrincipal.addTab("PanelCarrito", panelCarrito);
      inicializarPanelPerfil();
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
      dialogAgregarLibro   = new DialogAgregarLibro(evento);//TODO HACER PANEL UN JDIALOG MOSTRADO LUEGO DE ACCIONAR UN BOTON EN PANEL LIBROS
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

   private void inicializarPanelPerfil () {
      panelPerfil = new PanelPerfil(this, evento);
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
      panelPerfil.setDatosUsuario(datosUsuario);
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