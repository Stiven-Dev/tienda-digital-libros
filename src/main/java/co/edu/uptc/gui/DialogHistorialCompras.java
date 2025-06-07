package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DialogHistorialCompras extends JDialog {
   private final VentanaPrincipal   ventanaPrincipal;
   private final Font               fuenteCabecera = new Font("Arial", Font.BOLD, 15);
   private final Font               fuenteCelda    = new Font("Lucida Sans Unicode", Font.PLAIN, 15);
   private final Evento            evento;
   private       ArrayList<Compra> compraLocales;
   private       DefaultTableModel model;
   private       Compra            compraSeleccionada;

   public DialogHistorialCompras (VentanaPrincipal ventanaPrincipal, Evento evento) {
      super(new Frame(), "Historial de Compras", true);
      this.ventanaPrincipal = ventanaPrincipal;
      this.evento           = evento;
      setLayout(new BorderLayout());
      inicializarPanel();
      setSize(800, 600);
      setResizable(false);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   private void inicializarPanel () {
      model = getDefaultTableModel();
      rellenarTablaCompras();
      JTable tableCompras = new JTable(model);
      tableCompras.getColumnModel().getColumn(1).setCellRenderer(celdasFormateadas());
      tableCompras.getTableHeader().setFont(fuenteCabecera);
      tableCompras.setFont(fuenteCelda);
      JScrollPane scrollPane = new JScrollPane(tableCompras);
      add(scrollPane, BorderLayout.CENTER);
      modificacionesCompras();
   }

   private DefaultTableModel getDefaultTableModel () {
      return new DefaultTableModel(NOMBRE_COLUMNAS.getCabecera(), 0) {
         @Override public boolean isCellEditable (int row, int column) {
            return column == 4;
         }

         @Override public Class<?> getColumnClass (int columna) {
            return switch (columna) {
               case 0 -> long.class;
               case 1 -> double.class;
               case 2 -> int.class;
               case 4 -> Boolean.class;
               default -> String.class;
            };
         }
      };
   }

   private DefaultTableCellRenderer celdasFormateadas () {
      return new DefaultTableCellRenderer() {
         @Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 1) {
               value = String.format("$%,.2f", (double) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      };
   }

   private void rellenarTablaCompras () {
      ventanaPrincipal.refrescar();
      compraLocales = ventanaPrincipal.obtenerListaCompras();
      model.setRowCount(0); // Limpiar el modelo antes de agregar nuevos datos
      for (Compra compra : compraLocales) {
         Object[] fila = new Object[NOMBRE_COLUMNAS.values().length];
         fila[NOMBRE_COLUMNAS.ID_COMPRA.getIndex()]    = compra.getIDcompra();
         fila[NOMBRE_COLUMNAS.VALOR.getIndex()]        = compra.getValorCompra();
         fila[NOMBRE_COLUMNAS.CANTIDAD.getIndex()]     = compra.getCantidadCompra();
         fila[NOMBRE_COLUMNAS.FECHA.getIndex()]        = compra.getFechaCompra();
         fila[NOMBRE_COLUMNAS.VER_DETALLES.getIndex()] = false;
         model.addRow(fila);
      }
   }

   private void modificacionesCompras () {
      model.addTableModelListener(event -> {
         if (event.getColumn() == 4) {
            verDetalles(event.getFirstRow());
         }
      });
   }

   private void verDetalles (int filaEvento) {
      final int columnaVerDetalles = NOMBRE_COLUMNAS.VER_DETALLES.getIndex();
      if (!(Boolean) model.getValueAt(filaEvento, columnaVerDetalles)) return;
      setCompraSeleccionada(filaEvento);
      model.setValueAt(false, filaEvento, columnaVerDetalles);
      evento.actionPerformed(new ActionEvent(this, 0, Evento.EVENTO.MOSTRAR_DETALLES_COMPRA.name()));
   }

   private void setCompraSeleccionada (int filaEvento) {
      compraSeleccionada = new Compra();
      compraSeleccionada.setIDcompra((long) model.getValueAt(filaEvento, NOMBRE_COLUMNAS.ID_COMPRA.getIndex()));
      String fechaCompra = (String) model.getValueAt(filaEvento, NOMBRE_COLUMNAS.FECHA.getIndex());
      compraSeleccionada.setFechaCompra(LocalDateTime.parse(fechaCompra, Compra.FORMATO_FECHA));
      compraSeleccionada.setValorCompra((double) model.getValueAt(filaEvento, NOMBRE_COLUMNAS.VALOR.getIndex()));
      compraSeleccionada.setCantidadCompra((int) model.getValueAt(filaEvento, NOMBRE_COLUMNAS.CANTIDAD.getIndex()));
      for (Compra compra : compraLocales) {
         if (compra.getIDcompra() == compraSeleccionada.getIDcompra()) {
            compraSeleccionada.setMetodoPago(compra.getMetodoPago());
            compraSeleccionada.setPorcentajeDescuento(compra.getPorcentajeDescuento());
            break;
         }
      }
      compraSeleccionada.setLibrosComprados(ventanaPrincipal.obtenerListaDetallesCompraPorID(compraSeleccionada.getIDcompra()));
      ventanaPrincipal.setCompraSeleccionada(compraSeleccionada);
   }

   public void refrescarLista () {
      rellenarTablaCompras();
   }

   public enum NOMBRE_COLUMNAS {
      ID_COMPRA(0, "ID Compra"),
      VALOR(1, "Valor Total"),
      CANTIDAD(2, "Cantidad de Libros"),
      FECHA(3, "Fecha de Compra"),
      VER_DETALLES(4, "Ver Detalles");
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

      public int getIndex () {
         return index;
      }

      String getName () {
         return name;
      }
   }
}