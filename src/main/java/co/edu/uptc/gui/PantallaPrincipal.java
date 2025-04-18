package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

public class PantallaPrincipal extends JPanel{
	private final Evento                evento;
	private final VentanaPrincipal      ventanaPrincipal;
   private final JTabbedPane           panelPrincipal;
	private       PanelLibros           panelLibros;
	private       PanelCarrito          panelCarrito;
	private       PanelPerfil           panelPerfil;
	private       PanelAgregarLibro     panelAgregarLibro; //Solo para los administradores
	private       PanelActualizarLibro  panelActualizarLibro; //Solo para los administradores
	private       PanelEliminarLibro    panelEliminarLibro; //Solo para los administradores
	private       PanelCrearCuentas     panelCrearCuentas; //Solo para los administradores
	private       PanelHistorialCompras panelHistorialCompras;

	public PantallaPrincipal (VentanaPrincipal ventana, Evento evento){
		this.evento           = evento;
		this.ventanaPrincipal = ventana;
		setLayout(new BorderLayout());
		panelPrincipal = new JTabbedPane();
      Font fuentePestania = new Font("Arial", Font.BOLD, 15);
      panelPrincipal.setFont(fuentePestania);

		inicializarPanelLibros(ventana);
		panelPrincipal.addTab("Lista de Libros", panelLibros);
		inicializarPanelCarrito();
		panelPrincipal.addTab("PanelCarrito", panelCarrito);
		inicializarPanelPerfil();
		panelPrincipal.addTab("Perfil", panelPerfil);

		add(panelPrincipal, BorderLayout.CENTER);
		ventana.add(this);
	}

	public void agregarPanelesSegunRol (String rol){
		if (rol.equals("ADMIN")){
			inicalizarPanelesAdministrador();
			panelPrincipal.addTab("Agregar Libro", panelAgregarLibro);
			panelPrincipal.addTab("Modificar Libro", panelActualizarLibro);
			panelPrincipal.addTab("Eliminar Libro", panelEliminarLibro);
			panelPrincipal.addTab("Crear Cuentas", panelCrearCuentas);
		}
		inicializarPanelHistorialCompras();
		panelPrincipal.addTab("Historial de Compras", panelHistorialCompras);
	}

	private void inicalizarPanelesAdministrador (){
		panelAgregarLibro    = new PanelAgregarLibro(evento);
		panelActualizarLibro = new PanelActualizarLibro(evento);
		panelEliminarLibro   = new PanelEliminarLibro(evento);
		panelCrearCuentas    = new PanelCrearCuentas(evento);
	}

	private void inicializarPanelLibros (VentanaPrincipal ventana){
		panelLibros = new PanelLibros(ventana, evento);
	}

	private void inicializarPanelCarrito (){
		panelCarrito = new PanelCarrito(ventanaPrincipal, evento);
	}

	private void inicializarPanelPerfil (){
		panelPerfil = new PanelPerfil(this, evento);
	}

	private void inicializarPanelHistorialCompras (){
		panelHistorialCompras = new PanelHistorialCompras(ventanaPrincipal, panelPerfil.getCID());
	}

	PanelLibros getPanelLibros (){
		return panelLibros;
	}

	Object[] getDatosLibroNuevo (){
		return panelAgregarLibro.getDatosLibro();
	}

	void iniciarSesion (Object[] datosUsuario){
		panelPerfil.setDatosUsuario(datosUsuario);
		ventanaPrincipal.eliminarPanelLoginSignUp();
		activarFuncionalidadBotones();
	}

	private void activarFuncionalidadBotones (){
		panelPerfil.validarSesionIniciada();
		panelAgregarLibro.validarSesionIniciada();
		panelActualizarLibro.validarSesionIniciada();
		panelEliminarLibro.validarSesionIniciada();
		panelCrearCuentas.validarSesionIniciada();
	}

	PanelActualizarLibro getPanelActualizarLibro (){
		return panelActualizarLibro;
	}

	PanelCarrito getPanelCarrito (){
		return panelCarrito;
	}

	PanelEliminarLibro getPanelEliminarLibro (){
		return panelEliminarLibro;
	}

	PanelCrearCuentas getPanelCrearCuentas (){
		return panelCrearCuentas;
	}
}