package co.edu.uptc.gui;

import javax.swing.*;

/**
 * Diálogo que muestra la factura de una compra realizada.
 * Permite visualizar los detalles de la compra al usuario.
 */
public class DialogFacturaCompra extends JDialog {
   /**
    * Referencia al manejador de eventos de la aplicación.
    */
   private final Evento evento;

   /**
    * Constructor del diálogo de factura de compra.
    *
    * @param evento manejador de eventos
    */
   public DialogFacturaCompra (Evento evento) {
      super(new JFrame(), "Factura de Compra", true);
      this.evento = evento;
      initComponents();
      pack();
      setResizable(false);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   /**
    * Inicializa los componentes gráficos del diálogo de factura.
    */
   private void initComponents () {

   }
}