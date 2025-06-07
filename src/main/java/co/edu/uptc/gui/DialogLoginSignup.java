package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class DialogLoginSignup extends JDialog {
   private final Evento             evento;
   private final Font               fuenteLabel     = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private final Font               fuenteTextField = new Font("Arial", Font.PLAIN, 20);
   private final Font               fuenteBoton     = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private final JLabel             mensajeDeError  = new JLabel(" ", SwingConstants.CENTER);
   private       JPanel             panelLogin;
   private       JPanel             panelRegistro;
   private       JTextField         boxCorreoLogin;
   private       JPasswordField     boxContrasenaLogin;
   private       JTextField         boxNombreCompleto;
   private       JTextField         boxCorreo;
   private       JTextField         boxDireccion;
   private       JTextField         boxTelefono;
   private       JPasswordField     boxContrasena;
   private       JButton            botonRegistrar;
   private       JButton            botonIniciarSesion;
   private final JPanel             panelCentral    = new JPanel(new CardLayout());
   private final GridBagConstraints gbcFooter       = new GridBagConstraints();

   public DialogLoginSignup (VentanaPrincipal ventanaPrincipal, Evento evento) {
      super(ventanaPrincipal, "Inicio de Sesión / Registro", true);
      this.evento = evento;
      agregarLogin();
      agregarRegistro();
      panelCentral.add(panelLogin, "LOGIN");
      panelCentral.add(panelRegistro, "REGISTRO");
      add(panelCentral);
      pack();
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setLocationRelativeTo(this);
      setResizable(false);
   }

   private void agregarLogin () {
      //Panel de Login
      panelLogin = new JPanel(new BorderLayout());
      //Campos de Usuario y Contraseña
      JPanel panelLoginDatos = new JPanel(new GridBagLayout());
      JLabel labelUsuario    = new JLabel("Correo Electrónico", SwingConstants.CENTER);
      JLabel labelContrasena = new JLabel("Contraseña", SwingConstants.CENTER);
      //Asignacion de fuente a cada label
      labelUsuario.setFont(fuenteLabel);
      labelContrasena.setFont(fuenteLabel);
      boxCorreoLogin     = new JTextField("regular1@gmail.com");
      boxContrasenaLogin = new JPasswordField("regular1");
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
      JCheckBox checkBoxMostrarContrasena = getCheckBoxMostrarContrasena();
      panelLoginDatos.add(checkBoxMostrarContrasena, gbc);
      //Botones
      JPanel panelBotonesLogin = new JPanel(new GridBagLayout());
      gbcFooter.insets   = new Insets(10, 10, 10, 10);
      gbcFooter.fill     = GridBagConstraints.BOTH;
      gbcFooter.weightx  = 0.9f;
      gbcFooter.weighty  = 0.9f;
      gbcFooter.gridx    = 0;
      gbcFooter.gridy    = 0;
      botonIniciarSesion = new JButtonVerde("Iniciar Sesión");
      botonIniciarSesion.setActionCommand(Evento.EVENTO.INICIAR_SESION.name());
      botonIniciarSesion.addActionListener(evento);
      panelBotonesLogin.add(botonIniciarSesion, gbcFooter);
      gbcFooter.gridy = 1;
      JButton linkRegistrarse = new JButtonAzul("");
      linkRegistrarse.setAction(new AbstractAction() {
         @Override public void actionPerformed (ActionEvent e) {
            CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
            cardLayout.show(panelCentral, "REGISTRO");
            getRootPane().setDefaultButton(botonRegistrar);
            pack();
            revalidate();
            repaint();
         }
      });
      linkRegistrarse.setText("Crear Una Cuenta");
      panelBotonesLogin.add(linkRegistrarse, gbcFooter);
      //Asignacion de fuente a cada boton
      botonIniciarSesion.setFont(fuenteBoton);
      linkRegistrarse.setFont(fuenteBoton);
      panelLogin.add(panelLoginDatos, BorderLayout.CENTER);
      panelLogin.add(panelBotonesLogin, BorderLayout.SOUTH);
      getRootPane().setDefaultButton(botonIniciarSesion);
   }

   private JCheckBox getCheckBoxMostrarContrasena () {
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
      checkBoxMostrarContrasena.setFont(fuenteTextField);
      return checkBoxMostrarContrasena;
   }

   private void agregarRegistro () {
      //Panel SignUp
      panelRegistro = new JPanel(new BorderLayout());
      //Labels
      JLabel labelNombreCompleto = new JLabel("*Nombre Completo", SwingConstants.CENTER);
      JLabel labelCorreo         = new JLabel("*Correo Electrónico", SwingConstants.CENTER);
      JLabel labelDireccion      = new JLabel("Dirección", SwingConstants.CENTER);
      JLabel labelTelefono       = new JLabel("Teléfono", SwingConstants.CENTER);
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

      JPanel panelFooterRegistro = new JPanel(new GridBagLayout());
      gbcFooter.insets  = new Insets(5, 5, 5, 5);
      gbcFooter.weighty = 0.35f;
      gbcFooter.weightx = 0.9f;
      gbcFooter.gridy   = 0;
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setFont(new Font("Arial", Font.BOLD, 20));
      panelFooterRegistro.add(mensajeDeError, gbcFooter);

      gbcFooter.gridy = 1;
      botonRegistrar  = new JButtonVerde("Crear Cuenta");
      botonRegistrar.setActionCommand(Evento.EVENTO.REGISTRAR_CLIENTE.name());
      botonRegistrar.addActionListener(_ -> {
         mensajeDeError.setText(obtenerMensajeDeError());
         if (mensajeDeError.getText().isEmpty()) {
            botonRegistrar.removeActionListener(evento);
            botonRegistrar.addActionListener(evento);
         }
      });
      panelFooterRegistro.add(botonRegistrar, gbcFooter);

      gbcFooter.gridy = 2;
      JButton linkIniciarSesion = new JButtonAzul("");
      linkIniciarSesion.setAction(new AbstractAction() {
         @Override public void actionPerformed (ActionEvent e) {
            CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
            cardLayout.show(panelCentral, "LOGIN");
            getRootPane().setDefaultButton(botonIniciarSesion);
            pack();
            revalidate();
            repaint();
         }
      });
      linkIniciarSesion.setText("Ya tengo una Cuenta");
      panelFooterRegistro.add(linkIniciarSesion, gbcFooter);
      //Asignacion de fuente a cada boton
      botonRegistrar.setFont(fuenteBoton);
      linkIniciarSesion.setFont(fuenteBoton);
      //Se agrega el panel de datos y de botones
      panelRegistro.add(panelRegistroDatos, BorderLayout.CENTER);
      panelRegistro.add(panelFooterRegistro, BorderLayout.SOUTH);
   }

   private String obtenerMensajeDeError () {
      //Validacion de Campos Vacios
      {
         if (boxNombreCompleto.getText().isEmpty()) {
            return "Debe rellenar el campo Nombre Completo";
         }
         if (boxCorreo.getText().isEmpty()) {
            return "Debe rellenar el campo Correo Electrónico";
         }
         if (Arrays.toString(boxContrasena.getPassword()).isEmpty()) {
            return "Debe rellenar el campo Contraseña";
         }
      }
      //Validar tamaño y formato de los campos
      if (!boxTelefono.getText().isBlank()) {
         if (boxTelefono.getText().length() != 10) {
            return "El campo Teléfono debe tener 10 caracteres numéricos";
         }
         if (!boxTelefono.getText().matches("3[0-9]{9}")) {
            return "El campo Teléfono debe empezar por 3 y tener 10 caracteres numéricos";
         }
      }

      if (!boxDireccion.getText().isBlank()) {
         final String regexDireccion = "^(Calle|Carrera|Avenida|Diagonal|Transversal|Circunvalar)\\s\\d+\\s*#\\s*\\d+(-\\d+)?(\\s*,\\s*[\\w\\s]+)?$\n";
         if (!boxDireccion.getText().matches(regexDireccion)) {
            return "El campo Dirección debe tener la siguiente forma: Calle 15 # 15 - 15, Indicaciones Adicionales";
         }
      }

      return "";
   }

   public Usuario getDatosRegistro () {
      Usuario usuario = new Usuario();
      usuario.setNombreCompleto(boxNombreCompleto.getText());
      usuario.setCorreoElectronico(boxCorreo.getText());
      usuario.setDireccionEnvio(boxDireccion.getText());
      if (boxTelefono.getText().isBlank()) {
         usuario.setTelefonoContacto(0L);
      } else {
         usuario.setTelefonoContacto(Long.parseLong(boxTelefono.getText()));
      }
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
            JOptionPane.showMessageDialog(null, "El correo Electrónico no es valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
         }
      });
   }
}