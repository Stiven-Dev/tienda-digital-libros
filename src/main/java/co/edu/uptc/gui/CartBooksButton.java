package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

public class CartBooksButton extends JButton {
   private        String texto = "Carrito";
   private static int    count = 0;

   public CartBooksButton () {
      Dimension iconSize = new Dimension(25, 25);
      setPreferredSize(iconSize);
      setMaximumSize(iconSize);
      setContentAreaFilled(false);
      setFocusPainted(false);
   }

   public static void setCount (int cantidad) {
      count = cantidad;
   }

   @Override protected void paintComponent (Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(new Color(118, 118, 143));
      g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
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