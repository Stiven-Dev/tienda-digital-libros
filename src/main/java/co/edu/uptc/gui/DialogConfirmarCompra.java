package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;

import javax.swing.*;
import java.util.HashMap;

public class DialogConfirmarCompra extends JDialog {
   private final VentanaPrincipal       ventana;
   private final Evento                 evento;
   private final Compra.METODO_PAGO     metodoPago;
   private final HashMap<Long, Integer> listaArticulos;

   public DialogConfirmarCompra (VentanaPrincipal ventana, Evento evento, Compra.METODO_PAGO metodoPago, HashMap<Long, Integer> itemsCarrito) {
      super(new JFrame(), "¿Confirmar Compra?", true);
      this.ventana        = ventana;
      this.evento         = evento;
      this.metodoPago     = metodoPago;
      this.listaArticulos = itemsCarrito;
      initComponents();
      pack();
      setResizable(false);
      setLocationRelativeTo(ventana);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   private void initComponents () {

   }

   public boolean getRespuesta () {
      return true;
   }
}