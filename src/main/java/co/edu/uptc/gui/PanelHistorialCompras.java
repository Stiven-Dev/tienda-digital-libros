package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHistorialCompras extends JPanel {
   private final VentanaPrincipal  ventanaPrincipal;
   private final Font              fuenteCabecera = new Font("Arial", Font.BOLD, 15);
   private final Font              fuenteCelda    = new Font("Lucida Sans Unicode", Font.PLAIN, 15);
   private final String[]          nombreColumnas = {"#", "Cantidad", "Valor", "Fecha"};
   private       DefaultTableModel model;
   private final int               ID;

   public PanelHistorialCompras (VentanaPrincipal ventanaPrincipal, int ID) {
      this.ventanaPrincipal = ventanaPrincipal;
      this.ID               = ID;
      setLayout(new BorderLayout());
      inicializarPanel();
   }

   private void inicializarPanel () {
      model = getDefaultTableModel();
      rellenarTablaCompras();
      JTable tableCompras = new JTable(model);
      tableCompras.getColumnModel().getColumn(2).setCellRenderer(celdasFormateadas());
      tableCompras.getTableHeader().setFont(fuenteCabecera);
      tableCompras.setFont(fuenteCelda);
      JScrollPane scrollPane = new JScrollPane(tableCompras);
      add(scrollPane, BorderLayout.CENTER);
   }

   private DefaultTableModel getDefaultTableModel () {
      return new DefaultTableModel(nombreColumnas, 0) {
         @Override public boolean isCellEditable (int row, int column) {
            return false;
         }

         @Override public Class<?> getColumnClass (int columna) {
            return switch (columna) {
               case 0 -> long.class;
               case 1 -> int.class;
               case 2 -> double.class;
               default -> String.class;
            };
         }
      };
   }

   private DefaultTableCellRenderer celdasFormateadas () {
      return new DefaultTableCellRenderer() {
         @Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 2) {
               value = String.format("$%,.2f", (double) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      };
   }

   private void rellenarTablaCompras () {
      model.setDataVector(ventanaPrincipal.obtenerListaCompras(ID), nombreColumnas);
   }
}