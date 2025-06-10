package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para la creación de nuevas cuentas de usuario.
 * Permite ingresar los datos requeridos y validar la información antes de registrar el usuario.
 */
public class DialogAdministrarCuentas extends JDialog {
   /**
    * Referencia al manejador de eventos de la aplicación.
    */
   private final Evento                   evento;
   /**
    * Botón para validar si el correo ya está registrado.
    */
   private final JButton                  botonValidarCorreo    = new JButton("Validar Usuario");
   /**
    * Botón para actualizar la cuenta de usuario.
    * Este botón puede ser utilizado para editar los datos de un usuario existente
    * Este botón no será mostrado en la interfaz hasta que se intente validar un correo existente, en ese caso aparecerá
    */
   private final JButton                  botonActualizarCuenta = new JButton("Actualizar Cuenta");
   /**
    * Botón para crear la cuenta de usuario.
    */
   private final JButton                  botonCrearCuenta      = new JButton("Crear Usuario");
   /**
    * Fuente utilizada para los labels.
    */
   private final Font                     fuenteLabel           = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   /**
    * Fuente utilizada para los campos de texto.
    */
   private final Font                     fuenteCampoEditable   = new Font("Times New Roman", Font.PLAIN, 20);
   /**
    * Fuente utilizada para los botones.
    */
   private final Font                     fuenteBoton           = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   /**
    * Etiqueta para mostrar mensajes de error.
    */
   private final JLabel                   mensajeDeError        = new JLabel(" ", JLabel.CENTER);
   /**
    * ComboBox para seleccionar el tipo de usuario (REGULAR, PREMIUM o ADMIN).
    */
   private final JComboBox<Usuario.ROLES> comboBoxTipoUsuario   = new JComboBox<>(Usuario.ROLES.values());
   /**
    * Restricciones de diseño para los componentes del panel.
    */
   private final GridBagConstraints       gbc                   = new GridBagConstraints();
   /**
    * Campo de texto para el correo electrónico.
    */
   private       JTextField               boxCorreo;
   /**
    * Campo de texto para la contraseña.
    */
   private       JTextField               boxClave;
   /**
    * Campo de texto para el nombre del usuario.
    */
   private       JTextField               boxNombre;
   /**
    * Campo de texto para la dirección del usuario.
    */
   private       JTextField               boxDireccion;
   /**
    * Campo de texto para el teléfono del usuario.
    */
   private       JTextField               boxTelefono;
   /**
    * Usuario obtenido para editar sus datos.
    * Este campo se utiliza para almacenar los datos de un usuario existente cuando se edita su cuenta.
    */
   private       Usuario                  usuarioObtenido;

   /**
    * Constructor del panel de creación de cuentas.
    *
    * @param evento manejador de eventos
    */
   public DialogAdministrarCuentas (Evento evento) {
      super(new JFrame(), "Administrar Cuenta", true);
      this.evento = evento;
      setLayout(new GridBagLayout());

      gbc.fill    = GridBagConstraints.BOTH;
      gbc.insets  = new Insets(5, 5, 5, 5);
      gbc.weightx = 1;
      gbc.gridx   = 0;
      inicializarPanel();
      setSize(600, 800);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   /**
    * Inicializa el panel principal y sus componentes.
    */
   private void inicializarPanel () {
      inicializarPanelCampos();
      inicializarPanelFooter();
   }

   /**
    * Inicializa el panel de campos de texto para ingresar los datos del usuario.
    */
   private void inicializarPanelCampos () {
      JPanel panelDatos = new JPanel(new GridLayout(12, 1));
      //Creacion de Labels y centrado de cada uno
      JLabel labelNombre      = new JLabel("*Nombre", JLabel.CENTER);
      JLabel labelCorreo      = new JLabel("*Correo Electronico", JLabel.CENTER);
      JLabel labelDireccion   = new JLabel("Dirección", JLabel.CENTER);
      JLabel labelTelefono    = new JLabel("Teléfono", JLabel.CENTER);
      JLabel labelClave       = new JLabel("*Contraseña", JLabel.CENTER);
      JLabel labelTipoUsuario = new JLabel("*Tipo de Usuario", JLabel.CENTER);
      //Asignacion de fuente a cada label
      labelNombre.setFont(fuenteLabel);
      labelCorreo.setFont(fuenteLabel);
      labelDireccion.setFont(fuenteLabel);
      labelTelefono.setFont(fuenteLabel);
      labelClave.setFont(fuenteLabel);
      labelTipoUsuario.setFont(fuenteLabel);
      //Creacion de Text Fields
      boxNombre    = new JTextField();
      boxCorreo    = new JTextField();
      boxDireccion = new JTextField();
      boxTelefono  = new JTextField();
      boxClave     = new JPasswordField();

      //Centrado de Text Fields
      boxNombre.setHorizontalAlignment(JTextField.CENTER);
      boxCorreo.setHorizontalAlignment(JTextField.CENTER);
      boxDireccion.setHorizontalAlignment(JTextField.CENTER);
      boxTelefono.setHorizontalAlignment(JTextField.CENTER);
      boxClave.setHorizontalAlignment(JPasswordField.CENTER);
      //Asignacion de fuente a cada text field
      boxNombre.setFont(fuenteCampoEditable);
      boxCorreo.setFont(fuenteCampoEditable);
      boxDireccion.setFont(fuenteCampoEditable);
      boxTelefono.setFont(fuenteCampoEditable);
      boxClave.setFont(fuenteCampoEditable);
      comboBoxTipoUsuario.setFont(fuenteCampoEditable);
      //Agregacion de Labels y Text Fields a Panel
      panelDatos.add(labelNombre);
      panelDatos.add(boxNombre);
      panelDatos.add(labelCorreo);
      panelDatos.add(boxCorreo);
      panelDatos.add(labelDireccion);
      panelDatos.add(boxDireccion);
      panelDatos.add(labelTelefono);
      panelDatos.add(boxTelefono);
      panelDatos.add(labelClave);
      panelDatos.add(boxClave);
      panelDatos.add(labelTipoUsuario);
      panelDatos.add(comboBoxTipoUsuario);
      gbc.weighty = 0.7;
      gbc.gridy   = 0;
      add(panelDatos, gbc);
   }

   /**
    * Inicializa el panel del pie de página con los botones y mensajes.
    */
   private void inicializarPanelFooter () {
      JPanel             panelFooter = new JPanel(new GridBagLayout());
      GridBagConstraints gbcFooter   = new GridBagConstraints();
      gbcFooter.insets = new Insets(5, 10, 5, 10);
      gbcFooter.fill   = GridBagConstraints.BOTH;
      final float pesoXBotones = 0.5f;
      final float pesoYBotones = 0.25f;
      final float pesoXMensaje = 1;
      final float pesoYMensaje = 0.1f;
      gbcFooter.gridx   = 0;
      gbcFooter.gridy   = 0;
      gbcFooter.weightx = pesoXBotones;
      gbcFooter.weighty = pesoYBotones;
      botonValidarCorreo.setAlignmentX(JButton.CENTER);
      botonValidarCorreo.setActionCommand(Evento.EVENTO.VALIDAR_CORREO.name());
      botonValidarCorreo.addActionListener(evento);

      panelFooter.add(botonValidarCorreo, gbcFooter);
      gbcFooter.gridy++;
      botonActualizarCuenta.setAlignmentX(JButton.CENTER);
      botonActualizarCuenta.setActionCommand(Evento.EVENTO.ACTUALIZAR_CLIENTE_ADMIN.name());
      botonActualizarCuenta.addActionListener(evento);
      panelFooter.add(botonActualizarCuenta, gbcFooter);
      gbcFooter.gridy++;

      botonCrearCuenta.setAlignmentX(JButton.CENTER);
      botonCrearCuenta.setActionCommand(Evento.EVENTO.CREAR_CUENTA.name());
      botonCrearCuenta.addActionListener(evento);

      //Asignacion de fuente al boton
      botonValidarCorreo.setFont(fuenteBoton);
      botonCrearCuenta.setFont(fuenteBoton);
      botonActualizarCuenta.setFont(fuenteBoton);
      panelFooter.add(botonCrearCuenta, gbcFooter);
      gbcFooter.gridy++;

      gbcFooter.weightx = pesoXMensaje;
      gbcFooter.weighty = pesoYMensaje;
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setFont(new Font("Arial", Font.BOLD, 16));
      panelFooter.add(mensajeDeError, gbcFooter);
      gbcFooter.gridy++;

      final JLabel labelAviso = new JLabel("*La contraseña no será mostrada por seguridad*", JLabel.CENTER);
      labelAviso.setForeground(Color.ORANGE);
      labelAviso.setFont(new Font("Arial", Font.ITALIC, 12));
      panelFooter.add(labelAviso, gbcFooter);
      gbc.weighty = 0.3f;
      gbc.gridy++;
      add(panelFooter, gbc);
      getRootPane().setDefaultButton(botonValidarCorreo);
   }

   /**
    * Valida los campos del formulario y retorna un mensaje de error si existe algún problema.
    *
    * @return mensaje de error o cadena vacía si no hay errores
    */
   String obtenerMensajeDeError () {
      //Validacion de Campos Vacios
      {
         if (boxNombre.getText().isBlank()) {
            return "Debe rellenar el campo Nombre";
         }
         if (boxCorreo.getText().isBlank()) {
            return "Debe rellenar el campo Correo Electronico";
         }
         if (boxClave.getText().isBlank()) {
            return "Debe rellenar el campo Contraseña";
         }
      }
      //Validacion de formato valido Correo, Direccion y Teléfono
      {
         final String regexCorreo = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
         if (!boxCorreo.getText().matches(regexCorreo)) {
            return "El campo Correo Electronico tiene un formato inválido";
         }
         if (!boxDireccion.getText().isBlank()) {
            final String regexDireccion = "^(Calle|Carrera|Avenida|Diagonal|Transversal|Circunvalar)\\s+\\d+[A-Za-z]?\\s*(#|No\\.)\\s*\\d+(?:-\\d+)?(?:\\s*,\\s*[\\w\\s]+)?$";
            if (!boxDireccion.getText().matches(regexDireccion)) {
               return "La Dirección tiene un formato inválido";
            }
         }
         if (!boxTelefono.getText().isBlank() && !boxTelefono.getText().equals("0")) {
            final String regexTelefono = "^3[0-9]{9}$";
            if (!boxTelefono.getText().matches(regexTelefono)) {
               return "El Teléfono tiene un formato inválido";
            }
         }
         if (boxClave.getText().trim().length() < 8) {
            return "La Contraseña debe tener al menos 8 caracteres";
         }
      }
      return "";
   }

   /**
    * Obtiene el correo electrónico ingresado en el formulario.
    *
    * @return correo electrónico o cadena vacía si está en blanco o si está en formato inválido
    */
   public String getCorreo () {
      if (boxCorreo.getText().isBlank()) {
         return "";
      }
      final String regexCorreo = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
      if (!boxCorreo.getText().matches(regexCorreo)) {
         setMensajeDeError("El campo Correo Electronico tiene un formato inválido");
         return "";
      }
      return boxCorreo.getText();
   }

   /**
    * Establece un mensaje de error en el formulario.
    *
    * @param mensaje mensaje de error a mostrar
    */
   public void setMensajeDeError (String mensaje) {
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setText(mensaje);
   }

   /**
    * Muestra un mensaje indicando que el usuario está disponible.
    */
   public void setMensajeDisponible () {
      mensajeDeError.setForeground(Color.GREEN);
      mensajeDeError.setText("Correo Disponible");
   }

   /**
    * Obtiene los datos del usuario ingresados en el formulario.
    *
    * @return objeto Usuario con los datos del formulario
    */
   public Usuario getDatosUsuario () {
      Usuario usuario = new Usuario();
      if (usuarioObtenido != null) {
         usuario.setID(usuarioObtenido.getID());
      }
      usuario.setCorreoElectronico(boxCorreo.getText());
      usuario.setNombreCompleto(boxNombre.getText());
      usuario.setDireccionEnvio(boxDireccion.getText());
      String stringTelefonoContacto = boxTelefono.getText();
      if (stringTelefonoContacto.isBlank()) {
         usuario.setTelefonoContacto(0);
      } else {
         usuario.setTelefonoContacto(Long.parseLong(stringTelefonoContacto));
      }
      usuario.setClaveAcceso(boxClave.getText().toCharArray());
      usuario.setTipoUsuario((Usuario.ROLES) comboBoxTipoUsuario.getSelectedItem());
      return usuario;
   }

   /**
    * Establece los datos de un usuario existente en el formulario.
    *
    * @param usuario objeto Usuario con los datos a mostrar
    */
   public void setDatosUsuario (Usuario usuario) {
      usuarioObtenido = usuario;
      usuarioObtenido.setID(usuario.getID());
      boxNombre.setText(usuario.getNombreCompleto());
      boxCorreo.setText(usuario.getCorreoElectronico());
      boxDireccion.setText(usuario.getDireccionEnvio());
      boxTelefono.setText(String.valueOf(usuario.getTelefonoContacto()));
      boxClave.setText(String.valueOf(usuario.getClaveAcceso()));
      comboBoxTipoUsuario.setSelectedItem(usuario.getTipoUsuario());
   }

   public void limpiarCampos () {
      boxNombre.setText("");
      boxTelefono.setText("");
      boxClave.setText("");
      boxDireccion.setText("");
      comboBoxTipoUsuario.setSelectedItem(Usuario.ROLES.REGULAR);
   }
}