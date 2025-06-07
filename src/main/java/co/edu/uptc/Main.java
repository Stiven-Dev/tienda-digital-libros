package co.edu.uptc;

import co.edu.uptc.gui.VentanaPrincipal;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Clase principal que inicia la aplicación de la Tienda Digital de Libros.
 */
public class Main {
   /**
    * Metodo principal que configura el tema y lanza la ventana principal.
    * @param args Argumentos de línea de comandos (no utilizados).
    */
   public static void main (String[] args) {
      FlatDarkLaf.setup();
      new VentanaPrincipal();
   }
}