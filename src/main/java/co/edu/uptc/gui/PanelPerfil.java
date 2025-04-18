package co.edu.uptc.gui;

import co.edu.uptc.modelo.ROLES;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelPerfil extends JPanel{
	private final Evento            evento;
	private final PantallaPrincipal pantallaPrincipal;
	private final JButton           botonGuardar           = new JButton("Guardar");
	private final JTextField        boxNombreCompleto      = new JTextField();
	private final JTextField        boxCorreo              = new JTextField();
	private final JTextField        boxDireccion           = new JTextField();
	private final JTextField        boxTelefono            = new JTextField();
	private final Font              fuenteLabel            = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
	private final Font              fuenteTextField        = new Font("Times New Roman", Font.PLAIN, 20);
	private final Font              fuenteBoton            = new Font("Lucida Sans Unicode", Font.BOLD, 20);
	private final JLabel            mensajeDeError         = new JLabel();
	private       Object[]          datosUsuario           = new Object[7];
	private       String            nombreCompleto         = "";
	private       String            correoElectronico      = "";
	private       String            direccion              = "";
	private       long              telefono               = 0;
	private       ROLES             tipoUsuario            = ROLES.REGULAR;
	private       int               CID;
	private final JLabel            labelTipoUsuarioActual = new JLabel(tipoUsuario.name());
	private       JPasswordField    boxContrasena          = new JPasswordField();

	public PanelPerfil (PantallaPrincipal pantallaPrincipal, Evento evento){
		this.evento            = evento;
		this.pantallaPrincipal = pantallaPrincipal;
		inicializarPanelPerfil();
	}

	private void inicializarPanelPerfil (){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		refrescarDatosPerfil(datosUsuario);
	}

	private void refrescarDatosPerfil (Object[] datosUsuario){
		if (datosUsuario.length < 5 || ! VentanaPrincipal.LOGIN_CORRECTO){
			rellenarDatosVacios();
			return;
		}

		//Datos de Usuario
		nombreCompleto    = (String) datosUsuario[0];
		correoElectronico = (String) datosUsuario[1];
		direccion         = (String) datosUsuario[2];
		telefono          = (long) datosUsuario[3];
		CID               = (int) datosUsuario[4];
		PanelCarrito.setIdentificadorCarrito(CID);
		tipoUsuario = VentanaPrincipal.obtenerTipoUsuario(correoElectronico);

		inicializarPanelDatosUsuario();
		inicializarPanelFooter();

		revalidate();
		repaint();
	}

	private void rellenarDatosVacios (){
		inicializarPanelDatosUsuario();
		inicializarPanelFooter();
	}

	private void inicializarPanelDatosUsuario (){
		if (VentanaPrincipal.LOGIN_CORRECTO){
			removeAll();
		}
		//Banners de Datos
		//Banner de Nombre Completo
		JPanel panelNombreCompleto = new JPanel(new GridLayout(1, 2));
		panelNombreCompleto.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		                                                               "Nombre",
		                                                               TitledBorder.CENTER,
		                                                               TitledBorder.TOP,
		                                                               fuenteLabel
		));
		boxNombreCompleto.setText(nombreCompleto);
		boxNombreCompleto.setPreferredSize(new Dimension(160, 25));
		panelNombreCompleto.add(boxNombreCompleto);
		add(panelNombreCompleto);

		//Banner de Correo Electronico
		JPanel panelCorreoElectronico = new JPanel(new GridLayout(1, 2));
		panelCorreoElectronico.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		                                                                  "Correo Electronico",
		                                                                  TitledBorder.CENTER,
		                                                                  TitledBorder.TOP,
		                                                                  fuenteLabel
		));
		boxCorreo.setText(correoElectronico);
		boxCorreo.setPreferredSize(new Dimension(160, 25));
		panelCorreoElectronico.add(boxCorreo);
		add(panelCorreoElectronico);

		//Banner de Direccion
		JPanel panelDireccion = new JPanel(new GridLayout(1, 2));
		panelDireccion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		                                                          "Dirección",
		                                                          TitledBorder.CENTER,
		                                                          TitledBorder.TOP,
		                                                          fuenteLabel
		));
		boxDireccion.setText(direccion);
		boxDireccion.setPreferredSize(new Dimension(160, 25));
		panelDireccion.add(boxDireccion);
		add(panelDireccion);

		//Banner de Teléfono
		JPanel panelTelefono = new JPanel(new GridLayout(1, 2));
		panelTelefono.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		                                                         "Teléfono",
		                                                         TitledBorder.CENTER,
		                                                         TitledBorder.TOP,
		                                                         fuenteLabel
		));
		boxTelefono.setText(String.valueOf(telefono));
		boxTelefono.setPreferredSize(new Dimension(160, 25));
		panelTelefono.add(boxTelefono);
		add(panelTelefono);

		//Banner de Contraseña
		JPanel panelContrasena = new JPanel(new GridLayout(2, 1));
		panelContrasena.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		                                                           "Contraseña",
		                                                           TitledBorder.CENTER,
		                                                           TitledBorder.TOP,
		                                                           fuenteLabel
		));
		boxContrasena = new JPasswordField();
		boxContrasena.setPreferredSize(new Dimension(160, 25));
		JCheckBox checkBoxMostrarContrasena = new JCheckBox("Mostrar Contraseña");
		checkBoxMostrarContrasena.setSelected(false);
		checkBoxMostrarContrasena.setHorizontalAlignment(JCheckBox.CENTER);
		checkBoxMostrarContrasena.addActionListener(_ -> {
			if (checkBoxMostrarContrasena.isSelected()){
				boxContrasena.setEchoChar((char) 0);
			}else{
				boxContrasena.setEchoChar('•');
			}
		});
		panelContrasena.add(boxContrasena);
		panelContrasena.add(checkBoxMostrarContrasena);
		add(panelContrasena);

		//Banner de Tipo de Usuario
		JPanel panelTipoUsuario = new JPanel(new GridLayout(1, 2));
		panelTipoUsuario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		                                                            "Tipo de Usuario",
		                                                            TitledBorder.CENTER,
		                                                            TitledBorder.TOP,
		                                                            fuenteLabel
		));
		labelTipoUsuarioActual.setText(tipoUsuario.name());
		labelTipoUsuarioActual.setHorizontalAlignment(JLabel.CENTER);
		labelTipoUsuarioActual.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));
		panelTipoUsuario.add(labelTipoUsuarioActual);
		add(panelTipoUsuario);

		//Asignacion de fuente a cada textField
		boxNombreCompleto.setFont(fuenteTextField);
		boxCorreo.setFont(fuenteTextField);
		boxDireccion.setFont(fuenteTextField);
		boxTelefono.setFont(fuenteTextField);
		boxContrasena.setFont(fuenteTextField);

		revalidate();
		repaint();
	}

	private void inicializarPanelFooter (){
		JPanel panelFooter = new JPanel();
		if (VentanaPrincipal.LOGIN_CORRECTO){
			panelFooter.setLayout(new GridLayout(2, 1));
			mensajeDeError.setForeground(Color.RED);
			mensajeDeError.setFont(new Font("Times New Roman", Font.BOLD, 20));
			mensajeDeError.setHorizontalAlignment(JLabel.CENTER);
			panelFooter.add(mensajeDeError);

			botonGuardar.addActionListener(_ -> {
				mensajeDeError.setText(obtenerMensajeDeError());
			});
			botonGuardar.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			panelFooter.add(botonGuardar);

			//Asignacion de fuente al boton
			botonGuardar.setFont(fuenteBoton);

			pantallaPrincipal.agregarPanelesSegunRol(tipoUsuario.name());
		}else{
			panelFooter.setLayout(new GridLayout(1, 1));
			JButton botonLoginSignUp = new JButton("Login / SignUp");
			botonLoginSignUp.setActionCommand(Evento.EVENTO.LOGIN_SIGNUP.name());
			botonLoginSignUp.addActionListener(evento);
			botonLoginSignUp.setAlignmentX(JComponent.CENTER_ALIGNMENT);

			//Asignacion de fuente al boton
			botonLoginSignUp.setFont(fuenteBoton);
			panelFooter.add(botonLoginSignUp);
		}

		add(panelFooter);
		revalidate();
		repaint();
	}

	private String obtenerMensajeDeError (){
		//Validacion de Campos Vacios
		{
			if (boxNombreCompleto.getText().isEmpty()){
				return "Debe rellenar el campo Nombre Completo";
			}
			if (boxCorreo.getText().isEmpty()){
				return "Debe rellenar el campo Correo Electronico";
			}
			if (boxDireccion.getText().isEmpty()){
				return "Debe rellenar el campo Dirección";
			}
			if (boxTelefono.getText().isEmpty()){
				return "Debe rellenar el campo Teléfono";
			}
			if (boxContrasena.getPassword().length < 8){
				return "El campo contraseña debe tener mínimo 8 digitos";
			}
		}

		//Validacion de Formato Valido
		{
			final String regexCorreo = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
			if (! boxCorreo.getText().matches(regexCorreo)){
				return "El campo Correo Electronico tiene un formato inválido";
			}

			final String regexDireccion = "^(Calle|Carrera|Avenida|Diagonal|Transversal|Circunvalar)\\s\\d+\\s*(#|No\\.)\\s*\\d+(-\\d+)?(\\s*,\\s*[\\w\\s]+)" +
			                              "?$\n";
			if (! boxDireccion.getText().matches(regexDireccion)){
				return "El campo Dirección debe tener la siguiente forma: (Calle / Carrera / Avenida / Diagonal / Transversal / Circunvalar) número (# / No.)" +
				       " " +
				       "número - numero, Texto Adicional";
			}

			final String regexTelefono = "^3[0-9]{9}$";
			if (! boxTelefono.getText().matches(regexTelefono)){
				return "El campo Teléfono tiene un formato inválido";
			}
		}
		return "";
	}

	public void setDatosUsuario (Object[] datosUsuario){
		this.datosUsuario = datosUsuario;
		refrescarDatosPerfil(this.datosUsuario);
	}

	void validarSesionIniciada (){
		if (! mensajeDeError.getText().isBlank()){
			botonGuardar.setActionCommand(Evento.EVENTO.ACTUALIZAR_DATOS_CLIENTE.name());
			botonGuardar.addActionListener(evento);
		}
	}

	public int getCID (){
		return CID;
	}
}
