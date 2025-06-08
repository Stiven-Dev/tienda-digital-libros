package co.edu.uptc.gui;

import java.awt.*;

/**
 * Botón azul personalizado que hereda de JButtonColor.
 */
public class JButtonAzul extends JButtonColor {
   /**
    * Crea un botón azul con el texto dado.
    *
    * @param text texto a mostrar en el botón
    */
   public JButtonAzul (String text) {
      super(text, new Color(73, 129, 175), new Color(128, 139, 206, 150));
   }
}