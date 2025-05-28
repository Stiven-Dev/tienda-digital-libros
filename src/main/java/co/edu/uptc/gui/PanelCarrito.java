package co.edu.uptc.gui;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.gui.Evento.EVENTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashMap;

public class PanelCarrito extends JPanel {
   private final Evento                 evento;
   private final VentanaPrincipal       ventanaPrincipal;
   private final HashMap<Long, Integer> carritoDeCompras;
   private final Font                   fuenteCabecera     = new Font("Arial", Font.BOLD, 15);
   private final Font                   fuenteCelda        = new Font("Lucida Sans Unicode", Font.PLAIN, 15);
   private final Font                   fuenteBoton        = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private       int                    cantidadLibros     = 0;
   private       double                 valorTotalImpuesto = 0;
   private       double                 subTotal           = 0;
   private       double                 total              = 0;
   private final JLabel                 labelCantidad      = new JLabel(String.format("Cantidad de libros: %d", cantidadLibros));
   private final JLabel                 labelImpuesto      = new JLabel(String.format("Impuesto: $%,.2f", valorTotalImpuesto));
   private final JLabel                 labelSubTotal      = new JLabel(String.format("Subtotal: $%,.2f", subTotal));
   private final JLabel                 labelTotal         = new JLabel(String.format("Total: $%f", total));
   private       GridBagConstraints     gbc;
   public        DefaultTableModel      model;

   public PanelCarrito (VentanaPrincipal ventanaPrincipal, Evento evento) {
      this.evento           = evento;
      this.ventanaPrincipal = ventanaPrincipal;
      setLayout(new GridBagLayout());
      inicializarPanelCarrito();
      carritoDeCompras = new HashMap<>();
      inicializarPanelFooter();
   }

   public DefaultTableModel getDefaultTableModel () {
      // La última columna es de tipo Boolean para mostrar un JCheckBox
      return new DefaultTableModel(NOMBRE_COLUMNAS.getCabecera(), 0) {
         @Override public boolean isCellEditable (int row, int column) {
            return column >= 7 && column < 10;
         }

         @Override public Class<?> getColumnClass (int columna) {
            return switch (columna) {
               case 0 -> long.class;
               case 3, 4, 6 -> double.class;
               case 5 -> int.class;
               case 7, 8, 9 -> Boolean.class;
               default -> String.class;
            };
         }
      };
   }

   private DefaultTableCellRenderer celdasFormateadas () {
      return new DefaultTableCellRenderer() {
         @Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            switch (column) {
               case 3, 4, 6 -> value = String.format("$%,.2f", (double) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      };
   }

   public HashMap<Long, Integer> getCarritoDeComprasTemporal () {
      return carritoDeCompras;
   }

   private void inicializarPanelCarrito () {
      gbc         = new GridBagConstraints();
      gbc.insets  = new Insets(0, 5, 0, 5);
      gbc.fill    = GridBagConstraints.BOTH;
      gbc.gridx   = 0;
      gbc.gridy   = 0;
      gbc.weightx = 1;
      gbc.weighty = 0.7f;

      model = getDefaultTableModel();
      modificacionesCarrito();
      JTable tableCarrito = new JTable(model) {
         @Override public Component prepareRenderer (TableCellRenderer renderer, int row, int column) {
            Component component = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
               component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(220, 220, 220));
            } else {
               component.setBackground(Color.ORANGE);
            }
            return component;
         }
      };
      tableCarrito.getTableHeader().setFont(fuenteCabecera);
      tableCarrito.setFont(fuenteCelda);
      tableCarrito.setRowHeight(30);
      formatearColumnas(tableCarrito);
      JScrollPane scrollPane = new JScrollPane(tableCarrito);
      add(scrollPane, gbc);
   }

   private void formatearColumnas (JTable tableCarrito) {
      tableCarrito.getColumnModel().getColumn(3).setCellRenderer(celdasFormateadas());
      tableCarrito.getColumnModel().getColumn(4).setCellRenderer(celdasFormateadas());
      tableCarrito.getColumnModel().getColumn(6).setCellRenderer(celdasFormateadas());
   }

   private void inicializarPanelFooter () {
      JPanel panelDescripcionCompra = new JPanel(new GridLayout(3, 1));
      panelDescripcionCompra.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                                                                        "Descripcion de la compra",
                                                                        TitledBorder.RIGHT,
                                                                        TitledBorder.TOP,
                                                                        fuenteCabecera));
      //Centrado de lables
      labelCantidad.setHorizontalAlignment(JLabel.RIGHT);
      labelImpuesto.setHorizontalAlignment(JLabel.RIGHT);
      labelSubTotal.setHorizontalAlignment(JLabel.RIGHT);
      //Asignacion de fuente a cada label
      labelCantidad.setFont(fuenteCelda);
      labelImpuesto.setFont(fuenteCelda);
      labelSubTotal.setFont(fuenteCelda);
      //Se añaden los labels al panel
      panelDescripcionCompra.add(labelCantidad);
      panelDescripcionCompra.add(labelImpuesto);
      panelDescripcionCompra.add(labelSubTotal);

      gbc.gridy   = 1;
      gbc.weighty = 0.15f;
      add(panelDescripcionCompra, gbc);

      //Footer (incluye el label de precio total)
      JButton botonPagarEfectivo = new JButton("Pagar En Efectivo");
      JButton botonPagarTarjeta  = new JButton("Pagar Con Tarjeta");
      botonPagarEfectivo.setActionCommand(EVENTO.PAGAR_EFECTIVO.name());
      botonPagarTarjeta.setActionCommand(EVENTO.PAGAR_TARJETA.name());
      botonPagarTarjeta.addActionListener(evento);
      botonPagarEfectivo.addActionListener(evento);
      labelTotal.setHorizontalAlignment(JLabel.CENTER);
      labelTotal.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 20));

      GridBagConstraints gbcFooter = new GridBagConstraints();
      gbcFooter.insets = new Insets(5, 5, 10, 5);
      gbcFooter.fill   = GridBagConstraints.HORIZONTAL;
      final float pesoBoton   = 0.15f;
      JPanel      panelFooter = new JPanel(new GridBagLayout());
      gbcFooter.gridx   = 0;
      gbcFooter.weightx = 0.7f;
      panelFooter.add(labelTotal, gbcFooter);
      gbcFooter.gridx   = 1;
      gbcFooter.weightx = pesoBoton;
      panelFooter.add(botonPagarEfectivo, gbcFooter);
      gbcFooter.gridx = 2;
      panelFooter.add(botonPagarTarjeta, gbcFooter);
      //Asignacion de fuente a cada boton
      botonPagarEfectivo.setFont(fuenteBoton);
      botonPagarTarjeta.setFont(fuenteBoton);

      gbc.gridy   = 2;
      gbc.weighty = 0.1f;
      add(panelFooter, gbc);

      actualizarLabelsCompra();
   }

   private void modificacionesCarrito () {
      model.addTableModelListener(event -> {
         int fila    = event.getFirstRow();
         int columna = event.getColumn();
         switch (columna) {
            case 7 -> sumarAlCarrito(fila);
            case 8 -> quitarAlCarrito(fila);
            case 9 -> descartarDelCarrito(fila);
            default -> {
               return;
            }
         }
         actualizarDatosCompra();
      });
   }

   private void sumarAlCarrito (int fila) {
      final int columnaAgregar = NOMBRE_COLUMNAS.AGREGAR.getIndex();
      if (!((boolean) model.getValueAt(fila, columnaAgregar))) {
         return;
      }
      final int columnaISBN     = NOMBRE_COLUMNAS.ISBN.getIndex();
      final int columnaCantidad = NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      try {
         long  ISBN     = (long) model.getValueAt(fila, columnaISBN);
         int   cantidad = (int) model.getValueAt(fila, columnaCantidad);
         Libro libro    = obtenerLibroModel(fila);
         if (carritoDeCompras.get(ISBN) >= 10) {
            JOptionPane.showMessageDialog(null, "No se pueden agregar mas de 10 unidades del mismo libro", "Alerta", JOptionPane.INFORMATION_MESSAGE);
            return;
         }
         if (libro.getCantidadDisponible() > cantidad) {
            cantidad += 1;
            carritoDeCompras.replace(ISBN, cantidad);
            model.setValueAt(cantidad, fila, columnaCantidad);
            actualizarPrecioVenta(ISBN, fila);
            return;
         }
         String mensaje = String.format("No quedan mas unidades de \"%s\"", libro.getTitulo());
         JOptionPane.showMessageDialog(null, mensaje, "Alerta", JOptionPane.INFORMATION_MESSAGE);
      } catch (NullPointerException e) {
         System.err.println(e.getMessage());
      } finally {
         model.setValueAt(false, fila, columnaAgregar);
      }
   }

   private Libro obtenerLibroModel (int fila) {
      int  columnaISBN = NOMBRE_COLUMNAS.ISBN.getIndex();
      long ISBN        = (long) model.getValueAt(fila, columnaISBN);
      return ventanaPrincipal.obtenerLibroMedianteISBN(ISBN);
   }

   private void quitarAlCarrito (int fila) {
      final int columnaQuitar = NOMBRE_COLUMNAS.QUITAR.getIndex();
      if (!(boolean) model.getValueAt(fila, columnaQuitar)) {
         return;
      }
      final int columnaISBN     = NOMBRE_COLUMNAS.ISBN.getIndex();
      final int columnaCantidad = NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      try {
         int  cantidad = (int) model.getValueAt(fila, columnaCantidad);
         long ISBN     = (long) model.getValueAt(fila, columnaISBN);
         if (cantidad > 1) {
            cantidad -= 1;
            carritoDeCompras.replace(ISBN, cantidad);
            model.setValueAt(cantidad, fila, columnaCantidad);
            actualizarPrecioVenta(ISBN, fila);
            model.setValueAt(false, fila, columnaQuitar);
            return;
         }
         model.removeRow(fila);
         carritoDeCompras.remove(ISBN);
      } catch (NullPointerException e) {
         System.err.println(e.getMessage());
      }
   }

   private void descartarDelCarrito (int fila) {
      final int columnaISBN = NOMBRE_COLUMNAS.ISBN.getIndex();
      long      ISBN        = (long) model.getValueAt(fila, columnaISBN);
      carritoDeCompras.remove(ISBN);
      model.removeRow(fila);
   }

   void agregarArticulo (long ISBN) {
      Libro libro = ventanaPrincipal.obtenerLibroMedianteISBN(ISBN);
      if (libro == null) {
         return;
      }
      if (carritoDeCompras.get(ISBN) == null) {
         carritoDeCompras.put(ISBN, 1);
         rellenarDatosFilaLibro(libro);
         actualizarDatosCompra();
         return;
      }
      aumentarCantidad(ISBN);
   }

   private void rellenarDatosFilaLibro (Libro libro) {
      Object[] datosFila = new Object[10];
      long     ISBN      = libro.getISBN();
      datosFila[NOMBRE_COLUMNAS.ISBN.getIndex()]           = libro.getISBN();
      datosFila[NOMBRE_COLUMNAS.TITULO.getIndex()]         = libro.getTitulo();
      datosFila[NOMBRE_COLUMNAS.AUTORES.getIndex()]        = libro.getAutores();
      datosFila[NOMBRE_COLUMNAS.VALOR_UNITARIO.getIndex()] = libro.getPrecioVenta();
      datosFila[NOMBRE_COLUMNAS.VALOR_IMPUESTO.getIndex()] = obtenerPrecioImpuesto(ISBN);
      datosFila[NOMBRE_COLUMNAS.CANTIDAD.getIndex()]       = 1;
      datosFila[NOMBRE_COLUMNAS.VALOR_TOTAL.getIndex()]    = libro.getPrecioVenta();
      datosFila[NOMBRE_COLUMNAS.AGREGAR.getIndex()]        = false;
      datosFila[NOMBRE_COLUMNAS.QUITAR.getIndex()]         = false;
      datosFila[NOMBRE_COLUMNAS.DESCARTAR.getIndex()]      = false;
      model.addRow(datosFila);
   }

   void aumentarCantidad (long ISBN) {
      final int columnaISBN     = NOMBRE_COLUMNAS.ISBN.getIndex();
      final int columnaCantidad = NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      int       cantidad        = carritoDeCompras.get(ISBN);
      Libro     libro           = ventanaPrincipal.obtenerLibroMedianteISBN(ISBN);
      if (libro.getCantidadDisponible() <= cantidad) {
         String mensaje = String.format("No quedan mas unidades de \"%s\"", libro.getTitulo());
         JOptionPane.showMessageDialog(null, String.format(mensaje, "Alerta", JOptionPane.INFORMATION_MESSAGE));
         return;
      }
      if (cantidad >= 10) {
         JOptionPane.showMessageDialog(null, "No se pueden agregar mas de 10 unidades del mismo libro", "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      if (ventanaPrincipal.obtenerLibroMedianteISBN(ISBN).getCantidadDisponible() <= cantidad) {
         return;
      }
      cantidad += 1;
      for (int i = 0; i < model.getRowCount(); i++) {
         if ((long) model.getValueAt(i, columnaISBN) == ISBN) {
            carritoDeCompras.replace(ISBN, cantidad);
            model.setValueAt(cantidad, i, columnaCantidad);
            actualizarPrecioVenta(ISBN, i);
            return;
         }
      }
   }

   private void actualizarPrecioVenta (long ISBN, int filaModel) {
      final int columnaPrecioVenta = NOMBRE_COLUMNAS.VALOR_TOTAL.getIndex();
      double    valorUnitario      = obtenerLibroModel(filaModel).getPrecioVenta();
      int       cantidad           = carritoDeCompras.get(ISBN);
      double    precioVenta        = ventanaPrincipal.obtenerPrecioTotalProducto(valorUnitario, cantidad);
      model.setValueAt(precioVenta, filaModel, columnaPrecioVenta);
   }

   private double obtenerPrecioImpuesto (long ISBN) {
      double valorUnitario = ventanaPrincipal.obtenerLibroMedianteISBN(ISBN).getPrecioVenta();
      return ventanaPrincipal.obtenerPrecioImpuesto(valorUnitario);
   }

   private void actualizarDatosCompra () {
      cantidadLibros     = ventanaPrincipal.obtenerCantidadLibros(model);
      valorTotalImpuesto = ventanaPrincipal.obtenerValorTotalImpuesto(model);
      subTotal           = ventanaPrincipal.obtenerSubTotalVenta(model);
      if (ventanaPrincipal.usuarioRegistrado()) {
         total = ventanaPrincipal.obtenerTotalVenta(subTotal);
      } else {
         total = subTotal;
      }
      actualizarLabelsCompra();
   }

   private void actualizarLabelsCompra () {
      labelCantidad.setText(String.format("Cantidad de libros: %d", cantidadLibros));
      labelImpuesto.setText(String.format("Impuesto: $%,.2f", valorTotalImpuesto));
      labelSubTotal.setText(String.format("Subtotal: $%,.2f", subTotal));
      labelTotal.setText(String.format("Total: $%,.2f", total));
   }

   public enum NOMBRE_COLUMNAS {
      ISBN(0, "ISBN"),
      TITULO(1, "Título"),
      AUTORES(2, "Autores"),
      VALOR_UNITARIO(3, "Vlr Unitario"),
      VALOR_IMPUESTO(4, "Vlr Impuesto"),
      CANTIDAD(5, "Cantidad"),
      VALOR_TOTAL(6, "Vlr Total"),
      AGREGAR(7, "+"),
      QUITAR(8, "-"),
      DESCARTAR(9, "x");
      private final int    index;
      private final String name;

      NOMBRE_COLUMNAS (int index, String name) {
         this.index = index;
         this.name  = name;
      }

      public int getIndex () {
         return index;
      }

      static String[] getCabecera () {
         String[] cabecera = new String[10];
         for (NOMBRE_COLUMNAS columna : NOMBRE_COLUMNAS.values()) {
            cabecera[columna.getIndex()] = columna.name;
         }
         return cabecera;
      }
   }
}