package co.edu.uptc.gui;

import javax.swing.*;

public class DialogFacturaCompra extends JDialog {
   private final Evento evento;

   public DialogFacturaCompra (Evento evento) {
      super(new JFrame(), "Factura de Compra", true);
      this.evento = evento;
      initComponents();
      pack();
      setResizable(false);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   private void initComponents () {

   }
}