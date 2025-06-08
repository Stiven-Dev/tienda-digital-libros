package co.edu.uptc.gui;

import java.awt.*;

/**
 * Botón rojo personalizado que hereda de JButtonColor.
 */
public class JButtonRojo extends JButtonColor {
   /**
    * Crea un botón rojo con el texto dado.
    *
    * @param text texto a mostrar en el botón
    */
   public JButtonRojo (String text) {
      super(text, new Color(200, 120, 120), new Color(200, 120, 120, 150));
   }
}