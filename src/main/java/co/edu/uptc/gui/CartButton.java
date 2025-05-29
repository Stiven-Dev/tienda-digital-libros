package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CartButton extends JButton {
   private int       count;
   private ImageIcon cartIcon;

   public CartButton () {
      this.count = 0;
      String iconPath = "src/cart.png";
      URL    resource = getClass().getResource(iconPath);
      if (resource != null) {
         cartIcon = new ImageIcon(resource);
      } else {
         System.err.println("Icono no encontrado: " + iconPath);
      }
      setPreferredSize(new Dimension(40, 40));
      setContentAreaFilled(false);
      setFocusPainted(false);
      setBorderPainted(false);
   }

   public void setCount (int count) {
      this.count = count;
      repaint();
   }

   @Override protected void paintComponent (Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      if (count == 0 && cartIcon != null) {
         int iconWidth  = cartIcon.getIconWidth();
         int iconHeight = cartIcon.getIconHeight();
         int x          = (getWidth() - iconWidth) / 2;
         int y          = (getHeight() - iconHeight) / 2;
         cartIcon.paintIcon(this, g2, x, y);
      } else {
         g2.setColor(new Color(120, 120, 200));
         g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
         g2.setColor(Color.WHITE);
         g2.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 16));
         String      text        = String.valueOf(count);
         FontMetrics fontMetrics = g2.getFontMetrics();
         int         textWidth   = fontMetrics.stringWidth(text);
         int         textAscent  = fontMetrics.getAscent();
         int         x           = (getWidth() - textWidth) / 2;
         int         y           = (getHeight() + textAscent) / 2 - 2;
         g2.drawString(text, x, y);
      }
      g2.dispose();
      super.paintComponent(g);
   }
}