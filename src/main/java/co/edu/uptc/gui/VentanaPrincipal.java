package co.edu.uptc.gui;

import co.edu.uptc.modelo.ROLES;
import co.edu.uptc.modelo.Tienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class VentanaPrincipal extends JFrame{
	public static boolean           LOGIN_CORRECTO;
	private final Evento            evento;
	private final Tienda            tienda;
	private       DialogLoginSignup dialogLoginSignup;
	private       PantallaPrincipal pantallaPrincipal;

	public VentanaPrincipal (){
		evento         = new Evento(this);
		tienda         = new Tienda();
		LOGIN_CORRECTO = false;
		inicializarFrame();
	}

	public static void guardarCarritoDeCompras (int CID, HashMap<Long, Integer> carritoDeCompras){
		//Tienda.guardarCarritoDeCompras(CID, carritoDeCompras);
	}

	static ROLES obtenerTipoUsuario (String correoElectronico){
		return Tienda.obtenerTipoUsuario(correoElectronico);
	}

	public Object[] obtenerDatosLibroCarrito (long ISBN){
		Object[] datosObtenidos = tienda.buscarLibro(ISBN);
		String   titulo         = (String) datosObtenidos[1];
		String   autores        = (String) datosObtenidos[2];
		double   precioUnitario = (double) datosObtenidos[7];
		int      cantidad       = 1;

		return new Object[]{ISBN, titulo, autores, precioUnitario, cantidad};
	}

	private void inicializarFrame (){
		setTitle("Tienda Digital de Libros");
		setLocationRelativeTo(null);

		//Esto para que no se cierre la ventana de golpe, sino que primero guarde y luego cierre
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override public void windowClosing (WindowEvent e){
				salirFormaSegura();
			}
		});
		this.pantallaPrincipal = new PantallaPrincipal(this, evento);
		setVisible(true);
		setResizable(true);
		setSize(1400, 600);
	}

	void validarInicioSesion (){
		String[] datosUsuario      = getDatosLogin();
		String   correoElectronico = datosUsuario[0];
		String   claveAcceso       = datosUsuario[1];
		if (! tienda.validarUsuarioLogin(correoElectronico, claveAcceso)){
			JOptionPane.showMessageDialog(this, "Usuario y/o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		LOGIN_CORRECTO = true;
		pantallaPrincipal.iniciarSesion(tienda.obtenerUsuarioSeguro(correoElectronico));
	}

	private String[] getDatosLogin (){
		return getDialogLoginSignUp().getDatosLogin();
	}

	public DialogLoginSignup getDialogLoginSignUp (){
		return dialogLoginSignup;
	}

	void validarRegistro (){
		Object[] datosUsuario      = getDatosSignUp();
		String   correoElectronico = (String) datosUsuario[1];
		if (! tienda.usuarioExiste(correoElectronico)){
			String                 nombreCompleto = (String) datosUsuario[0];
			String                 direccion      = (String) datosUsuario[2];
			long                   telefono       = (long) datosUsuario[3];
			char[]                 claveAcceso    = (char[]) datosUsuario[4];
			int                    CID            = Tienda.obtenerNuevoCID();
			HashMap<Long, Integer> carrito        = pantallaPrincipal.getPanelCarrito().getCarritoDeComprasTemporal();

			Object[] datos = new Object[]{nombreCompleto, correoElectronico, direccion, telefono, claveAcceso, CID, carrito};
			tienda.registrarUsuario(datos);
			LOGIN_CORRECTO = true;
			pantallaPrincipal.iniciarSesion(tienda.obtenerUsuarioSeguro(correoElectronico));
		}
	}

	private Object[] getDatosSignUp (){
		return getDialogLoginSignUp().getDatosRegistro();
	}

	Object[][] obtenerListaLibros (){
		return tienda.getDataVectorLibros();
	}

	void agregarLibroCarrito (){
		DefaultTableModel      tablaLibros      = pantallaPrincipal.getPanelLibros().getTableModel();
		HashMap<Long, Integer> carritoDeCompras = pantallaPrincipal.getPanelCarrito().getCarritoDeComprasTemporal();
		final int              columnaAgregar   = 10;
		for (int fila = 0; fila < tablaLibros.getRowCount(); fila++){
			Boolean valorAgregar = (Boolean) tablaLibros.getValueAt(fila, columnaAgregar);
			if (valorAgregar){
				Long ISBN = Long.parseLong(tablaLibros.getValueAt(fila, 0).toString());
				if (! carritoDeCompras.containsKey(ISBN)){
					carritoDeCompras.put(ISBN, 1);
					pantallaPrincipal.getPanelCarrito().agregarArticulo(ISBN);
				}else{
					pantallaPrincipal.getPanelCarrito().incrementarCantidad(ISBN);
				}
				tablaLibros.setValueAt(false, fila, columnaAgregar);
			}
		}
	}

	void agregarLibroArchivo (){
		if (tienda.agregarLibroArchivo(pantallaPrincipal.getDatosLibroNuevo())){
			JOptionPane.showMessageDialog(this, "Libro agregado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(this, "Libro ya existe", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void salirFormaSegura (){
		//pantallaPrincipal.getPanelCarrito().actualizarCarritoArchivo();
		dispose();
		System.exit(0);
	}

	void mostrarPanelLoginSignUp (){
		dialogLoginSignup = new DialogLoginSignup(this, evento);
		dialogLoginSignup.setVisible(true);
	}

	void buscarLibroActualizar (){
		long     ISBN  = pantallaPrincipal.getPanelActualizarLibro().getISBN();
		Object[] datos = tienda.buscarLibro(ISBN);
		if (datos != null){
			pantallaPrincipal.getPanelActualizarLibro().setDatosLibro(datos);
		}
	}

	void actualizarLibro (){
		Object[] datos = pantallaPrincipal.getPanelActualizarLibro().getDatosLibro();
		if (tienda.actualizarLibro(datos)){
			JOptionPane.showMessageDialog(this, "Libro actualizado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(this, "Libro no actualizado", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	double obtenerPrecioVentaTotal (DefaultTableModel model){
		return tienda.calcularSubTotalVenta(model);
	}

	double obtenerPrecioTotalProducto (double valorUnitario, int cantidad){
		return tienda.calcularPrecioVenta(valorUnitario, cantidad);
	}

	double obtenerPrecioImpuesto (double valorUnitario){
		return tienda.calcularValorImpuesto(valorUnitario);
	}

	void eliminarPanelLoginSignUp (){
		dialogLoginSignup.dispose();
	}

	void eliminarLibro (){
		long ISBN = pantallaPrincipal.getPanelEliminarLibro().getISBN();
		if (tienda.eliminarLibro(ISBN)){
			JOptionPane.showMessageDialog(this, "Libro eliminado correctamente", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(this, "No es posible eliminar el libro", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	void buscarLibroEliminar (){
		long ISBN = pantallaPrincipal.getPanelEliminarLibro().getISBN();
		if (ISBN == - 1){
			pantallaPrincipal.getPanelEliminarLibro().setMensajeDeError("Debe rellenar el campo ISBN");
			return;
		}
		Object[] datosObtenidos = tienda.buscarLibro(ISBN);
		if (datosObtenidos != null){
			pantallaPrincipal.getPanelEliminarLibro().setDatosLibro(datosObtenidos);
		}
	}

	void crearCuenta (){
		Object[] datosUsuario = pantallaPrincipal.getPanelCrearCuentas().getDatosUsuario();
		if (tienda.crearCuenta(datosUsuario)){
			JOptionPane.showMessageDialog(this, "Cuenta creada correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(this, "Error al crear la cuenta", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void usuarioExiste (){
		String correo = pantallaPrincipal.getPanelCrearCuentas().getCorreo();
		if (correo.isBlank()){
			pantallaPrincipal.getPanelCrearCuentas().setMensajeDeError("Debe rellenar el campo Correo Electronico");
			return;
		}
		if (tienda.usuarioExiste(correo)){
			pantallaPrincipal.getPanelCrearCuentas().setMensajeDeError("Ya hay un usuario con ese correo");
			return;
		}
		pantallaPrincipal.getPanelCrearCuentas().setMensajeDeError("Usuario Disponible");
	}

	void pagarEfectivo (){
		if (LOGIN_CORRECTO){
			JOptionPane.showMessageDialog(null, "Pago en efectivo", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(null, "Debe iniciar sesion para pagar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}
		//TODO
	}

	void pagarTarjeta (){
		if (LOGIN_CORRECTO){
			JOptionPane.showMessageDialog(null, "Pago con Tarjeta", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(null, "Debe iniciar sesion para pagar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
		}
		//TODO
	}

	Object[][] obtenerListaCompras (int CID){
		return tienda.getDataVectorCompras(CID);
	}
}