package co.edu.uptc.dao;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.DetalleCompra;
import co.edu.uptc.model.Tienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * DAO para operaciones relacionadas con la generación y visualización de facturas.
 * Extiende JDialog para mostrar la factura de una compra específica.
 */
public class FacturaDAO extends JDialog {
   /**
    * Instancia de la tienda para obtener datos y cálculos.
    */
   private final Tienda             tienda;
   /**
    * Compra seleccionada para mostrar en la factura.
    */
   private final Compra             compraSeleccionada;
   private       double             subTotal                     = 0;
   private       double             subTotalSinIVA               = 0;
   private       double             valorTotalDescuento          = 0;
   private       double             valorTotalConDescuentoSinIVA = 0;
   private       double             valorTotalImpuesto           = 0;
   private       double             total                        = 0;
   private       Compra.METODO_PAGO metodoPago                   = Compra.METODO_PAGO.EFECTIVO;
   private       JPanel             panelHeader;
   private       JPanel             panelArticulos;
   private       JPanel             panelPago;
   private       JPanel             panelFooter;

   /**
    * Constructor de FacturaDAO.
    *
    * @param tienda             Instancia de Tienda para cálculos y datos.
    * @param compraSeleccionada Compra a mostrar en la factura.
    */
   public FacturaDAO (Tienda tienda, Compra compraSeleccionada) {
      super(new Frame(), String.format("Factura #%d", compraSeleccionada.getIDcompra()), true);
      this.tienda             = tienda;
      this.compraSeleccionada = compraSeleccionada;
      initComponents();
      setSize(1000, 800);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   /**
    * Inicializa los componentes gráficos de la factura.
    */
   private void initComponents () {
      setLayout(new GridBagLayout());
      inicializarHeader();
      inicializarPanelArticulos();
      inicializarPanelTotales();
      inicializarPanelFooter();

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill    = GridBagConstraints.BOTH;
      gbc.insets  = new Insets(0, 5, 0, 5);
      gbc.gridx   = 0;
      gbc.gridy   = 0;
      gbc.weightx = 1;
      gbc.weighty = 0.15f;
      add(panelHeader, gbc);
      gbc.gridy++;
      gbc.weighty = 0.6f;
      add(panelArticulos, gbc);
      gbc.gridy++;
      gbc.weighty = 0.2f;
      add(panelPago, gbc);
      gbc.gridy++;
      gbc.weighty = 0.05f;
      add(panelFooter, gbc);
   }

   /**
    * Inicializa el panel de cabecera de la factura.
    */
   private void inicializarHeader () {
      panelHeader = new JPanel(new GridBagLayout());
      GridBagConstraints gbcHeader = new GridBagConstraints();
      gbcHeader.fill    = GridBagConstraints.BOTH;
      gbcHeader.insets  = new Insets(0, 5, 0, 5);
      gbcHeader.gridx   = 0;
      gbcHeader.gridy   = 0;
      gbcHeader.weightx = 1;
      gbcHeader.weighty = 0.3f;
      JLabel labelTitulo = new JLabel("Tienda Digital de Libros - Rico Gómez", JLabel.CENTER);
      labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
      JLabel labelNIT = new JLabel("NIT: 202411094-0", JLabel.CENTER);
      labelNIT.setFont(new Font("Arial", Font.PLAIN, 12));
      JLabel labelNumeroTelefono = new JLabel("(+57)3157135314", JLabel.CENTER);
      labelNumeroTelefono.setFont(new Font("Arial", Font.PLAIN, 12));
      JLabel labelFecha = new JLabel(getFecha(), JLabel.CENTER);
      labelFecha.setFont(new Font("Arial", Font.ITALIC, 10));
      panelHeader.add(labelTitulo, gbcHeader);
      gbcHeader.gridy++;
      gbcHeader.weighty = 0.25f;
      panelHeader.add(labelNIT, gbcHeader);
      gbcHeader.gridy++;
      gbcHeader.weighty = 0.25f;
      panelHeader.add(labelNumeroTelefono, gbcHeader);
      gbcHeader.gridy++;
      gbcHeader.weighty = 0.2f;
      panelHeader.add(labelFecha, gbcHeader);
   }

   /**
    * Inicializa el panel de artículos de la factura.
    */
   private void inicializarPanelArticulos () {
      DefaultTableModel model          = getDefaultTableModel();
      JTable            tableArticulos = new JTable(model);
      tableArticulos.setShowGrid(false);
      tableArticulos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      tableArticulos.getTableHeader().setReorderingAllowed(false);
      JScrollPane scrollPane = new JScrollPane(tableArticulos);

      panelArticulos = new JPanel(new GridBagLayout());
      GridBagConstraints gbcArticulos = new GridBagConstraints();
      gbcArticulos.fill    = GridBagConstraints.BOTH;
      gbcArticulos.insets  = new Insets(5, 0, 5, 0);
      gbcArticulos.gridx   = 0;
      gbcArticulos.gridy   = 0;
      gbcArticulos.weightx = 1;
      gbcArticulos.weighty = 1;
      panelArticulos.add(scrollPane, gbcArticulos);
      rellenarTableArticulos(model);
   }

   /**
    * Inicializa el panel de totales y métodos de pago.
    */
   private void inicializarPanelTotales () {
      panelPago = new JPanel(new GridBagLayout());
      GridBagConstraints gbcTotales = new GridBagConstraints();
      gbcTotales.fill    = GridBagConstraints.BOTH;
      gbcTotales.insets  = new Insets(5, 5, 5, 5);
      gbcTotales.weightx = 0.35f;
      gbcTotales.weighty = 0.1f;

      Font   fuenteLabelLeft                 = new Font("Arial", Font.BOLD, 11);
      JLabel labelTituloSubTotal             = new JLabel("SubTotal", JLabel.LEFT);
      JLabel labelTituloSubTotalSinIVA       = new JLabel("SubTotal -IVA", JLabel.LEFT);
      JLabel labelTituloDescuentoAplicado    = new JLabel("Valor Total Descontado", JLabel.LEFT);
      JLabel labelTituloSubTotalConDescuento = new JLabel("SubTotal -Descuento**", JLabel.LEFT);
      JLabel labelTituloImpuesto             = new JLabel("Impuesto", JLabel.LEFT);
      JLabel labelTituloTotalFinal           = new JLabel("Total", JLabel.LEFT);
      JLabel labelTituloMetodoPago           = new JLabel("Método de Pago", JLabel.LEFT);
      labelTituloSubTotal.setFont(fuenteLabelLeft);
      labelTituloSubTotalSinIVA.setFont(fuenteLabelLeft);
      labelTituloDescuentoAplicado.setFont(fuenteLabelLeft);
      labelTituloSubTotalConDescuento.setFont(fuenteLabelLeft);
      labelTituloImpuesto.setFont(fuenteLabelLeft);
      labelTituloTotalFinal.setFont(fuenteLabelLeft);
      labelTituloMetodoPago.setFont(fuenteLabelLeft);

      Font   fuenteLabelRight          = new Font("Lucida Sans Unicode", Font.PLAIN, 11);
      JLabel labelSubTotal             = new JLabel(formatearDouble(subTotal), JLabel.RIGHT);
      JLabel labelSubTotalSinIVA       = new JLabel(formatearDouble(subTotalSinIVA), JLabel.RIGHT);
      JLabel labelDescuentoAplicado    = new JLabel(formatearDouble(valorTotalDescuento), JLabel.RIGHT);
      JLabel labelSubTotalConDescuento = new JLabel(formatearDouble(valorTotalConDescuentoSinIVA), JLabel.RIGHT);
      JLabel labelImpuesto             = new JLabel(formatearDouble(valorTotalImpuesto), JLabel.RIGHT);
      JLabel labelTotalFinal           = new JLabel(formatearDouble(total), JLabel.RIGHT);
      JLabel labelMetodoPago           = new JLabel(metodoPago.getName(), JLabel.RIGHT);
      labelSubTotal.setFont(fuenteLabelRight);
      labelSubTotalSinIVA.setFont(fuenteLabelRight);
      labelDescuentoAplicado.setFont(fuenteLabelRight);
      labelSubTotalConDescuento.setFont(fuenteLabelRight);
      labelImpuesto.setFont(fuenteLabelRight);
      labelTotalFinal.setFont(fuenteLabelRight);
      labelMetodoPago.setFont(fuenteLabelRight);

      //Titulos (Left)
      gbcTotales.gridx = 0;
      gbcTotales.gridy = 0;
      panelPago.add(labelTituloSubTotal, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelTituloSubTotalSinIVA, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelTituloDescuentoAplicado, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelTituloSubTotalConDescuento, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelTituloImpuesto, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelTituloTotalFinal, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelTituloMetodoPago, gbcTotales);

      //Valores (Right)
      gbcTotales.gridx = 1;
      gbcTotales.gridy = 0;
      panelPago.add(labelSubTotal, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelSubTotalSinIVA, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelDescuentoAplicado, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelSubTotalConDescuento, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelImpuesto, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelTotalFinal, gbcTotales);
      gbcTotales.gridy++;
      panelPago.add(labelMetodoPago, gbcTotales);
   }

   /**
    * Inicializa el panel de pie de página de la factura.
    */
   private void inicializarPanelFooter () {
      panelFooter = new JPanel(new GridBagLayout());
      GridBagConstraints gbcFooter = new GridBagConstraints();
      gbcFooter.fill    = GridBagConstraints.BOTH;
      gbcFooter.insets  = new Insets(5, 5, 5, 5);
      gbcFooter.gridx   = 0;
      gbcFooter.gridy   = 0;
      gbcFooter.weightx = 0.35f;

      gbcFooter.weighty = 0.25f;
      JLabel label1            = new JLabel("*Valor Estandar con IVA Incluído", JLabel.CENTER);
      JLabel label2            = new JLabel("**Descuento aplicado a valor sin IVA", JLabel.CENTER);
      Font   fuenteLabelFooter = new Font("Arial", Font.ITALIC, 10);
      label1.setFont(fuenteLabelFooter);
      label2.setFont(fuenteLabelFooter);
      panelFooter.add(label1, gbcFooter);
      gbcFooter.gridy++;
      panelFooter.add(label2, gbcFooter);
      gbcFooter.gridy++;

      gbcFooter.weighty = 0.5f;
      JButton botonOK = new JButton("OK");
      botonOK.addActionListener(_ -> dispose());
      panelFooter.add(botonOK, gbcFooter);
   }

   /**
    * Obtiene la fecha de la compra en formato String.
    *
    * @return Fecha de la compra formateada.
    */
   public String getFecha () {
      return compraSeleccionada.getFechaCompra();
   }

   /**
    * Crea el modelo de tabla para los artículos de la factura.
    *
    * @return Modelo de tabla para JTable.
    */
   private DefaultTableModel getDefaultTableModel () {
      return new DefaultTableModel(NOMBRE_COLUMNAS.getCabecera(), 0) {
         @Override public boolean isCellEditable (int row, int column) {
            return false;
         }

         @Override public Class<?> getColumnClass (int columnIndex) {
            return switch (columnIndex) {
               case 1, 2, 3, 4, 5, 7 -> double.class;
               case 6 -> int.class;
               default -> String.class;
            };
         }
      };
   }

   /**
    * Llena la tabla de artículos con los datos de la compra seleccionada.
    *
    * @param model Modelo de tabla a rellenar.
    */
   private void rellenarTableArticulos (DefaultTableModel model) {
      double porcentajeDescuento = compraSeleccionada.getPorcentajeDescuento();
      for (DetalleCompra articulo : compraSeleccionada.getLibrosComprados()) {
         Object[] row = new Object[NOMBRE_COLUMNAS.values().length];
         row[NOMBRE_COLUMNAS.TITULO.getIndex()] = articulo.getTitulo();
         int cantidad = articulo.getCantidad();
         row[NOMBRE_COLUMNAS.CANTIDAD.getIndex()] = cantidad;

         double valorUnitario           = articulo.getValorUnitario();
         double valorBase               = valorUnitario / (1 + tienda.calcularPorcentajeImpuesto(valorUnitario));
         double valorConDescuentoSinIVA = tienda.obtenerValorConDescuentoSinIva(valorBase, porcentajeDescuento);
         double valorDescuentoAplicado  = valorBase - valorConDescuentoSinIVA;
         double valorImpuesto           = tienda.calcularValorImpuesto(valorConDescuentoSinIVA);
         double valorConDescuentoConIVA = tienda.obtenerValorConDescuentoConIva(valorBase, porcentajeDescuento);
         double totalArticulo           = valorConDescuentoConIVA * cantidad;

         row[NOMBRE_COLUMNAS.VALOR_UNITARIO.getIndex()]        = formatearDouble(valorUnitario);
         row[NOMBRE_COLUMNAS.PORCENTAJE_DESCUENTO.getIndex()]  = String.format("-%.1f%%", porcentajeDescuento * 100);
         row[NOMBRE_COLUMNAS.VALOR_MENOS_DESCUENTO.getIndex()] = formatearDouble(valorConDescuentoSinIVA);
         row[NOMBRE_COLUMNAS.VALOR_IVA.getIndex()]             = "+" + formatearDouble(valorImpuesto);
         row[NOMBRE_COLUMNAS.VALOR_FINAL.getIndex()]           = formatearDouble(valorConDescuentoConIVA);
         row[NOMBRE_COLUMNAS.TOTAL.getIndex()]                 = formatearDouble(totalArticulo);

         subTotal += valorUnitario * cantidad;
         subTotalSinIVA += valorBase * cantidad;
         //En este orden, aquí irá el descuento aplicado
         valorTotalDescuento += valorDescuentoAplicado * cantidad;
         valorTotalConDescuentoSinIVA += valorConDescuentoSinIVA * cantidad;
         valorTotalImpuesto += valorImpuesto * cantidad;
         total += valorConDescuentoConIVA * cantidad;
         metodoPago = compraSeleccionada.getMetodoPago();
         model.addRow(row);
      }
   }

   /**
    * Formatea un valor double a String con formato monetario.
    *
    * @param valor Valor a formatear.
    *
    * @return String formateado como valor monetario.
    */
   private String formatearDouble (double valor) {
      return String.format("$%,.2f", valor);
   }

   /**
    * Enum para los nombres y posiciones de las columnas de la tabla de la factura.
    */
   private enum NOMBRE_COLUMNAS {
      TITULO(0, "Titulo"),
      VALOR_UNITARIO(1, "Valor*"),
      PORCENTAJE_DESCUENTO(2, "% Descuento"),
      VALOR_MENOS_DESCUENTO(3, "Valor -Descuento**"),
      VALOR_IVA(4, "IVA"),
      VALOR_FINAL(5, "Valor Final"),
      CANTIDAD(6, "Cantidad"),
      TOTAL(7, "Total");
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

      String getName () {
         return name;
      }

      int getIndex () {
         return index;
      }
   }
}