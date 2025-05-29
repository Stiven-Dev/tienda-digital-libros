package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class DialogPerfil extends JDialog {
   private final  Evento         evento;
   private final  JButton        botonActualizar        = new JButtonVerde("Guardar");
   private final  JTextField     boxNombreCompleto      = new JTextField();
   private final  JTextField     boxCorreo              = new JTextField();
   private final  JTextField     boxDireccion           = new JTextField();
   private final  JTextField     boxTelefono            = new JTextField();
   private final  JLabel         labelTipoUsuarioActual = new JLabel();
   private final  Font           fuenteLabel            = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   private final  Font           fuenteTextField        = new Font("Times New Roman", Font.PLAIN, 20);
   private final  Font           fuenteBoton            = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private final  JLabel         mensajeDeError         = new JLabel();
   private final  JPasswordField boxContrasenaNueva     = new JPasswordField("");
   private final  JPasswordField boxContrasenaActual    = new JPasswordField("");
   private static Usuario        nuevosDatosUsuario     = null;
   private        Usuario        datosUsuario;

   public DialogPerfil (Evento evento, Usuario datosUsuario) {
      super(new Frame(), datosUsuario.getNombreCompleto(), true);
      this.evento = evento;
      pack();
      setResizable(false);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      inicializarPanelPerfil();
   }

   private void inicializarPanelPerfil () {
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      inicializarPanelDatosUsuario();
      inicializarPanelFooter();
   }

   private void inicializarPanelDatosUsuario () {
      //Banners de Datos
      //Banner de Nombre Completo
      JPanel panelNombreCompleto = new JPanel();
      panelNombreCompleto.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Nombre", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxNombreCompleto.setPreferredSize(new Dimension(160, 25));
      panelNombreCompleto.add(boxNombreCompleto);
      add(panelNombreCompleto);
      //Banner de Correo Electronico
      JPanel panelCorreo = new JPanel();
      panelCorreo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Correo Electronico", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxCorreo.setPreferredSize(new Dimension(160, 25));
      panelCorreo.add(boxCorreo);
      add(panelCorreo);
      //Banner de Direccion
      JPanel panelDireccion = new JPanel();
      panelDireccion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Dirección", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxDireccion.setPreferredSize(new Dimension(160, 25));
      panelDireccion.add(boxDireccion);
      add(panelDireccion);
      //Banner de Teléfono
      JPanel panelTelefono = new JPanel();
      panelTelefono.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Teléfono", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxTelefono.setPreferredSize(new Dimension(160, 25));
      panelTelefono.add(boxTelefono);
      add(panelTelefono);
      //Banner de Contraseña
      JPanel panelContrasena = new JPanel(new GridLayout(2, 1));
      panelContrasena.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Contraseña", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
      boxContrasenaNueva.setPreferredSize(new Dimension(160, 25));
      JCheckBox checkBoxMostrarContrasena = getCheckBoxMostrarContrasena();
      panelContrasena.add(boxContrasenaNueva);
      panelContrasena.add(checkBoxMostrarContrasena);
      add(panelContrasena);
      //Banner de Tipo de Usuario
      JPanel panelTipoUsuario = new JPanel();
      panelTipoUsuario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Tipo de Usuario", TitledBorder.CENTER, TitledBorder.TOP, fuenteLabel));
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
   }

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

   private void inicializarPanelFooter () {
      JPanel panelFooter = new JPanel();
      panelFooter.setLayout(new GridLayout(2, 1));
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setFont(new Font("Times New Roman", Font.BOLD, 20));
      mensajeDeError.setHorizontalAlignment(JLabel.CENTER);
      panelFooter.add(mensajeDeError);
      botonActualizar.setText("Actualizar Datos");
      botonActualizar.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      //Asignacion de fuente al boton
      botonActualizar.setFont(fuenteBoton);

      botonActualizar.setActionCommand(Evento.EVENTO.ACTUALIZAR_CLIENTE.name());
      botonActualizar.addActionListener(_ -> {
         mensajeDeError.setText(obtenerMensajeDeError());
         if (mensajeDeError.getText().isBlank()) {
            //No encontré otra forma de validar que cuando los campos esten llenos de forma correcta, agregar el listener de la clase Evento. Pero antes, eliminando el
            // anterior, esto para evitar repeticiones inesperadas al pulsar el botón
            botonActualizar.removeActionListener(evento);
            actualizarDatosUsuario();
            botonActualizar.addActionListener(evento);
         }
      });

      panelFooter.add(botonActualizar);
      add(panelFooter);
   }

   private String obtenerMensajeDeError () {
      //Validacion de Campos Vacios
      {
         if (boxNombreCompleto.getText().isBlank()) {
            return "Debe rellenar el campo Nombre Completo";
         }
         if (boxCorreo.getText().isBlank()) {
            return "Debe rellenar el campo Correo Electronico";
         }
         if (boxDireccion.getText().isBlank()) {
            return "Debe rellenar el campo Dirección";
         }
         if (boxTelefono.getText().isBlank()) {
            return "Debe rellenar el campo Teléfono";
         }
      }
      //Validacion de Formato Valido
      {
         //Validacion de Correo Electronico
         final String regexCorreo = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
         if (!boxCorreo.getText().matches(regexCorreo)) {
            return "El campo Correo Electronico tiene un formato inválido";
         }
         //Validacion de Direccion
         final String regexDireccion = "^(Calle|Carrera|Avenida|Diagonal|Transversal|Circunvalar)\\s\\d+\\s*(#|No\\.)\\s*\\d+(-\\d+)?(\\s*,\\s*[\\w\\s]+)" + "?$\n";
         if (!boxDireccion.getText().matches(regexDireccion)) {
            return "El campo Dirección debe tener la siguiente forma: (Calle / Carrera / Avenida / Diagonal / Transversal / Circunvalar) número (# / No.)" + " " +
                   "número - numero, Texto Adicional";
         }
         //Validacion de Telefono
         final String regexTelefono = "^3[0-9]{9}$";
         if (!boxTelefono.getText().matches(regexTelefono)) {
            return "El campo Teléfono tiene un formato inválido";
         }
         //Validacion de Contraseña, si no se ingresa una nueva, no se solicita, solo se hace cambio de contraseña si se ingresa una nueva
         if (boxContrasenaNueva.getPassword().length > 0 && boxContrasenaNueva.getPassword().length < 8) {
            return "El campo contraseña debe tener mínimo 8 digitos";
         }
      }
      return "";
   }

   public void setDatosUsuario (Usuario datosUsuario) {
      this.datosUsuario = datosUsuario;
      boxNombreCompleto.setText(datosUsuario.getNombreCompleto());
      boxCorreo.setText(datosUsuario.getCorreoElectronico());
      boxDireccion.setText(datosUsuario.getDireccionEnvio());
      boxTelefono.setText(String.valueOf(datosUsuario.getTelefonoContacto()));
      labelTipoUsuarioActual.setText(datosUsuario.getTipoUsuario().name());
      inicializarPanelFooter();
   }

   private void actualizarDatosUsuario () {
      nuevosDatosUsuario = new Usuario();
      nuevosDatosUsuario.setID(datosUsuario.getID());
      nuevosDatosUsuario.setNombreCompleto(boxNombreCompleto.getText());
      nuevosDatosUsuario.setCorreoElectronico(boxCorreo.getText());
      nuevosDatosUsuario.setDireccionEnvio(boxDireccion.getText());
      nuevosDatosUsuario.setTelefonoContacto(Long.parseLong(boxTelefono.getText()));
      nuevosDatosUsuario.setTipoUsuario(Usuario.ROLES.valueOf(labelTipoUsuarioActual.getText()));
      nuevosDatosUsuario.setClaveAcceso(boxContrasenaNueva.getPassword());
   }

   public static Usuario getNuevosDatosUsuario () {
      return nuevosDatosUsuario;
   }
}