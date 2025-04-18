package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

public class PanelAgregarLibro extends JPanel{
	private final Evento            evento;
	private final JButton           botonGuardar    = new JButton("Guardar");
	private final Font              fuenteLabel     = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
	private final Font              fuenteTextField = new Font("Times New Roman", Font.PLAIN, 20);
	private final Font              fuenteBoton     = new Font("Lucida Sans Unicode", Font.BOLD, 20);
	private final String[]          FORMATOS        = {"DIGITAL", "IMPRESO"};
	private final JComboBox<String> comboBoxFormato = new JComboBox<>(FORMATOS);
	private final JLabel            mensajeDeError  = new JLabel();
	private       JPanel            panelCampos;
	private       JPanel            panelFooter;
	private       JTextField        boxISBN;
	private       JTextField        boxTitulo;
	private       JTextField        boxAutor;
	private       JTextField        boxAnioPublicacion;
	private       JTextField        boxGenero;
	private       JTextField        boxEditorial;
	private       JTextField        boxNumPaginas;
	private       JTextField        boxPrecioVenta;
	private       JTextField        boxCantidadInventario;

	public PanelAgregarLibro (Evento evento){
		this.evento = evento;
		setLayout(new BorderLayout());
		inicializarPanel();
	}

	private void inicializarPanel (){
		inicializarPanelCampos();
		inicializarPanelFooter();
		add(panelCampos, BorderLayout.CENTER);
		add(panelFooter, BorderLayout.SOUTH);
	}

	private void inicializarPanelCampos (){
		panelCampos = new JPanel(new GridBagLayout());

		//Creacion de Labels y centrado de cada uno
		JLabel labelISBN               = new JLabel("*ISBN", SwingConstants.CENTER);
		JLabel labelTitulo             = new JLabel("*Titulo", SwingConstants.CENTER);
		JLabel labelAutor              = new JLabel("*Autor(es)", SwingConstants.CENTER);
		JLabel labelAnioPublicacion    = new JLabel("*Año de Publicación", SwingConstants.CENTER);
		JLabel labelGenero             = new JLabel("*Género", SwingConstants.CENTER);
		JLabel labelEditorial          = new JLabel("*Editorial", SwingConstants.CENTER);
		JLabel labelNumPaginas         = new JLabel("*Número de Páginas", SwingConstants.CENTER);
		JLabel labelPrecioVenta        = new JLabel("*Precio de Venta", SwingConstants.CENTER);
		JLabel labelCantidadInventario = new JLabel("*Cantidad de Inventario", SwingConstants.CENTER);
		JLabel labelFormato            = new JLabel("*Formato", SwingConstants.CENTER);

		//Asignacion de fuente a cada label
		labelISBN.setFont(fuenteLabel);
		labelTitulo.setFont(fuenteLabel);
		labelAutor.setFont(fuenteLabel);
		labelAnioPublicacion.setFont(fuenteLabel);
		labelGenero.setFont(fuenteLabel);
		labelEditorial.setFont(fuenteLabel);
		labelNumPaginas.setFont(fuenteLabel);
		labelPrecioVenta.setFont(fuenteLabel);
		labelCantidadInventario.setFont(fuenteLabel);
		labelFormato.setFont(fuenteLabel);

		//Text Fields
		boxISBN               = new JTextField();
		boxTitulo             = new JTextField();
		boxAutor              = new JTextField("Anonimo");
		boxAnioPublicacion    = new JTextField();
		boxGenero             = new JTextField();
		boxEditorial          = new JTextField();
		boxNumPaginas         = new JTextField("1");
		boxPrecioVenta        = new JTextField("1.0");
		boxCantidadInventario = new JTextField("1");
		JComboBox<String> comboBoxFormato = new JComboBox<>(FORMATOS);

		//Asignacion de fuente a cada text field
		boxISBN.setFont(fuenteTextField);
		boxTitulo.setFont(fuenteTextField);
		boxAutor.setFont(fuenteTextField);
		boxAnioPublicacion.setFont(fuenteTextField);
		boxGenero.setFont(fuenteTextField);
		boxEditorial.setFont(fuenteTextField);
		boxNumPaginas.setFont(fuenteTextField);
		boxPrecioVenta.setFont(fuenteTextField);
		boxCantidadInventario.setFont(fuenteTextField);

		//Centrado de JTextFields
		boxISBN.setHorizontalAlignment(JTextField.CENTER);
		boxTitulo.setHorizontalAlignment(JTextField.CENTER);
		boxAutor.setHorizontalAlignment(JTextField.CENTER);
		boxAnioPublicacion.setHorizontalAlignment(JTextField.CENTER);
		boxGenero.setHorizontalAlignment(JTextField.CENTER);
		boxEditorial.setHorizontalAlignment(JTextField.CENTER);
		boxNumPaginas.setHorizontalAlignment(JTextField.CENTER);
		boxPrecioVenta.setHorizontalAlignment(JTextField.CENTER);
		boxCantidadInventario.setHorizontalAlignment(JTextField.CENTER);
		comboBoxFormato.setSelectedItem("IMPRESO");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill   = GridBagConstraints.BOTH;

		//Peso Componente
		gbc.weightx = 0.45f; //Esta asignación hace que todos los layouts tengan este peso, hasta que se cambie

		//Fila 0, Columnas 0 y 1 ⇒ Labels ISBN y Titulo
		gbc.gridy = 0;
		gbc.gridx = 0;
		panelCampos.add(labelISBN, gbc);
		gbc.gridx = 1;
		panelCampos.add(labelTitulo, gbc);

		//Fila 1, Columna 0 y 1 ⇒ Box ISBN y Titulo
		gbc.gridy = 1;
		gbc.gridx = 0;
		panelCampos.add(boxISBN, gbc);
		gbc.gridx = 1;
		panelCampos.add(boxTitulo, gbc);

		//Fila 2, Columna 0 y 1 => Labels Autor y Año de Publicación
		gbc.gridy = 2;
		gbc.gridx = 0;
		panelCampos.add(labelAutor, gbc);
		gbc.gridx = 1;
		panelCampos.add(labelAnioPublicacion, gbc);

		//Fila 3, Columna 0 y 1 => Box Autor y Año de Publicación
		gbc.gridy = 3;
		gbc.gridx = 0;
		panelCampos.add(boxAutor, gbc);
		gbc.gridx = 1;
		panelCampos.add(boxAnioPublicacion, gbc);

		//Fila 4, Columna 0 y 1 => Labels Género y Editorial
		gbc.gridy = 4;
		gbc.gridx = 0;
		panelCampos.add(labelGenero, gbc);
		gbc.gridx = 1;
		panelCampos.add(labelEditorial, gbc);

		//Fila 5, Columna 0 y 1 => Box Género y Editorial
		gbc.gridy = 5;
		gbc.gridx = 0;
		panelCampos.add(boxGenero, gbc);
		gbc.gridx = 1;
		panelCampos.add(boxEditorial, gbc);

		//Fila 6, Columna 0 y 1 => Label #Páginas y Precio de Venta
		gbc.gridy = 6;
		gbc.gridx = 0;
		panelCampos.add(labelNumPaginas, gbc);
		gbc.gridx = 1;
		panelCampos.add(labelPrecioVenta, gbc);

		//Fila 7, Columna 0 y 1 => Box #Páginas y Precio de Venta
		gbc.gridy = 7;
		gbc.gridx = 0;
		panelCampos.add(boxNumPaginas, gbc);
		gbc.gridx = 1;
		panelCampos.add(boxPrecioVenta, gbc);

		//Fila 8, Columna 0 y 1 => Label Cantidad de Inventario y Formato
		gbc.gridy = 8;
		gbc.gridx = 0;
		panelCampos.add(labelCantidadInventario, gbc);
		gbc.gridx = 1;
		panelCampos.add(labelFormato, gbc);

		//Fila 9, Columna 0 y 1 => Box Cantidad de Inventario y Formato
		gbc.gridy = 9;
		gbc.gridx = 0;
		panelCampos.add(boxCantidadInventario, gbc);
		gbc.gridx = 1;
		panelCampos.add(comboBoxFormato, gbc);

		add(panelCampos, BorderLayout.CENTER);
	}

	private void inicializarPanelFooter (){
		panelFooter = new JPanel(new GridLayout(2, 1));

		mensajeDeError.setForeground(Color.RED);
		mensajeDeError.setFont(new Font("Arial", Font.BOLD, 20));
		mensajeDeError.setHorizontalAlignment(JLabel.CENTER);
		panelFooter.add(mensajeDeError);

		JButton botonGuardar = new JButton("Guardar");
		botonGuardar.addActionListener(e -> {
			mensajeDeError.setText(obtenerMensajeDeError());
		});

		//Asignacion de fuente al boton
		botonGuardar.setFont(fuenteBoton);
		panelFooter.add(botonGuardar);

		add(panelFooter, BorderLayout.SOUTH);
	}

	private String obtenerMensajeDeError (){
		//Validacion de Campos Vacios
		{
			if (boxISBN.getText().isEmpty()){
				return "Debe rellenar el campo ISBN";
			}
			if (boxTitulo.getText().isEmpty()){
				return "Debe rellenar el campo Titulo";
			}
			if (boxAnioPublicacion.getText().isEmpty()){
				return "Debe rellenar el campo Año de Publicación";
			}
			if (boxGenero.getText().isEmpty()){
				return "Debe rellenar el campo Género";
			}
			if (boxEditorial.getText().isEmpty()){
				return "Debe rellenar el campo Editorial";
			}
			if (boxNumPaginas.getText().isEmpty()){
				return "Debe rellenar el campo Número de Páginas";
			}
			if (boxPrecioVenta.getText().isEmpty()){
				return "Debe rellenar el campo Precio de Venta";
			}
			if (boxCantidadInventario.getText().isEmpty()){
				return "Debe rellenar el campo Cantidad de Inventario";
			}
		}
		//Validacion de Formato Valido
		{
			//Validacion de ISBN
			if (! boxISBN.getText().matches("^[0-9]{10,13}$")){
				return "El campo ISBN debe tener de 10 a 13 caracteres numéricos";
			}

			//Validacion de año de publicacion
			try{
				int anioPublicacion = Integer.parseInt(boxAnioPublicacion.getText());
				if (anioPublicacion > 2025 || anioPublicacion < 868){
					return "El campo Año de Publicación debe estar entre 868 y 2025";
				}
			}catch (NumberFormatException e){
				return "El campo Año de Publicación debe ser un número entero";
			}

			//Validacion de Número de Páginas
			try{
				int numeroPaginas = Integer.parseInt(boxNumPaginas.getText());
				if (numeroPaginas < 1){
					return "El campo Número de Páginas debe ser un entero positivo";
				}
			}catch (NumberFormatException e){
				return "El campo Número de Páginas debe ser un número entero";
			}

			//Validacion de Precio de Venta
			try{
				double precioVenta = Double.parseDouble(boxPrecioVenta.getText());
				if (precioVenta < 1.0){
					return "El campo Precio de Venta debe ser un número positivo superior a 1";
				}
			}catch (NumberFormatException e){
				return "El campo Precio de Venta debe ser un número";
			}

			//Validacion de Cantidad de Inventario
			try{
				int cantidadInventario = Integer.parseInt(boxCantidadInventario.getText());
				if (cantidadInventario < 1){
					return "El campo Cantidad de Inventario debe ser un entero positivo";
				}
			}catch (NumberFormatException e){
				return "El campo Cantidad de Inventario debe ser un número entero";
			}
		}

		return "";
	}

	Object[] getDatosLibro (){
		Object[] datos = new Object[10];
		datos[0] = Long.parseLong(boxISBN.getText());
		datos[1] = boxTitulo.getText();
		datos[2] = boxAutor.getText();
		datos[3] = Integer.parseInt(boxAnioPublicacion.getText());
		datos[4] = boxGenero.getText();
		datos[5] = boxEditorial.getText();
		datos[6] = Integer.parseInt(boxNumPaginas.getText());
		datos[7] = Double.parseDouble(boxPrecioVenta.getText());
		datos[8] = Integer.parseInt(boxCantidadInventario.getText());
		datos[9] = comboBoxFormato.getSelectedItem().toString(); //Se entiende que siempre tendra un elemento seleccionado
		return datos;
	}

	void validarSesionIniciada (){
		if (mensajeDeError.getText().isBlank()){
			botonGuardar.setActionCommand(Evento.EVENTO.AGREGAR_LIBRO_AL_ARCHIVO.name());
			botonGuardar.addActionListener(evento);
		}
	}
}
