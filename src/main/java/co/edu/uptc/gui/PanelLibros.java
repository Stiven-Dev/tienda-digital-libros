package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelLibros extends JPanel{
	private final Evento            evento;
	private final VentanaPrincipal  ventana;
	private final String[]          nombreColumnas = {"ISBN",
	                                                  "Titulo",
	                                                  "Autor",
	                                                  "Genero",
	                                                  "# Paginas",
	                                                  "Editorial",
	                                                  "Año",
	                                                  "Formato",
	                                                  "Precio",
	                                                  "Cantidad Disponible",
	                                                  "Agregar"
	};
	private final Font              fuenteCabecera = new Font("Arial", Font.BOLD, 15);
	private final Font              fuenteCelda    = new Font("Lucida Sans Unicode", Font.PLAIN, 12);
	private final Font              fuenteBoton    = new Font("Lucida Sans Unicode", Font.BOLD, 20);
	private       DefaultTableModel model;

	public PanelLibros (VentanaPrincipal ventana, Evento evento){
		this.ventana = ventana;
		this.evento  = evento;
		inicializarPanelLibros();
	}

	private DefaultTableModel getDefaultTableModel (){
		return new DefaultTableModel(nombreColumnas, 0){
			@Override public boolean isCellEditable (int row, int column){
				return column == 10;
			}

			@Override public Class<?> getColumnClass (int columnIndex){
				// La última columna es de tipo Boolean para mostrar un JCheckBox
				return (columnIndex == 10) ? Boolean.class : String.class;
			}
		};
	}

	private DefaultTableCellRenderer celdasDoubleFormateadas (){
		return new DefaultTableCellRenderer(){
			@Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				if (column == 8){
					value = String.format("$%,.2f", (double) value);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};
	}

	private void inicializarPanelLibros (){
		setLayout(new BorderLayout());
		model = getDefaultTableModel();
		refrescarLista();
		JTable tableLibros = new JTable(model);
		tableLibros.getTableHeader().setFont(fuenteCabecera);
		tableLibros.setFont(fuenteCelda);
		formatearColumnas(tableLibros);
		tableLibros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollPane = new JScrollPane(tableLibros);
		add(scrollPane, BorderLayout.CENTER);

		JButton botonAgregar = new JButton("Agregar Libro");
		botonAgregar.setActionCommand(Evento.EVENTO.AGREGAR_LIBRO_AL_CARRITO.name());
		botonAgregar.addActionListener(evento);
		add(botonAgregar, BorderLayout.SOUTH);

		//Asignacion de fuente al boton
		botonAgregar.setFont(fuenteBoton);
	}

	private void formatearColumnas (JTable tableLibros){
		tableLibros.getColumnModel().getColumn(8).setCellRenderer(celdasDoubleFormateadas());
	}

	private void refrescarLista (){
		model.setDataVector(ventana.obtenerListaLibros(), nombreColumnas);
	}

	public DefaultTableModel getTableModel (){
		return model;
	}
}
