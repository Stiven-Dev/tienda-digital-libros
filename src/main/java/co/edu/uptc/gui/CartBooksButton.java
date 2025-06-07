package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Botón personalizado para mostrar el carrito de compras en la interfaz gráfica.
 * Muestra el texto "Carrito" y, si hay elementos en el carrito, muestra la cantidad entre corchetes.
 */
public class CartBooksButton extends JButton {
   // Contador estático de libros en el carrito
   private static int    count = 0;
   // Texto mostrado en el botón
   private        String texto = "Carrito";

   /**
    * Constructor que configura el tamaño y apariencia básica del botón.
    */
   public CartBooksButton () {
      Dimension iconSize = new Dimension(25, 25);
      setPreferredSize(iconSize);
      setMaximumSize(iconSize);
      setContentAreaFilled(false);
      setFocusPainted(false);
   }

   /**
    * Establece la cantidad de libros en el carrito.
    * @param cantidad número de libros
    */
   public static void setCount (int cantidad) {
      count = cantidad;
   }

   /**
    * Dibuja el botón con el texto y la cantidad de libros si corresponde.
    */
   @Override protected void paintComponent (Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(new Color(118, 118, 143));
      g2.fillRoundRect(0, 0, getWidth(), getHeight(), 2, 2);
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));

      FontMetrics fontMetrics = g2.getFontMetrics();
      int         width       = fontMetrics.stringWidth(texto);
      int         ascent      = fontMetrics.getAscent();
      int         x           = (getWidth() - width) / 2;
      int         y           = (getHeight() + ascent) / 2 - 2;
      g2.drawString(texto, x, y);
      g2.dispose();
      super.paintComponent(g);
      revalidate();
      repaint();
   }

   /**
    * Cambia el texto del botón según la cantidad de libros en el carrito.
    * Si la cantidad es 0, muestra solo "Carrito".
    * @param text texto a mostrar (debe ser un número)
    */
   @Override public void setText (String text) {
      final String regexNumber = "^\\d+$";
      if (text.matches(regexNumber)) {
         setCount(Integer.parseInt(text));
         if (count < 1) {
            texto = "Carrito";
         } else {
            texto = String.format("Carrito [%d]", count);
         }
      } else {
         texto = text;
      }
      paintComponent(getGraphics());
   }
}