package co.edu.uptc.gui;

import java.awt.*;

/**
 * Botón verde personalizado que hereda de JButtonColor.
 */
public class JButtonVerde extends JButtonColor {
   /**
    * Crea un botón verde con el texto dado.
    *
    * @param text texto a mostrar en el botón
    */
   public JButtonVerde (String text) {
      super(text, new Color(107, 182, 107), new Color(120, 200, 120, 150));
   }
}