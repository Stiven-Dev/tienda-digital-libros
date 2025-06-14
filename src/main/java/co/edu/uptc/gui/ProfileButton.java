package co.edu.uptc.gui;

import co.edu.uptc.entity.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Botón personalizado para mostrar el perfil del usuario y acceder a opciones de cuenta.
 * Permite ver el perfil, historial de compras y cerrar sesión.
 */
public class ProfileButton extends JButton {
   /**
    * Referencia al manejador de eventos de la aplicación.
    */
   private final Evento                   evento;
   /**
    * Referencia a la ventana principal.
    */
   private final VentanaPrincipal         ventanaPrincipal;
   /**
    * Iniciales del usuario mostradas en el botón.
    */
   private       String                   initials = "N/R";
   /**
    * Diálogo para visualizar y actualizar el perfil.
    */
   private       DialogPerfil             dialogPerfil;
   /**
    * Diálogo para mostrar el historial de compras.
    */
   private       DialogHistorialCompras   dialogHistorialCompras;
   /**
    * Diálogo para crear, modificar y validar cuentas de usuario (Solo ADMIN).
    */
   private       DialogAdministrarCuentas dialogAdministrarCuentas;

   /**
    * Constructor del botón de perfil.
    *
    * @param ventanaPrincipal referencia a la ventana principal
    * @param evento           manejador de eventos
    */
   public ProfileButton (VentanaPrincipal ventanaPrincipal, Evento evento) {
      this.evento           = evento;
      this.ventanaPrincipal = ventanaPrincipal;
      this.setActionCommand(Evento.EVENTO.LOGIN_SIGNUP.name());
      this.addActionListener(evento);
      Dimension iconSize = new Dimension(20, 20);
      setPreferredSize(iconSize);
      setMaximumSize(iconSize);
      setContentAreaFilled(false);
      setFocusPainted(false);
      setOpaque(false);
   }

   /**
    * Dibuja el botón con las iniciales del usuario y el color personalizado.
    *
    * @param g contexto gráfico
    */
   @Override protected void paintComponent (Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      Color backgroundColor = new Color(208, 165, 49, 255);
      g2.setColor(backgroundColor);
      g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));
      FontMetrics fontMetrics = g2.getFontMetrics();
      int         textWidth   = fontMetrics.stringWidth(initials);
      int         textAscent  = fontMetrics.getAscent();
      int         x           = (getWidth() - textWidth) / 2;
      int         y           = (getHeight() + textAscent) / 2 - 2;
      g2.drawString(initials, x, y);
      g2.dispose();
      super.paintComponent(g);
   }

   /**
    * Inicia la sesión del usuario, muestra las iniciales y habilita el menú de opciones.
    *
    * @param usuario usuario autenticado
    */
   public void iniciarSesion (Usuario usuario) {
      removeActionListener(evento);
      String nombreUsuario = usuario.getNombreCompleto();
      if (nombreUsuario != null && !nombreUsuario.isBlank()) {
         String[] partsName = nombreUsuario.strip().split(" ");
         switch (partsName.length) {
            //Si solo hay una palabra, se toma la primera letra en mayúscula y la segunda letra en minúscula
            case 1 -> {
               if (nombreUsuario.length() < 2) {
                  initials = nombreUsuario.toUpperCase();
                  break;
               }
               initials = "" + Character.toUpperCase(nombreUsuario.charAt(0)) + Character.toLowerCase(nombreUsuario.charAt(1));
            }
            //Si hay 2 o 3 palabras, se toma la primera letra de la primera y de la segunda, entendienose que la segunda palabra debería ser el apellido
            case 2, 3 -> initials = ("" + partsName[0].charAt(0) + partsName[1].charAt(0)).toUpperCase();
            //Si hay 4 o más palabras, se toma la primera letra de la primer palabra y la primera letra de la tercer palabra
            default -> initials = ("" + partsName[0].charAt(0) + partsName[1].charAt(0)).toUpperCase();
         }
      }

      JPopupMenu popupMenu = getJPopupMenu(usuario);
      this.addActionListener(_ -> popupMenu.show(this, 0, this.getHeight()));
   }

   private JPopupMenu getJPopupMenu (Usuario usuario) {
      JPopupMenu popupMenu       = new JPopupMenu();
      JMenuItem  modificarPerfil = new JMenuItem("Ver Perfil");
      JMenuItem  cerrarSesion    = new JMenuItem("LogOut");
      modificarPerfil.addActionListener(_ -> {
         dialogPerfil = new DialogPerfil(evento, ventanaPrincipal.getUsuarioActual());
         dialogPerfil.refrescarDatosUsuario(ventanaPrincipal.getUsuarioActual());
         dialogPerfil.setVisible(true);
      });
      popupMenu.add(modificarPerfil);

      if (usuario.getTipoUsuario() == Usuario.ROLES.ADMIN) {
         JMenuItem administrarCuentas = new JMenuItem("Administrar Cuentas");
         administrarCuentas.addActionListener(_ -> {
            dialogAdministrarCuentas = new DialogAdministrarCuentas(evento);
            dialogAdministrarCuentas.setVisible(true);
         });
         popupMenu.add(administrarCuentas);
      } else {
         JMenuItem historialDeCompras = new JMenuItem("Ver Compras");
         historialDeCompras.addActionListener(_ -> {
            dialogHistorialCompras = new DialogHistorialCompras(ventanaPrincipal, evento);
            dialogHistorialCompras.refrescarLista();
            dialogHistorialCompras.setVisible(true);
         });
         popupMenu.add(historialDeCompras);
      }
      cerrarSesion.addActionListener(_ -> mostrarDialogoCerrarSesion());
      popupMenu.add(cerrarSesion);
      return popupMenu;
   }

   /**
    * Muestra el diálogo para confirmar el cierre de sesión.
    */
   private void mostrarDialogoCerrarSesion () {
      JDialog dialogCerrarSesion = new JDialog(new JFrame(), "¿Cerrar Sesión?", true);
      dialogCerrarSesion.setLayout(new BorderLayout());
      dialogCerrarSesion.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

      //Label de pregunta sobre cerrar sesión
      JLabel labelPregunta = new JLabel("¿Está seguro de que desea cerrar sesión?");
      labelPregunta.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));
      labelPregunta.setHorizontalAlignment(JLabel.CENTER);
      dialogCerrarSesion.add(labelPregunta, BorderLayout.NORTH);

      //Botones de cerrar sesión y cancelar
      JPanel             panelBotones = new JPanel(new GridBagLayout());
      GridBagConstraints gbc          = new GridBagConstraints();
      gbc.insets = new Insets(5, 5, 5, 5);
      gbc.fill   = GridBagConstraints.BOTH;
      JButtonRojo  botonCerrarSesion = new JButtonRojo("Cerrar Sesión");
      JButtonVerde botonCancelar     = new JButtonVerde("Cancelar");

      //Definicion de evento para los botones
      botonCerrarSesion.setAction(new AbstractAction() {
         @Override public void actionPerformed (ActionEvent e) {
            ventanaPrincipal.cerrarSesion();
            dialogCerrarSesion.dispose();
         }
      });
      botonCerrarSesion.setText("Cerrar Sesión");
      botonCancelar.addActionListener(_ -> dialogCerrarSesion.dispose());

      gbc.weighty = 1;
      gbc.weightx = 0.45f;
      gbc.gridy   = 0;
      gbc.gridx   = 0;
      panelBotones.add(botonCancelar, gbc);
      gbc.gridx = 1;
      panelBotones.add(botonCerrarSesion, gbc);

      dialogCerrarSesion.add(panelBotones, BorderLayout.SOUTH);
      dialogCerrarSesion.pack();
      dialogCerrarSesion.setLocationRelativeTo(null);
      dialogCerrarSesion.setVisible(true);
   }

   /**
    * Retorna el diálogo de perfil asociado al botón.
    *
    * @return diálogo de perfil
    */
   public DialogPerfil getDialogPerfil () {
      return dialogPerfil;
   }

   /**
    * Retorna el diálogo de historial de compras asociado al botón.
    *
    * @return diálogo de historial de compras
    */
   public DialogAdministrarCuentas getDialogAdministrarCuentas () {
      return dialogAdministrarCuentas;
   }
}