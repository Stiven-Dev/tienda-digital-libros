package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.Libro;
import co.edu.uptc.model.Tienda;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Diálogo para confirmar la compra de los libros seleccionados en el carrito.
 * Muestra el resumen de la compra, permite confirmar o cancelar y aplica descuentos según el método de pago.
 */
public class DialogConfirmarCompra extends JDialog {
   // Referencia a la ventana principal
   private final VentanaPrincipal       ventanaPrincipal;
   // Lista de artículos (ISBN y cantidad)
   private final HashMap<Long, Integer> listaArticulos;
   // Método de pago seleccionado
   private final Compra.METODO_PAGO     metodoPago;
   // Porcentaje de descuento aplicado
   private final double                 porcentajeDescuento;
   // Modelo de la tabla de artículos
   private final DefaultTableModel      model = getDefaultTableModel();
   // Valor total y cantidad total de la compra
   private       double                 valorTotalCompra;
   private       int                    cantidadTotalCompra;

   /**
    * Constructor del diálogo de confirmación de compra.
    * @param ventanaPrincipal ventana principal de la aplicación
    * @param itemsCarrito artículos seleccionados en el carrito
    * @param metodoPago método de pago elegido
    * @param porcentajeDescuento descuento aplicado según el usuario
    */
   public DialogConfirmarCompra (VentanaPrincipal ventanaPrincipal, HashMap<Long, Integer> itemsCarrito, Compra.METODO_PAGO metodoPago, double porcentajeDescuento) {
      super(new JFrame(), "¿Confirmar Compra?", true);
      this.ventanaPrincipal    = ventanaPrincipal;
      this.listaArticulos      = itemsCarrito;
      this.metodoPago          = metodoPago;
      this.porcentajeDescuento = porcentajeDescuento;
      initComponents();
      setSize(1000, 800);
      setResizable(false);
      setLocationRelativeTo(ventanaPrincipal);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   /**
    * Inicializa los componentes gráficos del diálogo.
    */
   private void initComponents () {
      setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill    = GridBagConstraints.BOTH;
      gbc.insets  = new Insets(5, 5, 5, 5);
      gbc.gridx   = 0;
      gbc.gridy   = 0;
      gbc.weightx = 1;
      gbc.weighty = 0.1;
      String mensajeConfirmacion = String.format("¿Confirmar Compra con %s?", metodoPago.getName());
      JLabel labelTitulo         = new JLabel(mensajeConfirmacion, JLabel.CENTER);
      labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
      add(labelTitulo, gbc);
      gbc.gridy++;
      gbc.weighty = 0.5;

      JTable tableArticulos = new JTable(model);
      tableArticulos.setDefaultRenderer(Object.class, celdasDoubleFormateadas());
      JScrollPane scrollPane = new JScrollPane(tableArticulos);
      add(scrollPane, gbc);
      rellenarTabla();
      gbc.gridy++;
      gbc.weighty = 0.15;

      JPanel panelResumenCompra = new JPanel(new GridLayout(2, 1, 5, 5));
      panelResumenCompra.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "Resumen de la Compra", TitledBorder.CENTER, TitledBorder.TOP));
      String textoCantidadTotalCompra = String.format("Cantidad de artículos: %d", cantidadTotalCompra);
      String textoTotalCompra         = String.format("Total de la Compra $%,.2f", valorTotalCompra);
      JLabel labelCantidadCompra      = new JLabel(textoCantidadTotalCompra, JLabel.RIGHT);
      JLabel labelTotalCompra         = new JLabel(textoTotalCompra, JLabel.RIGHT);
      labelCantidadCompra.setFont(new Font("Arial", Font.BOLD, 14));
      labelTotalCompra.setFont(new Font("Arial", Font.BOLD, 14));
      panelResumenCompra.add(labelCantidadCompra);
      panelResumenCompra.add(labelTotalCompra);
      add(panelResumenCompra, gbc);

      gbc.gridy++;
      gbc.weightx = 0.6;
      JPanel  panelBotones   = new JPanel(new GridLayout(1, 2, 10, 5));
      Font    fuenteBoton    = new Font("Lucida Sans Unicode", Font.BOLD, 15);
      JButton botonConfirmar = new JButtonVerde("Confirmar");
      JButton botonCancelar  = new JButtonRojo("Cancelar");
      botonConfirmar.setAction(new AbstractAction() {
         @Override public void actionPerformed (ActionEvent e) {
            ventanaPrincipal.setRespuesta(true);
            dispose();
         }
      });

      botonCancelar.setAction(new AbstractAction() {
         @Override public void actionPerformed (ActionEvent e) {
            ventanaPrincipal.setRespuesta(false);
            dispose();
         }
      });
      panelBotones.add(botonCancelar);
      panelBotones.add(botonConfirmar);
      botonConfirmar.setFont(fuenteBoton);
      botonCancelar.setFont(fuenteBoton);
      add(panelBotones, gbc);
      gbc.gridy++;
      gbc.weightx = 1;
      gbc.weighty = 0.1;
      JLabel labelNota = new JLabel("**Si algún libro no fue mostrado es porque fue eliminado del registro**", JLabel.CENTER);
      labelNota.setFont(new Font("Arial", Font.ITALIC, 12));
      add(labelNota, gbc);
   }

   /**
    * Rellena la tabla con los artículos seleccionados en el carrito.
    * Calcula el valor total y la cantidad total de la compra.
    */
   private void rellenarTabla () {
      if (listaArticulos.isEmpty()) {
         ventanaPrincipal.setRespuesta(false);
         dispose();
         return;
      }
      model.setRowCount(0);
      valorTotalCompra    = 0;
      cantidadTotalCompra = 0;
      for (Map.Entry<Long, Integer> entry : listaArticulos.entrySet()) {
         long     ISBN       = entry.getKey();
         int      cantidad   = entry.getValue();
         Libro    libro      = ventanaPrincipal.obtenerLibroMedianteISBN(ISBN);
         Object[] itemCompra = new Object[NOMBRE_COLUMNAS.values().length];
         if (libro == null) {
            Tienda.agregarLog("Libro no encontrado en la tienda: " + ISBN);
            continue;
         }
         double valorUnitario           = libro.getPrecioVenta();
         double valorBase               = valorUnitario / (1 + ventanaPrincipal.obtenerPorcentajeImpuesto(valorUnitario));
         double valorConDescuentoSinIVA = ventanaPrincipal.obtenerValorConDescuentoSinIva(valorBase, porcentajeDescuento);
         double valorImpuesto           = ventanaPrincipal.obtenerPrecioImpuesto(valorConDescuentoSinIVA);
         double valorFinal              = valorConDescuentoSinIVA + valorImpuesto;
         double totalArticulo           = valorFinal * cantidad;
         itemCompra[NOMBRE_COLUMNAS.TITULO.getIndex()]                   = libro.getTitulo();
         itemCompra[NOMBRE_COLUMNAS.VALOR_BASE_CON_DESCUENTO.getIndex()] = valorConDescuentoSinIVA;
         itemCompra[NOMBRE_COLUMNAS.VALOR_IMPUESTO.getIndex()]           = valorImpuesto;
         itemCompra[NOMBRE_COLUMNAS.VALOR_FINAL.getIndex()]              = valorFinal;
         itemCompra[NOMBRE_COLUMNAS.CANTIDAD.getIndex()]                 = cantidad;
         itemCompra[NOMBRE_COLUMNAS.VALOR_TOTAL.getIndex()]              = totalArticulo;
         model.addRow(itemCompra);
         cantidadTotalCompra += cantidad;
         valorTotalCompra += totalArticulo;
      }
   }

   /**
    * Crea y retorna el modelo de tabla por defecto para los artículos de la compra.
    * @return DefaultTableModel configurado para la tabla de artículos
    */
   private DefaultTableModel getDefaultTableModel () {
      return new DefaultTableModel(NOMBRE_COLUMNAS.getCabecera(), 0) {
         @Override public boolean isCellEditable (int row, int column) {
            return false;
         }

         @Override public Class<?> getColumnClass (int columnIndex) {
            return switch (NOMBRE_COLUMNAS.values()[columnIndex]) {
               case VALOR_BASE_CON_DESCUENTO, VALOR_IMPUESTO, VALOR_FINAL, VALOR_TOTAL -> double.class;
               case CANTIDAD -> int.class;
               default -> String.class;
            };
         }
      };
   }

   /**
    * Crea y retorna un renderizador de celdas para formatear los valores double como moneda.
    * @return DefaultTableCellRenderer personalizado
    */
   private DefaultTableCellRenderer celdasDoubleFormateadas () {
      return new DefaultTableCellRenderer() {
         @Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 1 || column == 2 || column == 3 || column == 5) {
               value = String.format("$%,.2f", (double) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      };
   }

   /**
    * Enum que define los nombres y el orden de las columnas de la tabla de compra.
    */
   enum NOMBRE_COLUMNAS {
      TITULO(0, "Título"),
      VALOR_BASE_CON_DESCUENTO(1, "Vlr"),
      VALOR_IMPUESTO(2, "Vlr Impuesto"),
      VALOR_FINAL(3, "Vlr Final"),
      CANTIDAD(4, "Cantidad"),
      VALOR_TOTAL(5, "Vlr Total");
      private final int    index;
      private final String name;

      NOMBRE_COLUMNAS (int index, String name) {
         this.index = index;
         this.name  = name;
      }

      static String[] getCabecera () {
         String[] cabecera = new String[NOMBRE_COLUMNAS.values().length];
         for (NOMBRE_COLUMNAS columna : NOMBRE_COLUMNAS.values()) {
            cabecera[columna.getIndex()] = columna.getName();
         }
         return cabecera;
      }

      int getIndex () {
         return index;
      }

      String getName () {
         return name;
      }
   }
}