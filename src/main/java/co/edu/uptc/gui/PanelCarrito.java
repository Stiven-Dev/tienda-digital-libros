package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;

public class PanelCarrito extends JPanel{
	private final  Evento                 evento;
	private static int                    identificadorCarrito;
	private final  VentanaPrincipal       ventanaPrincipal;
	private final  String[]               nombreColumnas     = {"ISBN",
	                                                            "Titulo",
	                                                            "Autor",
	                                                            "Precio C/u",
	                                                            "Vlr Impuesto",
	                                                            "Cantidad",
	                                                            "Precio Total",
	                                                            "+",
	                                                            "-",
	                                                            "X"
	};
	private final  HashMap<Long, Integer> carritoDeCompras;
	private final  Font                   fuenteCabecera     = new Font("Arial", Font.BOLD, 15);
	private final  Font                   fuenteCelda        = new Font("Lucida Sans Unicode", Font.PLAIN, 15);
	private final  Font                   fuenteBoton        = new Font("Lucida Sans Unicode", Font.BOLD, 20);
	private final  JButton                botonPagarEfectivo = new JButton("Pagar en Efectivo");
	private final  JButton                botonPagarTarjeta  = new JButton("Pagar con Tarjeta");
	public         DefaultTableModel      model;
	private        JLabel                 labelTotal;

	public PanelCarrito (VentanaPrincipal ventanaPrincipal, Evento evento){
		this.evento           = evento;
		this.ventanaPrincipal = ventanaPrincipal;
		inicializarPanelCarrito();
		carritoDeCompras = new HashMap<>();
		inicializarPanelFooter();
	}

	public DefaultTableModel getDefaultTableModel (){
		// La última columna es de tipo Boolean para mostrar un JCheckBox
		return new DefaultTableModel(nombreColumnas, 0){
			@Override public boolean isCellEditable (int row, int column){
				return column >= 7 && column < 10;
			}

			@Override public Class<?> getColumnClass (int columna){
				return switch (columna){
					case 0 -> long.class;
					case 3, 4, 6 -> double.class;
					case 5 -> int.class;
					case 7, 8, 9 -> Boolean.class;
					default -> String.class;
				};
			}
		};
	}

	private DefaultTableCellRenderer celdasDoubleFormateadas (){
		return new DefaultTableCellRenderer(){
			@Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				if (column == 3 || column == 4 || column == 6){
					value = String.format("$%,.2f", (double) value);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};
	}

	public HashMap<Long, Integer> getCarritoDeComprasTemporal (){
		return carritoDeCompras;
	}

	private void inicializarPanelCarrito (){
		setLayout(new BorderLayout());
		model = getDefaultTableModel();
		modificacionesCarrito(model);
		JTable tableCarrito = new JTable(model);
		tableCarrito.getTableHeader().setFont(fuenteCabecera);
		tableCarrito.setFont(fuenteCelda);
		formatearColumnas(tableCarrito);
		JScrollPane scrollPane = new JScrollPane(tableCarrito);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void formatearColumnas (JTable tableCarrito){
		tableCarrito.getColumnModel().getColumn(3).setCellRenderer(celdasDoubleFormateadas());
		tableCarrito.getColumnModel().getColumn(4).setCellRenderer(celdasDoubleFormateadas());
		tableCarrito.getColumnModel().getColumn(6).setCellRenderer(celdasDoubleFormateadas());
	}

	private void inicializarPanelFooter (){
		//Footer (incluye el label de precio total)
		botonPagarEfectivo.setActionCommand(Evento.EVENTO.PAGAR_EFECTIVO.name());
		botonPagarTarjeta.setActionCommand(Evento.EVENTO.PAGAR_TARJETA.name());
		botonPagarTarjeta.addActionListener(evento);
		botonPagarEfectivo.addActionListener(evento);

		labelTotal = new JLabel("Total: $0.00");
		labelTotal.setHorizontalAlignment(JLabel.CENTER);
		labelTotal.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 10, 5);
		gbc.fill   = GridBagConstraints.HORIZONTAL;
		float[] pesoComponentes = {0.5f, 0.15f, 0.15f};
		JPanel  footer          = new JPanel(new GridBagLayout());

		gbc.gridx   = 0;
		gbc.weightx = pesoComponentes[0];
		footer.add(labelTotal, gbc);

		gbc.gridx   = 1;
		gbc.weightx = pesoComponentes[1];
		footer.add(botonPagarEfectivo, gbc);

		gbc.gridx   = 2;
		gbc.weightx = pesoComponentes[2];
		footer.add(botonPagarTarjeta, gbc);

		//Asignacion de fuente a cada boton
		botonPagarEfectivo.setFont(fuenteBoton);
		botonPagarTarjeta.setFont(fuenteBoton);

		add(footer, BorderLayout.SOUTH);
	}

	private void modificacionesCarrito (DefaultTableModel model){
		model.addTableModelListener(event -> {
			// Obtenemos la fila y columna que cambiaron
			int fila    = event.getFirstRow();
			int columna = event.getColumn();
			//Validamos el aumento o disminución de cantidad. También si se descarta un producto
			switch (columna){
				case 7 -> sumarAlCarrito(model, fila);
				case 8 -> quitarAlCarrito(model, fila);
				case 9 -> descartarDelCarrito(model, fila);
				default -> actualizarPrecioTotal();
			}
			if (VentanaPrincipal.LOGIN_CORRECTO){
				actualizarCarritoArchivo();
			}
		});
	}

	private void sumarAlCarrito (DefaultTableModel model, int fila){
		final int columnaAgregar = 7;
		if (! ((boolean) model.getValueAt(fila, columnaAgregar))){
			return;
		}
		final int columnaCantidad = 5;
		try{
			int cantidad = (int) (model.getValueAt(fila, columnaCantidad)); //Posible error en la conversion de tipo de dato, en ese caso usar parseInt()
			model.setValueAt(cantidad + 1, fila, columnaCantidad);
		}catch (NullPointerException e){
			System.err.println(e.getMessage());
		}finally{
			model.setValueAt(false, fila, columnaAgregar);
		}
		actualizarCarrito(model);
	}

	private void quitarAlCarrito (DefaultTableModel model, int fila){
		final int columnaQuitar = 8;
		if (! (boolean) model.getValueAt(fila, columnaQuitar)){
			return;
		}
		final int columnaCantidad = 5;
		try{
			int cantidad = Integer.parseInt(model.getValueAt(fila, columnaCantidad).toString());
			if (cantidad > 1){
				model.setValueAt(cantidad - 1, fila, columnaCantidad);
			}else{
				model.removeRow(fila);
			}
		}catch (NullPointerException e){
			System.err.println(e.getMessage());
		}finally{
			model.setValueAt(false, fila, columnaQuitar);
		}
		actualizarCarrito(model);
	}

	private void actualizarCarrito (DefaultTableModel model){
		final int columnaISBN          = 0;
		final int columnaValorUnitario = 3;
		final int columnaCantidad      = 5;
		final int columnaPrecioVenta   = 6;
		for (int fila = 0; fila < model.getRowCount(); fila++){
			long ISBN     = (long) model.getValueAt(fila, columnaISBN);
			int  cantidad = (int) model.getValueAt(fila, columnaCantidad);
			if (cantidad < 1){
				carritoDeCompras.remove(ISBN);
			}
			carritoDeCompras.put(ISBN, cantidad);
			double valorUnitario = (double) model.getValueAt(fila, columnaValorUnitario);
			double precioVenta   = obtenerPrecioVenta(valorUnitario, cantidad);
			model.setValueAt(precioVenta, fila, columnaPrecioVenta);
		}
	}

	private void descartarDelCarrito (DefaultTableModel model, int fila){
		final int columnaISBN = 0;
		carritoDeCompras.remove((long) model.getValueAt(fila, columnaISBN));
		model.removeRow(fila);
	}

	public void actualizarCarritoArchivo (){
		HashMap<Long, Integer> carritoDeCompras = getCarritoDeComprasTemporal();
		VentanaPrincipal.guardarCarritoDeCompras(identificadorCarrito, carritoDeCompras);
	}

	void agregarArticulo (long ISBN){
		model.addRow(formatearArticulo(ISBN));
		carritoDeCompras.put(ISBN, 1);
	}

	private Object[] formatearArticulo (long ISBN){
		Object[] datosImportados = ventanaPrincipal.obtenerDatosLibroCarrito(ISBN);
		String   titulo          = (String) datosImportados[1];
		String   autores         = (String) datosImportados[2];
		double   precioUnitario  = (double) datosImportados[3];
		double   precioImpuesto  = obtenerPrecioImpuesto(precioUnitario);
		int      cantidad        = obtenerCantidadLibro(ISBN);
		double   precioTotal     = obtenerPrecioVenta(precioUnitario, cantidad);
		boolean  agregar         = false;
		boolean  quitar          = false;
		boolean  descartar       = false;
		return new Object[]{ISBN, titulo, autores, precioUnitario, precioImpuesto, cantidad, precioTotal, agregar, quitar, descartar};
	}

	private double obtenerPrecioVenta (double valorUnitario, int cantidad){
		return ventanaPrincipal.obtenerPrecioTotalProducto(valorUnitario, cantidad);
	}

	private double obtenerPrecioImpuesto (double valorUnitario){
		return ventanaPrincipal.obtenerPrecioImpuesto(valorUnitario);
	}

	private void actualizarPrecioTotal (){
		double precioTotal = ventanaPrincipal.obtenerPrecioVentaTotal(model);
		labelTotal.setText(String.format("$%,.2f", precioTotal));
	}

	void incrementarCantidad (Long ISBN){
		final int columnaISBN           = 0;
		final int columnaPrecioUnitario = 3;
		final int columnaCantidad       = 5;
		final int columnaPrecioVenta    = 6;
		for (int fila = 0; fila < model.getRowCount(); fila++){
			if (model.getValueAt(fila, columnaISBN).equals(ISBN)){
				int cantidad = ((int) model.getValueAt(fila, columnaCantidad)) + 1;
				carritoDeCompras.put(ISBN, cantidad);
				model.setValueAt(cantidad, fila, columnaCantidad);
				double precioUnitario = (double) model.getValueAt(fila, columnaPrecioUnitario);
				model.setValueAt(obtenerPrecioVenta(precioUnitario, cantidad), fila, columnaPrecioVenta);
				break;
			}
		}
		actualizarPrecioTotal();
	}

	private int obtenerCantidadLibro (long ISBN){
		if (carritoDeCompras.get(ISBN) == null){
			return 1;
		}
		return carritoDeCompras.get(ISBN);
	}

	public static void setIdentificadorCarrito (int paramIdentificadorCarrito){
		identificadorCarrito = paramIdentificadorCarrito;
	}
}