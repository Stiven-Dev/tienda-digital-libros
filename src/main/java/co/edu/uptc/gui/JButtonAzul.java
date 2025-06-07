package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

public class JButtonAzul extends JButton {
   private final Color defaultColor = new Color(73, 129, 175);
   private final Color pressedColor = new Color(128, 139, 206, 150);

   public JButtonAzul (String text) {
      super(text);
      setContentAreaFilled(false);
      setOpaque(false);
      setBorderPainted(false);
      setFocusPainted(false);
      setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));
   }

   @Override protected void paintComponent (Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      Color fillColor = getModel().isPressed() ? pressedColor : defaultColor;
      g2.setColor(fillColor);
      g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
      g2.dispose();
      super.paintComponent(g);
   }
}