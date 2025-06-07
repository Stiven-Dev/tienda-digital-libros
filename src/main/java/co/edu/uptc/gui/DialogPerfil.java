package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class DialogPerfil extends JDialog {
   private final Evento         evento;
   private final JButton        botonActualizar        = new JButtonVerde("Guardar");
   private final JTextField     boxNombreCompleto      = new JTextField();
   private final JTextField     boxCorreo              = new JTextField();
   private final JTextField     boxDireccion           = new JTextField("");
   private final JTextField     boxTelefono            = new JTextField("");
   private final JLabel         labelTipoUsuarioActual = new JLabel();
   private final Font           fuenteLabel            = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   private final Font           fuenteTextField        = new Font("Times New Roman", Font.PLAIN, 20);
   private final Font           fuenteBoton            = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private final JLabel         mensajeDeError         = new JLabel();
   private final JPasswordField boxContrasenaNueva     = new JPasswordField("");
   private       Usuario        nuevosDatosUsuario     = null;
   private       Usuario        datosUsuario;

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

   private void initComponents () {
      setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
      inicializarPanelDatosUsuario();
      inicializarPanelFooter();
   }

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

   public void setMensajeDeError () {
      mensajeDeError.setText(obtenerMensajeDeError());
   }

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

   public Usuario getNuevosDatosUsuario () {
      actualizarNuevosDatosUsuario();
      return nuevosDatosUsuario;
   }

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