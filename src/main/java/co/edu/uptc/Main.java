package co.edu.uptc;

import co.edu.uptc.gui.VentanaPrincipal;
import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
   public static void main (String[] args) {
      FlatDarculaLaf.setup();
      new VentanaPrincipal();
   }
}