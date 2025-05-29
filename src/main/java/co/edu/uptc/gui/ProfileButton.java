package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;

public class ProfileButton extends JButton {
   private final Evento evento;
   private       String initials = "X";

   //TODO generar el evento para que al ser clickeado muestre el Dialog de Iniciar Sesión/Registro. Pero que si ya se inició sesión, muestre el menú desplegable con las opciones
   // de perfil
   public ProfileButton (Evento evento) {
      this.evento = evento;
      setPreferredSize(new Dimension(40, 40));
      setContentAreaFilled(false);
      setFocusPainted(false);
      setBorderPainted(false);
      setOpaque(false);
   }

   @Override protected void paintComponent (Graphics g) {
      int        diameter = Math.min(getWidth(), getHeight());
      Graphics2D g2       = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(Color.GRAY);
      g2.fillOval(0, 0, diameter, diameter);
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 16));
      FontMetrics fm         = g2.getFontMetrics();
      int         textWidth  = fm.stringWidth(initials);
      int         textAscent = fm.getAscent();
      int         x          = (diameter - textWidth) / 2;
      int         y          = (diameter + textAscent) / 2 - 4;
      g2.drawString(initials, x, y);
      g2.dispose();
      super.paintComponent(g);
   }

   public void iniciarSesion (Usuario usuario) {
      String nombreUsuario = usuario.getNombreCompleto();
      if (nombreUsuario != null && !nombreUsuario.isBlank()) {
         String[] partsName = nombreUsuario.split(" ");
         switch (partsName.length) {
            //Si solo hay una palabra, se toma la primera letra en mayúscula y la segunda letra en minúscula
            case 1 -> initials = (String.valueOf(nombreUsuario.charAt(0)).toUpperCase()) + (String.valueOf(nombreUsuario.charAt(1)).toLowerCase());
            //Si hay 2 o 3 palabras, se toma la primera letra de la primera y de la segunda, entendienose que la segunda palabra debería ser el apellido
            case 2, 3 -> initials = ((String.valueOf(partsName[0].charAt(0))) + (nombreUsuario.charAt(partsName[1].charAt(0)))).toUpperCase();
            //Si hay 4 o más palabras, se toma la primera letra de la primer palabra y la primera letra de la tercer palabra
            default -> initials = ((String.valueOf(partsName[0].charAt(0))) + (nombreUsuario.charAt(partsName[2].charAt(0)))).toUpperCase();
         }

         JPopupMenu popupMenu       = new JPopupMenu();
         JMenuItem  modificarPerfil = new JMenuItem("Ver Perfil"); //TODO Validar si es mas conveniente mostrar el nombre completo, solo el primer nombre o "Ver Perfil"
         JMenuItem  cerrarSesion    = new JMenuItem("LogOut");
         modificarPerfil.addActionListener(_ -> {
            DialogPerfil dialogPerfil = new DialogPerfil(evento, usuario);
            dialogPerfil.setVisible(true);
         });

         popupMenu.add(modificarPerfil);

         if (usuario.getTipoUsuario() == Usuario.ROLES.ADMIN) {
            JMenuItem historialDeCompras = new JMenuItem("Ver Compras");
            historialDeCompras.addActionListener(_ -> {
               //new DialogHistorialCompras(evento);
            });
            popupMenu.add(historialDeCompras);
         }
         cerrarSesion.addActionListener(_ -> {
            mostrarDialogoCerrarSesion();
         });
         popupMenu.add(cerrarSesion);
      }
   }

   private void mostrarDialogoCerrarSesion () {
      JDialog dialogCerrrarSesion = new JDialog(new JFrame(), "¿Cerrar Sesión?", true);
      dialogCerrrarSesion.setLayout(new GridBagLayout());
      dialogCerrrarSesion.pack();
      dialogCerrrarSesion.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialogCerrrarSesion.setLocationRelativeTo(null);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets  = new Insets(5, 5, 5, 5);
      gbc.fill    = GridBagConstraints.BOTH;
      gbc.gridx   = 0;
      gbc.gridy   = 0;
      gbc.weighty = 0.45f;
      gbc.weightx = 0.9f;

      //Label de pregunta sobre cerrar sesión
      JLabel labelPregunta = new JLabel("¿Está seguro de que desea cerrar sesión?");
      labelPregunta.setHorizontalAlignment(JLabel.CENTER);
      labelPregunta.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));
      dialogCerrrarSesion.add(labelPregunta, gbc);

      //Botones de cerrar sesión y cancelar
      JButtonRojo  botonCerrarSesion = new JButtonRojo("Cerrar Sesión");
      JButtonVerde botonCancelar     = new JButtonVerde("Cancelar");

      //Definicion de evento para los botones
      botonCerrarSesion.setActionCommand(Evento.EVENTO.CERRAR_SESION.name());
      botonCerrarSesion.addActionListener(evento);
      botonCancelar.addActionListener(_ -> dialogCerrrarSesion.dispose());

      gbc.gridy   = 1;
      gbc.weightx = 0.45f;
      dialogCerrrarSesion.add(botonCancelar, gbc);

      gbc.gridx = 1;
      dialogCerrrarSesion.add(botonCerrarSesion, gbc);
   }
}