package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;

public class PanelCrearCuentas extends JPanel {
   private final Evento     evento;
   private final JButton    botonValidarCorreo = new JButton("Validar Usuario");
   private final JButton    botonCrearCuenta   = new JButton("Crear Usuario");
   private final Font       fuenteLabel        = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   private final Font       fuenteTextField    = new Font("Times New Roman", Font.PLAIN, 20);
   private final Font       fuenteBoton        = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private       JTextField boxCorreo;
   private       JTextField boxClave;
   private       JTextField boxNombre;
   private       JTextField boxDireccion;
   private       JTextField boxTelefono;
   private final JLabel     mensajeDeError     = new JLabel();

   public PanelCrearCuentas (Evento evento) {
      this.evento = evento;
      inicializarPanel();
   }

   private void inicializarPanel () {
      setLayout(new BorderLayout());
      inicializarPanelCampos();
      inicializarPanelFooter();
   }

   private void inicializarPanelCampos () {
      JPanel panelDatos = new JPanel(new GridLayout(10, 1));
      //Creacion de Labels y centrado de cada uno
      JLabel labelNombre    = new JLabel("Nombre", SwingConstants.CENTER);
      JLabel labelCorreo    = new JLabel("Correo Electronico", SwingConstants.CENTER);
      JLabel labelDireccion = new JLabel("Dirección", SwingConstants.CENTER);
      JLabel labelTelefono  = new JLabel("Teléfono", SwingConstants.CENTER);
      JLabel labelClave     = new JLabel("Contraseña", SwingConstants.CENTER);
      //Centrado de Labels
      labelNombre.setHorizontalAlignment(JLabel.CENTER);
      labelCorreo.setHorizontalAlignment(JLabel.CENTER);
      labelDireccion.setHorizontalAlignment(JLabel.CENTER);
      labelTelefono.setHorizontalAlignment(JLabel.CENTER);
      labelClave.setHorizontalAlignment(JLabel.CENTER);
      //Asignacion de fuente a cada label
      labelNombre.setFont(fuenteLabel);
      labelCorreo.setFont(fuenteLabel);
      labelDireccion.setFont(fuenteLabel);
      labelTelefono.setFont(fuenteLabel);
      labelClave.setFont(fuenteLabel);
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
      boxNombre.setFont(fuenteTextField);
      boxCorreo.setFont(fuenteTextField);
      boxDireccion.setFont(fuenteTextField);
      boxTelefono.setFont(fuenteTextField);
      boxClave.setFont(fuenteTextField);
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
      add(panelDatos, BorderLayout.CENTER);
   }

   private void inicializarPanelFooter () {
      JPanel             panelFooter = new JPanel(new GridBagLayout());
      GridBagConstraints gbc         = new GridBagConstraints();
      gbc.insets = new Insets(5, 5, 0, 5);
      gbc.fill   = GridBagConstraints.BOTH;
      final float pesoBotones = 0.4f;
      final float pesoMensaje = 0.2f;
      gbc.gridx   = 0;
      gbc.gridy   = 0;
      gbc.weightx = pesoBotones;
      botonValidarCorreo.setActionCommand(Evento.EVENTO.VALIDAR_CORREO.name());
      botonValidarCorreo.addActionListener(_ -> {
         if (!boxCorreo.getText().isBlank()) {
            botonValidarCorreo.removeActionListener(evento);
            botonValidarCorreo.addActionListener(evento);
         }
      });
      botonValidarCorreo.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      panelFooter.add(botonValidarCorreo, gbc);
      gbc.gridx   = 0;
      gbc.gridy   = 1;
      gbc.weightx = pesoBotones;
      botonCrearCuenta.setActionCommand(Evento.EVENTO.CREAR_CUENTA.name());
      botonCrearCuenta.addActionListener(_ -> {
         mensajeDeError.setText(obtenerMensajeDeError());
         if (mensajeDeError.getText().isBlank()) {
            botonCrearCuenta.removeActionListener(evento);
            botonCrearCuenta.addActionListener(evento);
         }
      });
      botonCrearCuenta.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      panelFooter.add(botonCrearCuenta, gbc);
      //Asignacion de fuente al boton
      botonValidarCorreo.setFont(fuenteBoton);
      botonCrearCuenta.setFont(fuenteBoton);
      gbc.gridx   = 0;
      gbc.gridy   = 2;
      gbc.weightx = pesoMensaje;
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setFont(new Font("Arial", Font.BOLD, 20));
      mensajeDeError.setHorizontalAlignment(JLabel.CENTER);
      panelFooter.add(mensajeDeError, gbc);
      add(panelFooter, BorderLayout.SOUTH);
   }

   private String obtenerMensajeDeError () {
      //Validacion de Campos Vacios
      {
         if (boxNombre.getText().isBlank()) {
            return "Debe rellenar el campo Nombre";
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
         final String regexDireccion = "^(Calle|Carrera|Avenida|Diagonal|Transversal|Circunvalar)\\s\\d+\\s*(#|No\\.)\\s*\\d+(-\\d+)?(\\s*,\\s*[\\w\\s]+)" + "?$\n";
         if (!boxDireccion.getText().matches(regexDireccion)) {
            return "El campo Dirección debe tener la siguiente forma: (Calle / Carrera / Avenida / Diagonal / Transversal / Circunvalar) número (# / No.)" + " " +
                   "número - numero, Texto Adicional";
         }
         final String regexTelefono = "^3[0-9]{9}$";
         if (!boxTelefono.getText().matches(regexTelefono)) {
            return "El campo Teléfono tiene un formato inválido";
         }
      }
      return "";
   }

   public String getCorreo () {
      if (boxCorreo.getText().isBlank()) {
         return "";
      }
      return boxCorreo.getText();
   }

   public Usuario getDatosUsuario () {
      Usuario usuario = new Usuario();
      usuario.setCorreoElectronico(boxCorreo.getText());
      usuario.setNombreCompleto(boxNombre.getText());
      usuario.setDireccionEnvio(boxDireccion.getText());
      usuario.setTelefonoContacto(Long.parseLong(boxTelefono.getText()));
      usuario.setClaveAcceso(boxClave.getText().toCharArray());
      return usuario;
   }

   public void setMensajeDeError (String mensaje) {
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setText(mensaje);
   }

   public void setMensajeDisponible (String mensaje) {
      mensajeDeError.setForeground(Color.GREEN);
      mensajeDeError.setText(mensaje);
   }
}