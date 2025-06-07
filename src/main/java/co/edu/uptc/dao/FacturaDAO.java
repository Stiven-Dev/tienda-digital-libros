package co.edu.uptc.dao;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.DetalleCompra;
import co.edu.uptc.model.Tienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FacturaDAO extends JDialog {
   private final Tienda             tienda;
   private final Compra             compraSeleccionada;
   private       double             subTotal   = 0;
   private       double              valorTotalDescuento          = 0;
   private       double              valorTotalConDescuentoSinIVA = 0;
   private       double              valorTotalImpuesto           = 0;
   private       double             total      = 0;
   private       Compra.METODO_PAGO metodoPago = Compra.METODO_PAGO.EFECTIVO;
   private       JPanel             panelHeader;
   private       JPanel              panelArticulos;
   private       JPanel              panelPago;
   private       JPanel              panelFooter;

   public FacturaDAO (Tienda tienda, Compra compraSeleccionada) {
      super(new Frame(), "Factura", true);
      this.tienda             = tienda;
      this.compraSeleccionada = compraSeleccionada;
      initComponents();
      setSize(800, 600);
      //pack();
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   private void initComponents () {
      setLayout(new GridBagLayout());
      inicializarHeader();
      inicializarPanelArticulos();
      inicializarPanelTotales();
      inicializarPanelFooter();

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill    = GridBagConstraints.BOTH;
      gbc.insets  = new Insets(5, 5, 5, 5);
      gbc.gridx   = 0;
      gbc.gridy   = 0;
      gbc.weightx = 1;
      gbc.weighty = 0.1f;
      add(panelHeader, gbc);
      gbc.gridy++;
      gbc.weighty = 0.7f;
      add(panelArticulos, gbc);
      gbc.gridy++;
      gbc.weighty = 0.15f;
      add(panelPago, gbc);
      gbc.gridy++;
      gbc.weighty = 0.05f;
      add(panelFooter, gbc);
   }

   private void inicializarHeader () {
      panelHeader = new JPanel(new GridBagLayout());
      GridBagConstraints gbcHeader = new GridBagConstraints();
      gbcHeader.fill    = GridBagConstraints.BOTH;
      gbcHeader.insets  = new Insets(5, 5, 5, 5);
      gbcHeader.gridx   = 0;
      gbcHeader.gridy   = 0;
      gbcHeader.weightx = 1;
      gbcHeader.weighty = 0.3f;
      JLabel labelTitulo = new JLabel("Tienda Digital de Libros - Rico Gómez", JLabel.CENTER);
      labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
      JLabel labelNIT = new JLabel("NIT: 202411094-0", JLabel.CENTER);
      labelNIT.setFont(new Font("Arial", Font.PLAIN, 16));
      JLabel labelNumeroTelefono = new JLabel("(+57)3157135314", JLabel.CENTER);
      labelNumeroTelefono.setFont(new Font("Arial", Font.PLAIN, 16));
      JLabel labelFecha = new JLabel(getFecha(), JLabel.CENTER);
      labelFecha.setFont(new Font("Arial", Font.ITALIC, 13));
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

   private void inicializarPanelTotales () {
      panelPago = new JPanel(new GridBagLayout());
      GridBagConstraints gbcPago = new GridBagConstraints();
      gbcPago.fill    = GridBagConstraints.BOTH;
      gbcPago.insets  = new Insets(5, 5, 5, 5);
      gbcPago.weightx = 0.35f;
      gbcPago.weighty = 0.15f;

      Font   fuenteLabelLeft                 = new Font("Arial", Font.BOLD, 14);
      JLabel labelTituloSubTotal             = new JLabel("SubTotal*", JLabel.LEFT);
      JLabel labelTituloDescuentoAplicado    = new JLabel("Valor Total Descontado", JLabel.LEFT);
      JLabel labelTituloSubTotalConDescuento = new JLabel("SubTotal -Descuento**", JLabel.LEFT);
      JLabel labelTituloImpuesto             = new JLabel("Impuesto", JLabel.LEFT);
      JLabel labelTituloTotalFinal           = new JLabel("Total", JLabel.LEFT);
      JLabel labelTituloMetodoPago           = new JLabel("Método de Pago", JLabel.LEFT);
      labelTituloSubTotal.setFont(fuenteLabelLeft);
      labelTituloDescuentoAplicado.setFont(fuenteLabelLeft);
      labelTituloSubTotalConDescuento.setFont(fuenteLabelLeft);
      labelTituloImpuesto.setFont(fuenteLabelLeft);
      labelTituloTotalFinal.setFont(fuenteLabelLeft);
      labelTituloMetodoPago.setFont(fuenteLabelLeft);

      Font   fuenteLabelRight          = new Font("Lucida Sans Unicode", Font.PLAIN, 14);
      JLabel labelSubTotal             = new JLabel(formatearDouble(subTotal), JLabel.RIGHT);
      JLabel labelDescuentoAplicado    = new JLabel(formatearDouble(valorTotalDescuento), JLabel.RIGHT);
      JLabel labelSubTotalConDescuento = new JLabel(formatearDouble(valorTotalConDescuentoSinIVA), JLabel.RIGHT);
      JLabel labelImpuesto             = new JLabel(formatearDouble(valorTotalImpuesto), JLabel.RIGHT);
      JLabel labelTotalFinal           = new JLabel(formatearDouble(total), JLabel.RIGHT);
      JLabel labelMetodoPago           = new JLabel(metodoPago.getName(), JLabel.RIGHT);
      labelSubTotal.setFont(fuenteLabelRight);
      labelDescuentoAplicado.setFont(fuenteLabelRight);
      labelSubTotalConDescuento.setFont(fuenteLabelRight);
      labelImpuesto.setFont(fuenteLabelRight);
      labelTotalFinal.setFont(fuenteLabelRight);
      labelMetodoPago.setFont(fuenteLabelRight);

      //Titulos (Left)
      gbcPago.gridx = 0;
      gbcPago.gridy = 0;
      panelPago.add(labelTituloSubTotal, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelTituloDescuentoAplicado, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelTituloSubTotalConDescuento, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelTituloImpuesto, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelTituloTotalFinal, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelTituloMetodoPago, gbcPago);

      //Valores (Right)
      gbcPago.gridx = 1;
      gbcPago.gridy = 0;
      panelPago.add(labelSubTotal, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelDescuentoAplicado, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelSubTotalConDescuento, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelImpuesto, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelTotalFinal, gbcPago);
      gbcPago.gridy++;
      panelPago.add(labelMetodoPago, gbcPago);
   }

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
      Font   fuenteLabelFooter = new Font("Arial", Font.ITALIC, 12);
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

   public String getFecha () {
      return compraSeleccionada.getFechaCompra();
   }

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

   private void rellenarTableArticulos (DefaultTableModel model) {
      double porcentajeDescuento = compraSeleccionada.getPorcentajeDescuento();
      for (DetalleCompra articulo : compraSeleccionada.getLibrosComprados()) {
         Object[] row = new Object[NOMBRE_COLUMNAS.values().length];
         row[NOMBRE_COLUMNAS.TITULO.getIndex()] = articulo.getTitulo();
         int cantidad = articulo.getCantidad();
         row[NOMBRE_COLUMNAS.CANTIDAD.getIndex()] = cantidad;

         double valorUnitario           = articulo.getValorUnitario();
         double valorBase               = valorUnitario / (1 + tienda.calcularPorcentajeImpuesto(valorUnitario));
         double valorConDescuento       = tienda.obtenerValorConDescuentoSinIva(valorBase, porcentajeDescuento);
         double valorDescuentoAplicado  = valorBase - valorConDescuento;
         double valorImpuesto           = tienda.calcularValorImpuesto(valorConDescuento);
         double valorConDescuentoConIVA = tienda.obtenerValorConDescuentoConIva(valorBase, porcentajeDescuento);
         double totalArticulo           = valorConDescuentoConIVA * cantidad;

         row[NOMBRE_COLUMNAS.VALOR_UNITARIO.getIndex()]        = formatearDouble(valorUnitario);
         row[NOMBRE_COLUMNAS.PORCENTAJE_DESCUENTO.getIndex()]  = String.format("-%.1f%%", porcentajeDescuento * 100);
         row[NOMBRE_COLUMNAS.VALOR_MENOS_DESCUENTO.getIndex()] = formatearDouble(valorConDescuento);
         row[NOMBRE_COLUMNAS.VALOR_IVA.getIndex()]             = "+" + formatearDouble(valorImpuesto);
         row[NOMBRE_COLUMNAS.VALOR_FINAL.getIndex()]           = formatearDouble(valorConDescuentoConIVA);
         row[NOMBRE_COLUMNAS.TOTAL.getIndex()]                 = formatearDouble(totalArticulo);

         subTotal += valorUnitario * cantidad;
         //En este orden, aquí irá el descuento aplicado
         valorTotalDescuento += valorDescuentoAplicado * cantidad;
         valorTotalConDescuentoSinIVA += valorConDescuento * cantidad;
         valorTotalImpuesto += valorImpuesto * cantidad;
         total += valorConDescuentoConIVA * cantidad;
         metodoPago = compraSeleccionada.getMetodoPago();
         model.addRow(row);
      }
   }

   private String formatearDouble (double valor) {
      return String.format("$%,.2f", valor);
   }

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

      String getName () {
         return name;
      }

      int getIndex () {
         return index;
      }

      static String[] getCabecera () {
         String[] cabecera = new String[NOMBRE_COLUMNAS.values().length];
         for (NOMBRE_COLUMNAS columna : NOMBRE_COLUMNAS.values()) {
            cabecera[columna.getIndex()] = columna.getName();
         }
         return cabecera;
      }
   }
}