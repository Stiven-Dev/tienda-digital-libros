package co.edu.uptc.gui;

import co.edu.uptc.gui.Evento.EVENTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelLibros extends JPanel {
   private final Evento            evento;
   private final VentanaPrincipal  ventana;
   private final Font              fuenteCabecera = new Font("Arial", Font.BOLD, 15);
   private final Font              fuenteCelda    = new Font("Lucida Sans Unicode", Font.PLAIN, 12);
   private final Font              fuenteBoton    = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private       DefaultTableModel model;

   public PanelLibros (VentanaPrincipal ventana, Evento evento) {
      this.ventana = ventana;
      this.evento  = evento;
      inicializarPanelLibros();
   }

   private DefaultTableModel getDefaultTableModel () {
      return new DefaultTableModel(NOMBRE_COLUMNAS.getCabecera(), 0) {
         @Override public boolean isCellEditable (int row, int column) {
            return column == 10;
         }

         @Override public Class<?> getColumnClass (int columnIndex) {
            return switch (columnIndex) {
               case 0 -> long.class;
               case 4, 9 -> int.class;
               case 8 -> double.class;
               case 10 -> Boolean.class;
               default -> String.class;
            };
         }
      };
   }

   private DefaultTableCellRenderer celdasDoubleFormateadas () {
      return new DefaultTableCellRenderer() {
         @Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 8) {
               value = String.format("$%,.2f", (double) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      };
   }

   private void inicializarPanelLibros () {
      setLayout(new BorderLayout());
      model = getDefaultTableModel();
      refrescarLista();
      JTable tableLibros = new JTable(model);
      tableLibros.getTableHeader().setFont(fuenteCabecera);
      tableLibros.setFont(fuenteCelda);
      formatearColumnas(tableLibros);
      tableLibros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      JScrollPane scrollPane = new JScrollPane(tableLibros);
      add(scrollPane, BorderLayout.CENTER);
      JButton botonAgregar = new JButton("Agregar Libro");
      botonAgregar.setActionCommand(EVENTO.AGREGAR_LIBRO_AL_CARRITO.name());
      botonAgregar.addActionListener(evento);
      add(botonAgregar, BorderLayout.SOUTH);
      //Asignacion de fuente al boton
      botonAgregar.setFont(fuenteBoton);
   }

   private void formatearColumnas (JTable tableLibros) {
      tableLibros.setDefaultRenderer(Object.class, celdasDoubleFormateadas());
   }

   private void refrescarLista () {
      model.setDataVector(ventana.obtenerVectorLibros(), NOMBRE_COLUMNAS.getCabecera());
      ventana.setLibrosLocales(ventana.obtenerMapLibros());
   }

   public DefaultTableModel getTableModel () {
      return model;
   }

   private enum NOMBRE_COLUMNAS {
      ISBN(0),
      TITULO(1),
      AUTOR(2),
      GENERO(3),
      PAGINAS(4),
      EDITORIAL(5),
      ANIO(6),
      FORMATO(7),
      PRECIO(8),
      CANTIDAD_DISPONIBLE(9),
      AGREGAR(10);
      private final int    index;
      private final String name;

      NOMBRE_COLUMNAS (int index) {
         this.index = index;
         this.name  = this.name();
      }

      int getIndex () {
         return index;
      }

      String getName () {
         return name;
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