package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Diálogo para visualizar y actualizar el perfil de usuario.
 * Permite modificar datos personales, dirección, teléfono y contraseña.
 */
public class DialogPerfil extends JDialog {
   /**
    * Referencia al manejador de eventos de la aplicación.
    */
   private final Evento         evento;
   /**
    * Botón para guardar los cambios del perfil.
    */
   private final JButton        botonActualizar        = new JButtonVerde("Guardar");
   /**
    * Campo de texto para el nombre completo del usuario.
    */
   private final JTextField     boxNombreCompleto      = new JTextField();
   /**
    * Campo de texto para el correo electrónico del usuario.
    */
   private final JTextField     boxCorreo              = new JTextField();
   /**
    * Campo de texto para la dirección del usuario.
    */
   private final JTextField     boxDireccion           = new JTextField("");
   /**
    * Campo de texto para el teléfono del usuario.
    */
   private final JTextField     boxTelefono            = new JTextField("");
   /**
    * Etiqueta que muestra el tipo de usuario actual.
    */
   private final JLabel         labelTipoUsuarioActual = new JLabel();
   /**
    * Fuente utilizada para los labels.
    */
   private final Font           fuenteLabel            = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   /**
    * Fuente utilizada para los campos de texto.
    */
   private final Font           fuenteTextField        = new Font("Times New Roman", Font.PLAIN, 20);
   /**
    * Fuente utilizada para el botón de guardar.
    */
   private final Font           fuenteBoton            = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   /**
    * Etiqueta para mostrar mensajes de error.
    */
   private final JLabel         mensajeDeError         = new JLabel();
   /**
    * Campo de contraseña para la nueva contraseña del usuario.
    */
   private final JPasswordField boxContrasenaNueva     = new JPasswordField("");
   /**
    * Objeto Usuario con los nuevos datos ingresados.
    */
   private       Usuario        nuevosDatosUsuario     = null;
   /**
    * Objeto Usuario con los datos actuales del usuario.
    */
   private       Usuario        datosUsuario;

   /**
    * Constructor del diálogo de perfil de usuario.
    * @param evento manejador de eventos
    * @param datosUsuario datos actuales del usuario
    */
   public DialogPerfil (Evento evento, Usuario datosUsuario) {
      super(new Frame(), datosUsuario.getNombreCompleto(), true);
      this.evento       = evento;
      this.datosUsuario = datosUsuario;
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      initComponents();
      pack();
      setResizable(false);
      setLocationRelativeTo(null);
   }

   /**
    * Inicializa los componentes gráficos del diálogo.
    */
   private void initComponents () {
      setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
      inicializarPanelDatosUsuario();
      inicializarPanelFooter();
   }

   /**
    * Inicializa el panel con los datos del usuario.
    */
   private void inicializarPanelDatosUsuario () {
      //Banners de Datos
      //Banner de Nombre Completo
      Dimension dimension           = new Dimension(500, 50);
      JPanel    panelNombreCompleto = new JPanel();
      panelNombreCompleto.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "*Nombre", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxNombreCompleto.setPreferredSize(dimension);
      panelNombreCompleto.add(boxNombreCompleto);
      add(panelNombreCompleto);
      //Banner de Correo Electronico
      JPanel panelCorreo = new JPanel();
      panelCorreo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "*Correo Electronico", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxCorreo.setPreferredSize(dimension);
      panelCorreo.add(boxCorreo);
      add(panelCorreo);
      //Banner de Direccion
      JPanel panelDireccion = new JPanel();
      panelDireccion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Dirección (Opcional)", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxDireccion.setPreferredSize(dimension);
      panelDireccion.add(boxDireccion);
      add(panelDireccion);
      //Banner de Teléfono
      JPanel panelTelefono = new JPanel();
      panelTelefono.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Teléfono (Opcional)", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxTelefono.setPreferredSize(dimension);
      panelTelefono.add(boxTelefono);
      add(panelTelefono);
      //Banner de Contraseña
      JPanel panelContrasena = new JPanel(new GridLayout(2, 1));
      panelContrasena.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Contraseña Nueva (Opcional)", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxContrasenaNueva.setPreferredSize(dimension);
      JCheckBox checkBoxMostrarContrasena = getCheckBoxMostrarContrasena();
      panelContrasena.add(boxContrasenaNueva);
      panelContrasena.add(checkBoxMostrarContrasena);
      add(panelContrasena);
      //Banner de Tipo de Usuario
      JPanel panelTipoUsuario = new JPanel();
      panelTipoUsuario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Tipo de Usuario (Informativo)", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      labelTipoUsuarioActual.setHorizontalAlignment(JLabel.CENTER);
      labelTipoUsuarioActual.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));
      panelTipoUsuario.add(labelTipoUsuarioActual);
      add(panelTipoUsuario);
      //Asignacion de fuente a cada textField
      boxNombreCompleto.setFont(fuenteTextField);
      boxCorreo.setFont(fuenteTextField);
      boxDireccion.setFont(fuenteTextField);
      boxTelefono.setFont(fuenteTextField);
      boxContrasenaNueva.setFont(fuenteTextField);

      //Rellenado de datos del usuario
      boxNombreCompleto.setText(datosUsuario.getNombreCompleto());
      boxCorreo.setText(datosUsuario.getCorreoElectronico());
      boxDireccion.setText(datosUsuario.getDireccionEnvio());
      boxTelefono.setText(String.valueOf(datosUsuario.getTelefonoContacto()));
      labelTipoUsuarioActual.setText(datosUsuario.getTipoUsuario().name());
   }

   /**
    * Crea y retorna el checkbox para mostrar/ocultar la contraseña nueva.
    * @return JCheckBox configurado
    */
   private JCheckBox getCheckBoxMostrarContrasena () {
      JCheckBox checkBoxMostrarContrasena = new JCheckBox("Mostrar Contraseña");
      checkBoxMostrarContrasena.setSelected(false);
      checkBoxMostrarContrasena.setHorizontalAlignment(JCheckBox.CENTER);
      checkBoxMostrarContrasena.addActionListener(_ -> {
         if (checkBoxMostrarContrasena.isSelected()) {
            boxContrasenaNueva.setEchoChar((char) 0);
         } else {
            boxContrasenaNueva.setEchoChar('•');
         }
      });
      return checkBoxMostrarContrasena;
   }

   /**
    * Inicializa el panel inferior con el botón de guardar y el mensaje de error.
    */
   private void inicializarPanelFooter () {
      JPanel panelFooter = new JPanel(new GridLayout(2, 1));
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setFont(new Font("Times New Roman", Font.BOLD, 20));
      mensajeDeError.setHorizontalAlignment(JLabel.CENTER);
      panelFooter.add(mensajeDeError);
      botonActualizar.setText("Actualizar Datos");
      botonActualizar.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      //Asignacion de fuente al boton
      botonActualizar.setFont(fuenteBoton);

      botonActualizar.setActionCommand(Evento.EVENTO.ACTUALIZAR_CLIENTE.name());
      botonActualizar.addActionListener(evento);

      panelFooter.add(botonActualizar);
      add(panelFooter);
      getRootPane().setDefaultButton(botonActualizar);
   }

   /**
    * Valida los campos del formulario y retorna un mensaje de error si hay algún problema.
    * @return mensaje de error o cadena vacía si no hay errores
    */
   public String obtenerMensajeDeError () {
      //Validacion de Campos Vacios
      {
         if (boxNombreCompleto.getText().isBlank()) {
            return "Debe rellenar el campo Nombre Completo";
         }
         if (boxCorreo.getText().isBlank()) {
            return "Debe rellenar el campo Correo Electronico";
         }
      }
      //Validacion de Formato Valido
      {
         //Validacion de Correo Electronico
         final String regexCorreo = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
         if (!boxCorreo.getText().matches(regexCorreo)) {
            return "El Correo Electronico tiene un formato inválido";
         }
         //Validacion de Direccion
         if (!boxDireccion.getText().isBlank()) {
            final String regexDireccion = "^(Calle|Carrera|Avenida|Diagonal|Transversal|Circunvalar)\\s+\\d+[A-Za-z]?\\s*(#|No\\.)\\s*\\d+(?:-\\d+)?(?:\\s*,\\s*[\\w\\s]+)?$";
            if (!boxDireccion.getText().matches(regexDireccion)) {
               return "La Dirección tiene un formato inválido";
            }
         }

         //Validacion de Telefono
         if (!boxTelefono.getText().isBlank() && !boxTelefono.getText().equals("0")) {
            final String regexTelefono = "^3[0-9]{9}$";
            if (!boxTelefono.getText().matches(regexTelefono)) {
               return "El Teléfono tiene un formato inválido";
            }
         }

         //Validacion de Contraseña Nueva
         if (boxContrasenaNueva.getPassword().length > 0 && boxContrasenaNueva.getPassword().length < 8) {
            return "La contraseña debe tener mínimo 8 digitos";
         }
      }
      return " ";
   }

   /**
    * Establece el mensaje de error en la interfaz.
    */
   public void setMensajeDeError () {
      mensajeDeError.setText(obtenerMensajeDeError());
   }

   /**
    * Actualiza el objeto Usuario con los nuevos datos ingresados en el formulario.
    */
   private void actualizarNuevosDatosUsuario () {
      nuevosDatosUsuario = new Usuario();
      nuevosDatosUsuario.setID(datosUsuario.getID());
      nuevosDatosUsuario.setNombreCompleto(boxNombreCompleto.getText());
      nuevosDatosUsuario.setCorreoElectronico(boxCorreo.getText());
      nuevosDatosUsuario.setDireccionEnvio(boxDireccion.getText());
      if (boxTelefono.getText().isBlank() || boxTelefono.getText().equals("0")) {
         nuevosDatosUsuario.setTelefonoContacto(0L);
      } else {
         nuevosDatosUsuario.setTelefonoContacto(Long.parseLong(boxTelefono.getText()));
      }
      nuevosDatosUsuario.setTipoUsuario(Usuario.ROLES.valueOf(labelTipoUsuarioActual.getText()));
      nuevosDatosUsuario.setClaveAcceso(boxContrasenaNueva.getPassword());
   }

   /**
    * Retorna el objeto Usuario con los nuevos datos ingresados.
    * @return Usuario actualizado
    */
   public Usuario getNuevosDatosUsuario () {
      actualizarNuevosDatosUsuario();
      return nuevosDatosUsuario;
   }

   /**
    * Refresca los datos mostrados en el formulario con los datos actualizados del usuario.
    * @param usuarioActualizado usuario actualizado
    */
   public void refrescarDatosUsuario (Usuario usuarioActualizado) {
      datosUsuario = usuarioActualizado;
      boxNombreCompleto.setText(usuarioActualizado.getNombreCompleto());
      boxCorreo.setText(usuarioActualizado.getCorreoElectronico());
      boxDireccion.setText(usuarioActualizado.getDireccionEnvio());
      boxTelefono.setText(String.valueOf(usuarioActualizado.getTelefonoContacto()));
      boxContrasenaNueva.setText("");
      labelTipoUsuarioActual.setText(usuarioActualizado.getTipoUsuario().name());
   }
}