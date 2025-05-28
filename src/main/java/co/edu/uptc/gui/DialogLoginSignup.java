package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class DialogLoginSignup extends JDialog {
   private final JTabbedPane    panelContenedor;
   private final Evento         evento;
   private final Font           fuenteLabel     = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   private final Font           fuenteTextField = new Font("Times New Roman", Font.PLAIN, 20);
   private final Font           fuenteBoton     = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private final JLabel         mensajeDeError  = new JLabel();
   private       JPanel         panelLogin;
   private       JPanel         panelRegistro;
   private       JTextField     boxCorreoLogin;
   private       JPasswordField boxContrasenaLogin;
   private       JTextField     boxNombreCompleto;
   private       JTextField     boxCorreo;
   private       JTextField     boxDireccion;
   private       JTextField     boxTelefono;
   private       JPasswordField boxContrasena;

   public DialogLoginSignup (VentanaPrincipal ventanaPrincipal, Evento evento) {
      super(ventanaPrincipal, "Inicio de Sesión / Registro", true);
      this.evento     = evento;
      panelContenedor = new JTabbedPane();
      Font fuentePestania = new Font("Arial", Font.BOLD, 15);
      panelContenedor.setFont(fuentePestania);
      agregarLogin();
      agregarRegistro();
      panelContenedor.addTab("Login", panelLogin);
      panelContenedor.addTab("SingUp", panelRegistro);
      add(panelContenedor);
      pack();
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setLocationRelativeTo(this);
   }

   private void agregarLogin () {
      //Panel de Login
      panelLogin = new JPanel(new BorderLayout());
      //Campos de Usuario y Contraseña
      JPanel panelLoginDatos = new JPanel(new GridBagLayout());
      JLabel labelUsuario    = new JLabel("Correo Electronico", SwingConstants.CENTER);
      JLabel labelContrasena = new JLabel("Contraseña", SwingConstants.CENTER);
      //Asignacion de fuente a cada label
      labelUsuario.setFont(fuenteLabel);
      labelContrasena.setFont(fuenteLabel);
      boxCorreoLogin     = new JTextField("admin1@gmail.com");
      boxContrasenaLogin = new JPasswordField("admin");
      //Asignacion de fuente a cada text field
      boxCorreoLogin.setFont(fuenteTextField);
      boxContrasenaLogin.setFont(fuenteTextField);
      //Validacion en tiempo real del campo de correo electronico
      validarCampoCorreo(boxCorreoLogin);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5, 5, 5, 5);
      gbc.fill   = GridBagConstraints.BOTH;
      //Peso Componente
      final float PESO_COMPONENTE = 0.2f;
      //Fila 0 y 1, Columna 0 => Usuario
      gbc.gridx   = 0;
      gbc.gridy   = 0;
      gbc.weightx = PESO_COMPONENTE;
      panelLoginDatos.add(labelUsuario, gbc);
      gbc.gridy = 1;
      panelLoginDatos.add(boxCorreoLogin, gbc);
      //Fila 2 y 3, Columna 0 => Contrasena
      gbc.gridx   = 0;
      gbc.gridy   = 2;
      gbc.weightx = PESO_COMPONENTE;
      panelLoginDatos.add(labelContrasena, gbc);
      gbc.gridy = 3;
      panelLoginDatos.add(boxContrasenaLogin, gbc);
      //Fila 4, columna 0=> CheckBox Mostrar Contraseña
      gbc.gridy = 4;
      JCheckBox checkBoxMostrarContrasena = new JCheckBox("Mostrar Contraseña");
      checkBoxMostrarContrasena.setSelected(false);
      checkBoxMostrarContrasena.setHorizontalAlignment(JCheckBox.CENTER);
      checkBoxMostrarContrasena.addActionListener(_ -> {
         if (checkBoxMostrarContrasena.isSelected()) {
            boxContrasenaLogin.setEchoChar((char) 0);
         } else {
            boxContrasenaLogin.setEchoChar('•');
         }
      });
      panelLoginDatos.add(checkBoxMostrarContrasena, gbc);
      //Botones
      JPanel  panelBotones       = new JPanel(new GridLayout(2, 1));
      JButton botonIniciarSesion = new JButton("Iniciar Sesión");
      botonIniciarSesion.setActionCommand(Evento.EVENTO.INICIAR_SESION.name());
      botonIniciarSesion.addActionListener(evento);
      panelBotones.add(botonIniciarSesion, gbc);
      JLabel linkRegistrarse = new JLabel("Registrarse");
      linkRegistrarse.setForeground(Color.GRAY);
      linkRegistrarse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      linkRegistrarse.setHorizontalAlignment(JLabel.CENTER);
      linkRegistrarse.addMouseListener(new MouseAdapter() {
         @Override public void mouseClicked (MouseEvent e) {
            panelContenedor.setSelectedComponent(panelRegistro);
         }
      });
      panelBotones.add(linkRegistrarse, gbc);
      //Asignacion de fuente a cada boton
      botonIniciarSesion.setFont(fuenteBoton);
      linkRegistrarse.setFont(fuenteBoton);
      panelLogin.add(panelLoginDatos, BorderLayout.CENTER);
      panelLogin.add(panelBotones, BorderLayout.SOUTH);
   }

   private void agregarRegistro () {
      //Panel SignUp
      panelRegistro = new JPanel(new BorderLayout());
      //Labels
      JLabel labelNombreCompleto = new JLabel("*Nombre Completo", SwingConstants.CENTER);
      JLabel labelCorreo         = new JLabel("*Correo Electronico", SwingConstants.CENTER);
      JLabel labelDireccion      = new JLabel("*Direccion", SwingConstants.CENTER);
      JLabel labelTelefono       = new JLabel("*Teléfono", SwingConstants.CENTER);
      JLabel labelContrasena     = new JLabel("*Contraseña", SwingConstants.CENTER);
      //Asignacion de fuente a cada label
      labelNombreCompleto.setFont(fuenteLabel);
      labelCorreo.setFont(fuenteLabel);
      labelDireccion.setFont(fuenteLabel);
      labelTelefono.setFont(fuenteLabel);
      labelContrasena.setFont(fuenteLabel);
      //Text Fields
      boxNombreCompleto = new JTextField("");
      boxCorreo         = new JTextField("admin.1@gmail.com");
      validarCampoCorreo(boxCorreo);
      boxDireccion  = new JTextField("");
      boxTelefono   = new JTextField("");
      boxContrasena = new JPasswordField("admin123");
      boxNombreCompleto.setHorizontalAlignment(JTextField.CENTER);
      boxCorreo.setHorizontalAlignment(JTextField.CENTER);
      boxDireccion.setHorizontalAlignment(JTextField.CENTER);
      boxTelefono.setHorizontalAlignment(JTextField.CENTER);
      boxContrasena.setHorizontalAlignment(JPasswordField.CENTER);
      //Botones y otros elementos
      JCheckBox checkBoxMostrarContrasena = new JCheckBox("Mostrar Contraseña");
      checkBoxMostrarContrasena.setSelected(false);
      checkBoxMostrarContrasena.setHorizontalAlignment(JCheckBox.RIGHT);
      checkBoxMostrarContrasena.addActionListener(_ -> {
         if (checkBoxMostrarContrasena.isSelected()) {
            boxContrasena.setEchoChar((char) 0);
         } else {
            boxContrasena.setEchoChar('•');
         }
      });
      //Asignacion de fuente a cada text field y al check box
      boxNombreCompleto.setFont(fuenteTextField);
      boxCorreo.setFont(fuenteTextField);
      boxDireccion.setFont(fuenteTextField);
      boxTelefono.setFont(fuenteTextField);
      boxContrasena.setFont(fuenteTextField);
      checkBoxMostrarContrasena.setFont(fuenteTextField);
      //Layout
      JPanel             panelRegistroDatos = new JPanel(new GridBagLayout());
      GridBagConstraints gbc                = new GridBagConstraints();
      gbc.insets = new Insets(5, 5, 5, 5);
      gbc.fill   = GridBagConstraints.BOTH;
      //Peso Componente
      gbc.weightx = 0.45f; //Esta asignación hace que todos los layouts tengan este peso, hasta que se cambie
      //Fila 0, Columnas 0 y 1 ⇒ Labels Nombre y Correo
      gbc.gridy = 0;
      gbc.gridx = 0;
      panelRegistroDatos.add(labelNombreCompleto, gbc);
      gbc.gridx = 1;
      panelRegistroDatos.add(labelCorreo, gbc);
      //Fila 1, Columna 0 y 1 ⇒ Box Nombre y Correo
      gbc.gridy = 1;
      gbc.gridx = 0;
      panelRegistroDatos.add(boxNombreCompleto, gbc);
      gbc.gridx = 1;
      panelRegistroDatos.add(boxCorreo, gbc);
      //Fila 2, Columna 0 y 1 => Labels Direccion y Teléfono
      gbc.gridy = 2;
      gbc.gridx = 0;
      panelRegistroDatos.add(labelDireccion, gbc);
      gbc.gridx = 1;
      panelRegistroDatos.add(labelTelefono, gbc);
      //Fila 3, Columna 0 y 1 => Box Direccion y Teléfono
      gbc.gridy = 3;
      gbc.gridx = 0;
      panelRegistroDatos.add(boxDireccion, gbc);
      gbc.gridx = 1;
      panelRegistroDatos.add(boxTelefono, gbc);
      //Fila 4, Columna 0 y 1 => Labels Tipo de Usuario y Contraseña
      gbc.gridy = 4;
      gbc.gridx = 0;
      panelRegistroDatos.add(labelContrasena, gbc);
      //Fila 5, Columna 0 y 1 => Box Tipo de Usuario y Contraseña
      gbc.gridy = 5;
      gbc.gridx = 0;
      panelRegistroDatos.add(boxContrasena, gbc);
      gbc.gridx = 1;
      panelRegistroDatos.add(checkBoxMostrarContrasena, gbc);
      JPanel panelBotones = new JPanel(new GridLayout(3, 1));
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setFont(new Font("Arial", Font.BOLD, 20));
      mensajeDeError.setHorizontalAlignment(JLabel.CENTER);
      panelBotones.add(mensajeDeError);
      JButton botonRegistrar = new JButton("Crear Cuenta");
      botonRegistrar.addActionListener(_ -> {
         mensajeDeError.setText(obtenerMensajeDeError());
         botonRegistrar.setActionCommand(Evento.EVENTO.REGISTRAR.name());
         if (mensajeDeError.getText().isEmpty()) {
            botonRegistrar.removeActionListener(evento);
            botonRegistrar.addActionListener(evento);
         }
      });
      panelBotones.add(botonRegistrar, gbc);
      JLabel linkIniciarSesion = new JLabel("Iniciar Sesión");
      linkIniciarSesion.setForeground(Color.GRAY);
      linkIniciarSesion.setHorizontalAlignment(JLabel.CENTER);
      linkIniciarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      linkIniciarSesion.addMouseListener(new MouseAdapter() {
         @Override public void mouseClicked (MouseEvent e) {
            panelContenedor.setSelectedComponent(panelLogin);
         }
      });
      panelBotones.add(linkIniciarSesion, gbc);
      //Asignacion de fuente a cada boton
      botonRegistrar.setFont(fuenteBoton);
      linkIniciarSesion.setFont(fuenteBoton);
      //Se agrega el panel de datos y de botones
      panelRegistro.add(panelRegistroDatos, BorderLayout.CENTER);
      panelRegistro.add(panelBotones, BorderLayout.SOUTH);
   }

   private String obtenerMensajeDeError () {
      //Validacion de Campos Vacios
      {
         if (boxNombreCompleto.getText().isEmpty()) {
            return "Debe rellenar el campo Nombre Completo";
         }
         if (boxCorreo.getText().isEmpty()) {
            return "Debe rellenar el campo Correo Electronico";
         }
         if (boxDireccion.getText().isEmpty()) {
            return "Debe rellenar el campo Direccion";
         }
         if (boxTelefono.getText().isEmpty()) {
            return "Debe rellenar el campo Teléfono";
         }
         if (Arrays.toString(boxContrasena.getPassword()).isEmpty()) {
            return "Debe rellenar el campo Contraseña";
         }
      }
      //Validar tamaño y formato de los campos
      if (boxTelefono.getText().length() != 10) {
         return "El campo Teléfono debe tener 10 caracteres";
      }
      if (!boxTelefono.getText().matches("3[0-9]{9}")) {
         return "El campo Teléfono debe tener 10 caracteres numéricos";
      }
      final String regexDireccion = "^(Calle|Carrera|Avenida|Diagonal|Transversal|Circunvalar)\\s\\d+\\s*(#|No\\.)\\s*\\d+(-\\d+)?(\\s*,\\s*[\\w\\s]+)?$\n";
      if (!boxDireccion.getText().matches(regexDireccion)) {
         return "El campo Dirección debe tener la siguiente forma: (Calle, Carrera, Avenida, Diagonal, Transversal, Circunvalar) número (# No.) número - " +
                "numero, Texto Adicional";
      }
      return "";
   }

   public Usuario getDatosRegistro () {
      Usuario usuario = new Usuario();
      usuario.setNombreCompleto(boxNombreCompleto.getText());
      usuario.setCorreoElectronico(boxCorreo.getText());
      usuario.setDireccionEnvio(boxDireccion.getText());
      usuario.setTelefonoContacto(Long.parseLong(boxTelefono.getText()));
      usuario.setClaveAcceso(boxContrasena.getPassword());
      return usuario;
   }

   public Usuario getDatosLogin () {
      Usuario usuario = new Usuario();
      usuario.setCorreoElectronico(boxCorreoLogin.getText());
      usuario.setClaveAcceso(boxContrasenaLogin.getPassword());
      return usuario;
   }

   private void validarCampoCorreo (JTextField boxCorreo) {
      boxCorreo.setInputVerifier(new InputVerifier() {
         @Override public boolean verify (JComponent input) {
            String       texto       = ((JTextField) input).getText();
            final String regexCorreo = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
            if (texto.matches(regexCorreo)) {
               return true;
            }
            JOptionPane.showMessageDialog(null, "El correo electronico no es valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
         }
      });
   }
}